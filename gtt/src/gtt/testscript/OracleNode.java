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
 * 
 */
package gtt.testscript;

import gtt.oracle.OracleData;
import gtt.testscript.visitor.ITestScriptVisitor;

import java.util.Iterator;

/**
 * @author SnowWolf
 * 
 *         created first in project testscript
 * 
 */
public class OracleNode extends FolderNode implements Cloneable {
	private OracleData m_data = new OracleData();

	OracleNode() {
		super("TestOracle");
	}

	public OracleData getOracleData() {
		return m_data;
	}

	public void setOracleData(OracleData d) {
		m_data = d;
	}

	public void accept(ITestScriptVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return getName();
	}

	public String toDetailString() {
		return toString();
	}

	public String toSimpleString() {
		return toString();
	}

	@Override
	public OracleNode clone() {
		OracleNode node = new OracleNode();
		node.setOracleData(m_data.clone());

		Iterator<AbstractNode> ite = iterator();
		while (ite.hasNext()) {
			AbstractNode obj = ite.next();
			node.add(obj.clone());
		}

		return node;
	}

	@Override
	public boolean add(AbstractNode node) {
		if (node == null)
			return false;
		if (node == this)
			return false;
		// 只能加入 ViewAssertNode
		if (!(node instanceof ViewAssertNode))
			return false;

		return super.add(node);
	}

}
