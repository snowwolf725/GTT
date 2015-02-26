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
import gtt.runner.KeyConverter;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

import org.netbeans.jemmy.operators.ComponentOperator;
import org.netbeans.jemmy.operators.JComponentOperator;

public class JComponentTester implements IComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if (comp == null || !(comp instanceof JComponent))
			return false;
		JComponentOperator operator = new JComponentOperator((JComponent) comp);
		operator.setVerification(false);

		return fireLowLevelEvent(info, operator);
	}

	private boolean fireLowLevelEvent(IEvent info, ComponentOperator operator) {
		// JComponent 負責的都是低階事件，
		// 所以使用 Robot 來發事件
		int eid = info.getEventId();
		// 要取得mouse button
		int mouse_btn = InputEvent.BUTTON1_MASK;
		// 要取得 modifier
		int key_modifier = KeyEvent.VK_CLEAR;
		char key_char = ' ';
		int mouse_ckCount = 1;

		if (eid == SwingTesterTag.PRESS_MOUSE) {
			int x = Integer.parseInt(info.getArguments().getValue("X"));
			int y = Integer.parseInt(info.getArguments().getValue("Y"));
			operator.moveMouse(x, y);
			operator.pressMouse();
			return true;
		}

		if (eid == SwingTesterTag.RELEASE_MOUSE) {
			int x = Integer.parseInt(info.getArguments().getValue("X"));
			int y = Integer.parseInt(info.getArguments().getValue("Y"));
			operator.moveMouse(x, y);
			operator.releaseMouse();
			return true;
		}
		if (eid == SwingTesterTag.PRESS_KEY) {
			// 1st arg is 'modifer"
			key_modifier = KeyConverter.StringToKeyCode(info.getArguments()
					.get(0).getValue());
			// 2nd arg is 'char'
			try {
				key_char = (info.getArguments().get(1).getValue()).charAt(0);
			} catch (StringIndexOutOfBoundsException siexp) {
				key_char = ' '; // default empty
			}
			if (KeyConverter.KeyCodeToString(key_modifier) != null)
				operator.pressKey(key_modifier);
			else
				operator.pressKey(KeyConverter.CharToKeyCode(key_char));
			return true;
		}

		if (eid == SwingTesterTag.RELEASE_KEY) {
			// 1st arg is 'modifer"
			key_modifier = KeyConverter.StringToKeyCode(info.getArguments()
					.get(0).getValue());
			// 2nd arg is 'char'
			try {
				key_char = (info.getArguments().get(1).getValue()).charAt(0);
			} catch (StringIndexOutOfBoundsException siexp) {
				key_char = ' '; // default empty
			}
			if (KeyConverter.KeyCodeToString(key_modifier) != null)
				operator.releaseKey(key_modifier);
			else
				operator.releaseKey(KeyConverter.CharToKeyCode(key_char));
			return true;
		}

		if (eid == SwingTesterTag.TYPE_KEY) {
			// 1st arg is 'modifer"
			key_modifier = KeyConverter.StringToKeyCode(info.getArguments()
					.get(0).getValue());
			// 2nd arg is 'char'
			try {
				key_char = (info.getArguments().get(1).getValue()).charAt(0);
			} catch (StringIndexOutOfBoundsException siexp) {
				key_char = ' '; // default empty
			}
			if (KeyConverter.KeyCodeToString(key_modifier) != null) {
				operator.typeKey(key_char, key_modifier);
			} else {
				operator.typeKey(key_char);
			}
			return true;
		}
		if (eid == SwingTesterTag.CLICK_MOUSE
				|| eid == SwingTesterTag.CLICK_NO_BLOCK) {
			try {
				clickMouse(info, operator, mouse_ckCount);
			} catch (AWTException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		if (eid == SwingTesterTag.CLICK_MOUSE_NOXY) {
//			operator.clickMouse();
			try {
				clickMouse(info, operator, 1);
			} catch (AWTException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		if (eid == SwingTesterTag.DRAG_MOUSE) {
			int x = Integer.parseInt(info.getArguments().getValue("X"));
			int y = Integer.parseInt(info.getArguments().getValue("Y"));
			operator.dragMouse(x, y, mouse_btn, key_modifier);
			return true;
		}
		if (eid == SwingTesterTag.ENTER_MOUSE) {
			operator.enterMouse();
			return true;
		}
		if (eid == SwingTesterTag.EXIT_MOUSE) {
			operator.exitMouse();
			return true;
		}
		if (eid == SwingTesterTag.MOVE_MOUSE) {
			int x = Integer.parseInt(info.getArguments().getValue("X"));
			int y = Integer.parseInt(info.getArguments().getValue("Y"));
			operator.moveMouse(x, y);
			return true;
		}
		if (eid == SwingTesterTag.SET_TIME_OUT) {
			long _timeout = 1000L;
			operator.getTimeouts().setTimeout(
					"ComponentOperator.WaitComponentTimeout", _timeout);
			operator.setTimeouts(operator.getTimeouts());
			return true;
		}

		return false;
	}

	private void clickMouse(IEvent info, ComponentOperator cp, 
			int mouse_ckCount) throws AWTException {
		// 使用Jemmy clickMouse 在測試crossowrd sage 有不正常的現象
		// 目前暫時先使用 Robot 低階事件。 - zwshen 2008/10/29
		// int mouse_btn, int mouse_ckCount) {
		// int x = Integer.parseInt(info.getArgumentList().getValue("X"));
		// int y = Integer.parseInt(info.getArgumentList().getValue("Y"));
		// _compOp.clickMouse(x, y, mouse_ckCount, mouse_btn);

		Robot rob = new Robot();
		if (mouse_ckCount < 1)
			mouse_ckCount = 1;

		rob.mouseMove(cp.getLocationOnScreen().x, cp.getLocationOnScreen().y);
		for (int ck = 0; ck < mouse_ckCount; ck++) {
			rob.mousePress(InputEvent.BUTTON1_MASK);
			rob.mouseRelease(InputEvent.BUTTON1_MASK);
		}
	}

}
