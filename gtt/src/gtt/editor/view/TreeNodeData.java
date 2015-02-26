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
package gtt.editor.view;

import gtt.testscript.AbstractNode;

/**
 * 自訂JTree node使用的 user object 用這個來實現測試腳本「simple-detail」的不同呈現方式
 */

public class TreeNodeData {
	AbstractNode m_Node;

	ITestScriptView m_View;

	public enum Color {
		NORMAL, PLAY, ERROR
	}

	public TreeNodeData(AbstractNode data, ITestScriptView view) {
		m_Node = data;
		m_View = view;
	}

	private Color m_Color = Color.NORMAL;

	public void setColor(Color c) {
		m_Color = c;
	}

	public static String ERROR_VIEW_LEVEL = "ERROR_VIEW_LEVEL";

	private String ERROR_PREFIX = "<HTML><font bgcolor=#FF7777>";
	private String PLAY_PREFIX = "<HTML><font bgcolor=#77FF77>";
	private String END_SUFFIX = "</font></HTML>";

	private String colorString(String msg) {
		if (m_Color == Color.ERROR)
			return ERROR_PREFIX + msg + END_SUFFIX;
		if (m_Color == Color.PLAY)
			return PLAY_PREFIX + msg + END_SUFFIX;

		// normal
		return msg;
	}

	public String toString() {
		return colorString(m_Node.toString());
//		switch (m_View.getViewLevel()) {
//		case ITestScriptView.SIMPLE_VIEW_LEVEL:
//			return colorString(m_Node.toSimpleString());
//		case ITestScriptView.NORMAL_VIEW_LEVEL:
//			return colorString(m_Node.toString());
//		case ITestScriptView.DETAIL_VIEW_LEVEL:
//			return colorString(m_Node.toDetailString());
//		}

//		return ERROR_VIEW_LEVEL;
	}

	public AbstractNode getData() {
		return m_Node;
	}

	public ITestScriptView getView() {
		return m_View;
	}
}
