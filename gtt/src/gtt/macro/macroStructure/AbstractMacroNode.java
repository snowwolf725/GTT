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
/**
 * Created on 2005/6/20
 */

// Macro 相關類別階層圖
// SingleEventBaseNode
// - MacroEventNode
// - SingleClassAssertNode
// - SngleComponentAssertNode
// - SingleComponentEventNode
// - SingleMacroEventNode
package gtt.macro.macroStructure;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroPath;
import gtt.macro.macroStructure.MacroBadSmellData;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gtt.util.DataPool;

import java.util.Iterator;

// public abstract class AbstractMacroNode extends DefaultMutableTreeNode {
public abstract class AbstractMacroNode implements Cloneable {

	private MacroPath m_MacroPath = new MacroPath(this);

	private AbstractMacroNode m_Parent;

	protected String m_Name = "";

	private DataPool _pool = DataPool.getDataPool();
	
	final public MacroPath getPath() {
		return m_MacroPath;
	}

	final public void setParent(AbstractMacroNode parent) {
		m_Parent = parent;
	}

	final public AbstractMacroNode getParent() {
		return m_Parent;
	}

	final public AbstractMacroNode[] getChildren() {
		if (!hasChildren())
			return new AbstractMacroNode[0]; 

		AbstractMacroNode[] result = new AbstractMacroNode[size()];
		for (int i = 0; i < size(); i++)
			result[i] = (AbstractMacroNode) get(i);

		return result;
	}

	public void setName(String name) {
		m_Name = name;
	}

	public String getName() {
		return m_Name;
	}

	public void setVariable(String var, Object data) {
		_pool.setData(var, data);
	}
	
	public Object getVariable(String var) {
		return _pool.getData(var);
	}
	
	public void accept(IMacroStructureVisitor v) {
	}

	public void accept(IMacroFitVisitor v) {
	}

	public AbstractMacroNode get(int index) {
		return null;
	}

	public boolean add(AbstractMacroNode node) {
		return false;
	}

	public boolean add(AbstractMacroNode node, int index) {
		return false;
	}

	public boolean remove(int index) {
		return false;
	}

	public boolean remove(AbstractMacroNode node) {
		return false;
	}

	public boolean removeAll() {
		return false;
	}

	public Iterator<AbstractMacroNode> iterator() {
		return null;
	}

	public boolean isContainer() {
		return false;
	}

	public int size() {
		return 0;
	}

	public boolean hasChildren() {
		return size() != 0;
	}

	public int indexOf(AbstractMacroNode node) {
		return -1;
	}

	public abstract AbstractMacroNode clone();

	
	private MacroBadSmellData m_badSmellData = new MacroBadSmellData();

	public MacroBadSmellData getBadSmellData() {
		return m_badSmellData;
	}

	public void setBadSmellData(MacroBadSmellData data) {
		m_badSmellData = data;
	}
	
	public abstract int getNodeID();
}
