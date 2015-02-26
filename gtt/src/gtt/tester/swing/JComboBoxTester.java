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


import javax.swing.JComboBox;

import org.netbeans.jemmy.operators.JComboBoxOperator;
import org.netbeans.jemmy.operators.Operator;

public class JComboBoxTester extends JComponentTester {

	class ItemStringComparator implements Operator.StringComparator {
		public boolean equals(String arg0, String arg1) {
			return arg0.toLowerCase().equals(arg1.toLowerCase());
		}
	}

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (comp==null || !(comp instanceof JComboBox))
			return false;
		JComboBoxOperator operator = new JComboBoxOperator((JComboBox) comp);
		operator.setVerification(false);

		int eid = info.getEventId();

		if (eid == SwingTesterTag.CLEAR_TEXT) {
			operator.clearText();
			return true;
		}
		if (eid == SwingTesterTag.ENTER_TEXT) {
			operator.enterText(info.getArguments().getValue("Text"));
			return true;
		}
		if (eid == SwingTesterTag.TYPE_TEXT) {
			operator.typeText(info.getArguments().getValue("Text"));
			return true;
		}
		if (eid == SwingTesterTag.SELECT_ITEM_BY_INDEX) {
			String index = info.getArguments().getValue("Index");
			operator.selectItem(Integer.parseInt(index));
			return true;
		}
		if (eid == SwingTesterTag.SELECT_ITEM_BY_STRING) {
			String item = info.getArguments().getValue("Item");
//			operator.selectItem(item, new ItemStringComparator() );
			operator.selectItem(item );
			return true;
		}

		return false;
	}

}
