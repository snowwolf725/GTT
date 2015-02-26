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
package gtt.util.swing;

import java.util.Iterator;
import java.util.Stack;

import javax.swing.tree.TreeNode;

/**
 * @author Z.W Shen 2005/06/02
 */
public class JTreeNodeIterator implements Iterator<TreeNode> {
	Stack<TreeNode> m_NodeStack = new Stack<TreeNode>();

	// factory method
	public static JTreeNodeIterator createIterator(TreeNode root) {
		return new JTreeNodeIterator(root);
	}

	private JTreeNodeIterator(TreeNode root) {
		m_NodeStack.push(root);
	}

	public boolean hasNext() {
		return !m_NodeStack.empty();
	}

	public TreeNode next() {
		TreeNode topNode;

		topNode = (TreeNode) m_NodeStack.pop();
		// 找到葉節點
		if (topNode.isLeaf())
			return topNode;
		// 將子節點倒著放入堆疊中
		for (int i = topNode.getChildCount() - 1; i >= 0; i--) {
			m_NodeStack.push(topNode.getChildAt(i));
		}
		// 直接回傳folder節點
		return topNode;
	}

	public void remove() {
		// Nothing to do
	}

}