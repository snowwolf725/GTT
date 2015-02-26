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

import gtt.editor.configuration.IConfiguration;
import gtt.editor.view.ITestScriptView;
import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.visitor.ITestScriptVisitor;

import java.util.Iterator;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;

/**
 * Model-View-Presenter 2007/02/08
 *
 * @author zwshen
 */

public interface ITestScriptPresenter {

	// =====================================================================
	public void handleOpenFile(String filepath);

	public void updateView();

	// =====================================================================
	public ITestScriptView getView();

	public void setView(ITestScriptView v);

	public TestScriptDocument getModel();

	public void setModel(TestScriptDocument doc);

	// =====================================================================
	public void acceptVisitor(ITestScriptVisitor v);

	public JTree getJTree();

	public void setJTree(JTree tree);

	public AbstractNode selectedNode();

	// =====================================================================
	// node operations

	public boolean modifyNode();

	public boolean upMoveNode();

	public boolean downMoveNode();

	public boolean removeNode();

	// public boolean modifyNode(ITestScriptVisitor v);

	public void selectTreeNode(TreeNode node);

	// =====================================================================
	// Cut/Copy/Paste
	// add by zws 2007/06/27

	public boolean cutNode();

	public boolean copyNode();

	public boolean pasteNode();

	public void resetTestScript();

	// =====================================================================
	// insert operations
	public boolean addNodes(List<AbstractNode> list);

	public boolean addNode(AbstractNode newNode);

	public boolean insertRerenceMacroEventNode(String paths);

	public boolean insertFolderNode();

	public boolean insertEventNode();

	public boolean insertViewAssertNode();

	public boolean insertModelAssertNode();

	public boolean insertLaunchNode();

	public boolean insertSleeperNode();

	public boolean insertBreakerNode();

	public boolean insertCommentNode();

	public boolean insertOracleNode();
	
	public Iterator<TreeNode> treeIterator();

	// =====================================================================
	// Configuration data - zws 2007/07/11
	public void setConfiguration(IConfiguration config);
}
