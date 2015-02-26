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
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.SwingUtilities;

public class SwingRecorder extends EventQueue implements Recorder {

	private boolean _isRecording = false;

	// System Event Queue
	private EventQueue _sysQ = Toolkit.getDefaultToolkit()
			.getSystemEventQueue();

	private EventQueue _tempQ = null;

	public SwingRecorder Clone() {
		SwingRecorder result = new SwingRecorder();
		result._sysQ = this._sysQ;
		result._tempQ = _tempQ;
		result.m_Capturer = m_Capturer;

		return result;
	}

	public SwingRecorder() {
//		System.out.println("SwingInterceptor Interceptor initialized...ok");
	}

	/**
	 * 擷取事件的核心函式
	 */
	protected void dispatchEvent(AWTEvent event) {
		// 在Dispatch Event之前對 AWT Event做處理
		storeDispatchEvent(event);

		// 處理完畢，將AWT Event真正轉發出去，讓AUT可以接受到事件
		super.dispatchEvent(event);
	}

	// 負責擷取並儲存事件的物件
	// 包括過瀘沒有要錄取的事件
	EventCapturer m_Capturer = new EventCapturer();

	/*
	 * 2006/07/22 zws
	 */
	private void storeDispatchEvent(AWTEvent event) {
		// 處於錄影模式
		if (!_isRecording)
			return;
		if (!checkForStore(event))
			return;

		// 存下捉到的事件
		m_Capturer.add(event);
	}

	private boolean checkForStore(AWTEvent event) {
		if (event.getSource() instanceof Component) {
			Component root = SwingUtilities.getRoot((Component) event
					.getSource());

			if (root == null)
				return true;

			// 不錄到本身應用程式的事件 (先暫且擋一下)
			if (root.toString().indexOf("view.MainEditor") != -1)
				return false;
		}

		return true;
	}

	public List<CaptureData> getCaptureData() {
		// 沒有經過抽象化
		return m_Capturer.getDataList();
	}

	private boolean checkSysEventQueue() {
		return (_sysQ != null && _sysQ instanceof SwingRecorder);
	}

	public void active() {
		_isRecording = true;

		_sysQ = Toolkit.getDefaultToolkit().getSystemEventQueue();

		if (checkSysEventQueue()) {
			// 判斷是否已經有放入 SwingRecorder
			_tempQ = ((SwingRecorder) _sysQ).Clone();
		}

		// 放入 SwingRecorder
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(this);
	}

	public void inactive() {
		_isRecording = false;

		if (_tempQ == null)
			Toolkit.getDefaultToolkit().getSystemEventQueue().push(
					new EventQueue());
		else
			Toolkit.getDefaultToolkit().getSystemEventQueue().push(_tempQ);
	}
}
