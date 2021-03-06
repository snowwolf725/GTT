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
package gtt.tester.swing;

import gtt.eventmodel.IEvent;


import javax.swing.JTextArea;

import org.netbeans.jemmy.operators.JTextAreaOperator;

public class JTextAreaTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JTextArea))
			return false;

		JTextAreaOperator _compEvent = new JTextAreaOperator((JTextArea) comp);
		int eid = info.getEventId();

		if (eid == SwingTesterTag.CHANGE_CARET_POS_BY_ROW_COL) {
			int _startRow = Integer.parseInt(info.getArguments().getValue(
					"StartRow"));
			int _startCol = Integer.parseInt(info.getArguments().getValue(
					"StartColumn"));
			_compEvent.changeCaretPosition(_startRow, _startCol);
			return true;
		}

		if (eid == SwingTesterTag.CHANGE_CARET_ROW) {
			int _startRow = Integer.parseInt(info.getArguments().getValue(
					"StartRow"));
			_compEvent.changeCaretRow(_startRow);
			return true;
		}

		if (eid == SwingTesterTag.SELECT_TEXT_BY_ROW_COL) {
			int _startRow = Integer.parseInt(info.getArguments().getValue(
					"StartRow"));
			int _startCol = Integer.parseInt(info.getArguments().getValue(
					"StartColumn"));
			int _endRow = Integer.parseInt(info.getArguments().getValue(
					"EndRow"));
			int _endCol = Integer.parseInt(info.getArguments().getValue(
					"EndColumn"));
			_compEvent.selectText(_startRow, _startCol, _endRow, _endCol);
			return true;
		}
		if (eid == SwingTesterTag.TYPE_TEXT_BY_ROW_COL) {
			int _startRow = Integer.parseInt(info.getArguments().getValue(
					"StartRow"));
			int _startCol = Integer.parseInt(info.getArguments().getValue(
					"StartColumn"));
			String _text = info.getArguments().getValue("Text");
			_compEvent.typeText(_text, _startRow, _startCol);
			return true;
		}

		return false;

	}

}
