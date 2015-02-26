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
/*
 * Created on 2004/2/17
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gtt.recorder;

import gtt.eventmodel.IComponent;
import gtt.recorder.atje.MouseEventModifierTable;
import gtt.tester.swing.SwingTesterTag;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/*******************************************************************************
 * 名稱 : JComponentEventData 作者 : 東宏 日期 : 2004/2/17 備註 : 對應JComponent ,
 * 儲存JCompoent要發Event所需的 data , 在fireEvent中 實做JComponent所能發的Event
 ******************************************************************************/

public class JComponentEventData {
	protected int _clickCount = 1;
	private int _mouseButton = InputEvent.BUTTON1_MASK;
	private int _x = 0, _y = 0;
	private char _char = ' ';
	private int m_eventId;

	final public int getEventID() {
		return m_eventId;
	}

	final public void setEventID(int id) {
		m_eventId = id;
	}

	public String getIDString() {
		return SwingTesterTag.forEvent(m_eventId);
	}

	protected int m_eventModifier = KeyEvent.VK_CLEAR;

	final public void setModifiers(int modifier) {
		m_eventModifier = modifier;
	}

	final public int getModifier() {
		return m_eventModifier;
	}

	protected IComponent m_ComponentInfo = null;

	final public IComponent getComponent() {
		return m_ComponentInfo;
	}

	public JComponentEventData(IComponent source, int eid) {
		m_ComponentInfo = source;
		m_eventId = eid;
	}

	public void setClickCount(int clickCount) {
		_clickCount = clickCount;
	}

	public void setMouseButton(int mouseButton) {
		_mouseButton = mouseButton;
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}

	public void setX(int x) {
		_x = x;
	}

	public void setY(int y) {
		_y = y;
	}

	public char getChar() {
		return _char;
	}

	public void setChar(char c) {
		_char = c;
	}

	public String getModifierShortString() {
		return MouseEventModifierTable.getShortModifierString(_mouseButton);
	}

}
