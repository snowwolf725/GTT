/**
 * 
 */
package gttlipse.scriptEditor.interpreter;

import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.oracle.AssertionChecker;
import gtt.oracle.ComponentCollector;
import gtt.oracle.OracleHandler;
import gtt.oracle.OracleUtil;
import gtt.oracle.SwingChecker;
import gtt.runner.Controller;
import gtt.runner.web.WebController;
import gtt.tester.macro.MacroTester;
import gtt.tester.macro.Reporter;
import gtt.tester.swing.ITester;
import gtt.tester.swing.SwingTester;
import gtt.tester.web.WebTester;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gtt.web.oracle.WebAssertionChecker;
import gttlipse.GTTFileSaver;
import gttlipse.GTTlipseConfig;
import gttlipse.TestProject;
import gttlipse.fit.node.ReferenceFitNode;
import gttlipse.scriptEditor.def.TestScriptTage;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import java.awt.Window;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;

import junit.framework.TestCase;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.interpreter
 * 
 */
public class Interpreter {
	
	private TestCaseNode m_classnode;

	private String m_methodname;

	private TestScriptDocument m_doc;

	private static MacroDocument m_macroDoc;

	private static Controller m_controller = new Controller();

	private static boolean m_doTestOracle = false;

	private static boolean m_doCollectCompInfo = false;

	private static int m_indexOfDoc = -1;

	private static GTTlipseConfig m_gttconfig;

	private static Reporter m_reporter = null;

	private static ComponentCollector m_collector = null;

	public Interpreter(Object obj) {
		if (obj == null) {
			System.err.println("[ERROR] Object is null.");
			return;
		}
		if (obj.getClass() == null) {
			System.err.println("[ERROR] Interpreter() in Class Reflection.");
			return;
		}

		initInterpreter(obj);
	}

	private void initInterpreter(Object obj) {
		m_macroDoc = new MacroDocument();

		// init fail log
		initFailLog();
		// init comp collector
		m_collector = new ComponentCollector();
		// load script
		doLoadScript();
		// find class node
		m_classnode = InterpreterUtil.findClassNode(obj);

		if (m_classnode == null) {
			System.err.println("[ERROR] class not found.");
			return;
		}

		m_reporter = new Reporter(m_macroDoc);
		
	}

	public Interpreter() {

	}

	private void doLoadScript() {
		m_gttconfig = TestProject.loadConfig();

		switch (m_gttconfig.getMode()) {
		case GTTlipseConfig.REPLAY_MODE:
			break;
		case GTTlipseConfig.COLLECT_MODE:
			// 收集oracle 模式
			m_doCollectCompInfo = true;
			m_collector.setCollect(true);
			break;
		case GTTlipseConfig.ORACLE_MODE:
			// 執行oracle 模式
			m_doTestOracle = true;
			break;
		}

		// load Script
		TestProject.loadTestScript();

		// load Macro Script
		m_macroDoc.openFile(TestProject.PROJECT_FILENAME);
	}

	private void doSaveScript() {
		GTTFileSaver saver = new GTTFileSaver();

		m_reporter.doSaveReport();
		m_reporter.doRemoveNDefNode();

		saver.doSave(TestProject.getProject(), m_macroDoc.getMacroScript(),
				TestProject.PROJECT_FILENAME, m_gttconfig);
	}

	public void GTTTestScript(String methodname, String scriptname) {
		if (methodname.equals(m_methodname) == false) {
			/* reset doc index */
			m_indexOfDoc = -1;
		}
		m_indexOfDoc++;
		m_methodname = methodname;
		m_doc = getDoc(methodname, scriptname);
		if (m_doc == null) {
			m_doc = getDoc("setUp", scriptname);
			m_indexOfDoc = -1;
		}
		if (m_doc == null) {
			m_doc = getDoc("tearDown", scriptname);
			m_indexOfDoc = -1;
		}
		if (m_doc == null) {
			System.err.println("TestScript Document not found");
			return;
		}

		playScript(m_doc.getScript());
		m_reporter.setCoverageInfo(m_collector.getAllComps(), m_collector
				.getUsedComps());
		m_reporter.computeCoverage();
		doSaveScript();
		
	}

	private void playScript(AbstractNode anode) {
		if (anode instanceof FolderNode) {
			// 遞迴處理subnode
			for (int i = 0; i < anode.size(); i++)
				playScript(anode.get(i));
			return;
		}

		if (anode instanceof EventNode) {
			processEventNode((EventNode) anode);
			return;
		}

		if (anode instanceof ViewAssertNode) {
			processViewAssertNode((ViewAssertNode) anode);
			return;
		}

		if (anode instanceof LaunchNode) {
			setupWindow((LaunchNode) anode);
			return;
		}

		if (anode instanceof ReferenceMacroEventNode) {
			processRefMacroEventNode((ReferenceMacroEventNode) anode);
			return;
		}

		if (anode instanceof ReferenceFitNode) {
			processReferenceFitNode((ReferenceFitNode) anode);
			return;
		}

		if (anode instanceof OracleNode) {
			processOracleNode((OracleNode) anode);
			return;
		}
	}

	private void processRefMacroEventNode(ReferenceMacroEventNode refnode) {
		AbstractMacroNode node = m_macroDoc.findByPath(refnode.getRefPath());
		if (node == null)
			return;
		
		if (node instanceof MacroEventNode)
			m_reporter.setCoverage((MacroEventNode) node);
		try {
			if (GTTlipseConfig.testingOnSwingPlatform()) {
				m_collector.setMainWindow((Window) m_controller
						.getAppMainWindow());
			}
			MacroTester tester = new MacroTester(m_macroDoc, m_reporter,
					m_collector);
			GTTlipseConfig m_gttconfig = TestProject.loadConfig();
			if (m_gttconfig != null)
				tester.setGlobalSleeperTime(m_gttconfig.getSleepTime());

			// 如果是web測試就得換掉tester

			if (tester.fire(refnode) == false) {
				recordFailureLog(refnode);
				alertJUnitFail(refnode.toString() + " Error\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processReferenceFitNode(ReferenceFitNode refnode) {
		try {
			m_collector.setMainWindow((Window) m_controller.getAppMainWindow());
			MacroTester tester = new MacroTester(m_macroDoc, m_reporter,
					m_collector);

			GTTlipseConfig m_gttconfig = TestProject.loadConfig();
			if (m_gttconfig != null)
				tester.setGlobalSleeperTime(m_gttconfig.getSleepTime());
			if (tester.fire(refnode) == false) {
				alertJUnitFail(refnode.toString() + " Error\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processEventNode(EventNode eventnode) {

		if (m_doCollectCompInfo == true) {
			m_collector.collect(m_collector.getAllComps(), null, m_controller
					.getAppMainWindow(), "", "");

			if (!m_collector.getUsedComps().contains(eventnode.getComponent()))
				m_collector.getUsedComps().add(eventnode.getComponent());

			// m_collector.storeComponent(m_collector.getUsedComps(),
			// eventnode
			// .getComponent());
		}

		m_reporter.setCoverage((EventNode) eventnode);

		boolean result = fireEvent((EventNode) eventnode);
		if (result == false) {
			recordFailureLog(eventnode);
		}
	}

	private void processOracleNode(OracleNode node) {
		OracleHandler handler = new OracleHandler(m_collector, m_controller
				.getAppMainWindow());
		boolean result = handler.handle(node.getOracleData());
		if (result == false) // 測試失敗
		{
			// 通知JUnit 失敗
			String msg = "TestOracle [" + node.getOracleData().getUUID()
					+ "] fails.";
			alertJUnitFail(msg);
		}

	}

	// 處理 ViewAssertNode
	private void processViewAssertNode(ViewAssertNode va) {
		if (m_doTestOracle == true) {
			// 正在產生 oracle value資料
			OracleUtil.setupExpectedValue(va);
			return;
		}

		AssertionChecker asserter = null;

		if (GTTlipseConfig.testingOnSwingPlatform()) {
			asserter = new SwingChecker();
		} else if (GTTlipseConfig.testingOnWebPlatform()) {
			asserter = new WebAssertionChecker();
		} else {
			System.out.println("[error] This is "
					+ GTTlipseConfig.getInstance().getPlatformOfTesting());
			System.out.println("What is asserter type ?");
		}

		boolean result = asserter.check(va.getComponent(), va.getAssertion());

		if (result == true)
			return; // 測試成功 - returned

		if (va.getParent() instanceof OracleNode) {
			// case1: 為 test oracle 測試失敗
			va.setActualValue(asserter.getActualValue());
			// oracle 本身會有執行結果
			recordFailureLog(va.getParent());
			return;
		}

		// case2: 為一般view assert 測試失敗
		recordFailureLog(va);
		alertJUnitFail(va.toString() + " [actual:" + asserter.getActualValue()
				+ "]");		
	}

	private void alertJUnitFail(String msg) {
		// 通知JUnit有測試失敗發生
		TestCase.fail("[gttlipse]" + msg);
	}

	private void initFailLog() {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(TestScriptTage.TESTRESULTFILE, false);
			out.write("init\n".getBytes());
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

	private void recordFailureLog(AbstractNode anode) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(TestScriptTage.TESTRESULTFILE, true);
			String failpath = "";
			TestMethodNode method = (TestMethodNode) m_classnode
					.getChildrenByName(m_methodname);
			String treepath = getAbstractNodeIndexPath(anode);
			failpath = method.getPath() + "/" + (method.indexOf(m_doc) - 1)
					+ treepath + "\n";
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

	private String getAbstractNodeIndexPath(AbstractNode node) {
		String result = "";
		AbstractNode parent = node;
		while (true) {
			if (parent == null || node == null)
				break;
			if ((parent instanceof FolderNode)
					&& parent.toSimpleString().equals("Root")) {
				break;
			}
			result = "/" + parent.getParent().indexOf(parent) + result;
			parent = parent.getParent();
		}
		return result;
	}

	public void setAUT(Window win) {
		m_controller.setAUT(win);
	}

	private void setupWindow(LaunchNode node) {
		if (GTTlipseConfig.testingOnSwingPlatform()) {
			m_controller.reset();
			m_controller.loadAUTbyMain(node.getLaunchData());
			m_controller.showAppWindow();
		} else {
			// if GTT runing in web mode AUTInfo node to create web page

			if (node.getClassName().equals("UP"))
				WebController.instance().getDriver().navigate().back();
			else if (node.getClassName().equals("DOWN"))
				WebController.instance().getDriver().navigate().forward();
			else
				WebController.instance().startupWebPage(node.getArgument());
		}
	}

	private boolean fireEvent(EventNode event) {
		try {
			ITester m_EventFirer = null;
			if (GTTlipseConfig.testingOnSwingPlatform()) {
				m_EventFirer = new SwingTester();
				System.out.println("Select Java test model!");
			} else if (GTTlipseConfig.testingOnWebPlatform()) {
				m_EventFirer = new WebTester();
				System.out.println("Select Web test model after!");
			} else {
				System.out.println("[error] This is "
						+ GTTlipseConfig.getInstance().getPlatformOfTesting());
				System.out.println("What is FireEvent type ?");
			}

			boolean result = m_EventFirer.fire(event);
			if (m_gttconfig == null)
				Thread.sleep(1000);
			else
				Thread.sleep(m_gttconfig.getSleepTime());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private TestScriptDocument getDoc(String methodname, String scriptname) {
		for (int i = 0; i < m_classnode.size(); i++) {
			if (!methodname.equals(m_classnode.getChildrenAt(i).getName()))
				continue;

			TestMethodNode method = (TestMethodNode) m_classnode
					.getChildrenAt(i);

			if (method.getDocAt(m_indexOfDoc) == null)
				continue;

			if (scriptname.equals(method.getDocAt(m_indexOfDoc).getName())) {
				return method.getDocAt(m_indexOfDoc);
			}
		}
		// non-exists document
		return null;
	}

	public Window getMainWindow() {
		return m_controller.getAppMainWindow();
	}

	public Window getWindow(String title) {
		Window mainwin = m_controller.getAppMainWindow();
		for (Window win : mainwin.getOwnedWindows()) {
			if (win.isVisible() == false)
				continue;
			if (win instanceof JFrame) {
				JFrame frm = (JFrame) win;
				if (frm.getTitle().equals(title)) {
					return frm;
				}
			} else if (win instanceof JDialog) {
				JDialog dialog = (JDialog) win;
				if (dialog.getTitle().equals(title)) {
					return dialog;
				}
			}
		}
		return null;
	}

}
