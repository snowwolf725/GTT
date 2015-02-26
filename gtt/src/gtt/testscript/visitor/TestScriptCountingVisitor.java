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
package gtt.testscript.visitor;

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
import gttlipse.fit.node.ReferenceFitNode;

import java.util.Iterator;



public class TestScriptCountingVisitor implements ITestScriptVisitor {
	int m_ctFolder = 0;
	int m_ctEvent = 0;
	int m_ctViewAssert = 0;
	int m_ctModelAssert = 0;
	int m_ctAutInfo = 0;
	int m_ctRefMacro = 0;

	public int getTotal() {
		return m_ctFolder + m_ctEvent + m_ctAutInfo + m_ctRefMacro
				+ m_ctModelAssert + m_ctViewAssert;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("===================\nScript Nodes:");
		sb.append("Event :" + m_ctEvent + "\n");
		sb.append("Folder :" + m_ctFolder + "\n");
		sb.append("MA :" + m_ctModelAssert + "\n");
		sb.append("VA :" + m_ctViewAssert + "\n");
		sb.append("AutInfo :" + m_ctAutInfo + "\n");
		sb.append("RefMacro :" + m_ctRefMacro + "\n");
		sb.append("----------------------------------------\n");
		sb.append("total :" + getTotal() + "\n");
		return sb.toString();
	}

	public void visit(FolderNode node) {
		m_ctFolder++;
		Iterator<AbstractNode> ite = node.iterator();
		while (ite.hasNext()) {
			(ite.next()).accept(this);
		}
	}

	public void visit(EventNode node) {
		m_ctEvent++;
	}

	public void visit(ViewAssertNode node) {
		m_ctViewAssert++;
	}

	public void visit(ModelAssertNode node) {
		m_ctModelAssert++;
	}

	public void visit(LaunchNode node) {
		m_ctAutInfo++;
	}

	public void visit(ReferenceMacroEventNode node) {
		m_ctRefMacro = 0;
	}

	public void visit(OracleNode node) {

	}

	@Override
	public void visit(SleeperNode node) {
		// TODO Auto-generated method stub
		
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
	public void visit(ReferenceFitNode node) {
		// TODO Auto-generated method stub
		
	}
}
