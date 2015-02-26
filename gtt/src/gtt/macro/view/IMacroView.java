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

import gtt.editor.view.IView;
import gtt.macro.IMacroPresenter;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;

import javax.swing.tree.TreeNode;

public interface IMacroView extends IView {

	// ////////////////////////////////////////////////////////////////
	// Model-View-Presenter
	public void setPresenter(IMacroPresenter p);

	// ////////////////////////////////////////////////////////////////
	// Tree Operations
	public IMacroTree getMacroTree();

	public void updateUI();

	// ////////////////////////////////////////////////////////////////
	// Document Operations

	public void init(MacroDocument doc);

	public void setTreeModel(Object tree_model);

//	public MacroDocument getMacroDocument();

	// ////////////////////////////////////////////////////////////////
	// Node Operations

	// public void insertMacroEvent();

	// 對選取的node插入一個新的macro node
	public boolean insertMacroComponentNode(String name);

	public boolean insertNode(AbstractMacroNode newNode);

	public void setSelectedNode(TreeNode node);

	public void modifyMacroEventNode();

	public void modifyComponentNode();

	/////////////////
	public Object getMainPanel();

	public String acquireInput(String msg, String title);


}