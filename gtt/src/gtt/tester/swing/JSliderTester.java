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


import javax.swing.JSlider;

import org.netbeans.jemmy.operators.JSliderOperator;

public class JSliderTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JSlider))
			return false;

		JSliderOperator _compEvent = new JSliderOperator((JSlider) comp);

		int eid = info.getEventId();
		if (eid == SwingTesterTag.SCROLL_TO_MAXIMUM) {
			_compEvent.scrollToMaximum();
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_MINIMUM) {
			_compEvent.scrollToMinimum();
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_PERCENT) {
			// Note: jemmy 沒有這個事件 zws 2007/05/25
			// Double p =
			// Double.parseDouble(info.getArgumentList().getValue("Value"));
			// _compEvent.scrollToValue(p);
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_VALUE) {
			int p = Integer.parseInt(info.getArguments().getValue("Value"));
			_compEvent.scrollToValue(p);
			return true;
		}

		return false;
	}

}
