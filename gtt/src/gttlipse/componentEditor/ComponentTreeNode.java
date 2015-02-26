package gttlipse.componentEditor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class ComponentTreeNode {
	private ComponentTreeNode m_Parent;
	private List<ComponentTreeNode> m_Storage = new LinkedList<ComponentTreeNode>();
	private String m_Name;

	public ComponentTreeNode getParent() {
		return m_Parent;
	}

	public void setParent(ComponentTreeNode p) {
		m_Parent = p;
	}
	
	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		m_Name = name;
	}

	public ComponentTreeNode(String name) {
		m_Name = name;
	}

	public Iterator<ComponentTreeNode> iterator() {
		return m_Storage.iterator();
	}

	public ComponentTreeNode get(int index) {
		if (index < 0)
			return null;
		if (index >= m_Storage.size())
			return null;

		return (ComponentTreeNode) m_Storage.get(index);
	}

	public boolean add(ComponentTreeNode c) {
		if (c == null)
			return false;

		if (c.getParent() != null) {
			c.getParent().remove(c);
		}

		if (m_Storage.add(c) == true) {
			// 加入成功
			c.setParent(this); // 設定 Parent
			return true;
		}

		// 加入失敗
		return false;
	}

	// 加在index之後
	public boolean add(ComponentTreeNode c, int index) {
		if (c == null)
			return false;
		if (index < -1)
			index = -1;

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
			((ComponentTreeNode) (c)).setParent(null);
		}

		return true;
	}

	public boolean remove(ComponentTreeNode c) {
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
	public boolean removeAllNode() {
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
		return m_Name;
//		return m_Name + " (" + size() + ")";
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

	public int indexOf(ComponentTreeNode node) {
		Iterator<ComponentTreeNode> ite = m_Storage.iterator();
		int idx = 0;
		while (ite.hasNext()) {
			ComponentTreeNode n = (ComponentTreeNode) ite.next();
			if (node.equals(n) || node == n)
				return idx;
			idx++;
		}

		return -1; // non-found
	}

	/**
	 * clone 給複製貼上使用
	 */
	public ComponentTreeNode clone() {
		ComponentTreeNode node = new ComponentTreeNode(m_Name);
		if (size() == 0)
			return node;

		Iterator<ComponentTreeNode> ite = iterator();
		while (ite.hasNext()) {
			node.add(((ComponentTreeNode) ite.next()).clone());
		}

		return node;
	}

	/**
	 * 取得該節點之下的子節點
	 */
	public ComponentTreeNode[] getChildren() {
		if (size() == 0)
			return null;

		ComponentTreeNode[] result = new ComponentTreeNode[size()];
		for (int i = 0; i < size(); i++)
			result[i] = get(i);
		return result;
	}
}
