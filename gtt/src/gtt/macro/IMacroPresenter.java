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
package gtt.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.view.IMacroTree;
import gtt.macro.view.IMacroView;

public interface IMacroPresenter {
	// =====================================================================
	// general method

	public AbstractMacroNode getSelectedNode();

	// =====================================================================
	public void handleOpenFile(String filepath);

	public void handleSaveFile(String filepath);

	// =====================================================================
	// Model
	public MacroDocument getModel();

	public void setModel(MacroDocument doc);

	// =====================================================================
	// View
	public IMacroView getView();

	public void setView(IMacroView view);
	
	public void updateView();

	// =====================================================================
	public IMacroTree getMacroTree();

	// =====================================================================
	// Node operations add by zwshen 2007/06/27

	public boolean deleteNode();

	public boolean upMoveNode();

	public boolean downMoveNode();

	public boolean Rename();
	
	public boolean modify_node();
	public boolean modify_contract();

	// =====================================================================
	// Cut/Copy/Paste add by zwshen 2007/06/27

	public boolean cutNode();

	public boolean copyNode();

	public boolean pasteNode();
	
	public boolean runMacroEvent();

}
