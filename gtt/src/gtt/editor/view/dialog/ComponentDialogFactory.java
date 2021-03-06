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


// SwingComponentDialogSelector 只產生 Component 編輯用的dialog
public class ComponentDialogFactory implements IScriptDialogAbstractFactory {

	// /////////////////////////////////////////////
	// Test Script
	// /////////////////////////////////////////////

	public INodeDialog createEventNodeDialog() {
		return new ComponentDialog();
	}

	public INodeDialog createFolderNodeDialog() {
		return new FolderNodeDialog();
	}

	public INodeDialog createModelAssertNodeDialog() {
		return DummyDialog.DUMMY_DIALOG;
	}

	public INodeDialog createViewAssertNodeDialog() {
		return new ComponentDialog();
	}

	// zws 2007/07/11
	public INodeDialog createReferenceMacroNodeDialog() {
		return new ReferenceMacroNodeDialog();
	}

	// zws 2007/07/11
	public INodeDialog createAUTInfoNodeDialog() {
		return new AUTInfoNodeDialog();
	}

	// /////////////////////////////////////////////
	// Macro
	// /////////////////////////////////////////////
	public INodeDialog createSingleMacroNodeDialog() {
		return DummyDialog.DUMMY_DIALOG;
	}

	public INodeDialog createMacroComponentDialog() {
		return DummyDialog.DUMMY_DIALOG;
	}

	@Override
	public INodeDialog createSleeperNodeDialog() {
		return new SleeperNodeDialog();
	}

	@Override
	public INodeDialog createCommentNodeDialog() {
		return new CommentNodeDialog();
	}

}
