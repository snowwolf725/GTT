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
	 * �^���ƥ󪺮֤ߨ禡
	 */
	protected void dispatchEvent(AWTEvent event) {
		// �bDispatch Event���e�� AWT Event���B�z
		storeDispatchEvent(event);

		// �B�z�����A�NAWT Event�u����o�X�h�A��AUT�i�H������ƥ�
		super.dispatchEvent(event);
	}

	// �t�d�^�����x�s�ƥ󪺪���
	// �]�A�L�y�S���n�������ƥ�
	EventCapturer m_Capturer = new EventCapturer();

	/*
	 * 2006/07/22 zws
	 */
	private void storeDispatchEvent(AWTEvent event) {
		// �B����v�Ҧ�
		if (!_isRecording)
			return;
		if (!checkForStore(event))
			return;

		// �s�U���쪺�ƥ�
		m_Capturer.add(event);
	}

	private boolean checkForStore(AWTEvent event) {
		if (event.getSource() instanceof Component) {
			Component root = SwingUtilities.getRoot((Component) event
					.getSource());

			if (root == null)
				return true;

			// �����쥻�����ε{�����ƥ� (���ȥB�פ@�U)
			if (root.toString().indexOf("view.MainEditor") != -1)
				return false;
		}

		return true;
	}

	public List<CaptureData> getCaptureData() {
		// �S���g�L��H��
		return m_Capturer.getDataList();
	}

	private boolean checkSysEventQueue() {
		return (_sysQ != null && _sysQ instanceof SwingRecorder);
	}

	public void active() {
		_isRecording = true;

		_sysQ = Toolkit.getDefaultToolkit().getSystemEventQueue();

		if (checkSysEventQueue()) {
			// �P�_�O�_�w�g����J SwingRecorder
			_tempQ = ((SwingRecorder) _sysQ).Clone();
		}

		// ��J SwingRecorder
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
