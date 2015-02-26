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
package gtt.macro.view.dialog;

import gtt.editor.view.dialog.INodeDialog;
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
import gtt.macro.view.IMacroView;
import gtt.macro.visitor.IMacroStructureVisitor;

/*
 * selector 有使用 Null Object pattern
 * 就可以不考慮dialog 為 null 的情形
 * -zwshen 2006/12/01
 */

/*
 * 這邊所產生的dialog 都是 swing version
 * -zwshen 2006/12/01
 */
public class SwingMacroDialogVisitor implements IMacroStructureVisitor {
	IMacroDialogAbstractFactory m_Factory;
	IMacroView m_view;

	public SwingMacroDialogVisitor(IMacroDialogAbstractFactory factory) {
		m_Factory = factory;
	}

	public SwingMacroDialogVisitor(IMacroDialogAbstractFactory factory,	IMacroView view) {
		m_Factory = factory;
		m_view = view;
	}

	public void visit(ComponentEventNode node) {
		appear(m_Factory.forComponentEventDialog(), node);
	}

	public void visit(ComponentNode node) {
	}

	public void visit(MacroComponentNode node) {
		appear(m_Factory.forMacroComponentDialog(), node);
	}

	public void visit(MacroEventNode node) {
		m_view.modifyMacroEventNode();
	}

	public void visit(ModelAssertNode node) {
	}

	public void visit(MacroEventCallerNode node) {
		appear(m_Factory.forMacroEventCallerDialog(), node);
	}

	public void visit(ViewAssertNode node) {
	}

	public void visit(ExistenceAssertNode node) {
	}
	
	public void visit(NDefComponentNode node) {
	}

	private void appear(INodeDialog dialog, AbstractMacroNode node) {
		dialog.setNode(node);
		dialog.appear();
	}

	@Override
	public void visit(BreakerNode node) {
	}

	@Override
	public void visit(CommentNode node) {
	}

	@Override
	public void visit(SleeperNode node) {
	}

	@Override
	public void visit(OracleNode node) {
	}

	@Override
	public void visit(LaunchNode node) {
	}

	@Override
	public void visit(SplitDataNode node) {
	}

	@Override
	public void visit(DynamicComponentNode node) {
	}

	@Override
	public void visit(IncludeNode node) {
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
	}

	@Override
	public void visit(SystemNode node) {
	}
}
