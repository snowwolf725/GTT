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
/***************************************************************************
 * Name   : CompositeEventData.java
 * Date   : 2003~004 
 * Author : 東宏 
 * Object : 在 GTT 中用來處理KeyString Event之物件.
 * ************************************************************************/
package gtt.recorder.atje;

import gtt.runner.KeyConverter;
import gtt.tester.swing.SwingTesterTag;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class KeyEventCompositeData extends KeyEventData {

	private String _keyString; // K
	private List<AbstractEvent> _keyEvents = new LinkedList<AbstractEvent>(); // K

	public KeyEventCompositeData() {
		_keyString = "";
		m_eventId = SwingTesterTag.TYPE_TEXT;
	}

	public KeyEventCompositeData(TemporaryComponentData source, int eventType,
			String keyString) {
		_keyString = keyString;
		m_eventId = eventType;
		m_ComponentInfo = source;
		for (int i = 0; i < keyString.length(); i++) {
			_keyEvents.add(new KeyEventData(source, KeyEvent.KEY_TYPED,
					KeyConverter.CharToKeyCode(keyString.charAt(i)), keyString
							.charAt(i)));
		}
	}

	public void addEvent(AbstractEvent event) {
		_keyEvents.add(event);
		if (event instanceof KeyEventData
				&& ((KeyEventData) event).getKeyChar() != KeyEvent.CHAR_UNDEFINED
				&& ((KeyEventData) event).getEventID() != KeyEvent.VK_ALL_CANDIDATES)
			_keyString += ((KeyEventData) event).getKeyChar();
	}

	public List<AbstractEvent> getEventDatas() {
		return _keyEvents;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[" + getIDString() + "]");
		sb.append("[" + _keyString + "]");
		return sb.toString();
	}

	public void setKeyString(String keyString) {
		_keyString = keyString;
		_keyEvents.clear();
		for (int i = 0; i < keyString.length(); i++) {
			_keyEvents.add(new KeyEventData(m_ComponentInfo,
					KeyEvent.KEY_TYPED, KeyConverter.CharToKeyCode(keyString
							.charAt(i)), keyString.charAt(i)));
		}
	}

	public String getKeyString() {
		return _keyString;
	}

}
