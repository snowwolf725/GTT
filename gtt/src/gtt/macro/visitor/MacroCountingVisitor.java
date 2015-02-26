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
 * Macro 計算節點個數
 *
 * @author zwshen
 */
public class MacroCountingVisitor implements IMacroStructureVisitor {

	int m_ctComponent = 0;
	int m_ctComponentEvent = 0;
	int m_ctMacroComponent = 0;
	int m_ctMacroEvent = 0;
	int m_ctCaller = 0;
	int m_ctModelAssert = 0;
	int m_ctViewAssert = 0;
	int m_ctExistAssert = 0;
	int _tests = 0;

	public int getTotal() {
		return m_ctComponent + m_ctComponentEvent + +m_ctMacroComponent
				+ m_ctMacroEvent + m_ctCaller + m_ctModelAssert
				+ m_ctViewAssert + m_ctExistAssert;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("=============\nMacro:\n");
		sb.append("MComp :" + m_ctMacroComponent + "\n");
		sb.append("MEvent :" + m_ctMacroEvent + "\n");
		sb.append("Component :" + m_ctComponent + "\n");
		sb.append("ComponentE :" + m_ctComponentEvent + "\n");
		sb.append("Caller :" + m_ctCaller + "\n");
		sb.append("MA :" + m_ctModelAssert + "\n");
		sb.append("VA :" + m_ctViewAssert + "\n");
		sb.append("EA :" + m_ctExistAssert + "\n");
		sb.append("----------------------------------------\n");
		sb.append("total :" + getTotal() + "\n");
		sb.append("Test Cases :" + _tests + "\n");
		return sb.toString();
	}

	public MacroCountingVisitor() {
	}

	public void visit(ComponentEventNode node) {
		m_ctComponentEvent++;
	}

	public void visit(ComponentNode node) {
		m_ctComponent++;
	}

	public void visit(MacroComponentNode node) {
		m_ctMacroComponent++;
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}
	}

	public void visit(MacroEventNode node) {
		m_ctMacroEvent++;
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}
		// 計算共有多少個測試
		if(node.getName().toLowerCase().equals("testsuite")) {
			_tests += node.size();
		}
	}

	public void visit(ModelAssertNode node) {
		m_ctModelAssert++;
	}

	public void visit(MacroEventCallerNode node) {
		m_ctCaller++;
	}

	public void visit(ViewAssertNode node) {
		m_ctViewAssert++;
	}
	
	public void visit(ExistenceAssertNode node) {
		m_ctExistAssert++;
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
	public void visit(DynamicComponentEventNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(IncludeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}
}
