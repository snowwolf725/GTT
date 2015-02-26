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
 * Created on 2005/3/17
 */
package gtt.macro.view;

import gtt.macro.IMacroPresenter;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.util.swing.JTreeUtil;

import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Implementation of MacroTree
 *
 * @author zwshen
 *
 */
public class MacroJTree extends JTree implements IMacroTree {
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	private IMacroPresenter m_Presenter;

	public MacroJTree(AbstractMacroNode root, IMacroPresenter p) {
		super(new DefaultMutableTreeNode(root));
		setName("MacroTree");
		m_Presenter = p;
		setCellRenderer(new MacroTreeCellRenderer());
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		initTreeAction();
	}

	protected void initTreeAction() {
		setDragEnabled(true);
		MouseInputAdapter starter = new MouseInputAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					m_Presenter.getView().modifyComponentNode();
				}
			}
		};

		addMouseListener(starter);
		addMouseMotionListener(starter);
	}

	public boolean insertNode(AbstractMacroNode newNode) {
		// 是否有選擇一個節點
		DefaultMutableTreeNode beAdded = getSelectedNode();

		// 若沒有，則安插到根節點下
		if (beAdded == null)
			beAdded = getModelRoot();

		return _insert(beAdded, new DefaultMutableTreeNode(newNode));
	}

	/**
	 * 這個method實現讓插入一個Tree node到指定的Tree node中
	 *
	 * @param newNode
	 *            要置入的新節點
	 * @param viewChild
	 *            指定的Tree node
	 * @return 回傳是否成功的insert
	 */
	private boolean _insert(DefaultMutableTreeNode viewParent,
			DefaultMutableTreeNode viewChild) {

		AbstractMacroNode viewParentObj = (AbstractMacroNode) viewParent
				.getUserObject();
		AbstractMacroNode viewChildObj = (AbstractMacroNode) viewChild
				.getUserObject();

		// 只有 MacroComponent 才有子節點
		if (viewParentObj instanceof MacroComponentNode) {
			// 要同步 Model/View 兩邊的資料
			// model 加成功，view 也需要跟著加一個
			if (viewParentObj.add(viewChildObj)) {
				// view
				viewParent.add(viewChild);
				// 遞回新增子節點
				if (viewChildObj.isContainer()) {
					insertSubNodes(viewChild);
				}
			}

		} else {
			// 否則的話，就加在下一個節點位置
			AbstractMacroNode parent = viewParentObj.getParent();
			if (parent == null)
				return false;

			// model 加成功，view 也需要跟著加一個
			if (parent.add(viewChildObj, parent.indexOf(viewParentObj) + 1)) {
				// view
				DefaultMutableTreeNode parentnode = (DefaultMutableTreeNode) viewParent
						.getParent();
				parentnode.insert(viewChild,
						parentnode.getIndex(viewParent) + 1);

				// 遞回新增子節點
				if (viewChildObj.isContainer()) {
					insertSubNodes(viewChild);
				}

			}
		}

		// UpdateForExpandTheNode(node);
		this.updateUI();

		return true;
	}

	private void _insertView(DefaultMutableTreeNode viewParent,
			DefaultMutableTreeNode viewChild) {
		AbstractMacroNode viewChildObj = (AbstractMacroNode) viewChild
				.getUserObject();

		// view
		viewParent.add(viewChild);
		// 遞回新增子節點
		if (viewChildObj.isContainer()) {
			insertSubNodes(viewChild);
		}
	}

	private void insertSubNodes(DefaultMutableTreeNode viewChild) {
		// 遞回新增子節點
		AbstractMacroNode viewChildObj = (AbstractMacroNode) viewChild
				.getUserObject();
		AbstractMacroNode childs[] = viewChildObj.getChildren();
		for (int i = 0; i < childs.length; i++) {
			_insertView(viewChild, new DefaultMutableTreeNode(childs[i]));
		}
	}

	public void UpdateForExpandTheNode(Object node) {
		JTreeUtil.expandSubTree(this, (TreeNode) node);
	}

	public DefaultMutableTreeNode getSelectedMacroComponent() {
		/**
		 * 延著樹往上找到第一個是 MacroComponent 的點 即為此子樹的根節點
		 */
		DefaultMutableTreeNode pNode = getSelectedNode();

		if (pNode == null)
			pNode = (DefaultMutableTreeNode) getModel().getRoot();

		if (pNode == null)
			return null;

		while (!pNode.isRoot()) {
			if (pNode.getUserObject() instanceof MacroComponentNode)
				return pNode;// find it

			pNode = (DefaultMutableTreeNode) pNode.getParent();
		}

		return (DefaultMutableTreeNode) getModel().getRoot();
	}

	public DefaultMutableTreeNode getModelRoot() {
		return (DefaultMutableTreeNode) getTreeModel().getRoot();
	}

	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode) getLastSelectedPathComponent();
	}

	public DefaultTreeModel getTreeModel() {
		return (DefaultTreeModel) getModel();
	}

	public void selectPath(Object path) {
		setSelectionPath((TreePath) path);
	}

	public TreePath selectedPath() {
		return (TreePath) getSelectionPath();
	}

	public TreePath[] selectedPaths() {
		return (TreePath[]) getSelectionPaths();
	}

	public void setTreeModel(Object tree_model) {
		this.setModel((TreeModel) tree_model);
	}

}
