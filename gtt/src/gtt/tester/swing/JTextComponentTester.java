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

import java.awt.Rectangle;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.netbeans.jemmy.operators.JTextComponentOperator;

public class JTextComponentTester extends JComponentTester {

	@Override
	public boolean fireEvent(IEvent info, Object comp) {
		if(super.fireEvent(info, comp)==true)
			return true;

		if (!(comp instanceof JTextComponent))
			return false;

		JTextComponentOperator _compEvent = new JTextComponentOperator(
				(JTextComponent) comp);

		int _id = info.getEventId();

		if (_id == SwingTesterTag.CHANGE_CARET_POSITION) {
			int _pos = Integer.parseInt(info.getArguments().getValue(
					"Position"));
			Rectangle start;
			try {
				start = ((JTextComponent) comp).modelToView(_pos);
				_compEvent.moveMouse(start.x, start.y);
				_compEvent.pressMouse();
				_compEvent.releaseMouse();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		}
		if (_id == SwingTesterTag.CLEAR_TEXT) {
			_compEvent.clearText();
			return true;
		}
		if (_id == SwingTesterTag.ENTER_TEXT) {
			String text = info.getArguments().getValue("Text");
			_compEvent.enterText(text);
			return true;
		}
		if (_id == SwingTesterTag.SCROLL_TO_POSITION) {
			int _pos = Integer.parseInt(info.getArguments().getValue(
					"Position"));
			_compEvent.scrollToPosition(_pos);
			return true;
		}
		if (_id == SwingTesterTag.SELECT_TEXT) {
			int _pos = Integer.parseInt(info.getArguments().getValue(
					"Position"));
			String text = info.getArguments().getValue("Text");
			_compEvent.selectText(text, _pos);
			return true;
		}
		if (_id == SwingTesterTag.SELECT_TEXT_POS) {
			int _pos = Integer.parseInt(info.getArguments().getValue(
					"Position"));
			int _endPos = Integer.parseInt(info.getArguments().getValue(
					"EndPosition"));
			_compEvent.selectText(_pos, _endPos);
			return true;
		}
		if (_id == SwingTesterTag.TYPE_TEXT) {
			int _pos = Integer.parseInt(info.getArguments().getValue(
					"Position"));
			String text = info.getArguments().getValue("Text");
			_compEvent.typeText(text, _pos);
			return true;
		}
		if (_id == SwingTesterTag.TYPE_TEXT_NO_POS) {
			String text = info.getArguments().getValue("Text");
			_compEvent.typeText(text);
			return true;
		}

		return false;

	}

}
