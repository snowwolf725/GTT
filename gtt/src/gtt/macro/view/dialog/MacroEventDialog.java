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
/*
 * Created on 2005/4/7
 */
package gtt.macro.view.dialog;

import gtt.editor.view.BoxTableView;
import gtt.eventmodel.Argument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.view.IMacroTree;
import gtt.macro.view.MacroTreeCellRenderer;
import gtt.util.swing.JTreeUtil;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class MacroEventDialog extends JDialog implements IMacroEventDialog {
	/**
	 * default serial version UID
	 */
	static final long serialVersionUID = 1L;

	private JTextField m_TextFieldName = new JTextField();

	private BoxTableView m_ArgumentBox = null;

	protected JButton m_AddArgButton = WidgetFactory.createJButton("+", 50, 20);

	protected JButton m_DelArgButton = WidgetFactory.createJButton("-", 50, 20);

	protected JButton m_AddEventButton = WidgetFactory.createJButton(
			"Add event", 130, 20);

	protected JButton m_AddAssertButton = WidgetFactory.createJButton(
			"Add assert", 130, 20);

	protected JButton m_AddSleeperButton = WidgetFactory.createJButton(
			"Add sleeper", 130, 20);

	protected JButton m_AddCommentButton = WidgetFactory.createJButton(
			"Add comment", 130, 20);

	protected JButton m_AddBreakerButton = WidgetFactory.createJButton(
			"Add break", 130, 20);

	protected JButton m_AddJUnitAssertButton = WidgetFactory.createJButton(
			"Add junit assert", 130, 20);

	protected JButton m_DelButton = WidgetFactory.createJButton("Delete", 130,
			20);

	protected JButton m_btnOK = new JButton("Ok");

	private JButton m_btnCancel = new JButton("Cancel");

	private DefaultMutableTreeNode m_hereMacroEventRoot = null;

	private DefaultMutableTreeNode m_outMacroEvent = null;

	private MacroEventNode m_hereMacroEvent = null;

	private PanelSelector m_PanelSelector = null;

	private IMacroTree m_outerMacroTree = null;

	private JTree m_hereMacroEventTree = null;

	public MacroEventDialog(IMacroTree mtree,
			DefaultMutableTreeNode outMacroEvent) {

		m_outerMacroTree = mtree;
		m_PanelSelector = new PanelSelector();

		// view
		m_outMacroEvent = outMacroEvent;
		// model- 使用 clone 的方式，才不會更改到正本的內容
		m_hereMacroEvent = ((MacroEventNode) outMacroEvent.getUserObject())
				.clone();

		// 本dialog 要使用的tree root，跟外面的Macro JTree 無關
		m_hereMacroEventRoot = new DefaultMutableTreeNode();
		// deep clone for tree view
		copyMacroEventToTreeNode(m_hereMacroEventRoot, m_hereMacroEvent);

		initDialog();
		updateArgumentListView();
	}

	private void initDialog() {
		setTitle("Macro Event Editor - " + m_hereMacroEvent.toString());
		setSize(590, 650);
		WidgetFactory.placeCenter(this);
		setResizable(true);

		initLayout();
		initDialogAction();
		initDialogName();
	}

	private void initLayout() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		// 1. Macro Event Information
		mainPanel.add(BorderLayout.NORTH, createMainMacroEventPanel());
		// 2. 4-sub-panel
		mainPanel.add(BorderLayout.CENTER, createMainInfoPanel());
		// 3. Ok/Cancel button
		mainPanel.add(BorderLayout.SOUTH, createMainButtonPanel());

		getContentPane().add(BorderLayout.CENTER, mainPanel);
	}

	private JPanel createMainMacroEventPanel() {
		JPanel upPanel = new JPanel(new BorderLayout());
		// title: 顯示簡單的說明給使用者
		// upPanel.add(BorderLayout.NORTH, createTitlePanel());
		// Macro Event
		upPanel.add(BorderLayout.CENTER, createMacroEventPanel());
		return upPanel;
	}

	private void initDialogName() {
		m_TextFieldName.setName("MEEditor_Name");
		m_ArgumentBox.setName("MEEditor_Argument");
		m_AddArgButton.setName("MEEditor_AddArg");
		m_DelArgButton.setName("MEEditor_DelArg");
		m_AddEventButton.setName("MEEditor_AddEvent");
		m_AddAssertButton.setName("MEEditor_AddAssert");
		m_AddJUnitAssertButton.setName("MEEditor_AddFileAssert");
		m_AddSleeperButton.setName("MEEditor_AddSleeper");
		m_AddCommentButton.setName("MEEditor_AddComment");
		m_AddBreakerButton.setName("MEEditor_AddBreaker");
		m_DelButton.setName("MEEditor_Del");
		m_btnOK.setName("MEEditor_OK");
		m_btnCancel.setName("MEEditor_Cancel");
		m_hereMacroEventTree.setName("MEEditor_EventTree");
	}

	@SuppressWarnings("serial")
	private void initDialogAction() {
		m_TextFieldName.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
			}

			public void keyReleased(KeyEvent arg0) {
				// 與 Macro tree 做同步顯示
				m_hereMacroEvent.setName(m_TextFieldName.getText());
				m_hereMacroEventTree.updateUI();
			}

			public void keyTyped(KeyEvent arg0) {
			}
		});
		m_AddArgButton.addActionListener(new AbstractAction("addArgButton") {
			public void actionPerformed(ActionEvent e) {
				doAddArgument();
				updateArgumentListModel();
			}
		});
		m_DelArgButton.addActionListener(new AbstractAction("delArgButton") {
			public void actionPerformed(ActionEvent e) {
				doDeleteArgument();
				updateArgumentListModel();
			}
		});

		m_AddEventButton
				.addActionListener(new AbstractAction("addEventButton") {
					public void actionPerformed(ActionEvent e) {
						addEventNode();
					}
				});

		m_AddAssertButton.addActionListener(new AbstractAction(
				"addAssertButton") {
			public void actionPerformed(ActionEvent e) {
				addViewAssertNode();
			}
		});

		m_AddJUnitAssertButton.addActionListener(new AbstractAction(
				"addModelAssertButton") {
			public void actionPerformed(ActionEvent e) {
				addModelAssertNode();
			}
		});

		m_AddSleeperButton.addActionListener(new AbstractAction(
				"addSleeperAction") {
			public void actionPerformed(ActionEvent e) {
				addSleeperNode();
			}
		});
		m_AddCommentButton.addActionListener(new AbstractAction(
				"addCommentAction") {
			public void actionPerformed(ActionEvent e) {
				addCommentNode();
			}
		});
		m_AddBreakerButton.addActionListener(new AbstractAction(
				"addBreakerAction") {
			public void actionPerformed(ActionEvent e) {
				addBreakerNode();
			}
		});

		m_DelButton.addActionListener(new AbstractAction("delButton") {
			public void actionPerformed(ActionEvent e) {
				deleteNode();
			}
		});

		m_btnOK.addActionListener(new AbstractAction("finishButton") {
			public void actionPerformed(ActionEvent e) {
				doOK();
			}
		});

		m_btnCancel.addActionListener(new AbstractAction("cancelButton") {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});

		m_hereMacroEventTree
				.addTreeSelectionListener(new PanelSelectorListener());
	}

	class PanelSelectorListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			try {
				AbstractMacroNode data = selectMacroNode(e);
				// 更換sub-panel
				m_PanelSelector.select(data, m_hereMacroEventTree,
						m_outerMacroTree, m_hereMacroEventRoot);
			} catch (ClassCastException exp) {
				// 選的selectedNode 不是 DefaultMutableTreeNode
				// nothing to do
				// zws 2006/10/26
			}
		}

		private AbstractMacroNode selectMacroNode(TreeSelectionEvent e) {
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) e
					.getPath().getLastPathComponent();

			return (AbstractMacroNode) selectedNode.getUserObject();
		}

	}

	private JPanel createMacroEventPanel() {
		JPanel macroPanel = new JPanel(new BorderLayout());
		macroPanel
				.setBorder(new TitledBorder(new EtchedBorder(), "Macro Event"));
		// ===initial part===
		Box eventListBox = Box.createVerticalBox();
		// Veritcal 1:
		eventListBox.add(createNameBox());
		// Veritcal 2:
		eventListBox.add(Box.createVerticalStrut(5));
		eventListBox.add(createArgumentBox(macroPanel));
		// Veritcal 3:
		eventListBox.add(Box.createVerticalStrut(5));
		eventListBox.add(createEventListBox());
		// ==================
		macroPanel.add(BorderLayout.CENTER, eventListBox);

		return macroPanel;
	}

	private Box createNameBox() {
		Box nameBox = Box.createHorizontalBox();
		nameBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory.createJLabel("Name", 80, 20, false);
		nameBox.add(labelName);
		nameBox.add(Box.createHorizontalStrut(5));
		WidgetFactory.setupJTextComponent(m_TextFieldName, "", 200, 20);
		nameBox.add(m_TextFieldName);
		nameBox.add(Box.createHorizontalGlue());
		return nameBox;
	}

	private Box createArgumentBox(JPanel macroPanel) {
		Box argBox = Box.createHorizontalBox();

		argBox.add(Box.createHorizontalStrut(5));
		argBox.add(WidgetFactory.createJLabel("Argument:", 80, 20, false));
		argBox.add(Box.createHorizontalStrut(5));

		m_ArgumentBox = BoxTableView.createArgumentBoxView(macroPanel);

		argBox.add(m_ArgumentBox.getView());

		Box btnBox = Box.createVerticalBox();
		btnBox.add(m_AddArgButton);
		btnBox.add(m_DelArgButton);

		argBox.add(Box.createHorizontalStrut(5));
		argBox.add(btnBox);
		argBox.add(Box.createHorizontalGlue());

		return argBox;
	}

	private Box createEventListBox() {
		Box btnBox = Box.createVerticalBox();
		btnBox.add(m_AddEventButton);
		btnBox.add(Box.createVerticalStrut(3));

		btnBox.add(m_AddAssertButton);
		btnBox.add(Box.createVerticalStrut(3));

		btnBox.add(m_AddSleeperButton);
		btnBox.add(Box.createVerticalStrut(3));

		btnBox.add(m_AddCommentButton);
		btnBox.add(Box.createVerticalStrut(3));

		btnBox.add(m_AddBreakerButton);
		btnBox.add(Box.createVerticalStrut(3));

		// btnBox.add(m_AddJUnitAssertButton);
		// btnBox.add(Box.createVerticalStrut(5));

		btnBox.add(m_DelButton);
		btnBox.add(Box.createVerticalStrut(5));

		// up, down move 有問題，暫時不使用 - zws 2007/07/11
		// btnBox.add(m_UpButton);
		// btnBox.add(Box.createVerticalStrut(5));
		// btnBox.add(m_DownButton);

		Box listBox = Box.createHorizontalBox();
		listBox.add(Box.createHorizontalStrut(5));
		JLabel labelName = WidgetFactory.createJLabel("Macro Event", 80, 20,
				false);
		listBox.add(labelName);
		listBox.add(Box.createHorizontalStrut(5));

		m_hereMacroEventTree = new JTree(m_hereMacroEventRoot);

		m_hereMacroEventTree.setCellRenderer(new MacroTreeCellRenderer());

		JScrollPane treePane = new JScrollPane(m_hereMacroEventTree);
		treePane.setPreferredSize(new Dimension(300, 200));
		listBox.add(treePane);
		listBox.add(Box.createHorizontalStrut(5));
		listBox.add(btnBox);
		listBox.add(Box.createHorizontalStrut(5));

		return listBox;
	}

	private JPanel createMainInfoPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Information"));
		panel.add(m_PanelSelector.getMainPanel());
		return panel;
	}

	private JPanel createMainButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BorderLayout());
		btnPanel.add(BorderLayout.WEST, m_btnOK);
		btnPanel.add(BorderLayout.EAST, m_btnCancel);
		buttonPanel.add(BorderLayout.EAST, btnPanel);

		return buttonPanel;
	}

	private void doAddArgument() {
		// 新增一列 參數列
		m_ArgumentBox.addNewRow();
	}

	private void doDeleteArgument() {
		// 移掉最後一列 argument
		m_ArgumentBox.removeLastRow();
	}

	// ////////////////////////////////////////////////////////
	private void addEventNode() {
		MacroComponentNode mc = (MacroComponentNode) m_outerMacroTree
				.getSelectedMacroComponent().getUserObject();
		ComponentSelectorDialog dialog = new ComponentSelectorDialog(mc);

		dialog.setVisible(true);

		if (dialog.selectedComponent() == null)
			return;

		AbstractMacroNode com = dialog.selectedComponent();
		// ComponentEvent 或是 MacroEvent
		AbstractMacroNode event_obj = createEvent(com);

		insertNodeUnderMacroEvent(event_obj);

		dialog.dispose();
	}

	private AbstractMacroNode createEvent(AbstractMacroNode com) {
		if (com instanceof ComponentNode) {
			return new ComponentEventNode((ComponentNode) com);
		}

		if (com instanceof MacroComponentNode) {
			MacroComponentNode mc = (MacroComponentNode) com;
			// 預設選擇第一個MacroEventNode
			MacroEventNode me = (MacroEventNode) mc.getMacroEvents().get(0);
			// 利用這個MacroEventNode path 來建構出一個 SingleMacroEventNode
			return new MacroEventCallerNode(me.getPath().toString());
		}
		// default: ComponentEvent
		return new ComponentEventNode((ComponentNode) com);
	}

	private void addViewAssertNode() {
		MacroComponentNode mc = (MacroComponentNode) m_outerMacroTree
				.getSelectedMacroComponent().getUserObject();
		ComponentSelectorDialog dialog = new ComponentSelectorDialog(mc);
		dialog.setVisible(true);
		if (dialog.selectedComponent() == null)
			return;

		AbstractMacroNode com = dialog.selectedComponent();

		if (com instanceof ComponentNode) {
			AbstractMacroNode obj = new ViewAssertNode((ComponentNode) com);
			insertNodeUnderMacroEvent(obj);
		}

		dialog.dispose();
	}

	private void addModelAssertNode() {
		insertNodeUnderMacroEvent(new ModelAssertNode());
	}

	// /////////////////////////////////////////////////////////
	private void insertNodeUnderMacroEvent(AbstractMacroNode obj) {
		// view
		DefaultMutableTreeNode tnode = new DefaultMutableTreeNode(obj);
		m_hereMacroEventRoot.add(tnode);
		// model
		m_hereMacroEvent.add(obj);
		JTreeUtil.expandNode(m_hereMacroEventTree, tnode);
	}

	private void deleteNode() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) m_hereMacroEventTree
				.getLastSelectedPathComponent();
		if (node == null)
			return; // nothing to be deleted

		// model
		AbstractMacroNode obj = (AbstractMacroNode) node.getUserObject();
		if (obj == null || obj.getParent() == null)
			return;
		obj.getParent().remove(obj);
		// view
		JTreeUtil.deleteNode(m_hereMacroEventTree);
	}

	private void updateArgumentListModel() {
		m_hereMacroEvent.getArguments().clear();
		for (int i = 0; i < m_ArgumentBox.getRowCount(); ++i) {
			String type = m_ArgumentBox.getValue(i, 0);
			String name = m_ArgumentBox.getValue(i, 1);
			String value = m_ArgumentBox.getValue(i, 2);

			m_hereMacroEvent.getArguments().add(
					Argument.create(type, name, value));
		}

		m_hereMacroEventTree.updateUI();
	}

	private void updateArgumentListView() {
		m_TextFieldName.setText(m_hereMacroEvent.getName());

		m_ArgumentBox.removeAll();
		int rowCount = 0;
		Iterator<Argument> ite = m_hereMacroEvent.getArguments().iterator();
		while (ite.hasNext()) {
			Argument arg = ite.next();
			m_ArgumentBox.addNewRow();
			m_ArgumentBox.setValue(arg.getType(), rowCount, 0);
			m_ArgumentBox.setValue(arg.getName(), rowCount, 1);
			m_ArgumentBox.setValue(arg.getValue(), rowCount, 2);
			rowCount++;
		}
	}

	private void doOK() {
		updateArgumentListModel();

		copyMacroEventToTreeNode(m_outMacroEvent, m_hereMacroEvent);

		m_outerMacroTree.updateUI();
		this.dispose();
	}

	// 將 MacroEventNode 的內容以view的方式，clone到DefaultTreeNode 中
	// zws 2007/05/01
	private void copyMacroEventToTreeNode(DefaultMutableTreeNode treenode,
			MacroEventNode macroevent) {
		// 由於 MacroEvent 下只有一層子節點
		// 故不用考慮各個子節點的deep clone情況
		treenode.removeAllChildren();
		MacroEventNode view_obj = (MacroEventNode) treenode.getUserObject();

		if (view_obj != null) {
			// 有view_obj，就要從model_obj clone 一份過來
			view_obj.removeAll(); // 先清掉即有的子節點
			view_obj.setName(macroevent.getName());
			// 還有 argument 的部份
			view_obj.setArguments(macroevent.getArguments().clone());

			for (int i = 0; i < macroevent.size(); i++) {
				AbstractMacroNode cur_obj = (AbstractMacroNode) macroevent.get(
						i).clone();
				// 建立 view obj 的內容 (MacroEvent裡的內容)
				view_obj.add(cur_obj);
				// 建立view TreeNode 節點
				treenode.add(new DefaultMutableTreeNode(cur_obj));
			}
		} else {
			// 沒有view_obj，就直接採用model_obj
			treenode.setUserObject(macroevent);
			for (int i = 0; i < macroevent.size(); i++) {
				// 建立 view 子節點
				treenode.add(new DefaultMutableTreeNode(macroevent.get(i)));
			}
		}
	}

	private void doCancel() {
		this.dispose();
	}

	private void addSleeperNode() {
		insertNodeUnderMacroEvent(new SleeperNode(100));
	}

	private void addCommentNode() {
		insertNodeUnderMacroEvent(new CommentNode());
	}

	private void addBreakerNode() {
		insertNodeUnderMacroEvent(new BreakerNode());
	}

}
