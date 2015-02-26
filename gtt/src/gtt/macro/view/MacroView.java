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
 * Created on 2005/3/2
 */
package gtt.macro.view;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gtt.macro.IMacroPresenter;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.view.dialog.ComponentEventDialog;
import gtt.macro.view.dialog.ComponentInfoDialog;
import gtt.macro.view.dialog.MacroEventDialog;
import gtt.util.swing.JTreeUtil;
import gtt.util.swing.WidgetFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class MacroView implements IMacroView {
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private JPanel m_MainPanel = new JPanel();

	public JPanel getMainPanel() {
		return m_MainPanel;
	}

	private IMacroTree m_MacroTree = null;

	private JToolBar m_toolBar = new JToolBar("MacroToolBar");

	private JButton m_Btn_insertANewMacro = null;

	private JButton m_Btn_insertANewEvent = null;

	private JButton m_Btn_insertComponent = null;

	private JButton m_Btn_CutAComOrEvent = null;

	private JButton m_Btn_CopyAComOrEvent = null;

	private JButton m_Btn_PasteAComOrEvent = null;

	private JButton m_Btn_deleteAComOrEvent = null;

	private JButton m_Btn_upMoveNode = null;

	private JButton m_Btn_downMoveNode = null;

	private JScrollPane m_DocumentPane = null;

	private MacroJTreePopupMenu m_PopupMenu = null;

	public MacroView() {
		initToolBar();
		initPanel();
		initName();
	}

	private void initName() {
		m_toolBar.setName("MacroTree_ToolBar");
		m_Btn_insertANewMacro.setName("MacroTree_insertANewMacro");
		m_Btn_insertANewEvent.setName("MacroTree_insertANewEvent");
		m_Btn_insertComponent.setName("MacroTree_insertComponent");

		m_Btn_CutAComOrEvent.setName("MacroTree_CutAComOrEvent");
		m_Btn_CopyAComOrEvent.setName("MacroTree_CopyAComOrEvent");
		m_Btn_PasteAComOrEvent.setName("MacroTree_PasteAComOrEvent");

		m_Btn_deleteAComOrEvent.setName("MacroTree_deleteAComOrEvent");
		m_Btn_upMoveNode.setName("MacroTree_upgradeNode");
		m_Btn_downMoveNode.setName("MacroTree_downgradeNode");

	}

	/*
	 * 將 View 的 business logic code全移到 presenter -by zws 2007/05/23
	 */
	IMacroPresenter m_Presenter;

	public void setPresenter(IMacroPresenter p) {
		m_Presenter = p;
	}

	public void setTreeModel(Object tree_model) {
		m_MacroTree.setTreeModel(tree_model);
		updateUI();
	}

	public void init(MacroDocument doc) {
		clear();

		m_MacroTree = new MacroJTree(doc.getMacroScript(), m_Presenter);

		initComponentPopupMenu();

		((MacroJTree) m_MacroTree)
				.addMouseListener(getTreePopupMenuShow_MouseAdapterAction());

		m_DocumentPane = new JScrollPane((MacroJTree) m_MacroTree);
		m_DocumentPane.setName("MacroTree_ScrollPane");
		m_MainPanel.add(m_DocumentPane, BorderLayout.CENTER);

		updateUI();

	}

	// public MacroDocument getMacroDocument() {
	// return m_MacroDoc;
	// }

	private void initComponentPopupMenu() {
		/*
		 * applied Command 2006/11/01
		 */
		m_PopupMenu = new MacroJTreePopupMenu();
		m_PopupMenu.setName("MacroTree_PopupMenu");

		// 移到presenter做 - zwshen 2007/07/10
		m_PopupMenu.setRenameCmd(new PopupMenuRenameCommand(m_Presenter));
		m_PopupMenu.setDeleteCmd(new PopupMenuDeleteCommand(m_Presenter));
		m_PopupMenu.setUpCmd(new PopupMenuUpCommand(m_Presenter));
		m_PopupMenu.setDownCmd(new PopupMenuDownCommand(m_Presenter));

		// 移到presenter做 - zwshen 2007/06/27
		m_PopupMenu.setCopyCmd(new PopupMenuCopyCommand(m_Presenter));
		m_PopupMenu.setCutCmd(new PopupMenuCutCommand(m_Presenter));
		m_PopupMenu.setPasteCmd(new PopupMenuPasteCommand(m_Presenter));

		m_PopupMenu.setEditCmd(new PopupMenuEditCommand(m_Presenter));
		m_PopupMenu.setEditContractCmd(new PopupMenuEditContractCommand(
				m_Presenter));
		m_PopupMenu.setRunCmd(new PopupMenuRunCommand(m_Presenter));
	}

	private void clear() {
		m_PopupMenu = null;
		if (m_DocumentPane == null)
			return;
		m_MainPanel.remove(m_DocumentPane);
		m_DocumentPane.removeAll();
	}

	/*
	 * private
	 */
	private static final String PATH = System.getProperty("user.dir")
			+ "/images/";

	private void initToolBar() {
		m_Btn_insertANewMacro = (JButton) WidgetFactory.getButton(null, PATH
				+ "addMacro.gif", PATH + "addMacro.gif", PATH + "addMacro.gif",
				null, "Insert Macro Component", 25, 25, false);
		m_Btn_insertANewMacro.addActionListener(getInsertANewMacroAction());
		m_toolBar.add(m_Btn_insertANewMacro);

		m_Btn_insertANewEvent = (JButton) WidgetFactory.getButton(null, PATH
				+ "addEditEvent.gif", PATH + "addEditEvent.gif", PATH
				+ "addEditEvent.gif", null, "Insert/Edit Macro Event", 25, 25,
				false);
		m_Btn_insertANewEvent.addActionListener(getInsertANewEventAction());
		m_toolBar.add(m_Btn_insertANewEvent);

		// add primitive component
		m_Btn_insertComponent = (JButton) WidgetFactory.getButton(null, PATH
				+ "node_component.gif", PATH + "node2_component.gif", PATH
				+ "node_component.gif", null, "Insert/Edit Component", 25, 25,
				false);
		m_Btn_insertComponent.addActionListener(getInsertComponentAction());
		m_toolBar.add(m_Btn_insertComponent);

		m_toolBar.addSeparator();

		m_Btn_CutAComOrEvent = (JButton) WidgetFactory.getButton(null, PATH
				+ "cut.gif", PATH + "cut.gif", PATH + "cut.gif", null, "Cut",
				25, 25, false);
		m_Btn_CutAComOrEvent.addActionListener(getCutNodeAction());
		m_toolBar.add(m_Btn_CutAComOrEvent);

		m_Btn_CopyAComOrEvent = (JButton) WidgetFactory.getButton(null, PATH
				+ "copy.gif", PATH + "copy.gif", PATH + "copy.gif", null,
				"Copy", 25, 25, false);
		m_Btn_CopyAComOrEvent.addActionListener(getCopyNodeAction());
		m_toolBar.add(m_Btn_CopyAComOrEvent);

		m_Btn_PasteAComOrEvent = (JButton) WidgetFactory.getButton(null, PATH
				+ "paste.gif", PATH + "paste.gif", PATH + "paste.gif", null,
				"Paste", 25, 25, false);
		m_Btn_PasteAComOrEvent.addActionListener(getPasteNodeAction());
		m_toolBar.add(m_Btn_PasteAComOrEvent);

		m_Btn_deleteAComOrEvent = (JButton) WidgetFactory.getButton(null, PATH
				+ "delete.gif", PATH + "delete.gif", PATH + "delete.gif", null,
				"Delete", 25, 25, false);
		m_Btn_deleteAComOrEvent.addActionListener(getDeleteNodeAction());
		m_toolBar.add(m_Btn_deleteAComOrEvent);

		m_toolBar.addSeparator();

		m_Btn_upMoveNode = (JButton) WidgetFactory.getButton(null, PATH
				+ "up.gif", PATH + "up.gif", PATH + "up.gif", null, "Move Up",
				25, 25, false);
		m_Btn_upMoveNode.addActionListener(getUpMoveNodeAction());
		m_toolBar.add(m_Btn_upMoveNode);

		m_Btn_downMoveNode = (JButton) WidgetFactory.getButton(null, PATH
				+ "down.gif", PATH + "down.gif", PATH + "down.gif", null,
				"Move down", 25, 25, false);
		m_Btn_downMoveNode.addActionListener(getDownNodeAction());
		m_toolBar.add(m_Btn_downMoveNode);

		m_toolBar.addSeparator();

		m_toolBar.setFloatable(false);
		m_toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
	}

	private void initPanel() {
		m_MainPanel.setLayout(new BorderLayout());
		m_MainPanel.setBorder(BorderFactory.createTitledBorder("Macro Editor"));
		m_MainPanel.add(m_toolBar, BorderLayout.NORTH);
	}

	private void insertComponentNode() {
		IEventModel m_SwingModel = EventModelFactory.getDefault();
		List<IComponent> cs = m_SwingModel.getComponents();

		IComponent ic = (IComponent) JOptionPane.showInputDialog(null,
				"Component selection", "Component", JOptionPane.PLAIN_MESSAGE,
				null, cs.toArray(), null);
		if (ic == null)
			return;

		ComponentNode c = new ComponentNode(ic);

		// 新增一個 ComponentNode
		m_MacroTree.insertNode(c.clone());
	}

	private void appearDialog(DefaultMutableTreeNode node) {
		AbstractMacroNode nodeObj = (AbstractMacroNode) node.getUserObject();
		if (nodeObj instanceof MacroEventNode) {
			new MacroEventDialog(m_MacroTree, node).setVisible(true);
			return;
		}

		if (nodeObj instanceof ModelAssertNode
				|| nodeObj instanceof ViewAssertNode
				|| nodeObj instanceof ComponentEventNode
				|| nodeObj instanceof MacroEventCallerNode) {
			// parent node 才是一個 MacroEventNode
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
					.getParent();
			new MacroEventDialog(m_MacroTree, parent).setVisible(true);
			return;
		}

		insertNode(MacroEventNode.create("MacroEvent"));
	}

	private void showTreePopupMenu(Component invoker, MouseEvent e) {
		m_PopupMenu.show(invoker, e.getX(), e.getY());
	}

	private String acquireInput(String msg) {
		return JOptionPane.showInputDialog(null, msg);
	}

	private ActionListener getInsertANewMacroAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = acquireInput("Name of Macro Component");
				insertMacroComponentNode(name);
			}
		};
	}

	private ActionListener getInsertANewEventAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyMacroEventNode();
			}
		};
	}

	private ActionListener getInsertComponentAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertComponentNode();
			}
		};
	}

	private ActionListener getCutNodeAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Presenter.cutNode();
			}
		};
	}

	private ActionListener getCopyNodeAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Presenter.copyNode();
			}
		};
	}

	private ActionListener getPasteNodeAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Presenter.pasteNode();
			}
		};
	}

	private ActionListener getDeleteNodeAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Presenter.deleteNode();
			}
		};
	}

	private ActionListener getUpMoveNodeAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Presenter.upMoveNode();
			}
		};
	}

	private ActionListener getDownNodeAction() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_Presenter.downMoveNode();
			}
		};
	}

	private MouseAdapter getTreePopupMenuShow_MouseAdapterAction() {
		return new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)
						&& e.getClickCount() == 1) {
					JTree tree = (JTree) e.getSource();
					TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					if (path == null)
						return;
					tree.setSelectionPath(path);
					showTreePopupMenu(tree, e);
				}
			}
		};
	}

	public IMacroTree getMacroTree() {
		return m_MacroTree;
	}

	// 對選取的node插入一個新的 MacroComponentNode
	public boolean insertMacroComponentNode(String name) {
		if (name == null || name.equals(""))
			return false;
		return m_MacroTree.insertNode(MacroComponentNode.create(name));
	}

	public boolean insertNode(AbstractMacroNode newNode) {
		if (newNode == null)
			return false;
		return m_MacroTree.insertNode(newNode);
	}

	public void setSelectedNode(TreeNode node) {
		JTreeUtil.setTreeSelectedNode((JTree) m_MacroTree, node);
	}

	public void updateUI() {
		m_MainPanel.repaint();
		m_MainPanel.updateUI();
	}

	@Override
	public String acquireInput(String msg, String title) {
		return JOptionPane.showInputDialog(title, msg);
	}

	public static IMacroView create(IMacroPresenter p) {
		MacroView view = new MacroView();
		view.setPresenter(p);
		p.setView(view);
		return view;
	}

	@Override
	public JComponent getView() {
		return (JComponent) m_MainPanel;
	}

	@Override
	public String getViewName() {
		return "MacroView";
	}

	@Override
	public void modifyMacroEventNode() {
		DefaultMutableTreeNode selectedNode = m_MacroTree.getSelectedNode();
		if (selectedNode == null)
			return;
		appearDialog(selectedNode);
	}

	@Override
	public void modifyComponentNode() {
		DefaultMutableTreeNode node = m_MacroTree.getSelectedNode();
		if (node == null)
			return;
		if (node.getUserObject() instanceof ComponentNode) {
			ComponentNode obj = (ComponentNode) node.getUserObject();
			new ComponentInfoDialog(null, "Edit Component", true, obj
					.getComponent()).setVisible(true);
		}
		if (node.getUserObject() instanceof ComponentEventNode) {
			ComponentEventNode obj = (ComponentEventNode) node.getUserObject();
			ComponentEventDialog dialog = new ComponentEventDialog();
			dialog.setNode(obj);
			dialog.appear();
			m_MacroTree.updateUI();
		}
		if (node.getUserObject() instanceof MacroEventCallerNode) {

		}
	}
}
