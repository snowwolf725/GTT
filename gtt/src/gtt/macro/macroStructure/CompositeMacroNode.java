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
package gtt.macro.macroStructure;

import gttlipse.macro.dialog.EditDialogFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ContainerNode 負責與容器相關的功能 2007/02/28
 * 
 * @author zwshen
 * 
 */
public abstract class CompositeMacroNode extends AbstractMacroNode {

	private List<AbstractMacroNode> m_Storage = new LinkedList<AbstractMacroNode>();

	public CompositeMacroNode(String name) {
		setName(name);
	}

	// refactoring
	// container 相關操作method
	public final AbstractMacroNode get(int index) {
		if (index < 0)
			return null;
		if (index >= m_Storage.size())
			return null;

		return (AbstractMacroNode) m_Storage.get(index);
	}

	// template method zws 2007/07/03
	// 用來輔助判斷是否add()動作能允許新增node
	protected abstract boolean isAllowedAdd(AbstractMacroNode node);

	public final boolean add(AbstractMacroNode node) {
		if (node == null)
			return false;

		if (!isAllowedAdd(node))
			return false;

		if (node.getParent() != null) {
			node.getParent().remove(node);
		}

		if (m_Storage.add(node) == false)
			return false; // fail

		// success
		node.setParent(this); // 設定 Parent
		return true;
	}

	public final boolean add(AbstractMacroNode node, int index) {
		if (node == null)
			return false;

		if (!isAllowedAdd(node))
			return false;

		try {
			m_Storage.add(index, node);
			node.setParent(this); // 設定 Parent
			return true;
		} catch (Exception exp) {
			return false;
		}
	}

	public final boolean remove(int index) {
		if (index < 0)
			return false;
		if (index >= m_Storage.size())
			return false;

		AbstractMacroNode node = m_Storage.remove(index);
		if (node == null)
			return false;

		// remove the reference to parent
		node.setParent(null);
		return true;
	}

	public final boolean remove(AbstractMacroNode node) {
		if (node == null)
			return false;

		if (m_Storage.remove(node) == false)
			return false;
		
		// remove the reference to parent
		node.setParent(null);
		return true;
	}

	public final boolean removeAll() {
		m_Storage.clear();
		return true;
	}

	public final boolean isContainer() {
		return true;
	}

	public final int size() {
		return m_Storage.size();
	}

	public final int indexOf(AbstractMacroNode node) {
		Iterator<AbstractMacroNode> ite = iterator();
		int idx = 0;
		while (ite.hasNext()) {
			if (ite.next() == node)
				return idx;
			idx++;
		}
		return -1;
	}

	public final Iterator<AbstractMacroNode> iterator() {
		return m_Storage.iterator();
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_COMPOSITE_MACRO_NODE;
	}
}
