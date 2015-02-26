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

public class MacroUtil {
	public static boolean insertNode(AbstractMacroNode parent,
			AbstractMacroNode child) {
		if (parent == null)
			return false;
		if (child == null)
			return false;

		return parent.add(child);
	}

	public static boolean removeNode(AbstractMacroNode parent,
			AbstractMacroNode child) {
		if (parent == null)
			return false;
		if (child == null || child.getParent() == null)
			return false;

		return parent.remove(child);
	}

	public static boolean removeNode(AbstractMacroNode parent, int idx) {
		if (parent == null)
			return false;
		if (idx < 0 || idx >= parent.size())
			return false;

		return parent.remove(idx);
	}

	public static boolean removeNode(AbstractMacroNode node) {
		if (node == null)
			return false;
		if (node.getParent() == null)
			return false;

		return node.getParent().remove(node);
	}

	public static boolean swapNode(AbstractMacroNode nodeA,
			AbstractMacroNode nodeB) {
		if (nodeA == null || nodeA.getParent() == null)
			return false;
		if (nodeB == null || nodeB.getParent() == null)
			return false;
		if (nodeA.getParent() != nodeB.getParent())
			return false;

		AbstractMacroNode p = nodeA.getParent();
		int idxA = p.indexOf(nodeA);
		int idxB = p.indexOf(nodeB);
		p.remove(nodeA);
		p.remove(nodeB);
		p.add(nodeB, idxA);
		p.add(nodeA, idxB);
		return true;
	}

	public static boolean upNode(AbstractMacroNode c) {
		if (c == null || c.getParent() == null)
			return false;
		int index = c.getParent().indexOf(c);
		if (index <= 0)
			return false;

		return swapNode((AbstractMacroNode) c.getParent().get(index - 1), c);
	}

	public static boolean downNode(AbstractMacroNode c) {
		if (c == null || c.getParent() == null)
			return false;
		int index = c.getParent().indexOf(c);
		if (index >= c.getParent().size() - 1)
			return false;

		return swapNode(c, (AbstractMacroNode) c.getParent().get(index + 1));
	}

//	public static MacroComponentNode getLocalParent(AbstractMacroNode node) {
//		if (node.getParent() == null || node.getParent().getParent() == null)
//			return null;
//
//		if (!(node.getParent() instanceof MacroEventNode))
//			return null;
//
//		if (!(node.getParent().getParent() instanceof MacroComponentNode))
//			return null;
//
//		return (MacroComponentNode) node.getParent().getParent();
//	}
}
