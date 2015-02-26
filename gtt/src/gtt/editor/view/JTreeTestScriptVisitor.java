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

import gtt.testscript.AbstractNode;
import gtt.testscript.BreakerNode;
import gtt.testscript.CommentNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.SleeperNode;
import gtt.testscript.ViewAssertNode;
import gtt.testscript.visitor.ITestScriptVisitor;
import gttlipse.fit.node.ReferenceFitNode;

import java.awt.Component;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


/**
 * 這個 visitor 用來將 Test Script 轉成 Swing JTree 的結構
 * 
 * @author zwshen
 * 
 */
public class JTreeTestScriptVisitor implements ITestScriptVisitor {

	private DefaultMutableTreeNode m_Root = null;

	private TreeNodeDataFactory m_Factory = null;

	public JTreeTestScriptVisitor(TreeNodeDataFactory f) {
		m_Factory = f;
	}

	public void visit(FolderNode node) {
		DefaultMutableTreeNode oldroot = m_Root;
		m_Root = new DefaultMutableTreeNode(m_Factory.createData(node));
		Iterator<AbstractNode> ite = node.iterator();
		while (ite.hasNext()) {
			((AbstractNode) ite.next()).accept(this);
		}
		if (oldroot != null) {
			// 避免多一個root節點
			oldroot.add(m_Root);
			m_Root = oldroot;
		}
	}

	public void visit(EventNode node) {
		addTreeNode(node);
	}

	public void visit(ViewAssertNode node) {
		addTreeNode(node);
	}

	public void visit(ModelAssertNode node) {
		addTreeNode(node);
	}

	private void addTreeNode(AbstractNode node) {
		m_Root.add(new DefaultMutableTreeNode(m_Factory.createData(node)));
	}

	public JTree createJTree() {
		JTree tree = new JTree(m_Root);
		tree.setCellRenderer(new ScriptTreeRender());
		return tree;
	}

	public void visit(LaunchNode node) {
		addTreeNode(node);
	}

	public void visit(ReferenceMacroEventNode node) {
		addTreeNode(node);
	}

	public void visit(OracleNode node) {
		DefaultMutableTreeNode oldroot = m_Root;
		m_Root = new DefaultMutableTreeNode(m_Factory.createData(node));
		Iterator<AbstractNode> ite = node.iterator();
		while (ite.hasNext()) {
			((AbstractNode) ite.next()).accept(this);
		}
		if (oldroot != null) {
			// 避免多一個root節點
			oldroot.add(m_Root);
			m_Root = oldroot;
		}
	}

	@Override
	public void visit(SleeperNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(BreakerNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(CommentNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(ReferenceFitNode node) {
		addTreeNode(node);
	}
}

class ScriptTreeRender extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final String PATH = System.getProperty("user.dir")
			+ "/images/";

	@SuppressWarnings("unused")
	private static final ImageIcon m_Icon_MacroComponent = new ImageIcon(PATH
			+ "node2_macro.gif");

	private static final ImageIcon m_Icon_SingleComponent = new ImageIcon(PATH
			+ "node2_component.gif");

	@SuppressWarnings("unused")
	private static final ImageIcon m_Icon_ComponentBranch = new ImageIcon(PATH
			+ "node2_branchComponent.gif");

	@SuppressWarnings("unused")
	private static final ImageIcon m_Icon_EventBranch = new ImageIcon(PATH
			+ "node2_branchEvent.gif");

	@SuppressWarnings("unused")
	private static final ImageIcon m_Icon_EventList = new ImageIcon(PATH
			+ "node2_eventList.gif");

	private static final ImageIcon m_Icon_MacroEvent = new ImageIcon(PATH
			+ "node2_eventList.gif");

	private static final ImageIcon m_Icon_ComponentEvent = new ImageIcon(PATH
			+ "node2_componentEvent.gif");

	private static final ImageIcon m_Icon_ComponentAssert = new ImageIcon(PATH
			+ "node2_componentAssert.gif");

	private static final ImageIcon m_Icon_JUnitAssert = new ImageIcon(PATH
			+ "node2_JUnitAssert.gif");

	private static final ImageIcon m_Icon_Folder = new ImageIcon(PATH
			+ "folder.gif");

	private static final ImageIcon m_Icon_AUTInfo = new ImageIcon(PATH
			+ "AUTInfoNode.png");
	private static final ImageIcon m_Icon_Sleeper = new ImageIcon(PATH
			+ "sleeper.gif");
	private static final ImageIcon m_Icon_Breaker = new ImageIcon(PATH
			+ "break.gif");
	private static final ImageIcon m_Icon_Comment = new ImageIcon(PATH
			+ "comment.gif");

	private static final ImageIcon m_Icon_Oracle = new ImageIcon(PATH
			+ "TestOracle.png");

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component comp = super.getTreeCellRendererComponent(tree, value,
				selected, expanded, leaf, row, hasFocus);
		try {
			AbstractNode node = ((TreeNodeData) ((DefaultMutableTreeNode) value)
					.getUserObject()).getData();

			if (node instanceof OracleNode) {
				setIcon(m_Icon_Oracle);
			} else if (node instanceof FolderNode) {
				setIcon(m_Icon_Folder);
			} else if (node instanceof EventNode) {
				setIcon(m_Icon_ComponentEvent);
			} else if (node instanceof ViewAssertNode) {
				setIcon(m_Icon_ComponentAssert);
			} else if (node instanceof ModelAssertNode) {
				setIcon(m_Icon_JUnitAssert);
			} else if (node instanceof ReferenceMacroEventNode) {
				setIcon(m_Icon_MacroEvent);
			} else if (node instanceof LaunchNode) {
				setIcon(m_Icon_AUTInfo);
			} else if (node instanceof SleeperNode) {
				setIcon(m_Icon_Sleeper);
			} else if (node instanceof BreakerNode) {
				setIcon(m_Icon_Breaker);
			} else if (node instanceof CommentNode) {
				setIcon(m_Icon_Comment);
			}
		} catch (Exception exp) {
			// default
			setIcon(m_Icon_SingleComponent);
		}
		return comp;
	}
}
