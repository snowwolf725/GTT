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

/**
 * ´ú¸Õ¸}¥»(Test Script) applied "Composite" pattern 20060808
 * 
 * @author zwshen
 */

public abstract class AbstractNode {
	AbstractNode m_Parent = null;
	TestScriptDocument _doc = null;

	public boolean add(AbstractNode c) {
		return false;
	}

	public AbstractNode get(int index) {
		return null;
	}

	public boolean add(AbstractNode c, int index) {
		return false;
	}

	public boolean remove(int index) {
		return false;
	}

	public boolean remove(AbstractNode child) {
		return false;
	}

	public int indexOf(AbstractNode node) {
		return -1;
	}

	public abstract void accept(ITestScriptVisitor v);

	// Null Object 
	protected static final AbstractNode[] NULL_CHILDREN = new AbstractNode[0];
	
	public int size() {
		return 0;
	}

	public boolean hasChildren() {
		return false;
	}

	public AbstractNode[] getChildren() {
		return NULL_CHILDREN;
	}

	public boolean isContainer() {
		return false;
	}

	public AbstractNode getParent() {
		return m_Parent;
	}

	public void setParent(AbstractNode p) {
		m_Parent = p;
	}
	
	public void setDocument(TestScriptDocument doc) {
		_doc = doc;
	}
	
	public TestScriptDocument getDocument() {
		return _doc;
	}

	public abstract AbstractNode clone();

	public abstract String toSimpleString();

	public abstract String toDetailString();
}
