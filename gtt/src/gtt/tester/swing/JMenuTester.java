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


import javax.swing.JMenu;

import org.netbeans.jemmy.operators.JMenuItemOperator;
import org.netbeans.jemmy.operators.JMenuOperator;

public class JMenuTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JMenu))
			return false;
		JMenuOperator operator = new JMenuOperator((JMenu) comp);
		operator.setVerification(false);
		
		int eid = info.getEventId();

		if (eid == SwingTesterTag.PUSH) {
			operator.push();
			return true;
		}
		if (eid == SwingTesterTag.PUSH_NO_BLOCK) {
			operator.pushNoBlock();
			return true;
		}
		if (eid == SwingTesterTag.PUSH_MENU) {
			operator.pushMenu(info.getArguments().getValue("Path"), "|");
			return true;
		}
		if (eid == SwingTesterTag.PUSH_MENU_NO_BLOCK) {
			operator.pushMenuNoBlock(info.getArguments().getValue("Path"),
					"|");
			return true;
		}
		if (eid == SwingTesterTag.SHOW_MENU_ITEM) {
			// 讓 JMenu 有多次機會show out
			int ct = 0;
			while (ct++ < 3) {
				JMenuItemOperator op = operator.showMenuItem(info
						.getArguments().getValue("Item"));
				if ( op.isShowing() == true)
					return true;
			}
			return false;
		}

		return false;
	}

}
