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
package gtt.editor.view;

import gtt.editor.presenter.IMenuBarPresenter;
import gtt.editor.presenter.ITestScriptPresenter;
import gtt.editor.presenter.MenuBarActionFactory;
import gtt.editor.presenter.MenuBarPresenter;
import gtt.editor.presenter.TestScriptPresenter;
import gtt.macro.IMacroPresenter;
import gtt.macro.MacroPresenter;
import gtt.macro.view.MacroView;
import gtt.runner.RunnerUtil;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainEditor extends JFrame implements IEditor {

	private static final long serialVersionUID = 1L;

	IView macro_view;
	IView script_view;
	TabbedView tabbed_view;
	ConsoleView console_view;
	TestResultView testresult_view;

	public TestResultView getTestResultView() {
		return testresult_view;
	}

	IMenuBarPresenter menubar_presenter;

	public static void main(String[] args) {
		// Document-View relationship
		new MainEditor().setVisible(true);
	}

	public MainEditor() {
		super("GUI Testing Tool");
		// init jemmy
		RunnerUtil.initJemmyModule();

		init();
		initMainToolBar();
		initMenuBar();
		initLayout();

		initLookAndFeel();

		showMessage("GTT Startup ok.");
	}

	private void initLookAndFeel() {
		// 預設使用 Windows style
		try {
			// LookAndFeel
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()
			// );
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			showMessage(e.getMessage());
		} catch (InstantiationException e) {
			showMessage(e.getMessage());
		} catch (IllegalAccessException e) {
			showMessage(e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			showMessage(e.getMessage());
		}
	}

	private void init() {
		initMainView();
		initTabbedView();
	}

	private void initMainView() {
		ITestScriptPresenter script_presenter = new TestScriptPresenter();
		IMacroPresenter macro_presenter = new MacroPresenter();

		menubar_presenter = MenuBarPresenter.create(script_presenter,
				macro_presenter);
		menubar_presenter.attach(this);

		script_view = TestScriptView.create(script_presenter);
		macro_view = MacroView.create(macro_presenter);
		// macro_presenter.setModel(MacroDocument.create());
	}

	private void initTabbedView() {
		tabbed_view = new TabbedView();
		console_view = new ConsoleView();
		testresult_view = new TestResultView(menubar_presenter
				.getScriptPresenter());

		tabbed_view.addTab(testresult_view);
		tabbed_view.addTab(console_view);
	}

	private void initLayout() {
		setSize(1024, 768);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JSplitPane p = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		p.setLeftComponent((JComponent) macro_view.getView());
		p.setRightComponent((JComponent) script_view.getView());
		p.setDividerLocation(450); // more spaces for macro panel
		p.setOneTouchExpandable(true);
		p.setContinuousLayout(true);

		JSplitPane p2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		p2.setTopComponent(p);
		p2.setBottomComponent(tabbed_view.getView());
		p2.setOneTouchExpandable(true);
		p2.setContinuousLayout(true);
		p2.setDividerLocation(500);

		getContentPane().add(p2);
		getContentPane().add(m_MainToolBar, BorderLayout.NORTH);
	}

	private void initToolBarActions() {
		// 使用 Factory 來統一管理、取得 Action
		// 跟 menubar 共用同一個 presenter
		MenuBarActionFactory factory = new MenuBarActionFactory(
				menubar_presenter);

		_newFileButton.addActionListener(factory.getFileNew());
		_openFileButton.addActionListener(factory.getFileOpen());
		_saveFileButton.addActionListener(factory.getFileSave());
		_saveAsFileButton.addActionListener(factory.getFileSaveAs());

		_recordingButton.addActionListener(factory.getStartCapture());
		_stopButton.addActionListener(factory.getStopCapture());
		_runButton.addActionListener(factory.getReplay());

		_launchAutButton.addActionListener(factory.getLaunchAUT());
		_terminatAutButton.addActionListener(factory.getTerminateAUT());

		_configButton.addActionListener(factory.getConfiguration());

		macroToScriptButton.addActionListener(factory.getMacroToScript());
		scriptToMacroButton.addActionListener(factory.getScriptToMacro());

	}

	private static final String IMAGE_DIRECTORY = System
			.getProperty("user.dir")
			+ "/images/";

	private void initToolBarNames() {
		m_MainToolBar.setName("toolBar");
		_saveFileButton.setName("saveFileB");
		_saveAsFileButton.setName("saveAsFileB");
		_openFileButton.setName("openFileB");
		_newFileButton.setName("newFileB");
		_recordingButton.setName("recordingB");
		_stopButton.setName("stopRecrodB");
		_runButton.setName("runB");
		_launchAutButton.setName("launchAppB");
		_terminatAutButton.setName("terminatAppB");
		_configButton.setName("conFigButton");

		macroToScriptButton.setName("MacroTree_MacroEventToTestScript");
		scriptToMacroButton.setName("MacroTree_TestScriptToMacroTree");

	}

	// =====================================================================
	// Tool bar 2007/03/20
	// =====================================================================
	private JToolBar m_MainToolBar = new JToolBar("File ToolBar");

	private JButton _saveFileButton, _saveAsFileButton, _openFileButton,
			_newFileButton, _recordingButton, _stopButton, _runButton,
			_launchAutButton, _terminatAutButton, _configButton;

	// testscript-macro 互轉 - zws 2007/05/23
	private JButton macroToScriptButton, scriptToMacroButton = null;
	
	private void initMainToolBar() {
		initToolBarButtons();
		initToolBarActions();
		initToolBarNames();
		initToolBarLayout();
	}

	private void initToolBarLayout() {
		m_MainToolBar.add(_newFileButton);
		m_MainToolBar.add(_openFileButton);
		m_MainToolBar.add(_saveFileButton);
		m_MainToolBar.add(_saveAsFileButton);

		m_MainToolBar.addSeparator();
		m_MainToolBar.add(_runButton);
		m_MainToolBar.add(_stopButton);
		m_MainToolBar.add(_recordingButton);

		m_MainToolBar.addSeparator();
		m_MainToolBar.add(_launchAutButton);
		m_MainToolBar.add(_terminatAutButton);
		m_MainToolBar.add(_configButton);

		m_MainToolBar.addSeparator();
		m_MainToolBar.add(macroToScriptButton);
		m_MainToolBar.add(scriptToMacroButton);

		m_MainToolBar.setFloatable(false);
		m_MainToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
	}

	private void initToolBarButtons() {
		_newFileButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "newFile.gif", IMAGE_DIRECTORY
						+ "newFile.gif", IMAGE_DIRECTORY + "newFile.gif", null,
				"New File", 25, 25, false);

		_openFileButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "openFile.gif", IMAGE_DIRECTORY
						+ "openFile.gif", IMAGE_DIRECTORY + "openFile.gif",
				null, "Open File", 25, 25, false);

		_saveFileButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "saveFile.gif", IMAGE_DIRECTORY
						+ "saveFile.gif", IMAGE_DIRECTORY + "saveFile.gif",
				null, "Save File", 25, 25, false);

		_saveAsFileButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "saveAsFile.gif", IMAGE_DIRECTORY
						+ "saveAsFile.gif", IMAGE_DIRECTORY + "saveAsFile.gif",
				null, "Save File As", 25, 25, false);

		_recordingButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "record.gif",
				IMAGE_DIRECTORY + "record2.gif",
				IMAGE_DIRECTORY + "record.gif", null, "Record", 25, 25, false);

		_stopButton = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "stop.gif", IMAGE_DIRECTORY + "stop2.gif", IMAGE_DIRECTORY
				+ "stop.gif", null, "Stop", 25, 25, false);

		// _addChecksByAutoButton = UIFactory.getButton("AutoAddChecks",80,25);
		_runButton = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "replay.gif", IMAGE_DIRECTORY + "replay2.gif",
				IMAGE_DIRECTORY + "replay.gif", null, "Run", 25, 25, false);

		_launchAutButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "openAUT.gif", IMAGE_DIRECTORY
						+ "openAUT.gif", IMAGE_DIRECTORY + "openAUT.gif", null,
				"Launch AUT", 25, 25, false);

		_terminatAutButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "reopenAUT.gif", IMAGE_DIRECTORY
						+ "reopenAUT.gif", IMAGE_DIRECTORY + "reopenAUT.gif",
				null, "Terminat AUT", 25, 25, false);

		_configButton = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "configuration.gif", IMAGE_DIRECTORY + "configuration.gif",
				IMAGE_DIRECTORY + "configuration.gif", null, "Configuration",
				23, 23, false);

		macroToScriptButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "insertToScript.gif", IMAGE_DIRECTORY
						+ "insertToScript.gif", IMAGE_DIRECTORY
						+ "insertToScript.gif", null,
				"Insert Macro Event to Test Script", 25, 25, false);

		scriptToMacroButton = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "cloneFromScript.gif", IMAGE_DIRECTORY
						+ "cloneFromScript.gif", IMAGE_DIRECTORY
						+ "cloneFromScript.gif", null,
				"Copy Test Script Data to Macro Node", 25, 25, false);
	}

	// =====================================================================
	// Menu bar 2006/12/08
	// =====================================================================
	private JMenuBar m_menuBar;

	private void initMenuBar() {
		m_menuBar = new JMenuBar();
		m_menuBar.setName("menuBar");
		setJMenuBar(m_menuBar);

		/**
		 * 使用 Simple Factory 來產生各menuitem 會用到的 action
		 *
		 * @1. 減化這個class 的 code-size 增進閱讀
		 * @2. 統一管理action
		 * @author zwshen 20051112
		 */
		MenuBarActionFactory factory = new MenuBarActionFactory(
				menubar_presenter);
		initMenuBarFile(factory);
		initMenuBarCR(factory);
		initMenuBarTools(factory);
		initMenuBarAUT(factory);
		initMenuBarAbout(factory);

				
	}

	private void initMenuBarAbout(MenuBarActionFactory factory) {
		// = = = = = = = = = = = = = = = = = =
		JMenu aboutMenu = new JMenu("About");
		aboutMenu.setName("menuAbout");

		// = = = = = = = = = = = = = = = = = =

		JMenuItem item;
		// = = = = = = = = = = = = = = = = = =
		item = aboutMenu.add(factory.getAbout());
		item.setName("menuAboutAbout");

		m_menuBar.add(aboutMenu);
	}

	private void initMenuBarTools(MenuBarActionFactory factory) {
		// = = = = = = = = = = = = = = = = = =
		JMenu menu = new JMenu("Tools");
		menu.setName("menuTools");
		menu.setMnemonic('T');

		JMenuItem item;

		item = menu.add(factory.getScriptReset());
		item.setName("menuScriptReset");
		menu.addSeparator();

		item = menu.add(factory.getComponentAbstraction());
		item.setName("menuComponentAbstraction");
		item = menu.add(factory.getMacroStatistics());
		item.setName("menuMacroStatistics");
		item = menu.add(factory.getNodeCounting());
		item.setName("menuNodesCounting");

		menu.addSeparator();
		item = menu.add(factory.getMacroToScript());
		item.setName("menuMacroToScript");
		item = menu.add(factory.getScriptToMacro());
		item.setName("menuScriptToMacro");

		
		menu.addSeparator();
//		item = menu.add(factory.getCreateMETS());
//		item.setName("menuCreateMETS");
		
		item = menu.add(factory.getStateCoverageTestGen());
		item = menu.add(factory.getEdgeCoverageTestGen());
		item = menu.add(factory.getCEdgeCoverageTestGen());
//		item.setName("menuCalculateContractLevel");	

		m_menuBar.add(menu);
	}

	private void initMenuBarAUT(MenuBarActionFactory factory) {
		// = = = = = = = = = = = = = = = = = =
		JMenu autMenu = new JMenu("AUT");
		autMenu.setToolTipText("Application Under Test");
		autMenu.setName("menuAUT");
		autMenu.setMnemonic('U');
		// = = = = = = = = = = = = = = = = = =
		JMenuItem item;
		// = = = = = = = = = = = = = = = = = =
		item = autMenu.add(factory.getLaunchAUT());
		item.setName("menuOpenAUT");
		item.setMnemonic('p');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				KeyEvent.CTRL_MASK));
		// = = = = = = = = = = = = = = = = = =
		item = autMenu.add(factory.getTerminateAUT());
		item.setName("menuReopenAUT");
		item.setMnemonic('o');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				KeyEvent.CTRL_MASK));
		// = = = = = = = = = = = = = = = = = =
		autMenu.addSeparator();
		// = = = = = = = = = = = = = = = = = =
		item = autMenu.add(factory.getConfiguration());
		item.setName("menuConfig");
		item.setMnemonic('r');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				KeyEvent.CTRL_MASK));
		// = = = = = = = = = = = = = = = = = =
		m_menuBar.add(autMenu);
	}

	private void initMenuBarCR(MenuBarActionFactory factory) {
		JMenuItem item;
		// = = = = = = = = = = = = = = = = = =
		JMenu crMenu = new JMenu("C/R");
		crMenu.setName("menuCaptureReplay");
		crMenu.setMnemonic('r');
		// = = = = = = = = = = = = = = = = = =
		item = crMenu.add(factory.getStartCapture());
		item.setName("menuRecord");
		item.setMnemonic('r');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				KeyEvent.CTRL_MASK));
		// = = = = = = = = = = = = = = = = = =
		item = crMenu.add(factory.getReplay());
		item.setName("menuReplay");
		item.setMnemonic('p');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				KeyEvent.CTRL_MASK));

		// = = = = = = = = = = = = = = = = = =
		item = crMenu.add(factory.getStopCapture());
		item.setName("menuStopCapture");
		// = = = = = = = = = = = = = = = = = =
		m_menuBar.add(crMenu);
	}

	private void initMenuBarFile(MenuBarActionFactory factory) {
		JMenu mFile = new JMenu("File");
		mFile.setName("menuFile");
		mFile.setMnemonic('f');
		// = = = = = = = = = = = = = = = = = =
		JMenuItem item = mFile.add(factory.getFileNew());
		item.setName("menuNew");
		item.setMnemonic('n');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.CTRL_MASK));
		// = = = = = = = = = = = = = = = = = =
		item = mFile.add(factory.getFileOpen());
		item.setName("menuOpen");
		item.setMnemonic('o');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK));
		// = = = = = = = = = = = = = = = = = =
		mFile.addSeparator();
		// = = = = = = = = = = = = = = = = = =
		item = mFile.add(factory.getFileSave());
		item.setName("menuSave");
		item.setMnemonic('s');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK));
		// = = = = = = = = = = = = = = = = = =
		item = mFile.add(factory.getSaveGTMLScript());
		item.setName("menuSaveGTML");

		// = = = = = = = = = = = = = = = = = =
		item = mFile.add(factory.getFileSaveAs());
		item.setName("menuSaveAs");
		item.setMnemonic('a');
		// = = = = = = = = = = = = = = = = = =
		mFile.addSeparator();
		// = = = = = = = = = = = = = = = = = =
		item = mFile.add(factory.getFileExit());
		item.setName("menuExit");
		item.setMnemonic('x');
		// = = = = = = = = = = = = = = = = = =
		m_menuBar.add(mFile);
	}

	public void showMessage(String msg) {
		// JOptionPane.showMessageDialog(this, msg);
		console_view.addText(msg);
	}

}
