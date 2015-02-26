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
package gtt.editor.presenter;

import gtt.editor.configuration.ConfigSave;
import gtt.editor.configuration.ConfigurationData;
import gtt.editor.configuration.IConfiguration;
import gtt.editor.view.AcquireUserInput;
import gtt.editor.view.IEditor;
import gtt.editor.view.TreeNodeData;
import gtt.editor.view.dialog.ConfigurationDialog;
import gtt.macro.IMacroPresenter;
import gtt.macro.MacroDocument;
import gtt.macro.io.EBNFSaveVisitor;
import gtt.macro.io.XmlMacroSaveVisitor;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.mfsm.ContractTestCaseGeneration;
import gtt.macro.transform.MacroTransform;
import gtt.macro.visitor.MacroCountingVisitor;
import gtt.macro.visitor.MacroStatisticsVisitor;
import gtt.runner.Controller;
import gtt.runner.PlaybackThread;
import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.io.TestScriptXmlSaveVisitor;
import gtt.testscript.visitor.TestScriptCountingVisitor;
import gtt.util.reverser.GUILayoutReverser;

import java.awt.Container;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MenuBarPresenter implements IMenuBarPresenter {

	ITestScriptPresenter m_tsPresenter;

	IMacroPresenter m_mPresenter;

	IEditor m_MainEditor;

	public void attach(IEditor v) {
		m_MainEditor = v;
	}

	public IEditor getView() {
		return m_MainEditor;
	}

	// =====================================================================
	// Constructor
	MenuBarPresenter(ITestScriptPresenter script_p, IMacroPresenter macro_p) {
		m_tsPresenter = script_p;
		m_mPresenter = macro_p;

		m_tsPresenter.setConfiguration(m_Configuration);
	}

	// ///////////////////////////////////////////////////////////////////
	// Configuration data
	IConfiguration m_Configuration = new ConfigurationData();

	public boolean configuration() {
		new ConfigurationDialog(m_Configuration).setVisible(true);

		return true;
	}

	public static MenuBarPresenter create(ITestScriptPresenter script_p,
			IMacroPresenter macro_p) {

		return new MenuBarPresenter(script_p, macro_p);
	}

	// ///////////////////////////////////////////////////////////////////
	// File I/O
	public boolean newFile() {

		// m_AUTHolder.terminateAUTApp();
		m_AUTController.terminate();

		// Document 內容有變動，需要更新JTree model
		// updateViewFromDocument();
		initNewDocument();

		// 預設存檔的檔名
		m_FilePath = null;

		m_Configuration = new ConfigurationData();
		m_tsPresenter.setConfiguration(m_Configuration);

		return true;
	}

	private void initNewDocument() {
		// test-script
		m_tsPresenter.setModel(TestScriptDocument.create());
		m_tsPresenter.updateView();
		// macro
		m_mPresenter.setModel(MacroDocument.create());
		m_mPresenter.updateView();
	}

	public boolean openFile() {
		String path = AcquireUserInput.acquireFilePath();
		if (path == null)
			return false;

		try {
			// test-script
			m_tsPresenter.handleOpenFile(path);
			// macro
			m_mPresenter.handleOpenFile(path);
			// Configuratiion information
			m_Configuration.openFile(path);

			// if (m_Configuration.getAUTFilePath() != "") {
			// // 載入並啟動 AUT
			// loadAUT(m_Configuration.getAUTFilePath(), m_Configuration
			// .getClassPath());
			// }
			// 記下檔名，以後可以直接存檔
			m_FilePath = path;

		} catch (Exception exp) {
			exp.printStackTrace();
			showMessage("\"" + path + "\" open fail.");
			return false;
		}

		showMessage("\"" + path + "\" open ok.");
		return true;
	}

	// private Controller m_controller = new Controller();
	private Controller m_AUTController = new Controller();

	// private void initDocument() {
	// // test-script
	// m_tsPresenter.setModel(TestScriptDocument.create());
	// m_tsPresenter.updateView();
	//
	// // macro
	// m_mPresenter.setModel(MacroDocument.create());
	// m_mPresenter.updateView();
	// }

	/* 載入待測應用程式 2005/06/14 */
	public boolean launchAUT() {
		if (m_Configuration.getAUTFilePath() == null) {
			configuration(); // 請使用者設定configuration
			return true;
		}

		// 載入AUT
		m_AUTController.loadAUT(m_Configuration);
		return true;
	}

	// 關閉待測應用程貸
	public boolean terimentAUT() {
		m_AUTController.terminate();
		return true;
	}

	String m_FilePath = null;

	public boolean saveFile() {
		if (m_FilePath == null) {
			m_FilePath = AcquireUserInput.acquireFilePath();
			if (m_FilePath == null)
				return false;
		}

		return _saveFile();
	}

	// saveAsFile 一律要詢問存檔檔名
	public boolean saveAsFile() {
		m_FilePath = AcquireUserInput.acquireFilePath();
		if (m_FilePath == null)
			return false;
		return _saveFile();
	}

	private boolean _saveFile() {
		try {
			Document xmlDocument = new org.apache.xerces.dom.DocumentImpl();
			Element xmlRoot = xmlDocument.createElement("GTT");
			xmlDocument.appendChild(xmlRoot);

			// testscript 的存檔
			TestScriptXmlSaveVisitor scriptv = new TestScriptXmlSaveVisitor(
					xmlDocument);
			m_tsPresenter.getModel().getScript().accept(scriptv);

			// macro 的存檔
			XmlMacroSaveVisitor macrov = new XmlMacroSaveVisitor(xmlDocument);
			m_mPresenter.getModel().getMacroScript().accept(macrov);

			// config 的存檔
			ConfigSave csaver = new ConfigSave(m_Configuration, xmlDocument);
			csaver.buildConfig();

			xmlRoot.appendChild(csaver.getConfigRoot());
			xmlRoot.appendChild(scriptv.getScriptRoot());
			xmlRoot.appendChild(macrov.getMacroRoot());

			saveXML(m_FilePath, xmlDocument);
			Runtime.getRuntime().freeMemory();
			Runtime.getRuntime().gc();
		} catch (Exception exp) {
			exp.printStackTrace();
			showMessage("\"" + m_FilePath + "\" save fail");
			return false;
		}

		showMessage("\"" + m_FilePath + "\" save ok");
		return true;
	}

	private boolean saveXML(String filepath, Document doc) {
		try {
			// Serialize DOM
			StringWriter stringOut = new StringWriter();
			com.sun.org.apache.xml.internal.serialize.XMLSerializer serial = new com.sun.org.apache.xml.internal.serialize.XMLSerializer(
					stringOut, createXmlFormat(doc));
			serial.asDOMSerializer(); // As a DOM Serializer
			serial.serialize(doc.getDocumentElement());
			PrintWriter writer = new PrintWriter(new FileOutputStream(filepath));
			writer.print(stringOut.toString());
			writer.flush();
			writer.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	// add by David Wu(2007-06-06)
	private com.sun.org.apache.xml.internal.serialize.OutputFormat createXmlFormat(Document doc) {
		com.sun.org.apache.xml.internal.serialize.OutputFormat format = new com.sun.org.apache.xml.internal.serialize.OutputFormat(
				doc);
		format.setEncoding("BIG5");
		format.setIndenting(true);
		format.setLineWidth(1000);
		return format;
	}

	// ///////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////
	// C/R
	public boolean loadAUT(String filepath, String classpath) {
		m_Configuration.setAUTFilePath(filepath);
		m_Configuration.setClassPath(classpath);

		m_AUTController.loadAUT(m_Configuration);
		return true;
	}

	public boolean startCapture() {
		if (m_Configuration.getAUTFilePath() == null) {
			showMessage("Please load AUT at first.");
			return false;
		}

		m_AUTController.loadAUT(m_Configuration);
		m_AUTController.showAppWindow();
		m_AUTController.startRecord();
		return true;
	}

	public void resetTestScript() {
		resetTestScriptNode((DefaultMutableTreeNode) m_tsPresenter.getJTree()
				.getModel().getRoot());

		m_tsPresenter.getView().updateUI();
	}

	private void resetTestScriptNode(DefaultMutableTreeNode node) {
		TreeNodeData data = ((TreeNodeData) node.getUserObject());
		data.setColor(TreeNodeData.Color.NORMAL);
		int size = node.getChildCount();
		for (int i = 0; i < size; i++)
			resetTestScriptNode((DefaultMutableTreeNode) node.getChildAt(i));
	}

	public synchronized boolean replay() {
		resetTestScript();
		if (runnerIsExecuting()) {
			showMessage("Please wait replay process finished.");
			return false;
		}
		if (m_Configuration.getAUTFilePath() == null) {
			showMessage("Please load AUT at first.");
			return false;
		}

		return _replay();
	}

	PlaybackThread m_ReplayerThread = null;

	public synchronized boolean _replay() {
		try {
			if (runnerIsExecuting()) {
				showMessage("The last replay is running.");
				return true;
			}

			// 播放時，同時需要TestScriptPresenter 及 MacroPresenter
			m_ReplayerThread = PlaybackThread.create(m_tsPresenter,
					m_mPresenter, m_Configuration, m_AUTController);
			m_ReplayerThread
					.setTestResultView(m_MainEditor.getTestResultView());
			m_ReplayerThread.start();
			return true;
		} catch (Exception exp) {
			showMessage("replay exception:" + exp.toString());
		} finally {
			m_ReplayerThread = null;
		}
		return false;
	}

	private boolean runnerIsExecuting() {
		return m_ReplayerThread != null && m_ReplayerThread.isRun();
	}

	public boolean stopCapture() {
		if (m_ReplayerThread != null) {
			m_ReplayerThread.terminate();
			m_ReplayerThread = null;
			return true;
		}
		List<AbstractNode> result = m_AUTController.stopRecordAndGetResult(2);
		return m_tsPresenter.addNodes(result);
	}

	// ///////////////////////////////////////////////////////////////////
	// Macro Event to ReferenceMacroEventNode
	// 這裡並不做轉換，只是放一個 reference
	public boolean macroToScript() {
		// 使用者選擇一個MacroEventNode，可插入 TestScriptNode
		AbstractMacroNode mnode = m_mPresenter.getSelectedNode();
		if (mnode == null)
			return false;

		// 選擇的節點必須要是 MacroEventNode，才能移到 test script 中
		if (!(mnode instanceof MacroEventNode))
			return false;

		m_tsPresenter.insertRerenceMacroEventNode(mnode.getPath().toString());
		return true;
	}

	// Test Script to MacroComponent, MacroEvent
	public boolean ScriptToMacro() {
		// 要選定一個macro
		if (m_mPresenter.getView().getMacroTree().getSelectedMacroComponent() == null)
			return false;

		// 使用者選擇一個MacroEventNode，可插入 TestScriptNode
		AbstractNode node = m_tsPresenter.selectedNode();
		if (node == null)
			return false;

		// 將 AbstractNode 轉成 AbstractMacroNode
		AbstractMacroNode mc = MacroTransform.instance().transform(node);

		// 將轉換過後的macro新增到macro tree中
		m_mPresenter.getMacroTree().insertNode(mc);

		return true;
	}

	/**
	 * automatically component abstraction 2007/10/16
	 * 
	 * @author zwshen
	 */
	public boolean doGUIHierarchyReverse() {
		GUILayoutReverser gr = new GUILayoutReverser();
		gr.reverse((Container) m_AUTController.getAppMainWindow());

		m_mPresenter.getMacroTree().insertNode(gr.getRoot());

		return false;
	}

	public boolean doMacroStatistics() {
		DefaultMutableTreeNode node = m_mPresenter.getMacroTree()
				.getSelectedMacroComponent();
		if (node == null)
			return false;

		AbstractMacroNode mnode = (AbstractMacroNode) node.getUserObject();
		MacroStatisticsVisitor info = new MacroStatisticsVisitor();
		mnode.accept(info);
		showMessage("total primitive component:"
				+ info.getPrimitiveComponentSize());

		return false;
	}

	public boolean doNodesCounting() {
		StringBuilder result = new StringBuilder("");

		AbstractNode aNode = m_tsPresenter.selectedNode();
		if (aNode != null) {
			TestScriptCountingVisitor tcv = new TestScriptCountingVisitor();
			aNode.accept(tcv);
			result.append(tcv.toString());

		}

		AbstractMacroNode mNode = m_mPresenter.getSelectedNode();
		if (mNode != null) {
			MacroCountingVisitor mcv = new MacroCountingVisitor();
			mNode.accept(mcv);
			result.append(mcv.toString());
		}

		showMessage(result.toString());
		return false;
	}

	@Override
	public ITestScriptPresenter getScriptPresenter() {
		return m_tsPresenter;
	}

	@Override
	public boolean saveGTMLScript() {
		if (m_FilePath == null) {
			m_FilePath = AcquireUserInput.acquireFilePath();
			if (m_FilePath == null)
				return false;
		}

		return _saveGTMLFile();
	}

	private boolean _saveGTMLFile() {
		String path = m_FilePath + ".gtml";
		try {
			// macro 的存檔
			EBNFSaveVisitor visitor = new EBNFSaveVisitor();
			m_mPresenter.getModel().getMacroScript().accept(visitor);
			visitor.saveFile(path);

			Runtime.getRuntime().freeMemory();
			Runtime.getRuntime().gc();
		} catch (Exception exp) {
			showMessage("\"" + path + "\" save fail");
			return false;
		}
		showMessage("\"" + path + "\" save ok");
		return true;
	}

	@Override
	public boolean generateSCTestCsae() {
		ContractTestCaseGeneration gen = new ContractTestCaseGeneration();
		gen.run(m_FilePath, ContractTestCaseGeneration.STATE_COVERAGE);
		showMessage("State coverage test cases have been generated.");
		return true;
	}

	@Override
	public boolean generateECTestCsae() {
		ContractTestCaseGeneration gen = new ContractTestCaseGeneration();
		gen.run(m_FilePath, ContractTestCaseGeneration.EDGE_COVERAGE);
		showMessage("Edge coverage test cases have been generated.");
		return true;
	}

	@Override
	public boolean generateCCTestCsae() {
		ContractTestCaseGeneration gen = new ContractTestCaseGeneration();
		gen.run(m_FilePath, ContractTestCaseGeneration.CEDGE_COVERAGE);
		showMessage("Consectutive-edge coverage test cases have been generated.");
		return true;
	}

	private void showMessage(String msg) {
		getView().showMessage(msg);
	}

	// public boolean calculateContractLevel() {
	// CalcualteContractLevelVisitor visitor = new
	// CalcualteContractLevelVisitor();
	// m_mPresenter.getModel().getMacroScript().accept(visitor);
	// getView().showMessage("Calculate contract levels finished.");
	// getView().showMessage(visitor.getCountResult());
	//
	// return false;
	// }

}
