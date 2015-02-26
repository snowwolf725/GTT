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

import gtt.recorder.atje.AbstractEvent;
import gtt.recorder.atje.CenterMouseEventData;
import gtt.recorder.atje.JListEventData;
import gtt.recorder.atje.JTabbedPaneEventData;
import gtt.recorder.atje.JTableEventData;
import gtt.recorder.atje.JTableHeaderEventData;
import gtt.recorder.atje.JTextEventData;
import gtt.recorder.atje.JTreeEventData;
import gtt.recorder.atje.KeyEventData;
import gtt.recorder.atje.MouseEventData;
import gtt.testscript.AbstractNode;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * �B�z�ƥ󪺩�H�� created at 2007/01/12
 *
 * 3. �ƥ�h���ഫ (Event Level Translation) �s�򪺧C��AE -> ���� AE 4. �ƥ����O�ഫ (Event
 * Convertion) AE -> JE
 *
 * @author zwshen
 *
 */

public class EventAbstracter implements IEventAbstracter {

	// Note: �����Ҽ{��H�ƪ�����- zwshen 2007/11/20
	public final List<AbstractNode> abstraction(List<CaptureData> data) {
		// Step 1. �C����H��
		List<AbstractEvent> ae_list = lowLevel(data);
		// Step 2. �����⹳��: �N�s��ƥ�⹳��
		List<AbstractEvent> medium_ae_list = mediumLevel(ae_list);
		// Step 3. �����⹳��: AbstractEvent -> JE -> EventNode
		return highLevel(medium_ae_list);
	}

	/**
	 * LowLevel: AWTEvent list -> Abstract Event list
	 */
	public List<AbstractEvent> lowLevel(List<CaptureData> datalist) {
		List<AbstractEvent> result = new LinkedList<AbstractEvent>();
		Iterator<CaptureData> ite = datalist.iterator();
		while (ite.hasNext()) {
			AbstractEvent ae = AWTEventAbstracter
					.convert(ite.next().awtEvent());
			if (ae == null)
				continue;
			result.add(ae);
		}
		return result;
	}

	/**
	 * ���ſ��v����H�� 3. �ƥ�h���ഫ (Event Level Translation) �s�򪺧C��AE -> ���� AE
	 */
	public List<AbstractEvent> mediumLevel(List<AbstractEvent> events) {
		List<AbstractEvent> result = new LinkedList<AbstractEvent>();
		for (int i = 0; i < events.size()-1; i++) {
			AbstractEvent o1 = events.get(i);
			AbstractEvent o2 = events.get(i + 1);
			// �s���ӷƹ��ƥ�
			if (successiveTwoMouseEvent(o1, o2)) {
				if (abstractTwoMouseEvent((MouseEventData) o1,
						(MouseEventData) o2) == true) {
					// abstract ���\�A������i�Өƥ�A�]�� i �� i+1 �X���@�Ӹ��������ƥ�
					events.remove(i);
					result.add(o2);
				}
				continue;
			}
			// �s������L�ƥ�
			if (successiveTwoKeyEvent(o1, o2)) {
				if (abstractTwoKeyEvent((KeyEventData) o1, (KeyEventData) o2) == true) {
					// abstract ���\�A������i�Өƥ�A �]�� i �� i+1 �X���@�Ӹ��������ƥ�
					events.remove(i);
					result.add(o2);
				}
				continue;
			}
			// �N�s��key event ��H���@��string
			// if (result.getComponentData().getComponent() instanceof
			// JTextComponent) {
			// abstractKeyString(result);
			// } else
			// {
			// abstractMouseData(result, 500);
			// }
		}

		return result;
	}

	// ��Ӫ��󳣬��ƹ��ƥ�
	private boolean successiveTwoMouseEvent(Object o1, Object o2) {
		return o1 instanceof MouseEventData && o2 instanceof MouseEventData;
	}

	// ��Ӫ��󳣬���L�ƥ�
	private boolean successiveTwoKeyEvent(Object o1, Object o2) {
		return o1 instanceof KeyEventData && o2 instanceof KeyEventData;
	}

	// �N ��L�ƥ� pressed + released -> typed
	private boolean abstractTwoKeyEvent(KeyEventData k1, KeyEventData k2) {
		if (k1.getEventID() != KeyEvent.KEY_PRESSED)
			return false;

		if (k2.getEventID() != KeyEvent.KEY_RELEASED)
			return false;

		if (k1.getKeyCode() != k2.getKeyCode())
			return false;

		k2.setEventID(KeyEvent.KEY_TYPED);
		return true;
	}

	// �s���� mouse event
	private boolean abstractTwoMouseEvent(MouseEventData me1, MouseEventData me2) {
		if (me1.getEventID() != MouseEvent.MOUSE_PRESSED)
			return false;
		if (me2.getEventID() != MouseEvent.MOUSE_RELEASED)
			return false;
		if (!abstractMouseClick(me1, me2))
			return false;

		me2.setEventID(MouseEvent.MOUSE_CLICKED);
		me2.setClickCount(1);
		return true;
	}

	// ��H�Ʒƹ��ƥ� press + release
	private boolean abstractMouseClick(MouseEventData press,
			MouseEventData release) {

		if (press instanceof JTableEventData
				&& release instanceof JTableEventData)
			return (((JTableEventData) press).getRow() == ((JTableEventData) release)
					.getRow())
					&& (((JTableEventData) press).getColumn() == ((JTableEventData) release)
							.getColumn());

		if (press instanceof JTableHeaderEventData
				&& release instanceof JTableHeaderEventData)
			return (((JTableHeaderEventData) press).getColumn()) == ((JTableHeaderEventData) release)
					.getColumn();

		if (press instanceof JTreeEventData
				&& release instanceof JTreeEventData)
			return (((JTreeEventData) press).getRow()) == ((JTreeEventData) release)
					.getRow();

		if (press instanceof JListEventData
				&& release instanceof JListEventData)
			return (((JListEventData) press).getIndex()) == ((JListEventData) release)
					.getIndex();

		if (press instanceof JTabbedPaneEventData
				&& release instanceof JTabbedPaneEventData)
			return (((JTabbedPaneEventData) press).getIndex()) == ((JTabbedPaneEventData) release)
					.getIndex();

		if (press instanceof JTextEventData
				&& release instanceof JTextEventData)
			return (((JTextEventData) press).getPosition()) == ((JTextEventData) release)
					.getPosition();

		if (press instanceof CenterMouseEventData
				&& release instanceof CenterMouseEventData)
			return (((CenterMouseEventData) press).getX()) == ((CenterMouseEventData) release)
					.getX()
					&& (((CenterMouseEventData) press).getY()) == ((CenterMouseEventData) release)
							.getY();
		// �̫�Ax,y�y�Эn�b�P�@���I�W
		if (press.getX() != release.getX())
			return false;

		if (press.getY() != release.getY())
			return false;

		return true;
	}

	/**
	 * ������H�ơG AbstractEvent -> (JComponentEventData) -> EventNode
	 */
	public List<AbstractNode> highLevel(List<AbstractEvent> data) {
		// �N�⹳�L �᪺���G�A�ন EventNode ���Φ��A�x�s�b FolderNode �U
		List<AbstractNode> result = new LinkedList<AbstractNode>();
		try {
			Iterator<AbstractEvent> ite = data.iterator();
			while (ite.hasNext()) {
				// �NAbstractEvent�নEventNode
				AbstractNode n = SwingEventFactory.convert(ite.next());
				if (n == null)
					continue;
				result.add(n);
			}
		} catch (Exception nep) {
			System.out.println("[toJemmy (highLevel)] " + nep.toString());
		}
		// return new CompositeDataTreeNode(result);
		return result;
	}

}
