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

import gtt.editor.configuration.IConfiguration;
import gtt.editor.view.AcquireUserInput;
import gtt.editor.view.ITestScriptView;
import gtt.editor.view.JTreeTestScriptVisitor;
import gtt.editor.view.TreeNodeData;
import gtt.editor.view.TreeNodeDataFactory;
import gtt.editor.view.dialog.ComponentDialogFactory;
import gtt.editor.view.dialog.SwingDialogTestScriptVisitor;
import gtt.editor.view.dialog.SwingScriptDialogFactory;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.visitor.ITestScriptVisitor;
import gtt.util.swing.JTreeNodeIterator;
import gtt.util.swing.JTreeUtil;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class TestScriptPresenter implements ITestScriptPresenter {

	private TestScriptDocument m_tsDocument;

	private ITestScriptView m_tsView;

	private JTree m_Tree;

	public TestScriptPresenter() {
		m_tsDocument = TestScriptDocument.create();
		m_Tree = new JTree(); // dummy jtree
	}

	public void setView(ITestScriptView v) {
		m_tsView = v;
	}

	public ITestScriptView getView() {
		return m_tsView;
	}

	public TestScriptDocument getModel() {
		return m_tsDocument;
	}

	public void acceptVisitor(ITestScriptVisitor v) {
		m_tsDocument.getScript().accept(v);
	}

	public void setModel(TestScriptDocument doc) {
		m_tsDocument = doc;
	}

	public JTree getJTree() {
		return m_Tree;
	}

	public void setJTree(JTree tree) {
		m_Tree = tree;
		m_Tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		m_Tree.putClientProperty("JTree.lineStyle", "Horizontal");
		tree.setName("ScriptTree");

		initTreeMouseAction();
		initTreeKeyAction();
	}

	private void initTreeKeyAction() {
		getJTree().addKeyListener(new KeyAdapter() {
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
			 * �S������ctrl
			 */
			private void processKey(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					modifyNodePart();
					break;
				case KeyEvent.VK_DELETE:
					removeNode();
					break;
				case KeyEvent.VK_F2:
					// ��F2�i�H�]�w�`�I��component��T
					// �άO����Folder Name
					modifyComponentPart();
					break;
				}
			}

			/*
			 * ������ ctrl
			 */
			private void processKeyWithCtrl(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					upMoveNode(); // move up
					break;
				case KeyEvent.VK_DOWN:
					downMoveNode(); // move down
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

	private void initTreeMouseAction() {
		getJTree().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JTree tree = (JTree) e.getSource();
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				if (path == null)
					return;

				if (SwingUtilities.isRightMouseButton(e)
						&& e.getClickCount() == 1) {
					// �ƹ��k��
					// m_TestUi.showTreePopupMenu(_testTree, e); // 2003art
				} else if (e.getClickCount() == 2) {
					// ����⦸ �} event dialog
					modifyNodePart();
				}
			}
		});
	}

	private DefaultMutableTreeNode getSelectedTreeNode() {
		try {
			return (DefaultMutableTreeNode) m_Tree
					.getLastSelectedPathComponent();
		} catch (NullPointerException npe) {
			return null;
		}
	}

	public AbstractNode selectedNode() {
		DefaultMutableTreeNode treeNode = getSelectedTreeNode();
		if (treeNode == null)
			return null;
		TreeNodeData data = ((TreeNodeData) (treeNode.getUserObject()));

		return data.getData();
	}

	public boolean downMoveNode() {
		JTreeUtil.downMove(m_Tree);
		return m_tsDocument.downNode(getSelectedScriptNode());
	}

	public boolean upMoveNode() {
		JTreeUtil.upMove(m_Tree);
		return m_tsDocument.upNode(getSelectedScriptNode());
	}

	private AbstractNode getSelectedScriptNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return null;
		TreeNodeData data = ((TreeNodeData) (node.getUserObject()));
		return data.getData();
	}

	private void modifyNodePart() {
		// 1. �ϥ� SwingDialogVisitor ��node type��dispatch
		// 2. �bnode���A�ϥ�SwingDialogFactory ���;A�X��dialog
		modifyNode(new SwingDialogTestScriptVisitor(
				new SwingScriptDialogFactory()));
	}

	private void modifyComponentPart() {
		// 1. �ϥ� SwingDialogVisitor ��node type��dispatch
		// 2. �bnode���A�ϥ�ComponentDialogSecltor ���;A�X��dialog
		modifyNode(new SwingDialogTestScriptVisitor(
				new ComponentDialogFactory()));
	}

	private IComponent acquireComponent() {
		List<IComponent> candidates = EventModelFactory.getDefault().getComponents();
		return AcquireUserInput.acquireComponent(candidates);
	}

	private IEvent acquireEvent(IComponent c) {
		List<IEvent> candidate = EventModelFactory.getDefault().getEvents(c);
		return AcquireUserInput.acquireEvent(candidate);
	}

	private void insertNode(DefaultMutableTreeNode treeNode,
			AbstractNode newNode) {
		TreeNodeData data = ((TreeNodeData) (treeNode.getUserObject()));
		AbstractNode cur_nodeObj = data.getData();

		if (cur_nodeObj.isContainer()) {
			// ���l�`�I�����p
			// model �s�W�@�Ӹ`�I
			cur_nodeObj.add(newNode);

			DefaultMutableTreeNode cur_treeNode = new DefaultMutableTreeNode(
					new TreeNodeData(newNode, data.getView()));
			// DefaultTreeModel treeModel = (DefaultTreeModel) getJTree()
			// .getModel();

			// view �s�W�@�Ӹ`�I
			treeNode.add(cur_treeNode);
			// treeModel.insertNodeInto(arg0, arg1, arg2)

			if (newNode.isContainer()) {
				// ���^�s�W�l�`�I
				insertSubNodes(newNode, cur_treeNode);
			}

		} else {
			// �S���l�`�I�����p
			// �[�J�� parent ���l�`�I��
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) treeNode
					.getParent();
			TreeNodeData parent_data = ((TreeNodeData) (parent.getUserObject()));
			AbstractNode parent_nodeObj = parent_data.getData();

			// model �s�W�@�Ӹ`�I
			parent_nodeObj
					.add(newNode, parent_nodeObj.indexOf(cur_nodeObj) + 1);

			DefaultMutableTreeNode cur_treeNode = new DefaultMutableTreeNode(
					new TreeNodeData(newNode, parent_data.getView()));
			// view �s�W�@�Ӹ`�I
			parent.insert(cur_treeNode, parent.getIndex(treeNode) + 1);

			if (newNode.isContainer()) {
				// ���^�s�W�l�`�I
				insertSubNodes(newNode, cur_treeNode);
			}
		}

		m_tsView.updateUI();
	}

	private void insertSubNodes(AbstractNode newNode,
			DefaultMutableTreeNode cur_treeNode) {
		// ���^�s�W�l�`�I
		AbstractNode childs[] = newNode.getChildren();
		int size = childs.length;
		for (int idx = 0; idx < size; idx++) {
			insertNode(cur_treeNode, childs[idx]);
		}
	}

	public boolean removeNode() {
		// model �R�����
		boolean r = m_tsDocument.removeNode(getSelectedScriptNode());
		// view �R�����
		JTreeUtil.deleteNode(m_Tree);
		return r;
	}

	private void reloadTreeModel() {
		((DefaultTreeModel) m_Tree.getModel()).reload(getSelectedTreeNode());
	}

	// 1. �ϥ� SwingDialogVisitor ��node type��dispatch
	private boolean modifyNode(ITestScriptVisitor v) {
		AbstractNode node = getSelectedScriptNode();
		if (node == null)
			return false;

		node.accept(v);

		reloadTreeModel();
		return true;
	}

	public boolean addNode(AbstractNode newNode) {
		// added by zwshen 070523
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		insertNode(node, newNode);
		return true;
	}

	public boolean insertRerenceMacroEventNode(String paths) {
		// added by zwshen 070523
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		AbstractNode newNode = new NodeFactory()
				.createReferenceMacroEventNode(paths);

		insertNode(node, newNode);

		m_tsView.updateUI();
		return true;
	}

	public boolean insertViewAssertNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		IComponent c = acquireComponent();

		if (c == null)
			return false;

		AbstractNode newNode = new NodeFactory().createViewAssertNode(c,
				new Assertion());

		insertNode(node, newNode);

		m_tsView.updateUI();
		return true;
	}

	public boolean insertFolderNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		String name = AcquireUserInput.acquireInput("Please input a folder name");

		if (name == null || name == "")
			return false; // �S����J�W�r

		insertNode(node, new NodeFactory().createFolderNode(name));

		m_tsView.updateUI();
		return true;
	}

	public boolean insertEventNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		// ���o�����T
		IComponent c = acquireComponent();

		if (c == null)
			return false;

		// ���o�ƥ��T
		IEvent e = acquireEvent(c);

		if (e == null)
			return false;
		AbstractNode newNode = new NodeFactory().createEventNode(c, e);

		insertNode(node, newNode);

		m_tsView.updateUI();
		return true;

	}

	public boolean insertModelAssertNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		insertNode(node, new NodeFactory().createModelAssertNode());

		m_tsView.updateUI();
		return true;
	}

	public boolean insertLaunchNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		// ���J�ݴ����ε{�� 2005/06/14
		String clspath;
		if (m_Configuration != null && m_Configuration.getAUTFilePath() != "")
			clspath = m_Configuration.getAUTFilePath();
		else {
			clspath = AcquireUserInput.acquireFilePath();
			if (clspath == null || clspath == "")
				return false;
			m_Configuration.setAUTFilePath(clspath);
		}

		if (clspath == null)
			return false;
		int idx = clspath.lastIndexOf(".class");
		if (idx < 1)
			return false;
		String clsname = clspath.substring(clspath.lastIndexOf("\\") + 1, idx);
		insertNode(node, new NodeFactory().createLaunchNode(clsname, clspath));
		m_tsView.updateUI();
		return true;
	}

	public boolean addNodes(List<AbstractNode> list) {
		if (list == null || list.size() < 0)
			return false;

		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I

		TreeNodeData n = (TreeNodeData) node.getUserObject();
		if (n.getData().isContainer()) {
			// �Ӷ���insert �i folder node
			for (int i = 0; i < list.size(); i++) {
				AbstractNode newNode = list.get(i);
				insertNode(node, newNode);
			}
		} else {
			// �˵�insert
			for (int i = list.size() - 1; i >= 0; i--) {
				AbstractNode newNode = list.get(i);
				insertNode(node, newNode);
			}
		}
		m_tsView.updateUI();
		return true;
	}

	public Iterator<TreeNode> treeIterator() {
		// �q�����}�l���ᨫ�X�� iterator
		return JTreeNodeIterator.createIterator(getSelectedTreeNode());
	}

	public void resetTestScript() {
		resetTestScriptNode((DefaultMutableTreeNode) this.getJTree().getModel()
				.getRoot());

		getView().updateUI();
	}

	private void resetTestScriptNode(DefaultMutableTreeNode node) {
		TreeNodeData data = ((TreeNodeData) node.getUserObject());
		data.setColor(TreeNodeData.Color.NORMAL);
		int size = node.getChildCount();
		for (int i = 0; i < size; i++)
			resetTestScriptNode((DefaultMutableTreeNode) node.getChildAt(i));
	}

	/* �bTest Script�W��� node */
	public synchronized void selectTreeNode(TreeNode node) {
		// �[�W synchronized - zws 2010/01/05
		try {
			DefaultTreeModel _treeModel = (DefaultTreeModel) getJTree()
					.getModel();
			getJTree().setSelectionPath(
					new TreePath(_treeModel.getPathToRoot(node)));
			getJTree().scrollPathToVisible(
					new TreePath(_treeModel.getPathToRoot(node)));
		} catch (NullPointerException nep) {
			// nothing
		}
	}

	public void handleOpenFile(String filepath) {

		// model ��Ū��
		if (m_tsDocument.openFile(filepath) == false)
			return;

		// �A��s view �W�� tree
		updateView();

		m_tsView.updateUI();
	}

	public void updateView() {
		JTreeTestScriptVisitor v = new JTreeTestScriptVisitor(
				new TreeNodeDataFactory(m_tsView));
		acceptVisitor(v);
		m_Tree.setModel(v.createJTree().getModel());
	}

	// //////////////////////////////////////////////////////////////
	// Cut/Copy/Paste

	// �O���Q�ŤU�νƨ�`�I
	AbstractNode m_cloneNode = null;

	public boolean copyNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false;
		AbstractNode obj = ((TreeNodeData) node.getUserObject()).getData();
		m_cloneNode = obj.clone();
		return false;
	}

	public boolean cutNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null || node.getParent() == null) {
			// �ڸ`�I�A�L�k�Q�ŤU
			m_tsView.showMessage("Invalid Cut action");
			return false;
		}
		AbstractNode obj = ((TreeNodeData) node.getUserObject()).getData();
		m_cloneNode = obj.clone();

		// �R���`�I
		removeNode();
		return true;
	}

	public boolean pasteNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null || m_cloneNode == null)
			return false;
		insertNode(node, m_cloneNode.clone());
		return true;
	}

	private IConfiguration m_Configuration;

	public void setConfiguration(IConfiguration config) {
		m_Configuration = config;
	}

	public boolean modifyNode() {
		if (selectedNode() == null)
			return false;
		modifyNodePart();
		return true;
	}

	@Override
	public boolean insertSleeperNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; 
		final long DEFAULT_TIME = 100;
		insertNode(node, new NodeFactory().createSleeperNode(DEFAULT_TIME));
		m_tsView.updateUI();
		return true;
	}

	@Override
	public boolean insertBreakerNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I
		insertNode(node, new NodeFactory().createBreakerNode());
		m_tsView.updateUI();
		return true;
	}

	public boolean insertCommentNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I
		insertNode(node, new NodeFactory().createCommentNode(""));
		m_tsView.updateUI();
		return true;
	}

	public boolean insertOracleNode() {
		DefaultMutableTreeNode node = getSelectedTreeNode();
		if (node == null)
			return false; // �S���`�I
		insertNode(node, new NodeFactory().createOracleNode());
		m_tsView.updateUI();
		return true;
	}

	// //////////////////////////////////////////////////////////////
}
