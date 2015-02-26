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


import javax.swing.JTabbedPane;

import org.netbeans.jemmy.operators.JTabbedPaneOperator;

public class JTabbedPaneTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JTabbedPane))
			return false;

		JTabbedPaneOperator _compEvent = new JTabbedPaneOperator(
				(JTabbedPane) comp);

		int eid = info.getEventId();

		if (eid == SwingTesterTag.SELECT_PAGE) {
			String title = info.getArguments().getValue("Title");
			_compEvent.selectPage(title);
			return true;
		}
		if (eid == SwingTesterTag.SELECT_PAGE_BY_INDEX) {
			int idx = Integer
					.parseInt(info.getArguments().getValue("Index"));
			_compEvent.selectPage(idx);
			return true;
		}

		return false;
	}

}
