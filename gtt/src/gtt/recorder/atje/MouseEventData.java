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

import gtt.tester.swing.SwingTesterTag;

import java.awt.event.MouseEvent;

public class MouseEventData extends AbstractEvent {
	public static final long DEFAULT_MOUSE_SLEEP = 10L;

	private int m_Xpos, m_Ypos;
	private int m_clickCount = 1;

	public MouseEventData() {
		// initialize
		m_eventId = MouseEvent.MOUSE_PRESSED;
		m_eventModifier = MouseEvent.BUTTON1_MASK;
		m_Xpos = 0;
		m_Ypos = 0;
		m_clickCount = 1;
	}

	public MouseEventData(TemporaryComponentData source, int id, int modifier) {
		this(source, id, 0, 0, 1, modifier);
	}

	MouseEventData(TemporaryComponentData source, int event_id, int x,
			int y, int clickCount, int modifier) {
		super(source, event_id);
		m_Xpos = x;
		m_Ypos = y;
		m_clickCount = clickCount;
		m_eventModifier = modifier;
	}

	final public void setX(int x) {
		m_Xpos = x;
	}

	final public int getX() {
		return m_Xpos;
	}

	final public void setY(int y) {
		m_Ypos = y;
	}

	final public int getY() {
		return m_Ypos;
	}

	final public void setClickCount(int count) {
		if (count < 0)
			return; // ³Ì¤Ö0¦¸
		m_clickCount = count;
	}

	final public int getClickCount() {
		return m_clickCount;
	}

	final public String getIDString() {
		return SwingTesterTag.forMouseEvent(m_eventId);
	}

	public String getModifierString() {
		return MouseEventModifierTable.getModifierString(m_eventModifier);
	}

	public String getModifierShortString() {
		return MouseEventModifierTable.getShortModifierString(m_eventModifier);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[" + getIDString() + "]");
		sb.append("[" + getModifierShortString() + "]");
		return sb.toString();
	}

}
