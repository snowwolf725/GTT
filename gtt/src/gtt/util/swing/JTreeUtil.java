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
package gtt.util.swing;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * A util for Swing.JTree zws 2006/10/26
 */

public class JTreeUtil {
	public static void expandSubTree(JTree tree, TreeNode node) {
		// 將 node 的子樹全部展開
		if (node == null)
			return;
		DefaultMutableTreeNode expandNode = (DefaultMutableTreeNode) node;
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.reload(expandNode);
		if (expandNode.isRoot() == false) {
			TreeNode parent = ((DefaultMutableTreeNode) expandNode).getParent();
			TreePath parentPath = new TreePath(
					((DefaultMutableTreeNode) parent).getPath());
			model.reload(parent);
			tree.expandPath(parentPath);
		}

		TreePath selePath = new TreePath(((DefaultMutableTreeNode) expandNode)
				.getPath());
		tree.expandPath(selePath);
		tree.repaint();
	}

	public static void upMove(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		if (node.isRoot())
			return;

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		int index = parent.getIndex(node);
		if (index <= 0)
			return;
		parent.remove(node);
		parent.insert(node, index - 1);

		TreePath path = tree.getSelectionPath();
		tree.setSelectionPath(path);
		tree.updateUI();
	}

	public static void downMove(JTree tree) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node == null)
			return;
		if (node.isRoot() == true)
			return;

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();

		int index = parent.getIndex(node);
		if (index >= parent.getChildCount() - 1)
			return;

		parent.remove(node);
		parent.insert(node, index + 1);

		// DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		// model.removeNodeFromParent(node);
		// model.insertNodeInto(node, parent, index + 1);
		TreePath path = tree.getSelectionPath();
		tree.setSelectionPath(path);
		tree.updateUI();
	}

	public static void expandNode(JTree tree, TreeNode node) {
		if (node == null)
			return;
		tree.updateUI();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		tree.setSelectionPath(new TreePath(model.getPathToRoot(node)));
		tree.scrollPathToVisible(new TreePath(model.getPathToRoot(node)));
	}

	public static void deleteNode(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null)
			return;

		if (node.isRoot())
			return;

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();

		if(parent==null) return;
		int index = parent.getIndex(node);
		if (index < 0)
			return;

		if (parent.getChildCount() > index + 1) {
			// path指到被刪除節點的下一個節點
			setTreeSelectedNode(tree, parent.getChildAt(index + 1));
		} else if (parent.getChildCount() == 1) {
			// 只有一個刪除節點，path就直接指到parent node
			setTreeSelectedNode(tree, parent);
		} else {
			// path指到被刪除點的前一個節點
			setTreeSelectedNode(tree, parent.getChildAt(index - 1));
		}

		// 刪除節點
		DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
		treeModel.removeNodeFromParent(node);
	}

	public static void setTreeSelectedNode(JTree tree, TreeNode node) {
		DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
		tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(node)));
		tree.scrollPathToVisible(new TreePath(treeModel.getPathToRoot(node)));
	}

}
