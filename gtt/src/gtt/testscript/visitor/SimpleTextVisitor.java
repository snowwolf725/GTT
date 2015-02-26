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




public class SimpleTextVisitor implements ITestScriptVisitor {

	public int m_Indent = 0;
	public void visit(FolderNode node) {
	
		m_Indent ++;
		for(int i=0;i<m_Indent;i++)
			System.out.print("-");
		System.out.println(">" + node.getName());
		Iterator<AbstractNode> ite = node.iterator();
		while(ite.hasNext()) {
			((AbstractNode)ite.next()).accept(this);
		}
		m_Indent --;
	}

	public void visit(EventNode node) {
		for(int i=0;i<m_Indent;i++)
			System.out.print("-");
		System.out.println(node.getComponent()+ "." + node.getEvent());
	}
	
	public void visit(ViewAssertNode node) {
		System.out.println( node.getComponent() );
	}

	public void visit(ModelAssertNode node) {
		System.out.println( "ModelAssertNode " + node );
	}

	/* (non-Javadoc)
	 * @see testscript.visitor.ITestScriptVisitor#visit(GTTlipse.scriptEditor.TestScript.AUTInfoNode)
	 */
	public void visit(LaunchNode node) {
		System.out.println( "AUTInfoNode " + node );
	}

	public void visit(ReferenceMacroEventNode node) {
		System.out.println( "ReferenceMacroEventNode " + node );
	}

	public void visit(OracleNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SleeperNode node) {
		System.out.println( "SleeperNode " + node );
	}

	@Override
	public void visit(BreakerNode node) {
		System.out.println( "breaker" );
	}

	@Override
	public void visit(CommentNode node) {
		System.out.println( "comment " + node.toString() );
	}

	@Override
	public void visit(ReferenceFitNode node) {
		System.out.println( "ReferenceMacroEventNode " + node );	
	}
}
