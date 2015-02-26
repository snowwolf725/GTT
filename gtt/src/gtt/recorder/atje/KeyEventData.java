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

//import testgen.*;
//import java.awt.Point;
import gtt.runner.KeyConverter;
import gtt.tester.swing.SwingTesterTag;

import java.awt.event.KeyEvent;

public class KeyEventData extends AbstractEvent {
	protected int _keyCode;
	protected char _keyChar;

	public KeyEventData() {
	}

	public KeyEventData(TemporaryComponentData source, int id, int keyCode,
			char keyChar) {
		super(source, id);

		if (id == KeyEvent.KEY_TYPED && keyChar == KeyEvent.CHAR_UNDEFINED) {
			throw new IllegalArgumentException("invalid keyChar");
		}
		if (id == KeyEvent.KEY_TYPED && keyCode == KeyEvent.VK_UNDEFINED) {
			throw new IllegalArgumentException("invalid keyCode");
		}

		_keyCode = keyCode;
		_keyChar = keyChar;
	}

	final public int getKeyCode() {
		return _keyCode;
	}

	final public String getKeyText() {
		return KeyEvent.getKeyText(_keyCode);
	}

	final public char getKeyChar() {
		return _keyChar;
	}

	final public String getIDString() {
		return SwingTesterTag.forKeyEvent(m_eventId);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[" + getIDString() + "]");
		String keyName = KeyConverter.KeyCodeToString(_keyCode);
		if (keyName != null)
			sb.append("[" + keyName + "]");
		else if (_keyChar != 65535)
			sb.append("[" + _keyChar + "]");
		return sb.toString();
	}
}

