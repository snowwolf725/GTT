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


import javax.swing.JList;

import org.netbeans.jemmy.operators.JListOperator;
import org.netbeans.jemmy.operators.Operator.DefaultStringComparator;

public class JListTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JList))
			return false;
		JListOperator _compEvent = new JListOperator((JList) comp);
		int eid = info.getEventId();

		if (eid == SwingTesterTag.SELECT_ITEMS_BY_INDEX) {
			// 可以multiple selection
			String[] items = info.getArguments().getValue("Items").split(
					" \t\n:,.");
			int[] indexs = new int[items.length];
			for (int i = 0; i < items.length; i++)
				indexs[i] = Integer.valueOf(items[i]);
			_compEvent.selectItems(indexs);
			return true;
		}
		if (eid == SwingTesterTag.SELECT_ITEMS_BY_TEXT) {
			// 可以multiple selection
			String[] items = info.getArguments().getValue("Items").split(
					" \t\n:,.");
			_compEvent.selectItem(items);
			return true;
		}
		if (eid == SwingTesterTag.CLICK_ON_ITEM_BY_INDEX) {
			int item = Integer
					.parseInt(info.getArguments().getValue("Item"));
			int clickCount = Integer.parseInt(info.getArguments().getValue(
					"ClickCount"));
			// 只按住第一個 index 就行了
			_compEvent.clickOnItem(item, clickCount);
			return true;
		}
		if (eid == SwingTesterTag.CLICK_ON_ITEM_BY_TEXT) {
			// 可以multiple selection
			String item = info.getArguments().getValue("Items");
			int clickCount = Integer.parseInt(info.getArguments().getValue(
					"ClickCount"));
			_compEvent.clickOnItem(item, clickCount);
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_ITEM_BY_INDEX) {
			int item = Integer
					.parseInt(info.getArguments().getValue("Item"));
			// 只按住第一個 index 就行了
			_compEvent.scrollToItem(item);
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_ITEM_BY_TEXT) {
			String item = info.getArguments().getValue("Item");
			// 只按住第一個 index 就行了
			_compEvent.scrollToItem(item, new DefaultStringComparator(true,
					true));
			return true;
		}

		return false;

	}

}
