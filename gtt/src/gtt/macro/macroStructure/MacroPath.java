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


import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroPath;

import java.util.LinkedList;
import java.util.List;

// 用來表描在Macro Structure 的一條tree path
public class MacroPath implements Cloneable {

	public final static String PATH_SEPARATOR = "::"; // 分隔用字串

	AbstractMacroNode m_MacroNode;

	public MacroPath(AbstractMacroNode macro) {
		m_MacroNode = macro;
	}

	final public List<String> list() {
		// Component Path 動態產生即可
		// -zws 2007/07/09
		List<String> m_path = new LinkedList<String>();
		AbstractMacroNode n = m_MacroNode;
		while (n != null && !(n instanceof InvisibleRootNode)) {
			if (n.getName() == null)
				m_path.add(0, "");
			else
				m_path.add(0, n.getName());
			n = n.getParent();
		}
		return m_path;
	}

	final public String toString() {
		List<String> paths = list();
		if (paths.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder(paths.get(0));
		for (int i = 1; i < paths.size(); i++)
			sb.append(PATH_SEPARATOR + paths.get(i));

		return sb.toString();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof MacroPath))
			return false;

		return toString().equals(o.toString());
	}

	public MacroPath clone() {
		return new MacroPath(m_MacroNode);
	}
	
}
