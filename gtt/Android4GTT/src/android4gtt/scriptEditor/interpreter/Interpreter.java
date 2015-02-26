package android4gtt.scriptEditor.interpreter;

import gtt.eventmodel.IEventModel;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.oracle.AssertionChecker;
import gtt.tester.swing.ITester;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.GTTlipseConfig;
import gttlipse.TestProject;
import gttlipse.scriptEditor.interpreter.InterpreterUtil;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.testScript.io.LoadScript;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android4gtt.AndroidPlatformInfo;
import android4gtt.eventmodel.AndroidModel;
import android4gtt.tester.android.AndroidChecker;
import android4gtt.tester.android.AndroidTester;
import android4gtt.tester.android.MacroTester;

import com.jayway.android.robotium.solo.Solo;

public class Interpreter {
	private TestCaseNode m_classnode;

	private String m_methodname;

	private TestScriptDocument m_doc;

	private static MacroDocument m_macroDoc;

	private static int m_indexOfDoc = -1;

	private static GTTlipseConfig m_gttconfig;
	
	private Solo m_solo = null;
	
	private Object m_res = null;
	
	private InputStream m_is = null;
	
	private InputStream m_desc_file = null;
	
	private String errorMsg = "";

	public Interpreter(Object obj, InputStream desc_file) {
		if (obj == null) {
			System.err.println("[ERROR] Object is null.");
			return;
		}
		if (obj.getClass() == null) {
			System.err.println("[ERROR] Interpreter() in Class Reflection.");
			return;
		}
		
		try {
			m_is = new FileInputStream("/sdcard/Android4GTT/GTTlipse.gtt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		m_desc_file = desc_file;

		initInterpreter(obj);
	}

	private void initInterpreter(Object obj) {
		m_macroDoc = new MacroDocument();

		// init fail log
		initFailLog();
		// load script
		doLoadScript();
		// find class node
		m_classnode = InterpreterUtil.findClassNode(obj);

		if (m_classnode == null) {
			System.err.println("[ERROR] class not found.");
			return;
		}

	}

	public Interpreter() {

	}

	private void doLoadScript() {
		m_gttconfig = TestProject.loadConfig();

		// load Script
		IEventModel theModel = new AndroidModel();
		theModel.initialize(m_desc_file);
		AndroidPlatformInfo.setEventModel(theModel);
		LoadScript loader = new LoadScript(theModel);
		loader.readFile("", m_is);
		try {
			m_desc_file.close();
			m_is.close();
			m_is = null;
			m_is = new FileInputStream("/sdcard/Android4GTT/GTTlipse.gtt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// load Macro Script
		m_macroDoc.openFile(m_is);
	}

//	private void doSaveScript() {
//		GTTFileSaver saver = new GTTFileSaver();
//
//		saver.doSave(TestProject.getProject(), m_macroDoc.getMacroScript(),
//				"res/raw/gttlipse.gtt", m_gttconfig);
//	}

	public String GTTTestScript(String methodname, String scriptname) {
		errorMsg = "";
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
			return "TestScript Document not found";
		}

		boolean result = playScript(m_doc.getScript());
		if(result == false)
			return errorMsg;
		else
			return "";
	}

	private boolean playScript(AbstractNode anode) {
		if (anode instanceof FolderNode) {
			// 遞迴處理subnode
			for (int i = 0; i < anode.size(); i++) {
				boolean result = playScript(anode.get(i));
				if (result == false) {
					return false;
				}
			}
			return true;
		}

		if (anode instanceof EventNode) {
			return processEventNode((EventNode) anode);
		}

		if (anode instanceof ViewAssertNode) {
			return processViewAssertNode((ViewAssertNode) anode);
		}

		if (anode instanceof ReferenceMacroEventNode) {
			return processRefMacroEventNode((ReferenceMacroEventNode) anode);
		}
		return false;
	}

	private boolean processRefMacroEventNode(ReferenceMacroEventNode refnode) {
		AbstractMacroNode node = m_macroDoc.findByPath(refnode.getRefPath());
		if (node == null)
			return false;
		
		try {
			MacroTester tester = new MacroTester(m_macroDoc, m_solo, m_res);
			tester.setModel(AndroidPlatformInfo.getEventModel());
			tester.setGlobalSleeperTime(500);
			GTTlipseConfig m_gttconfig = TestProject.loadConfig();
			if (m_gttconfig != null)
				tester.setGlobalSleeperTime(m_gttconfig.getSleepTime());

			if (tester.fire(refnode) == false) {
				recordFailureLog(refnode);
				setErrorMsg(refnode.toString() + " Error\n");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private boolean processEventNode(EventNode eventnode) {

		boolean result = fireEvent((EventNode) eventnode);
		if (result == false) {
			recordFailureLog(eventnode);
			setErrorMsg(eventnode.toString() + " Error\n");
			return false;
		}
		return true;
	}

	// 處理 ViewAssertNode
	private boolean processViewAssertNode(ViewAssertNode va) {
		AssertionChecker asserter = new AndroidChecker(m_solo, m_res);

		boolean result = asserter.check(va.getComponent(), va.getAssertion());

		if (result == false) {
			recordFailureLog(va);
			setErrorMsg(va.toString() + " [actual:" + asserter.getActualValue() + "]");
			return false;
		}
		return true;
	}

	private void setErrorMsg(String msg) {
		errorMsg = msg;
	}

	private void initFailLog() {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("/sdcard/Android4GTT/Test_Result.txt", false);
			out.write("Test Result : \n".getBytes());
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
		
		try {
			out = new FileOutputStream("/sdcard/Android4GTT/MacroGUITestResult.txt", false);
			out.write("".getBytes());
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
			out = new FileOutputStream("/sdcard/Android4GTT/Test_Result.txt", true);
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

	private boolean fireEvent(EventNode event) {
		try {
			ITester m_EventFirer = null;

			m_EventFirer = new AndroidTester(m_solo, m_res);
			m_EventFirer.setSleepTime(500);
			
			boolean result = m_EventFirer.fire(event);
			if (m_gttconfig == null)
				Thread.sleep(500);
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
	
	public void setSolo(Solo solo) {
		m_solo = solo;
	}
	
	public void setRes(Object res) {
		m_res = res;
	}
	
	public void finalize() {
		try {
			m_solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

