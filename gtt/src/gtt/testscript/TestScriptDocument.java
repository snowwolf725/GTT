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

import gtt.testscript.io.IXmlFileReader;
import gtt.testscript.io.TestScriptXmlReader;
import gttlipse.scriptEditor.testScript.BaseNode;

import java.util.Iterator;
import java.util.Stack;


/**
 * "Application Facade" 這個物件做為Test Script所有操作的"門面(Facade)"， 如新增、刪除、移動節點，或存讀檔等功能。
 * 1. 與GUI操作無關 2. 使用TDD的方式設計
 * 
 * @author zwshen 2006/08/25
 */

public class TestScriptDocument {

	AbstractNode m_script;

	private String m_name;

	private BaseNode m_parent;

	public TestScriptDocument(AbstractNode root) {
		m_script = root;
		m_script.setDocument(this);
		m_name = "";
	}

	public AbstractNode getScript() {
		return m_script;
	}

	/**
	 * 查詢 root 之下是否有子節點
	 * 
	 * @param parent
	 * @return
	 */
	public boolean hasChildren() {
		return m_script.size() > 0;
	}

	/**
	 * 取得該節點之下的子節點
	 * 
	 * @return
	 */
	public AbstractNode[] getChildren() {
		if (!hasChildren())
			return new AbstractNode[0]; // Empty Array

		AbstractNode[] result = new AbstractNode[m_script.size()];
		for (int i = 0; i < m_script.size(); i++)
			result[i] = m_script.get(i);
		return result;
	}

	/**
	 * 設定此份 Document 的名稱
	 * 
	 * @param n
	 *            the m_name to set
	 */
	public void setName(String n) {
		m_name = n;
	}

	/**
	 * @return the m_name
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * 設定擁有此份 TestScriptDocument 的擁有者
	 * 
	 * @param p
	 *            the m_parent to set
	 */
	public void setParent(BaseNode p) {
		m_parent = p;
	}

	/**
	 * @return the m_parent
	 */
	public BaseNode getParent() {
		return m_parent;
	}

	/**
	 * Insert an node
	 */
	public boolean insertNode(AbstractNode parent, AbstractNode child) {
		if (parent == null)
			return false;
		if (child == null)
			return false;

		return parent.add(child);
	}

	/**
	 * remove an node
	 */
	public boolean removeNode(AbstractNode parent, AbstractNode child) {
		if (parent == null)
			return false;
		if (child == null || child.getParent() == null)
			return false;

		return parent.remove(child);
	}

	public boolean removeNode(AbstractNode parent, int idx) {
		if (parent == null)
			return false;
		if (idx < 0 || idx >= parent.size())
			return false;

		return parent.remove(idx);
	}

	public boolean removeNode(AbstractNode node) {
		if (node == null)
			return false;
		if (node.getParent() == null)
			return false;

		return node.getParent().remove(node);
	}

	/**
	 * 交換二個節點
	 */
	public boolean swapNode(AbstractNode nodeA, AbstractNode nodeB) {
		if (nodeA == null || nodeA.getParent() == null)
			return false;
		if (nodeB == null || nodeB.getParent() == null)
			return false;
		if (nodeA.getParent() != nodeB.getParent())
			return false;

		AbstractNode p = nodeA.getParent();
		int idxA = p.indexOf(nodeA);
		int idxB = p.indexOf(nodeB);
		p.remove(nodeA);
		p.remove(nodeB);
		p.add(nodeB, idxA);
		p.add(nodeA, idxB);
		return true;
	}

	/**
	 * 將節點index往前(上)移一個
	 */
	public boolean upNode(AbstractNode c) {
		if (c == null || c.getParent() == null)
			return false;
		int index = c.getParent().indexOf(c);
		if (index <= 0)
			return false;

		return swapNode(c.getParent().get(index - 1), c);
	}

	/**
	 * 將節點index往後(下)移一個
	 */
	public boolean downNode(AbstractNode c) {
		if (c == null || c.getParent() == null)
			return false;
		int index = c.getParent().indexOf(c);
		if (index >= c.getParent().size() - 1)
			return false;

		return swapNode(c, c.getParent().get(index + 1));
	}

	/**
	 * clone 給複製貼上使用
	 */
	public TestScriptDocument clone() {
		TestScriptDocument doc = new TestScriptDocument(m_script.clone());
		doc.setName(getName());
		return doc;
	}

	/**
	 * Factory method 2006/12/08
	 */
	public static String DEFAULT_NAME = "TEST_SCRIPT";

	public static TestScriptDocument create() {
		return create(DEFAULT_NAME);
	}

	
	public static TestScriptDocument create(String name) {
		NodeFactory factory = new NodeFactory();
		TestScriptDocument doc = new TestScriptDocument(factory
				.createFolderNode("Root"));
		doc.setName(DEFAULT_NAME);
		return doc;
	}
	
	// /**
	// * save to xml file 2006/12/08
	// */
	//
	// public void saveFile(String filepath) {
	// TestScriptXmlSaveVisitor v = new TestScriptXmlSaveVisitor();
	// m_script.accept(v);
	// v.saveFile(filepath);
	// }

	/**
	 * load from xml file 2006/12/08
	 */
	public boolean openFile(String filepath) {
		try {
			IXmlFileReader r = new TestScriptXmlReader();
			AbstractNode newRoot = r.read(filepath);
			if (newRoot != null)
				m_script = newRoot;
		} catch (Exception exp) {
			return false;
		}
		return true;
	}

	public Iterator<AbstractNode> iterator() {
		return new TestScriptIterator(m_script);
	}
}

// test script 自訂 iterator

class TestScriptIterator implements Iterator<AbstractNode> {
	Stack<AbstractNode> m_NodeStack = new Stack<AbstractNode>();

	public TestScriptIterator(AbstractNode root) {
		if (root == null)
			return;
		m_NodeStack.push(root);
	}

	public boolean hasNext() {
		return !m_NodeStack.empty();
	}

	public AbstractNode next() {
		AbstractNode topNode;

		topNode = m_NodeStack.pop();
		// 找到葉節點
		if (!topNode.isContainer())
			return topNode;

		// 將子節點倒著放入堆疊中
		for (int i = topNode.size() - 1; i >= 0; i--) {
			m_NodeStack.push(topNode.get(i));
		}

		// 直接回傳folder節點
		return topNode;
	}

	public void remove() {
		// Nothing to do
	}
}
