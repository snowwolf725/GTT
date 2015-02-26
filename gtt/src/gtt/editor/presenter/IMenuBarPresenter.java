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
package gtt.editor.presenter;

import gtt.editor.view.IEditor;

public interface IMenuBarPresenter {

	public ITestScriptPresenter getScriptPresenter();

	// =====================================================================
	public void attach(IEditor v);

	public IEditor getView();

	// =====================================================================
	// File IO
	public boolean newFile();

	public boolean openFile();

	public boolean saveFile();

	public boolean saveAsFile();

	public boolean saveGTMLScript();

	// =====================================================================
	// AUT
	public boolean launchAUT();

	public boolean terimentAUT();

	public boolean loadAUT(String filepath, String classpath);

	// =====================================================================
	// Capture/Replay
	public boolean replay();

	public boolean startCapture();

	public boolean stopCapture();

	// =====================================================================
	// Script

	public void resetTestScript();

	// =====================================================================
	// Configuration

	public boolean configuration();

	// =====================================================================

	// automatically component abstraction
	public boolean doGUIHierarchyReverse();

	// Macro statistics
	public boolean doMacroStatistics();

	// Nodes Counting
	public boolean doNodesCounting();

	// testscript-macro ¤¬Âà
	public boolean macroToScript();

	public boolean ScriptToMacro();

	// State-coverage Test-Case Generation
	public boolean generateSCTestCsae();

	// Edge-coverage Test-Case Generation
	public boolean generateECTestCsae();

	// Consecutive-edge coverage Test-Case Generation
	public boolean generateCCTestCsae();

}
