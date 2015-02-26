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
package gtt.editor.view.dialog;

import gtt.testscript.AbstractNode;
import gtt.testscript.ReferenceMacroEventNode;

import javax.swing.JOptionPane;


public class ReferenceMacroNodeDialog implements INodeDialog {

	ReferenceMacroEventNode m_Macro;

	public void appear() {
		String name = JOptionPane.showInputDialog("Please input Macro path",
				m_Macro.getRefPath());

		if (name == null || name == "")
			return; // �S����J�W�r

		m_Macro.setRefPath(name);
	}

	public AbstractNode getNode() {
		return m_Macro;
	}

	public void setNode(Object node) {
		m_Macro = (ReferenceMacroEventNode) node;
	}

}
