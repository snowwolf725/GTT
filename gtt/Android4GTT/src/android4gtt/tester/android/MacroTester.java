package android4gtt.tester.android;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.logger.Logger;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.oracle.AssertionChecker;
import gtt.oracle.IOracleHandler;
import gtt.tester.macro.IMacroTester;
import gtt.tester.swing.ITester;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.ReferenceMacroEventNode;
import gttlipse.fit.node.ReferenceFitNode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.jayway.android.robotium.solo.Solo;

public class MacroTester implements IMacroTester {

	private MacroDocument m_MacroDocument;
	private ITester m_Tester = null;
	private IEventModel m_EventModel = null;
	private AssertionChecker m_Checker = null;
	private Arguments m_refArgument = null;
	private int error_type = NO_ERROR;
	private int global_sleep_time = 0;
	public static String REF_ARGUMENT_PREFIX = "@";
		
	public MacroTester(MacroDocument macroDoc, Solo solo, Object res) {
		m_Tester = new AndroidTester(solo, res);
		m_Checker = new AndroidChecker(solo, res);
		m_MacroDocument = macroDoc;
	}
	
	public void setModel(IEventModel model) {
		m_EventModel = model;
	}
	
	@Override
	public boolean fire(ReferenceMacroEventNode node) throws Exception {
		// 在Macro Model 中找到所參考到的 Macro Event
		AbstractMacroNode ref = m_MacroDocument.findByPath(node.getRefPath());
		if (ref == null)
			throw new Exception("ReferenceMacroEventNode " + node.getRefPath()
					+ " is nod found");

		// 處理 MacroEvent Node
		if (ref instanceof MacroEventNode)
			return processMacroEvent((MacroEventNode) ref);

		return false;
	}
	
	public boolean fire(MacroEventNode node) throws Exception {
		if (node == null)
			return false;

		return processMacroEvent(node);
	}

	@Override
	public boolean fire(ReferenceFitNode node) throws Exception {
		return false;
	}

	private boolean processMacroEvent(MacroEventNode me) throws Exception {
		if (me == null) {
			logging("Null MacroEventNode");
			return false;
		}

		logging("fire " + me.getPath());
		return visitChildren(me.iterator());
	}
	
	private boolean visitChildren(Iterator<AbstractMacroNode> ite)
	throws Exception {
		boolean r = true;
		// 只要某個事件發不成功，結果就會是 false
		while (ite.hasNext()) {
			AbstractMacroNode abs = ite.next();
			if (abs == null)
				continue;
			
			logging("\tVisit " + abs.toString());
			if (abs instanceof ComponentEventNode) {
				r = processComponentEvent((ComponentEventNode) abs);
				if (r == false) {
					error_type = EVENT_ERROR;
//					recordLoadTestingResultLog(abs.getPath().toString(),"FAIL");
				}
			}			
			else if (abs instanceof MacroEventCallerNode) {
				r = processMacroEventCaller((MacroEventCallerNode) abs);
			}
			else if (abs instanceof ViewAssertNode) {
				r = processViewAssert((ViewAssertNode) abs);
				if (r == false) {
					error_type = ORACLE_ERROR;
					recordFailureLog((ViewAssertNode) abs);
//					recordLoadTestingResultLog(abs.getPath().toString(),"FAIL");
				}
			}
			
			// Add global sleep time
			if (global_sleep_time > 0)
				Thread.sleep(global_sleep_time);
		
			// 只要有一個錯誤，就不再繼續執行
			if (r == false)
				return false;
		}
		return r;
	}

	private boolean processComponentEvent(ComponentEventNode ce) {
	
//		// Dynamic Component
//		String temp = null;
//		if (ce.getDyValue() != null) {
//			temp = ce.getDyValue();
//			ce.setDyValue(checkDynamicValue(ce.getDyValue()));
//		}
		EventNode en = transformComponentEvent(ce);
		Arguments arglist = en.getEvent().getArguments().clone();
		// 檢查 argument 中是否有替代參數
		checkArgumentDomainValue(en.getEvent().getArguments());
		// 轉成TestScript的EventNode，讓 EventFirver 來發事件
		// m_EventFirer.setSleepTime(1000);
		boolean result = m_Tester.fire(en);

		// 回復dyValue
//		if (ce.getDyValue() != null) {
//			if (!ce.getDyValue().isEmpty()) {
//				ce.setDyValue(temp);
//				ce.getComponent().getComponent().setName("");
//				ce.getComponent().getComponent().setText("");
//				ce.getComponent().getComponent().setTitle("");
//				ce.getComponent().getComponent().setWinType("");
//				ce.getComponent().getComponent().setIndex(1);
//			}
//		}

		// 恢復argument list
		en.getEvent().setArguments(arglist);
//		updateComponentCoverage(en.getComponent());

		return result;
	}
	
	// 檢查 argument 中是否有替代參數
	private void checkArgumentDomainValue(Arguments arglist) {
		if (m_refArgument == null) {
			return;
		}
		Iterator<Argument> ite = arglist.iterator();
		while (ite.hasNext()) {
			Argument arg = ite.next();
			// 檢查是否為 @ 開頭的value
			if (arg.getValue().indexOf(REF_ARGUMENT_PREFIX) != 0)
				continue;

			// 若是的話，則從 refArgument 取出對應的value
			String refName = arg.getValue().substring(1);
			String refValue = m_refArgument.getValue(refName);
			if (refValue != null) {
				arg.setValue(refValue);
			}
		}
	}
	
	public EventNode transformComponentEvent(ComponentEventNode ce) {
		IComponent ic = ce.getComponent().getComponent();
		if (ic == null) {
			logging("MacroTester can't find IComponent: " + ce.toString());
			return null;
		}

//		if (ce.getDyValue() != null) {
//			if ((!ce.getDyValue().isEmpty())
//					&& GTTlipseConfig.testingOnWebPlatform()) {
//				if (ce.getDyType().equals("Name:")) {
//					ic.setName(ce.getDyValue());
//				} else if (ce.getDyType().equals("Element Xpath:")) {
//					ic.setText(ce.getDyValue());
//				} else if (ce.getDyType().equals("Element Id:")) {
//					ic.setTitle(ce.getDyValue());
//				} else if (ce.getDyType().equals("Link Text:")) {
//					ic.setWinType(ce.getDyValue());
//				}
//				ic.setIndex(ce.getDyIndex());
//			}
//		}
		IEvent event = m_EventModel.getEvent(ic, ce.getEventType());
		event.setArguments(ce.getArguments().clone());

		// Coverage
//		if (m_reporter != null) {
//			m_reporter.setCoverage(ic, event);
//		}

		return new NodeFactory().createEventNode(ic, event);
	}
	
	private boolean processMacroEventCaller(MacroEventCallerNode node)
	throws Exception {
		// 找到所參考的 MacroEvent
		AbstractMacroNode ref = node.getReference();
		if (ref == null)
			throw new NullPointerException(node + " is not found.");
		
		if (ref instanceof MacroEventNode) {
			MacroEventNode me = (MacroEventNode) ref;
			// 處理「passed argument」的問題 - zwshen 2008/05/31
		
			Arguments arglist = node.getArguments().clone();
			// 檢查 argument 中是否有替代參數
			checkArgumentDomainValue(node.getArguments());

			Arguments oldAgumentList = m_refArgument;
			m_refArgument = node.getArguments().clone();
			// 記下 MacroEventCaller 的參數
			boolean result = processMacroEvent(me);
			m_refArgument = oldAgumentList; // 恢復 ArgumentList
			node.setArguments(arglist);
			return result;
		}
		
		return false;
	}
	
	private boolean processViewAssert(ViewAssertNode ve) {
		// 取得 IComponent
		IComponent ic = ve.getComponent().getComponent();
		// ve.dyValue不是空的話代表是dynamic component

//		String temp = null;
//		if (ve.getDyValue() != null) {
//			temp = ve.getDyValue();
//			ve.setDyValue(checkDynamicValue(ve.getDyValue()));
//		}
//
//		if (ve.getDyValue() != null) {
//			if (!ve.getDyValue().isEmpty()) {
//				if ((!ve.getDyValue().isEmpty())
//						&& GTTlipseConfig.testingOnWebPlatform()) {
//					if (ve.getDyType().equals("Name:")) {
//						ic.setName(ve.getDyValue());
//					} else if (ve.getDyType().equals("Element Xpath:")) {
//						ic.setText(ve.getDyValue());
//					} else if (ve.getDyType().equals("Element Id:")) {
//						ic.setTitle(ve.getDyValue());
//					} else if (ve.getDyType().equals("Link Text:")) {
//						ic.setWinType(ve.getDyValue());
//					}
//					ic.setIndex(ve.getDyIndex());
//				}
//			}
//		}
		// Find parameter and record assert value
		String expectAssertValue = ve.getAssertion().getValue();
//		ve.getAssertion().setValue(
//				checkDynamicValue(expectAssertValue));
		// Find parameter and record arguments
		Arguments argsBak = ve.getArguments().clone();
		checkArgumentDomainValue(ve.getArguments());

		// 針對FIT的ASSERT，從以macro name為名之參數，取得驗證資料 by Pan
//		String assertValue = ((MacroEventNode) ve.getParent()).getArguments()
//				.getValue(ve.getParent().getName() + "()");
		
		// 檢查是否為 @ 開頭的value
		if (expectAssertValue.indexOf(REF_ARGUMENT_PREFIX) == 0) {
			
			// 若是的話，則從 refArgument 取出對應的value
			String refName = expectAssertValue.substring(1);
			String refValue = m_refArgument.getValue(refName);
			if (refValue != null) {
				ve.getAssertion().setValue(refValue);
			}
		}

		if (ic == null) {
			logging("processViewAssert NULL IC");
			return false;
		}
		boolean result = false;
		result = m_Checker.check(ic, ve.getAssertion());
		
		// 恢復 assert value and arguments
		ve.getAssertion().setValue(expectAssertValue);
		ve.setArguments(argsBak);

		// 回復dyValue
//		if (ve.getDyValue() != null) {
//			if (!ve.getDyValue().isEmpty()) {
//				ve.setDyValue(temp);
//				ve.getComponent().getComponent().setName("");
//				ve.getComponent().getComponent().setText("");
//				ve.getComponent().getComponent().setTitle("");
//				ve.getComponent().getComponent().setWinType("");
//				ve.getComponent().getComponent().setIndex(1);
//			}
//		}
		return result;
	}
	
	private void logging(String msg) {
		// 應該使用 logging 機制，會比較乾淨
		 String prefix = "[MacroTester]";
		 Logger.getSimpleLogger().log(prefix + "-" + msg);
	}
	
	private void recordFailureLog(AbstractMacroNode abs) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("/sdcard/Android4GTT/MacroGUITestResult.txt", true);
			String failpath = "";
			int i = 0;
			for (i = 0; i < abs.getParent().getChildren().length; i++) {
				if (abs.getParent().get(i).equals(abs))
					break;
			}
			String path = abs.getPath().toString();
			path = path.substring(0, path.length() - 2);
			failpath = path + "\n" + i + "\n";// 第一行放父節點路徑，第二行放驗證結果錯誤的節點index
			out.write(failpath.getBytes());
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setTester(ITester tester) {
		m_Tester = tester;
	}

	@Override
	public void setOracleHandler(IOracleHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getErrorType() {
		return error_type;
	}
	
	public void setGlobalSleeperTime(int time) {
		if (time <= 0)
			return;
		global_sleep_time = time;
	}

}
