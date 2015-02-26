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
import gtt.runner.PlaybackNameBasedChooser;

import java.awt.Component;

import javax.swing.JScrollPane;

import org.netbeans.jemmy.operators.JScrollPaneOperator;

public class JScrollPaneTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JScrollPane))
			return false;

		JScrollPaneOperator _compEvent = new JScrollPaneOperator(
				(JScrollPane) comp);

		int eid = info.getEventId();

		if (eid == SwingTesterTag.SCROLL_TO_BOTTOM) {
			_compEvent.scrollToBottom();
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_COMPONENT) {
			String name = info.getArguments().getValue("ComponentName");
			Component theComp = _compEvent
					.findSubComponent(new PlaybackNameBasedChooser(name));
			_compEvent.scrollToComponent(theComp);
			return true;
		} else if (eid == SwingTesterTag.SCROLL_TO_HORIZONTAL_VALUE) {
			double x = Double.parseDouble(info.getArguments().getValue("X"));
			_compEvent.scrollToHorizontalValue(x);
			return true;
		} else if (eid == SwingTesterTag.SCROLL_TO_LEFT) {
			_compEvent.scrollToLeft();
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_RIGHT) {
			_compEvent.scrollToRight();
			return true;

		}
		if (eid == SwingTesterTag.SCROLL_TO_TOP) {
			_compEvent.scrollToTop();
			return true;

		}
		if (eid == SwingTesterTag.SCROLL_TO_VALUES) {
			double x = Double.parseDouble(info.getArguments().getValue("X"));
			double y = Double.parseDouble(info.getArguments().getValue("Y"));
			_compEvent.scrollToValues(x, y);
			return true;

		}
		if (eid == SwingTesterTag.SCROLL_TO_VERTICAL_VALUE) {
			double y = Double.parseDouble(info.getArguments().getValue("Y"));
			_compEvent.scrollToVerticalValue(y);
			return true;
		}

		return false;

	}

}
