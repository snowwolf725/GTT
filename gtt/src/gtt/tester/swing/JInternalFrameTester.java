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


import javax.swing.JInternalFrame;

import org.netbeans.jemmy.operators.JInternalFrameOperator;

public class JInternalFrameTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (super.fireEvent(info, comp) == true)
			return true;
		if (!(comp instanceof JInternalFrame))
			return false;

		JInternalFrameOperator _compEvent = new JInternalFrameOperator(
				(JInternalFrame) comp);

		int eid = info.getEventId();

		if (eid == SwingTesterTag.ACTIVATE) {
			_compEvent.activate();
			return true;
		}
		if (eid == SwingTesterTag.CLOSE) {
			_compEvent.close();
			return true;
		}
		if (eid == SwingTesterTag.DEICONIFY) {
			_compEvent.deiconify();
			return true;
		}
		if (eid == SwingTesterTag.DEMAXIMIZE) {
			_compEvent.demaximize();
			return true;
		}
		if (eid == SwingTesterTag.MAXIMIZE) {
			_compEvent.maximize();
			return true;
		}
		if (eid == SwingTesterTag.MOVE) {
			int x = Integer.parseInt(info.getArguments().getValue("X"));
			int y = Integer.parseInt(info.getArguments().getValue("Y"));
			_compEvent.move(x, y);
			return true;
		}
		if (eid == SwingTesterTag.RESIZE) {
			int width = Integer.parseInt(info.getArguments().getValue(
					"Width"));
			int height = Integer.parseInt(info.getArguments().getValue(
					"Height"));
			_compEvent.resize(width, height);
			return true;
		}
		if (eid == SwingTesterTag.SCROLL_TO_FRAME) {
			_compEvent.scrollToFrame();
			return true;
		}

		return false;

	}

}
