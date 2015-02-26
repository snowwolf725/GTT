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
package gtt.editor.view.dialog;

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

/* 
 * selector 有使用 Null Object pattern
 * 就可以不考慮dialog 為 null 的情形
 * -zwshen 2006/12/01
 */

/*
 * 這邊所產生的dialog 都是 swing version
 * -zwshen 2006/12/01
 */
public class SwingDialogTestScriptVisitor implements ITestScriptVisitor {
	IScriptDialogAbstractFactory m_AbstractFactory;

	public SwingDialogTestScriptVisitor(IScriptDialogAbstractFactory factory) {
		m_AbstractFactory = factory;
	}

	public void visit(FolderNode node) {
		INodeDialog dialog = m_AbstractFactory.createFolderNodeDialog();
		dialog.setNode(node);
		dialog.appear();
	}

	public void visit(EventNode node) {
		INodeDialog dialog = m_AbstractFactory.createEventNodeDialog();
		dialog.setNode(node);
		dialog.appear();
	}

	public void visit(ViewAssertNode node) {
		INodeDialog dialog = m_AbstractFactory.createViewAssertNodeDialog();
		dialog.setNode(node);
		dialog.appear();
	}

	public void visit(ModelAssertNode node) {
		INodeDialog dialog = m_AbstractFactory.createModelAssertNodeDialog();
		dialog.setNode(node);
		dialog.appear();
	}

	public void visit(LaunchNode node) {
		INodeDialog dialog = m_AbstractFactory.createAUTInfoNodeDialog();
		dialog.setNode(node);
		dialog.appear();
	}

	public void visit(ReferenceMacroEventNode node) {
		INodeDialog dialog = m_AbstractFactory.createReferenceMacroNodeDialog();
		dialog.setNode(node);
		dialog.appear();
	}

	public void visit(OracleNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SleeperNode node) {
		INodeDialog dialog = m_AbstractFactory.createSleeperNodeDialog();
		dialog.setNode(node);
		dialog.appear();

	}

	@Override
	public void visit(BreakerNode node) {

	}

	@Override
	public void visit(CommentNode node) {
		INodeDialog dialog = m_AbstractFactory.createCommentNodeDialog();
		dialog.setNode(node);
		dialog.appear();
	}

	@Override
	public void visit(ReferenceFitNode node) {
		// TODO Auto-generated method stub
		
	}
}
