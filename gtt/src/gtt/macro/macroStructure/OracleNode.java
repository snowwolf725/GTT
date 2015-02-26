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
/*
 * Created on 2005/3/12 by ­õ»Ê
 */
package gtt.macro.macroStructure;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gtt.oracle.OracleData;
import gttlipse.macro.dialog.EditDialogFactory;

public class OracleNode extends AbstractMacroNode {

	private OracleData m_data;

	public OracleNode() {
		setName("TestOracle");
		m_data = new OracleData();
	}

	public OracleNode(OracleData d) {
		setName("TestOracle");
		m_data = d.clone();
	}

	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	public OracleData getOracleData() {
		return m_data;
	}

	public String toString() {
		return getName();
	}

	@Override
	public OracleNode clone() {
		return new OracleNode(m_data);
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_ORACLE_NODE;
	}

}
