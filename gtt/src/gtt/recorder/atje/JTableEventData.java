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
package gtt.recorder.atje;


import java.awt.Component;
import java.awt.Point;

import javax.swing.JTable;

public class JTableEventData extends MouseEventData {

	private int _row, _column;

	public JTableEventData() {
		super();
		_row = 0;
		_column = 0;
	}

	public JTableEventData(TemporaryComponentData data, int id, int modifier,
			Component comp, Point point) {
		super(data, id, modifier);
		JTable table = (JTable) comp;
		
		_row = table.rowAtPoint(point);
		_column = table.columnAtPoint(point);
	}

	public int getRow() {
		return _row;
	}

	public int getColumn() {
		return _column;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("[row:" + _row + ",col:" + _column + "]");
		return sb.toString();
	}
}