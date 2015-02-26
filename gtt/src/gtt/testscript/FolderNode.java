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
package gtt.testscript;

import gtt.testscript.visitor.ITestScriptVisitor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FolderNode extends AbstractNode {

	private String m_Name;

	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		m_Name = name;
	}

	// package-private
	FolderNode(String name) {
		m_Name = name;
	}

	private List<AbstractNode> m_Storage = new LinkedList<AbstractNode>();

	public Iterator<AbstractNode> iterator() {
		return m_Storage.iterator();
	}

	public void accept(ITestScriptVisitor v) {
		v.visit(this);
	}

	public AbstractNode get(int index) {
		if (index < 0)
			return null;
		if (index >= m_Storage.size())
			return null;

		return (AbstractNode) m_Storage.get(index);
	}

	public boolean add(AbstractNode c) {
		if (c == null)
			return false;

		if (c.getParent() != null) {
			c.getParent().remove(c);
		}

		// add fail
		if (m_Storage.add(c) == false)
			return false;

		// 加入成功
		c.setParent(this); // 設定 Parent
		return true;
	}

	// 加在index之後
	public boolean add(AbstractNode c, int index) {
		if (c == null)
			return false;
		if (index < 0)
			index = 0;

		if (index > size())
			index = size();
		m_Storage.add(index, c);
		c.setParent(this);
		return true;
	}

	public boolean remove(int index) {
		if (index < 0)
			return false;
		if (index >= m_Storage.size())
			return false;

		Object c = m_Storage.remove(index);
		if (c != null) {
			// remove the reference to parent
			((AbstractNode) (c)).setParent(null);
		}

		return true;
	}

	public boolean remove(AbstractNode c) {
		if (c == null)
			return false;

		if (m_Storage.remove(c)) {
			// remove the reference to parent
			c.setParent(null);
			return true;
		}

		return false;
	}

	/**
	 * remove all node
	 */
	public boolean removeAll() {
		if (m_Storage == null || m_Storage.size() == 0)
			return false;

		// 一個一個的移除
		while (size() != 0) {
			m_Storage.remove(0);
		}

		return true;
	}

	public int size() {
		return m_Storage.size();
	}

	public String toString() {
		return m_Name + " (" + size() + ")";
	}

	public String toDetailString() {
		return toString();
	}

	public String toSimpleString() {
		return m_Name;
	}

	/**
	 * 可允許加入子節點 (Composite)
	 */
	public boolean isContainer() {
		return true;
	}

	public int indexOf(AbstractNode node) {
		Iterator<AbstractNode> ite = m_Storage.iterator();
		int idx = 0;
		while (ite.hasNext()) {
			AbstractNode n = (AbstractNode) ite.next();
			if (node.equals(n) || node == n)
				return idx;
			idx++;
		}

		return -1; // non-found
	}

	/**
	 * clone 給複製貼上使用
	 */
	public FolderNode clone() {
		FolderNode node = new FolderNode(m_Name);
		if (size() == 0)
			return node;

		Iterator<AbstractNode> ite = iterator();
		while (ite.hasNext()) {
			node.add(((AbstractNode) ite.next()).clone());
		}

		return node;
	}

	/**
	 * 取得該節點之下的子節點
	 */
	public AbstractNode[] getChildren() {
		if (size() == 0)
			return NULL_CHILDREN;

		AbstractNode[] result = new AbstractNode[size()];
		for (int i = 0; i < size(); i++)
			result[i] = get(i);
		return result;
	}
	
	public boolean hasChildren() {
		return (size()==0)?false:true;
	}
}
