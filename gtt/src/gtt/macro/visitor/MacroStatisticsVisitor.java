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
package gtt.macro.visitor;

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

import java.util.Iterator;

/**
 * Macro 統計資訊
 * 
 * @author zwshen
 */
public class MacroStatisticsVisitor implements IMacroStructureVisitor {

	public MacroStatisticsVisitor() {
	}

	public void visit(ComponentEventNode node) {
		// addTreeNode(node);
	}

	public int getPrimitiveComponentSize() {
		return count;
	}

	public int count = 0;

	public void visit(ComponentNode node) {
		count++;
		System.out.println(count + ": " + node.getComponent().toString());
	}

	public void visit(MacroComponentNode node) {
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			((AbstractMacroNode) ite.next()).accept(this);
		}
	}

	public void visit(MacroEventNode node) {
		// nothing
	}

	public void visit(ModelAssertNode node) {
		// nothing
	}
	
	public void visit(ExistenceAssertNode node) {
		// nothing
	}

	public void visit(MacroEventCallerNode node) {
		// nothing
	}

	public void visit(ViewAssertNode node) {
		// nothing
	}

	public void visit(NDefComponentNode node) {
		// nothing
	}

	@Override
	public void visit(BreakerNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(CommentNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SleeperNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(OracleNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(LaunchNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SplitDataNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(DynamicComponentNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(IncludeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}
}
