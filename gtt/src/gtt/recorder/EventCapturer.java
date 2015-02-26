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
package gtt.recorder;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * 負責mouse/key event的擷取 2007/01/11
 * 
 * 抽像化工作已移 AbstractEventAbstraction 2007/01/25
 * 
 * @author zwshen
 * 
 */
class EventCapturer {
	private boolean _isAltClickOnce = false;
	private boolean _isShiftClickOnce = false;
	private boolean _isCtrlClickOnce = false;

	private List<CaptureData> m_captures = new LinkedList<CaptureData>();

	public List<CaptureData> getDataList() {
		return m_captures;
	}

	private long start_time;

	public EventCapturer() {
		start_time = new Date().getTime(); // 錄影開始執行的時間
	}

	public void add(AWTEvent event) {
		// 只對AWT/Swing的事件有興趣
		// if (!(event.getSource() instanceof Component))
		// return;

		// 滑鼠事件
		if (event instanceof MouseEvent)
			captureMouseEvent((MouseEvent) event);

		// 鍵盤事件
		if (event instanceof KeyEvent)
			captureKeyEvent((KeyEvent) event);
	}

	private void setKeyFlag(KeyEvent event) {
		if (event.getID() == KeyEvent.KEY_PRESSED) {
			if (event.getKeyCode() == KeyEvent.VK_ALT && !_isAltClickOnce)
				_isAltClickOnce = true;
			else if (event.getKeyCode() == KeyEvent.VK_SHIFT
					&& !_isShiftClickOnce)
				_isShiftClickOnce = true;
			else if (event.getKeyCode() == KeyEvent.VK_CONTROL
					&& !_isCtrlClickOnce)
				_isCtrlClickOnce = true;
		}
		if (event.getID() == KeyEvent.KEY_RELEASED) {
			if (event.getKeyCode() == KeyEvent.VK_ALT && _isAltClickOnce)
				_isAltClickOnce = false;
			else if (event.getKeyCode() == KeyEvent.VK_SHIFT
					&& _isShiftClickOnce)
				_isShiftClickOnce = false;
			else if (event.getKeyCode() == KeyEvent.VK_CONTROL
					&& _isCtrlClickOnce)
				_isCtrlClickOnce = false;
		}
	}

	/**
	 *負責KeyEvent的截取
	 */
	private void captureKeyEvent(KeyEvent ke) {
		if (_isAltClickOnce)
			ke.setKeyChar(KeyEvent.CHAR_UNDEFINED);

		switch (ke.getID()) {
		case KeyEvent.KEY_PRESSED:
			captureKeyPressedEvent(ke);
			break;
		case KeyEvent.KEY_RELEASED:
			captureKeyReleasedEvent(ke);
			break;
		}

		// 設定flag
		setKeyFlag(ke);
	}

	/**
	 * 處理 key released
	 * 
	 * @param eventDatas
	 * @param ke
	 */
	private void captureKeyReleasedEvent(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ALT && !_isAltClickOnce)
			return;
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT && !_isShiftClickOnce)
			return;
		if (ke.getKeyCode() == KeyEvent.VK_CONTROL && !_isCtrlClickOnce)
			return;
		m_captures.add(new CaptureData(ke, getRecordTime()));
	}

	private long getRecordTime() {
		return new Date().getTime() - start_time;
	}

	/**
	 * 處理 key pressed
	 * 
	 * @param eventDatas
	 * @param ke
	 */
	private void captureKeyPressedEvent(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ALT && _isAltClickOnce)
			return;

		if (ke.getKeyCode() == KeyEvent.VK_SHIFT && _isShiftClickOnce)
			return;

		if (ke.getKeyCode() == KeyEvent.VK_CONTROL && _isCtrlClickOnce)
			return;

		m_captures.add(new CaptureData(ke, getRecordTime()));
	}

	/**
	 * 2004/04/06 負責MouseEvent的截取
	 */
	private void captureMouseEvent(MouseEvent event) {
		if (SwingUtilities.getRoot((Component) event.getSource()) == null)
			return; 

		if (event.getID() == MouseEvent.MOUSE_PRESSED
				|| event.getID() == MouseEvent.MOUSE_RELEASED) {

//			if (event.getSource() instanceof Window) {
//				displayWindowInformation(event);
//			}

			// recording time 是否有必要要從應用程式的啟動時間開始算？ zws 2007/01/11
			m_captures.add(new CaptureData(event, getRecordTime()));
		}
	}

//	private void displayWindowInformation(MouseEvent event) {
//		if (false) {
//			System.out.println("====================================");
//			System.out.println("SwingRecorder.captureMouseEvent() ");
//			System.out.println("event:" + event);
//			System.out.println("source:" + event.getSource());
//			System.out.println("window owner:"
//					+ ((Window) event.getSource()).getOwner());
//			System.out.println("====================================");
//		} else {
//			System.out.println("event: " + event);
//		}
//	}
}