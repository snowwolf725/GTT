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
package gtt.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.view.IMacroTree;
import gtt.macro.view.IMacroView;
import gtt.macro.view.JTreeMacroVisitor;
import gtt.macro.view.dialog.MacroContractDialog;
import gtt.macro.view.dialog.SwingMacroDialogFactory;
import gtt.macro.view.dialog.SwingMacroDialogVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gtt.runner.PlaybackStandalong;
import gttlipse.refactoring.macro.RefactoringFacade;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class MacroPresenter implements IMacroPresenter {

	// 持有 MacroTreeView
	IMacroView m_MacroView;

	MacroDocument m_MacroDoc;

	public MacroDocument getModel() {
		return m_MacroDoc;
	}

	public IMacroView getView() {
		return m_MacroView;
	}

	public void setView(IMacroView v) {
		m_MacroView = v;
		setModel(m_MacroDoc);
	}

	public MacroPresenter() {
		m_MacroDoc = MacroDocument.create();
	}

	public void setModel(MacroDocument doc) {
		m_MacroDoc = doc;
		if (m_MacroView != null)
			m_MacroView.init(m_MacroDoc);
		initTreeKeyAction();
		initTreeMouseAction();
	}

	public void handleOpenFile(String filepath) {
		// 先讓model 讀檔
		m_MacroDoc.openFile(filepath);

		// 再更新 view 上的 tree
		updateView();
	}

	public void updateView() {
		JTreeMacroVisitor v = new JTreeMacroVisitor();
		m_MacroDoc.getMacroScript().accept(v);
		m_MacroView.setTreeModel(v.createJTree().getModel());
		m_MacroView.updateUI();
	}

	public void handleSaveFile(String filepath) {
		m_MacroDoc.saveFile(filepath);
	}

	public IMacroTree getMacroTree() {
		return m_MacroView.getMacroTree();
	}

	// ///////////////////////////////////////////////////////////////////
	// Cut/Copy/Paste

	private AbstractMacroNode m_cloneMacro;
	private boolean isCutNode = false;

	public boolean copyNode() {
		DefaultMutableTreeNode node = getMacroTree().getSelectedNode();
		if (node == null)
			return false;

		AbstractMacroNode obj = (AbstractMacroNode) node.getUserObject();
		m_cloneMacro = obj.clone();
		// 設定parent，才能取得正確的 Component Path
		m_cloneMacro.setParent(obj.getParent());

		isCutNode = false;

		return true;
	}

	public boolean cutNode() {
		DefaultMutableTreeNode node = getMacroTree().getSelectedNode();
		if (node == null)
			return false;

		AbstractMacroNode obj = (AbstractMacroNode) node.getUserObject();
		m_cloneMacro = obj.clone();
		// 設定parent，才能取得正確的 Component Path
		m_cloneMacro.setParent(obj.getParent());

		// 刪掉節點
		isCutNode = true;
		deleteNode(); // 刪除所選的node
		return true;
	}

	public boolean pasteNode() {
		if (m_cloneMacro == null)
			return false;

		DefaultMutableTreeNode node = getMacroTree().getSelectedNode();
		if (node == null)
			return false;

		AbstractMacroNode data = m_cloneMacro.clone();

		if (data instanceof MacroEventNode) {
			AbstractMacroNode obj = (AbstractMacroNode) node.getUserObject();
			// 不是剪下的節點，而且是貼在 MacroEvent 的子節點中
			if (!isCutNode && obj.getParent() instanceof MacroEventNode) {
				// 在copy MacroEventNode時，要以 MacroEventCallNode 貼上
				data = new MacroEventCallerNode(m_cloneMacro.getPath()
						.toString());
			}
		}

		getMacroTree().insertNode(data);

		if(isCutNode) {
//			// update Reference
			String oldpath = m_cloneMacro.getPath().toString();
			String newpath = data.getPath().toString();

			new RefactoringFacade(m_MacroDoc.getMacroScript())
			.updateReferencePath(oldpath, newpath);			
		}


		return true;
	};

	// ////////////////////////////////////////////////////////////////////
	public boolean upMoveNode() {
		DefaultMutableTreeNode selectedNode = getMacroTree().getSelectedNode();

		if (selectedNode == null)
			return false;

		if (selectedNode.isRoot())
			return false;

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();
		int index = parent.getIndex(selectedNode);
		if (index <= 0)
			return false;

		// up move on View
		moveNodeTo(selectedNode, index - 1);

		// up move on Model
		return MacroUtil.upNode((AbstractMacroNode) selectedNode
				.getUserObject());
	}

	public boolean downMoveNode() {
		DefaultMutableTreeNode selectedNode = getMacroTree().getSelectedNode();

		if (selectedNode == null)
			return false;

		if (selectedNode.isRoot())
			return false;

		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();
		int index = parent.getIndex(selectedNode);
		if (index >= parent.getChildCount() - 1)
			return false;

		// down move on view
		moveNodeTo(selectedNode, index + 1);
		// down move on Model
		return MacroUtil.downNode((AbstractMacroNode) selectedNode
				.getUserObject());
	}

	private void moveNodeTo(DefaultMutableTreeNode node, int new_pos) {
		TreePath path = (TreePath) getMacroTree().selectedPath();

		// View 上的更新
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();

		// 更新 new_pos ，有可能在移除節點之後，發生ArrayOutOfIndex
		if (new_pos < 0)
			new_pos = 0;
		if (new_pos >= parent.getChildCount())
			new_pos = parent.getChildCount();

		parent.remove(node);
		parent.insert(node, new_pos);

		getMacroTree().selectPath(path);
		getMacroTree().updateUI();
	}

	public boolean deleteNode() {
		DefaultMutableTreeNode node = getMacroTree().getSelectedNode();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();

		if (parent == null)
			return false; // root 不能刪掉

		int index = parent.getIndex(node);
		if (index < 0)
			return false;

		if (parent.getChildCount() > index + 1)
			m_MacroView.setSelectedNode(parent.getChildAt(index + 1));
		else
			m_MacroView.setSelectedNode(parent);

		// model
		AbstractMacroNode obj = (AbstractMacroNode) node.getUserObject();
		obj.getParent().remove(obj);

		// view
		DefaultTreeModel treeModel = (DefaultTreeModel) getMacroTree()
				.getTreeModel();
		treeModel.removeNodeFromParent(node);

		return false;
	}

	// //////////////////////////////////////////////////////////////////////////
	public boolean Rename() {
		DefaultMutableTreeNode node = getMacroTree().getSelectedNode();
		if (node == null)
			return false;

		AbstractMacroNode mn = (AbstractMacroNode) node.getUserObject();
		String name = m_MacroView.acquireInput(mn.getName(), "Rename");

		if (name == null)
			return false;

		name = name.trim();
		if (name == "")
			return false;

		AbstractMacroNode nodeObj = (AbstractMacroNode) node.getUserObject();

		// 加上refactoring 功能 -zwshen 2010/03/25
		boolean result = new RefactoringFacade(m_MacroDoc.getMacroScript())
				.doRename(nodeObj, name);
		getMacroTree().updateUI();
		return result;
	}

	// //////////////////////////////////////////////////////////////////////////
	// ///////
	private void initTreeKeyAction() {
		((JTree) m_MacroView.getMacroTree()).addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				JTree tree = (JTree) e.getSource();
				Object o = tree.getLastSelectedPathComponent();

				if (o == null)
					return;

				if (e.getModifiers() == KeyEvent.CTRL_MASK) {
					processKeyWithCtrl(e);
				} else {
					processKey(e);
				}
			}

			/*
			 * 沒有按住ctrl
			 */
			private void processKey(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					modify_node();
					break;
				case KeyEvent.VK_DELETE:
					deleteNode();
					break;
				case KeyEvent.VK_F2:
					modify_node();
					break;
				case KeyEvent.VK_F3:
					modify_contract();
					break;
				}
			}

			/*
			 * 有按住 ctrl
			 */
			private void processKeyWithCtrl(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					// upMoveNode(); // move up
					break;
				case KeyEvent.VK_DOWN:
					// downMoveNode(); // move down
					break;
				case KeyEvent.VK_X:
					cutNode(); // cut node
					break;
				case KeyEvent.VK_C:
					copyNode(); // copy node
					break;
				case KeyEvent.VK_V:
					pasteNode(); // paste node
					break;
				}
			}
		});
	}

	public boolean modify_node() {
		// 對不同的Macro Node 使用不同的 Modify Dialog
		return modifyNode(new SwingMacroDialogVisitor(
				new SwingMacroDialogFactory(), this.m_MacroView));
	}

	public boolean modify_contract() {
		// 按F3 叫出 macro contract editor
		AbstractMacroNode node = (AbstractMacroNode) (getMacroTree()
				.getSelectedNode().getUserObject());
		if (node == null)
			return false;

		if (node instanceof MacroEventNode) {
			new MacroContractDialog(getMacroTree(), (MacroEventNode) node)
					.setVisible(true);
		}

		return true;

	}

	// 1. 使用 SwingDialogVisitor 依node type做dispatch
	private boolean modifyNode(IMacroStructureVisitor v) {
		AbstractMacroNode node = (AbstractMacroNode) (getMacroTree()
				.getSelectedNode().getUserObject());
		if (node == null)
			return false;
		node.accept(v);
		reloadTreeModel();
		return true;
	}

	private void reloadTreeModel() {
		((DefaultTreeModel) this.getMacroTree().getTreeModel())
				.reload(getMacroTree().getSelectedNode());
	}

	private void initTreeMouseAction() {
		((JTree) m_MacroView.getMacroTree())
				.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						JTree tree = (JTree) e.getSource();
						TreePath path = tree.getPathForLocation(e.getX(), e
								.getY());
						if (path == null)
							return;

						if (SwingUtilities.isRightMouseButton(e)
								&& e.getClickCount() == 1) {
							// 滑鼠右鍵
							// m_TestUi.showTreePopupMenu(_testTree, e); //
							// 2003art
						} else if (e.getClickCount() == 2) {
							// 左鍵兩次 開 event dialog
							// modifyNodePart();
						}
					}
				});
	}

	public AbstractMacroNode getSelectedNode() {
		try {
			return (AbstractMacroNode) m_MacroView.getMacroTree()
					.getSelectedNode().getUserObject();
		} catch (NullPointerException npe) {
			return null;
		}
	}

	@Override
	public boolean runMacroEvent() {
		// run macro event

		AbstractMacroNode node = (AbstractMacroNode) (getMacroTree()
				.getSelectedNode().getUserObject());
		if (node == null)
			return false;

		if (node instanceof MacroEventNode) {
			PlaybackStandalong player = new PlaybackStandalong(null, m_MacroDoc);
			try {
				player.runMacro(node.getPath().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return true;
	}
}
