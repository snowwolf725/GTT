/*
 * Copyright (C) 2006-2009
 * Woei-Kae Chen <wkc@csie.ntut.edu.tw>
 * Hung-Shing Chao <s9598007@ntut.edu.tw>
 * Tung-Hung Tsai <s159020@ntut.edu.tw>
 * Zhe-Ming Zhang <s2598001@ntut.edu.tw>
 * Zheng-Wen Shen <zwshen0603@gmail.com>
 * Jung-Chi Wang <snowwolf725@gmail.com>
 *
 * This file is part of GTT (GUI Testing Tool) Software.
 *
 * GTT is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * GTT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * GNU GENERAL PUBLIC LICENSE http://www.gnu.org/licenses/gpl
 */
package gtt.tester.macro;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroPath;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.oracle.AssertionChecker;
import gtt.oracle.ComponentCollector;
import gtt.oracle.IOracleHandler;
import gtt.oracle.SwingChecker;
import gtt.runner.web.WebController;
import gtt.tester.swing.ITester;
import gtt.tester.swing.SwingTester;
import gtt.tester.web.WebTester;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.web.oracle.WebAssertionChecker;
import gttlipse.GTTlipseConfig;
import gttlipse.fit.FitInfoCollector;
import gttlipse.fit.FixtureDefinition;
import gttlipse.fit.fixture.AbstractFitFixture;
import gttlipse.fit.fixture.ActionFixture;
import gttlipse.fit.fixture.ColumnFixture;
import gttlipse.fit.fixture.RowFixture;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.ReferenceFitNode;
import gttlipse.fit.node.processor.ComponentNamePool;
import gttlipse.fit.node.processor.EventTriggerNodeProcessor;
import gttlipse.fit.node.processor.FitAssertionNodeProcessor;
import gttlipse.fit.node.processor.FitStateAssertionNodeProcessor;
import gttlipse.fit.table.TableRow;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;


public class MacroTester implements IMacroTester {
	public static String REF_ARGUMENT_PREFIX = "@";
	private Reporter m_reporter = null;
	private ComponentCollector m_collector;
	private MacroDocument m_MacroDocument;
	private ITester m_Tester = null;
	private IEventModel m_EventModel = EventModelFactory.getDefault();
	private AssertionChecker m_Checker = null;
	private Arguments m_refArgument = null;
	
	private int error_type = NO_ERROR;
	private int global_sleep_time = 0;
	private long m_startTime = 0L;

	public MacroTester(MacroDocument doc) {
		m_MacroDocument = doc;
		if (GTTlipseConfig.testingOnSwingPlatform()) {
			m_Tester = new SwingTester();
			m_Checker = new SwingChecker();
		} else {
			m_Tester = new WebTester();
			m_Checker = new WebAssertionChecker();
		}
		m_startTime = System.currentTimeMillis();
	}

	public MacroTester(MacroDocument doc, Reporter reporter,
			ComponentCollector collector) {
		this(doc);

		m_reporter = reporter;
		m_collector = collector;
	}

	public void setTester(ITester tester) {
		m_Tester = tester;
	}

	public int getErrorType() {
		return error_type;
	}

	public void setGlobalSleeperTime(int time) {
		if (time <= 0)
			return;
		global_sleep_time = time;
	}

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

	@Override
	public boolean fire(ReferenceFitNode node) throws Exception {
		AbstractMacroNode ref = m_MacroDocument.findByPath(node
				.getReferencePath());
		if (ref == null)
			throw new Exception("ReferenceFitNode " + node.getReferencePath()
					+ " is nod found");

		// 處理 MacroEvent Node
		if (ref instanceof FitNode)
			return processFit((FitNode) ref);

		return false;
	}

	public boolean fire(MacroEventNode node) throws Exception {
		if (node == null)
			return false;

		return processMacroEvent(node);
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
					recordLoadTestingResultLog(abs.getPath().toString(),"FAIL");
				}
			}
			else if (abs instanceof DynamicComponentEventNode) {
				r = processDynamicComponentEvent((DynamicComponentEventNode) abs);
				if (r == false) {
					error_type = EVENT_ERROR;
					recordLoadTestingResultLog(abs.getPath().toString(),"FAIL");
				}
			}
			else if (abs instanceof ViewAssertNode) {
				r = processViewAssert((ViewAssertNode) abs);
				if (r == false) {
					error_type = ORACLE_ERROR;
					recordFailureLog((ViewAssertNode) abs);
					recordLoadTestingResultLog(abs.getPath().toString(),"FAIL");
				}
			}
			else if (abs instanceof ExistenceAssertNode) {
				r = processExistenceAssert((ExistenceAssertNode) abs);
				if (r == false) {
					error_type = ORACLE_ERROR;
					recordFailureLog((ExistenceAssertNode) abs);
					recordLoadTestingResultLog(abs.getPath().toString(),"FAIL");
				}
			}
			else if (abs instanceof LaunchNode) {
				r = processLaunchNode((LaunchNode) abs);
			}
			else if (abs instanceof ModelAssertNode) {
				r = processModelAssert((ModelAssertNode) abs);
				if (r == false)
					error_type = ORACLE_ERROR;
			}
			else if (abs instanceof MacroEventCallerNode) {
				r = processMacroEventCaller((MacroEventCallerNode) abs);
			}
			else if (abs instanceof SplitDataNode) {
				r = processSplitData((SplitDataNode) abs);
			}
			else if (abs instanceof SleeperNode) {
				r = processSleeperNode((SleeperNode) abs);
			}
			else if (abs instanceof BreakerNode) {
				return r;
			}
			else if (abs instanceof EventTriggerNode) {
				r = processEventTriggerNode((EventTriggerNode) abs);
			}
			else if (abs instanceof FitStateAssertionNode) {
				r = processFitStateAssertionNode((FitStateAssertionNode) abs);
			}
			else if (abs instanceof FitAssertionNode) {
				r = processFitAssertionNode((FitAssertionNode) abs);
			}
			else if (abs instanceof OracleNode) {
				r = processOracleNode((OracleNode) abs);
				// oracle 檢查失敗
				if (r == false)
					error_type = ORACLE_ERROR;
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

	private boolean processViewAssert(ViewAssertNode ve) {
		// 取得 IComponent
		// IComponent ic = findIComponent((MacroComponentNode) ve.getParent()
		// .getParent(), ve.getComponent().getName());
		IComponent ic = ve.getComponent().getComponent();
		// ve.dyValue不是空的話代表是dynamic component

		String temp = null;
		if (ve.getDyValue() != null) {
			temp = ve.getDyValue();
			ve.setDyValue(checkDynamicValue(ve.getDyValue()));
		}

		if (ve.getDyValue() != null) {
			if (!ve.getDyValue().isEmpty()) {
				if ((!ve.getDyValue().isEmpty())
						&& GTTlipseConfig.testingOnWebPlatform()) {
					if (ve.getDyType().equals("Name:")) {
						ic.setName(ve.getDyValue());
					} else if (ve.getDyType().equals("Element Xpath:")) {
						ic.setText(ve.getDyValue());
					} else if (ve.getDyType().equals("Element Id:")) {
						ic.setTitle(ve.getDyValue());
					} else if (ve.getDyType().equals("Link Text:")) {
						ic.setWinType(ve.getDyValue());
					}
					ic.setIndex(ve.getDyIndex());
				}
			}
		}
		// Find parameter and record assert value
		String expectAssertValue = ve.getAssertion().getValue();
		ve.getAssertion().setValue(
				checkDynamicValue(expectAssertValue));
		// Find parameter and record arguments
		Arguments argsBak = ve.getArguments().clone();
		checkArgumentDomainValue(ve.getArguments());

		// 針對FIT的ASSERT，從以macro name為名之參數，取得驗證資料 by Pan
		String assertValue = ((MacroEventNode) ve.getParent()).getArguments()
				.getValue(ve.getParent().getName() + "()");
		if (assertValue != null)
			ve.getAssertion().setValue(assertValue);

		if (ic == null) {
			logging("processViewAssert NULL IC");
			return false;
		}
		boolean result = false;
		if (GTTlipseConfig.testingOnSwingPlatform()) {
			// 目前swing版本還未支援多元件驗證
			result = m_Checker.check(ic, ve.getAssertion());
		} else {
			if (ic.getIndex() == -1) {// -1代表全部都要驗證
				result = m_Checker.checkMultipleAssertions(ic, ve
						.getAssertion());
			} else
				// 非-1代表有指定某一個元件
				result = m_Checker.check(ic, ve.getAssertion());
		}
		// 恢復 assert value and arguments
		ve.getAssertion().setValue(expectAssertValue);
		ve.setArguments(argsBak);

		// 回復dyValue
		if (ve.getDyValue() != null) {
			if (!ve.getDyValue().isEmpty()) {
				ve.setDyValue(temp);
				ve.getComponent().getComponent().setName("");
				ve.getComponent().getComponent().setText("");
				ve.getComponent().getComponent().setTitle("");
				ve.getComponent().getComponent().setWinType("");
				ve.getComponent().getComponent().setIndex(1);
			}
		}
		return result;
	}

	private boolean processLaunchNode(LaunchNode le) {
		if (GTTlipseConfig.testingOnSwingPlatform())
			return false;

		try {
			if (le.getArgument().equalsIgnoreCase("Loading URL")) {
				WebController.instance().startupWebPage(le.getClassPath());
			} else if (le.getArgument().equalsIgnoreCase("Previous Page")) {
				WebController.instance().getDriver().navigate().back();
			} else if (le.getArgument().equalsIgnoreCase("Next Page")) {
				WebController.instance().getDriver().navigate().forward();
			} else if (le.getArgument().equalsIgnoreCase("Change Browser")) {
				try {
					WebController.instance().getDriver().switchTo().window(
							le.getClassPath());
				} catch (Exception e) {
					System.out
							.println("No Found Windows Title Search for Frame Name.");
					WebController.instance().getDriver().switchTo().frame(
							le.getClassPath());
				}
			} else if (le.getArgument().equalsIgnoreCase("Default Browser")) {
				WebController.instance().getDriver().switchTo().window(
						WebController.instance().getWindowHandle());
			} else if (le.getArgument().equalsIgnoreCase("Close Browser")) {
				WebController.instance().closeAllBrowser();
			} else if (le.getArgument().equalsIgnoreCase("Click OK On Next Alert")) {
				WebController.instance().clickAlertOK();
			} else if (le.getArgument().equalsIgnoreCase("Choose OK On Next Confirmation")) {
				WebController.instance().clickConfirmOK();
			} else if (le.getArgument().equalsIgnoreCase("Choose Cancel On Next Confirmation")) {
				WebController.instance().clickConfirmCANCEL();
			} else if (le.getArgument().equalsIgnoreCase("AJAX waiting time")) {
				WebController.instance().waitingTime(Integer.parseInt(le.getClassPath()));
			} else if (le.getArgument().equalsIgnoreCase("Parse and store components of the current page")) {
				WebController.instance().webTransForm(m_MacroDocument.findByPath(le.getClassPath()));
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean processExistenceAssert(ExistenceAssertNode ee) {
		IComponent ic = ee.getComponent();
		if (GTTlipseConfig.testingOnSwingPlatform()) {
			// 目前swing版本還未支援驗證元件是否存在
			return false;
		}

		// 全部驗證，只能驗證有幾個存在
		if (ic.getIndex() == 0)
			return WebController.instance().findComponents(ic).size() == ee
					.getExpectResultCount();

		// 只驗證一個
		boolean result = (WebController.instance().find(ic) != null);
		// 找到後和預期解答比
		return (result == ee.getExpectResult());
	}

	private boolean processModelAssert(ModelAssertNode node) {
		// 暫時不支援 ModelAssertNode
		return true; // always true
	}

	public EventNode transformComponentEvent(ComponentEventNode ce) {
		// IComponent ic = findIComponent((MacroComponentNode) ce.getParent()
		// .getParent(), ce.getComponent().getName());

		IComponent ic = ce.getComponent().getComponent();
		if (ic == null) {
			logging("MacroTester can't find IComponent: " + ce.toString());
			return null;
		}

		if (ce.getDyValue() != null) {
			if ((!ce.getDyValue().isEmpty())
					&& GTTlipseConfig.testingOnWebPlatform()) {
				if (ce.getDyType().equals("Name:")) {
					ic.setName(ce.getDyValue());
				} else if (ce.getDyType().equals("Element Xpath:")) {
					ic.setText(ce.getDyValue());
				} else if (ce.getDyType().equals("Element Id:")) {
					ic.setTitle(ce.getDyValue());
				} else if (ce.getDyType().equals("Link Text:")) {
					ic.setWinType(ce.getDyValue());
				}
				ic.setIndex(ce.getDyIndex());
			}
		}

		// if (ic == null)
		// ic = findIComponentByReferencePath(ce);

		IEvent event = m_EventModel.getEvent(ic, ce.getEventType());
		event.setArguments(ce.getArguments().clone());

		// Coverage
		if (m_reporter != null) {
			m_reporter.setCoverage(ic, event);
		}

		return new NodeFactory().createEventNode(ic, event);
	}

	private boolean processComponentEvent(ComponentEventNode ce) {
		// Dynamic Component
		String temp = null;
		if (ce.getDyValue() != null) {
			temp = ce.getDyValue();
			ce.setDyValue(checkDynamicValue(ce.getDyValue()));
		}
		EventNode en = transformComponentEvent(ce);
		Arguments arglist = en.getEvent().getArguments().clone();
		// 檢查 argument 中是否有替代參數
		checkArgumentDomainValue(en.getEvent().getArguments());
		// 轉成TestScript的EventNode，讓 EventFirver 來發事件
		// m_EventFirer.setSleepTime(1000);
		boolean result = m_Tester.fire(en);

		// 回復dyValue
		if (ce.getDyValue() != null) {
			if (!ce.getDyValue().isEmpty()) {
				ce.setDyValue(temp);
				ce.getComponent().getComponent().setName("");
				ce.getComponent().getComponent().setText("");
				ce.getComponent().getComponent().setTitle("");
				ce.getComponent().getComponent().setWinType("");
				ce.getComponent().getComponent().setIndex(1);
			}
		}

		// 恢復argument list
		en.getEvent().setArguments(arglist);
		updateComponentCoverage(en.getComponent());

		return result;
	}

	private boolean processDynamicComponentEvent(DynamicComponentEventNode node) {
		DynamicComponentNode component = node.getComponent();
		ArrayList<EventNode> eventNodes = null;
		String altSrc = "";
		boolean result = true;
		
		// When the component is not exited
		if (component == null) {
			return false;
		}
		
		// When the source contains the '@' sign, it will be translated into an expected
		// value(the component name)
		altSrc = checkDynamicValue(component.getSource());
		
		// Dynamic finding
		component.dynamicFinding(altSrc);

		// Produce EventNode by IComponent and IEvent
		eventNodes = transformDynamicEvents(node);
		if (eventNodes == null) {
			return false;
		}
		
		// Get EventNodes from the list and fire it
		for(EventNode en : eventNodes) {
			Arguments argList = en.getEvent().getArguments().clone();
			checkArgumentDomainValue(en.getEvent().getArguments());
			
			// Fire EventNode
			m_Tester.setSleepTime(1000);
			result = m_Tester.fire(en);
			
			// Revert the argument list
			en.getEvent().setArguments(argList);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<EventNode> transformDynamicEvents(DynamicComponentEventNode node) {
		String target = DynamicComponentNode.DEFAULT_TARGET;
		ArrayList<EventNode> eventNodes = null;
		ArrayList<ComponentNode> compNodes = null;
		
		// Get the list of component nodes from DataPool
		compNodes = (ArrayList<ComponentNode>)node.getVariable(target);
		if (compNodes == null) {
			return null;
		}
		
		// Pack EventNodes by IComponent and IEvent
		eventNodes = new ArrayList<EventNode>();
		for(ComponentNode n : compNodes) {
			IComponent ic = n.getComponent();
			if (ic == null) {
				return null;
			}
			
			IEvent event = m_EventModel.getEvent(ic, node.getEventType());
			event.setArguments(node.getArguments().clone());
			
			eventNodes.add(new NodeFactory().createEventNode(ic, event));
		}
		
		return eventNodes;
	}
	
	private boolean processSleeperNode(SleeperNode node) {
		try {
			Thread.sleep(node.getSleepTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean processMacroEventCaller(MacroEventCallerNode node)
			throws Exception {
		// Check special events of the System component
		String paths[] = node.getReferencePath().split(MacroPath.PATH_SEPARATOR);
		if (paths[1].equalsIgnoreCase("SYSTEM")) {
			if (paths[2].equalsIgnoreCase("SplitBySign")) {
				return processSyetemSplitBySign(node);
			}
			
			if (paths[2].equalsIgnoreCase("SplitByWeight")) {
				return processSyetemSplitByWeight(node);
			}
			
			if (paths[2].equalsIgnoreCase("LoadTesting")) {
				return processSyetemLoadTesting(node);		
			}
		}
		
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
	
	// uhsing 2011.05.17
	// Process a event of SplitBySign which is in the System component
	private boolean processSyetemSplitBySign(MacroEventCallerNode node) {
		Arguments args = node.getArguments();
		String data = checkDynamicValue(args.getValue("Data"));
		String sign = checkDynamicValue(args.getValue("Sign"));
		String target = checkDynamicValue(args.getValue("Target"));
		
		if (target.equals("") || sign.equals("")) {
			return false;
		}
		
		if (data.equals("")) {
			// Use the target(variable) as a key to store the null object
			node.setVariable(target, null);
			return false;
		}
		
		// Split the data by the specific separator
		String[] datas = data.split(sign);
		
		// Copy the component name from a string array into the list
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < datas.length; i++) {
			list.add(datas[i]);
		}
		
		// Use the target(variable) as a key to store the result
		node.setVariable(target, list);
		
		return true;
	}

	// uhsing 2011.06.17
	// Process a event of SplitByWeight which is in the System component
	private boolean processSyetemSplitByWeight(MacroEventCallerNode node) {
		Arguments args = node.getArguments();
		String data = checkDynamicValue(args.getValue("Data"));
		String weight = checkDynamicValue(args.getValue("Weight"));
		String target = checkDynamicValue(args.getValue("Target"));
		
		if (target.equals("") || weight.equals("")) {
			return false;
		}
		
		if (data.equals("")) {
			// Use the target(variable) as a key to store the null object
			node.setVariable(target, null);
			return false;
		}

		// Transfer the weight to the integer type
		int w = Integer.parseInt(weight);
		int count = data.length() / w;
		boolean isRemaining = ((data.length() % w) == 0) ? false : true;
		
		// Split the data by weight and store it into the list
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < count; i++) {
			String temp = "";
			
			for(int j = 0; j < w; j++) {
				temp += data.charAt((w * i) + j);
			}
			list.add(temp);
		}
		
		// The remaining characters will append to the list
		if (isRemaining) {
			String temp = "";
			
			for(int i = (w * count); i < data.length(); i++) {
				temp += data.charAt(i);
			}
			list.add(temp);
		}
		
		// Use the target(variable) as a key to store the result
		node.setVariable(target, list);
		
		return true;
	}
	
	// 暫定處理 system 的 loadtesting event
	private boolean processSyetemLoadTesting(MacroEventCallerNode node) throws Exception {
		Arguments args = node.getArguments();
		String macroEvent = checkDynamicValue(args.getValue("MacroEvent"));
		
		AbstractMacroNode tempRef = m_MacroDocument.findByPath(macroEvent);
		if(tempRef == null)
			throw new NullPointerException(node + " is not found.");
		if(tempRef instanceof MacroEventNode) {
			boolean result = processMacroEvent((MacroEventNode) tempRef);
			return result;
		}	
		
		return false;
	}
	
	// uhsing 2011.03.18 for SplitDataNode
	private boolean processSplitData(SplitDataNode node) {
		String target = node.getTarget();
		String separator = node.getSeparator();
		String data = node.getData();
		String[] datas = null;
		
		if (target.equals("") || separator.equals("")) {
			return false;
		}
		
		if (data.equals("")) {
			// Use the target(variable) as a key to store the null object
			node.setVariable(target, null);
			return false;
		}
		
		// Check the data is a real(meaningful) data or not
		// When the data contains a '@' sign, it will return a expected value
		// But otherwise it will just return an original value
		data = checkDynamicValue(data);
		
		// Split the data by the specific separator
		datas = data.split(separator);
		
		// Copy the component name from a string array into the list
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < datas.length; i++) {
			list.add(datas[i]);
		}
		
		// Use the target(variable) as a key to store the result
		node.setVariable(target, list);
		
		return true;
	}
	
	private void updateComponentCoverage(IComponent comp) {
		if (m_collector == null)
			return;
		if (!m_collector.isCollect())
			return;

		m_collector.collect(m_collector.getAllComps(), null, m_collector
				.getMainWindow(), "", "");

		if (!m_collector.getUsedComps().contains(comp))
			m_collector.getUsedComps().add(comp);
		// m_collector.storeComponent(m_collector.getUsedComps(), ic);
	}

	// 檢查 argument 中是否有替代參數
	private void checkArgumentDomainValue(Arguments arglist) {
		if (m_refArgument == null) {
			return;
		}
		Iterator<Argument> ite = arglist.iterator();
		while (ite.hasNext()) {
			Argument arg = ite.next();
			// 檢查是否為 % 開頭的value
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

	private String checkDynamicValue(String value) {
		// 檢查是否為 @ 開頭的value
		if (value.indexOf(REF_ARGUMENT_PREFIX) == 0 && m_refArgument != null) {
			String newValue = m_refArgument.getValue(value.substring(1));
			return newValue;
		}
		return value;
	}

	private void logging(String msg) {
		// 應該使用 logging 機制，會比較乾淨
		// zws 2007/01/04
		// String prefix = "[MacroTester]";
		// Logger.getSimpleLogger().log(prefix + "-" + msg);
		// System.out.println(prefix+msg);
	}

	/**
	 * For FitRunner
	 * 
	 * @param info
	 */

	/**
	 * preEdition of FitRunner move into MacroTester for integration Fit into
	 * Macro
	 */
	FitInfoCollector m_fitInfoCollector = new FitInfoCollector();
	ComponentNamePool m_componentNamePool = new ComponentNamePool();
	AbstractFitFixture m_fixture;

	private boolean processFit(FitNode node) {
		try {
			initialize(node);
			for (int i = 0; i < m_fitInfoCollector.getFitData().getFitTable()
					.getRowElement().size(); i++) {
				if (m_fixture.getMacroEventNode(i) != null) {
					try {
						testResult(m_fitInfoCollector.getFitData()
								.getFitTable().getRowElement().get(i),
								fire(m_fixture.getMacroEventNode(i)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (!m_fitInfoCollector.updateTable())
				System.out.println("MacroTesater updateTable Fail!");
		} catch (NullPointerException e) {
			System.err.println("Triger Fit node error");
		}
		return false;
	}

	// initialize Fit Node data
	private void initialize(FitNode node) {
		m_fitInfoCollector.collectAll(node);

		if (node.getFixtureType().compareTo(FixtureDefinition.ActionFixture) == 0)
			m_fixture = new ActionFixture(m_fitInfoCollector.getFitData()
					.getFitTable(), node.getMacroEventCallerNode(),
					m_MacroDocument);
		else if (node.getFixtureType().compareTo(
				FixtureDefinition.ColumnFixture) == 0)
			m_fixture = new ColumnFixture(m_fitInfoCollector.getFitData()
					.getFitTable(), node.getMacroEventCallerNode(),
					m_MacroDocument);
		else if (node.getFixtureType().compareTo(FixtureDefinition.RowFixture) == 0)
			m_fixture = new RowFixture(m_fitInfoCollector.getFitData()
					.getFitTable(), node.getMacroEventCallerNode(),
					m_MacroDocument);

		setupComponentNamePool(node);
	}

	// 增入EventTrigger node之處理 by Pan
	private boolean processEventTriggerNode(EventTriggerNode abs) {
		try {
			Arguments argumentList = ((MacroEventNode) abs.getParent())
					.getArguments();
			abs.setArguments(argumentList);
			updateComponentNamePool(argumentList);
			return new EventTriggerNodeProcessor(m_Tester, m_componentNamePool)
					.process(abs);
		} catch (NullPointerException e) {
			return false;
		}
	}

	// 增入Fit state assertion node之處理 by Pan
	private boolean processFitStateAssertionNode(FitStateAssertionNode abs) {
		try {
			abs.setArguments(((MacroEventNode) abs.getParent()).getArguments());
			return new FitStateAssertionNodeProcessor(m_Checker).process(abs);
		} catch (NullPointerException e) {
			return false;
		}
	}

	// 增入Fit assertion node之處理 by Pan
	private boolean processFitAssertionNode(FitAssertionNode abs) {
		try {
			Arguments argumentList = ((MacroEventNode) abs.getParent())
					.getArguments();
			abs.setArguments(argumentList);
			updateComponentNamePool(argumentList);
			return new FitAssertionNodeProcessor(m_Checker, m_componentNamePool)
					.process(abs);
		} catch (NullPointerException e) {
			return false;
		}
	}

	private void setupComponentNamePool(FitNode node) {
		AbstractMacroNode[] children = node.getChildren();
		if (children.length == 0)
			return;
		for (int i = 0; i < children.length; i++)
			m_componentNamePool.putNode(children[i]);
	}

	private void updateComponentNamePool(Arguments list) {
		m_componentNamePool.setArgumentList(list);
		m_componentNamePool.generate();
	}

	private void testResult(TableRow row, boolean result) {
		for (int i = 0; i < row.getSize(); i++) {
			if (result)
				row.get(i).setColor(SWT.COLOR_DARK_GREEN);
			else
				row.get(i).setColor(SWT.COLOR_RED);
		}
	}

	private IOracleHandler m_OracleHandler;

	public void setOracleHandler(IOracleHandler handler) {
		m_OracleHandler = handler;
	}

	// for OracleNode - zwshen 2009/12/23
	private boolean processOracleNode(OracleNode oracle) {
		if (m_OracleHandler == null)
			return true;

		return m_OracleHandler.handle(oracle.getOracleData());
	}

	private void recordFailureLog(AbstractMacroNode abs) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("MacroGUITestResult.txt", true);
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
	
	private void recordLoadTestingResultLog(String path, String type) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("tempResult.txt", true);
			int iTime = (int) Math.floor((System.currentTimeMillis()-m_startTime)/1000);
			out.write((type+","+path+","+iTime+"\n").getBytes());
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

}
