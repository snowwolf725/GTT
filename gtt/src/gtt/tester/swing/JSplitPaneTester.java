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


import javax.swing.JSplitPane;

import org.netbeans.jemmy.operators.JSplitPaneOperator;

public class JSplitPaneTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JSplitPane))
			return false;

		JSplitPaneOperator _compEvent = new JSplitPaneOperator(
				(JSplitPane) comp);

		int eid = info.getEventId();

		if (eid == SwingTesterTag.EXPAND_LEFT) {
			_compEvent.expandLeft();
			return true;
		}
		if (eid == SwingTesterTag.EXPAND_RIGHT) {
			_compEvent.expandRight();
			return true;
		}
		if (eid == SwingTesterTag.MOVE_DIVIDER) {
			int v = Integer.parseInt(info.getArguments().getValue("Value"));
			_compEvent.moveDivider(v);
			return true;
		}
		if (eid == SwingTesterTag.MOVE_TO_MAXIMUM) {
			_compEvent.moveToMaximum();
			return true;
		}
		if (eid == SwingTesterTag.MOVE_TO_MINIMUM) {
			_compEvent.moveToMinimum();
			return true;
		}

		return false;

	}

}
