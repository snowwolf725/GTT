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
package gtt.macro.view;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.SystemNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitStateAssertionNode;

import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeMacroVisitor implements IMacroStructureVisitor {

	private DefaultMutableTreeNode m_Root = null;

	public JTree createJTree() {
		JTree tree = new JTree(m_Root);
		return tree;
	}

	public JTreeMacroVisitor() {
	}

	public void visit(ComponentEventNode node) {
		addTreeNode(node);
	}

	public void visit(ComponentNode node) {
		addTreeNode(node);
	}

	private void addTreeNode(AbstractMacroNode node) {
		m_Root.add(new DefaultMutableTreeNode(node));
	}

	public void visit(MacroComponentNode node) {
		DefaultMutableTreeNode oldroot = m_Root;
		m_Root = new DefaultMutableTreeNode(node);

		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}

		if (oldroot != null) {
			// 避免多一個root節點
			oldroot.add(m_Root);
			m_Root = oldroot;
		}
	}

	public void visit(MacroEventNode node) {
		DefaultMutableTreeNode oldroot = m_Root;
		m_Root = new DefaultMutableTreeNode(node);
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}
		if (oldroot != null) {
			// 避免多一個root節點
			oldroot.add(m_Root);
			m_Root = oldroot;
		}
	}

	public void visit(ModelAssertNode node) {
		addTreeNode(node);
	}

	public void visit(MacroEventCallerNode node) {
		addTreeNode(node);
	}

	public void visit(ViewAssertNode node) {
		addTreeNode(node);
	}
	
	public void visit(ExistenceAssertNode node) {
		addTreeNode(node);
	}

	public void visit(NDefComponentNode node) {
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
	public void visit(SleeperNode node) {
		addTreeNode(node);
	}
	
	public void visit(EventTriggerNode node) {
		addTreeNode(node);
	}
	
	public void visit(FitStateAssertionNode node) {
		addTreeNode(node);
	}
	
	@Override
	public void visit(OracleNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(LaunchNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(SplitDataNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(DynamicComponentNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(IncludeNode node) {
		addTreeNode(node);
		
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		addTreeNode(node);
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}
}
