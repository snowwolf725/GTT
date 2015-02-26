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
 * Created on 2005/5/19
 */
package gtt.macro.macroStructure;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.macro.dialog.EditDialogFactory;

public class ModelAssertNode extends AbstractMacroNode {
	private String m_ClassUrl = null;
	private String m_MethodName = null;

	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	public ModelAssertNode() {
	}

	public ModelAssertNode(ModelAssertNode node) {
		setInfo(node.getClassUrl(), node.getMethod());
	}

	/**
	 * @return Returns the m_ClassUrl.
	 */
	public String getClassUrl() {
		return m_ClassUrl;
	}

	public boolean setInfo(String classUrl, String method) {
		if (classUrl == null || method == null)
			return false;
		m_ClassUrl = classUrl;
		m_MethodName = method;
		return true;
	}

	/**
	 * @return Returns the m_Method.
	 */
	public String getMethod() {
		return m_MethodName;
	}

	public ModelAssertNode clone() {
		return new ModelAssertNode(this);
	}

	public String getName() {
		return getMethod();
	}

	public void setName(String name) {
		// nothing to do
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_MODE_ASSERT_NODE;
	}
}
