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

import gtt.editor.presenter.ITestScriptPresenter;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/**
 * Model-View-Presenter
 * 
 * @author zwshen 2006/08/25
 */
public class TestScriptView extends JPanel implements ITestScriptView, IView {

	private static final long serialVersionUID = 1L;

	// ///////////////////////////////////////////////////////////////////
	// buttons on Toolbar
	// private JButton m_btnText;

	// private JButton _stopAddNodeButton, _convertButton;
	private JButton m_btnLaunch;
	// private JButton _resetStateB, _synButton; // 20040227

	// insert new node
	private JButton m_btnAddFolder, m_btnAddEventNode, m_btnAddViewAssertNode,
			m_btnAddModelAssertNode;

	private JButton m_btnAddSleeperNode, m_btnAddBreakerNode,
			m_btnAddCommentNode;

	private JButton m_btnAddOracleNode;

	private JButton m_btnResetScript;

	// Cut/Copy/Paste
	private JButton m_btnCopy, m_btnPaste, m_btnCut;

	// Remove/Edit
	private JButton m_btnRemove, m_btnEditNode, m_btnUp, m_btnDown;

	// Test Script 的呈現方式 simple, normal, detail 2006/08/28
	// private JButton m_btnSimpleView, m_btnNormalView, m_btnDetailView;

	// ///////////////////////////////////////////////////////////////////

	private JToolBar m_TestScriptToolBar;

	ITestScriptPresenter m_tsPresenter;

	// Factory method zws 2007/03/13
	public static TestScriptView create(ITestScriptPresenter p) {
		return new TestScriptView(p);
	}

	private TestScriptView(ITestScriptPresenter p) {
		setLayout(new GridLayout(1, 1));
		// view + model
		p.setView(this);
		m_tsPresenter = p;
		initJTree();
		// initMenuBar();
		initTestScriptToolBars();
		// initLayout();
		this.add(createTestScriptPane());
	}

	private void initJTree() {
		/**
		 * 使用 Facotry Object 來產生 TreeNodeData 使得讓 JTreeVisitor 不需要認得
		 * TestScriptView
		 */
		JTreeTestScriptVisitor v = new JTreeTestScriptVisitor(
				new TreeNodeDataFactory(this));
		m_tsPresenter.acceptVisitor(v);
		m_tsPresenter.setJTree(v.createJTree());

		m_TreePane = new JScrollPane(m_tsPresenter.getJTree());
	}

	// =====================================================================

	JScrollPane m_TreePane;

	private JPanel createScriptToolPane() {
		JPanel tool = new JPanel();
		tool.setLayout(new BorderLayout());
		tool.add(m_TestScriptToolBar, BorderLayout.NORTH);
		return tool;
	}

	private JPanel createTestScriptPane() {
		JPanel p = new JPanel(new GridLayout(1, 1));
		p.setBorder(BorderFactory.createTitledBorder("Test Script Editor"));
		p.add(createScriptPanel());
		return p;
	}

	private JPanel createScriptPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		// 有一個 tool bar
		panel.add(createScriptToolPane(), BorderLayout.NORTH);
		// 有一個 script tree
		panel.add(m_TreePane, BorderLayout.CENTER);
		return panel;
	}

	private void initTestScriptToolBars() {

		m_TestScriptToolBar = new JToolBar("Test Script ToolBar");
		// 先建立要用的 buttons
		initTestScriptToolBarButtons();

		// 建立 Actinos
		initTestScriptToolBarButtonAction();

		// 再放入至 Toolbar 中
		initTestScriptToolBarButtonLocation();
	}

	/**
	 * 使用在 Test Script 上的工具列的按鈕
	 */
	private static final String IMAGE_DIRECTORY = System
			.getProperty("user.dir")
			+ "/images/";

	private void initTestScriptToolBarButtons() {

		// _stopAddNodeButton = (JButton) WidgetFactory.getButton(null,
		// IMAGE_DIRECTORY + "stopAction.gif", IMAGE_DIRECTORY
		// + "stopAction.gif", IMAGE_DIRECTORY + "stopAction.gif",
		// null, "Stop", 25, 25, false);

		m_btnAddEventNode = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "insertEvent.gif", IMAGE_DIRECTORY
						+ "insertEvent.gif", IMAGE_DIRECTORY
						+ "insertEvent.gif", null, "Event", 25, 25, false);
		m_btnAddViewAssertNode = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "insertTest.gif", IMAGE_DIRECTORY
						+ "insertTest.gif", IMAGE_DIRECTORY + "insertTest.gif",
				null, "View Assertion", 25, 25, false);

		m_btnAddModelAssertNode = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "insertJUnitTest.gif", IMAGE_DIRECTORY
						+ "insertJUnitTest.gif", IMAGE_DIRECTORY
						+ "insertJUnitTest.gif", null, "JUnit class", 25, 25,
				false);

		m_btnAddFolder = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "insertFolder.gif", IMAGE_DIRECTORY
						+ "insertFolder.gif", IMAGE_DIRECTORY
						+ "insertFolder.gif", null, "Folder", 25, 25, false);
		m_btnEditNode = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "editNode.gif", IMAGE_DIRECTORY + "editNode.gif",
				IMAGE_DIRECTORY + "editNode.gif", null, "Edit", 25, 25, false);
		m_btnRemove = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "delete.gif", IMAGE_DIRECTORY + "delete.gif", IMAGE_DIRECTORY
				+ "delete.gif", null, "Delete", 25, 25, false);
		m_btnUp = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "up.gif", IMAGE_DIRECTORY + "up.gif", IMAGE_DIRECTORY
				+ "up.gif", null, "Move Up", 25, 25, false);
		m_btnDown = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "down.gif", IMAGE_DIRECTORY + "down.gif", IMAGE_DIRECTORY
				+ "down.gif", null, "Move Down", 25, 25, false);
		m_btnCut = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "cut.gif", IMAGE_DIRECTORY + "cut.gif", IMAGE_DIRECTORY
				+ "cut.gif", null, "Cut", 25, 25, false);
		m_btnCopy = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "copy.gif", IMAGE_DIRECTORY + "copy.gif", IMAGE_DIRECTORY
				+ "copy.gif", null, "Copy", 25, 25, false);
		m_btnPaste = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "paste.gif", IMAGE_DIRECTORY + "paste.gif", IMAGE_DIRECTORY
				+ "paste.gif", null, "Paste", 25, 25, false);

		// _convertButton = (JButton) WidgetFactory.getButton(null,
		// IMAGE_DIRECTORY + "convert.gif", IMAGE_DIRECTORY
		// + "convert.gif", IMAGE_DIRECTORY + "convert.gif", null,
		// "Convert", 25, 25, false);

		m_btnAddBreakerNode = (JButton) WidgetFactory
				.getButton(null, IMAGE_DIRECTORY + "insert_break.gif",
						IMAGE_DIRECTORY + "insert_break.gif", IMAGE_DIRECTORY
								+ "breakinsert_break.gif", null, "Break", 25,
						25, false);
		m_btnAddSleeperNode = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "insert_sleeper.gif", IMAGE_DIRECTORY
						+ "insert_sleeper.gif", IMAGE_DIRECTORY
						+ "insert_sleeper.gif", null, "Sleeper", 25, 25, false);

		m_btnAddCommentNode = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "insert_comment.gif", IMAGE_DIRECTORY
						+ "insert_comment.gif", IMAGE_DIRECTORY
						+ "insert_comment.gif", null, "Comment", 25, 25, false);

		// add oracle
		m_btnAddOracleNode = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "TestOracle.png", IMAGE_DIRECTORY
						+ "TestOracle.png", IMAGE_DIRECTORY + "TestOracle.png",
				null, "Oracle", 25, 25, false);

		m_btnLaunch = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
				+ "insertReopenAUT.gif", IMAGE_DIRECTORY
				+ "insertReopenAUT.gif", IMAGE_DIRECTORY
				+ "insertReopenAUT.gif", null, "Launch", 25, 25, false);

		m_btnResetScript = (JButton) WidgetFactory.getButton(null,
				IMAGE_DIRECTORY + "resetScript.gif", IMAGE_DIRECTORY
						+ "resetScript.gif", IMAGE_DIRECTORY
						+ "resetScript.gif", null, "Reset Script", 25, 25,
				false);
		//
		// _synButton = (JButton) WidgetFactory.getButton(null, IMAGE_DIRECTORY
		// + "syncComp.gif", IMAGE_DIRECTORY + "syncComp.gif",
		// IMAGE_DIRECTORY + "syncComp.gif", null,
		// "Synchronsize test script with AUT", 25, 25, false); // 20040227

		// m_btnSimpleView = new JButton("-");
		// m_btnNormalView = new JButton("=");
		// m_btnDetailView = new JButton("+");
		// m_btnText = new JButton("T");
	}

	/*
	 * Test Script view level 3 scales: SIMPLE_VIEW_LEVEL, NORMAL_VIEW_LEVEL,
	 * DETAIL_VIEW_LEVEL
	 */

	// private int m_ViewLevel = NORMAL_VIEW_LEVEL;
	// public int getViewLevel() {
	// return m_ViewLevel;
	// }
	//
	// private void setViewLevel(int level) {
	// m_ViewLevel = level;
	// updateUI();
	// }
	public synchronized void updateUI() {
		if (m_tsPresenter == null)
			return;
		m_tsPresenter.getJTree().updateUI();
		m_TreePane.updateUI();
	}

	/**
	 * setup each button action listener
	 */
	private void initTestScriptToolBarButtonAction() {
		// //////////////////////////////////////////////////
		// 新增節點
		m_btnAddFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.insertFolderNode();
			}
		});

		// //////////////////////////////////////////////////
		// 新增節點
		m_btnAddEventNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.insertEventNode();
			}
		});

		// //////////////////////////////////////////////////
		// 新增節點
		m_btnAddViewAssertNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.insertViewAssertNode();
			}
		});
		// //////////////////////////////////////////////////
		// 新增節點

		m_btnAddModelAssertNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.insertModelAssertNode();
			}
		});

		// //////////////////////////////////////////////////
		// 新增節點
		m_btnLaunch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// reset node
				m_tsPresenter.insertLaunchNode();
			}
		});

		// //////////////////////////////////////////////////
		// 新增節點
		m_btnAddSleeperNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// reset node
				m_tsPresenter.insertSleeperNode();
			}
		});

		// 新增節點
		m_btnAddBreakerNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.insertBreakerNode();
			}
		});

		// 新增節點
		m_btnAddCommentNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.insertCommentNode();
			}
		});

		// 新增OracleNod節點
		m_btnAddOracleNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.insertOracleNode();
			}
		});

		// 新增節點
		m_btnResetScript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.resetTestScript();
			}
		});

		// //////////////////////////////////////////////////
		// 編輯節點
		m_btnEditNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.modifyNode();
			}
		});

		m_btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.removeNode();
			}
		});

		m_btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.upMoveNode();
			}
		});

		m_btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.downMoveNode();
			}
		});

		// //////////////////////////////////////////////////
		// Cut/Copy/Paste
		m_btnCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.cutNode();
			}
		});

		m_btnPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.pasteNode();
			}
		});

		m_btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_tsPresenter.copyNode();
			}
		});
	}

	private void initTestScriptToolBarButtonLocation() {
		m_TestScriptToolBar.add(m_btnAddEventNode);
		m_TestScriptToolBar.add(m_btnAddViewAssertNode);
		m_TestScriptToolBar.add(m_btnAddModelAssertNode);

		m_TestScriptToolBar.addSeparator();
		m_TestScriptToolBar.add(m_btnAddSleeperNode);
		m_TestScriptToolBar.add(m_btnAddCommentNode);
		m_TestScriptToolBar.add(m_btnAddBreakerNode);
		m_TestScriptToolBar.add(m_btnAddOracleNode);

		m_TestScriptToolBar.addSeparator();
		m_TestScriptToolBar.add(m_btnAddFolder);
		m_TestScriptToolBar.add(m_btnLaunch);

		m_TestScriptToolBar.addSeparator();
		m_TestScriptToolBar.add(m_btnEditNode);
		m_TestScriptToolBar.add(m_btnRemove);
		m_TestScriptToolBar.add(m_btnUp);
		m_TestScriptToolBar.add(m_btnDown);
		m_TestScriptToolBar.addSeparator();
		m_TestScriptToolBar.add(m_btnCut);
		m_TestScriptToolBar.add(m_btnCopy);
		m_TestScriptToolBar.add(m_btnPaste);

		m_TestScriptToolBar.addSeparator();
		m_TestScriptToolBar.add(m_btnResetScript); // reset script

		m_TestScriptToolBar.putClientProperty("JToolBar.isRollover",
				Boolean.TRUE);
		m_TestScriptToolBar.setFloatable(false);
	}


	@Override
	public JComponent getView() {
		return this;
	}

	@Override
	public String getViewName() {
		return this.getName();
	}
	

	@Override
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
}
