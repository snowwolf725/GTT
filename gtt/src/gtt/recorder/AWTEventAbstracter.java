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
import gtt.recorder.atje.PercentMouseEventData;
import gtt.recorder.atje.TemporaryComponentData;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * 事件抽象化 from AWTevent to AbstractEvent (low level abstraction)
 */
public class AWTEventAbstracter {
	public static AbstractEvent convert(AWTEvent awtEvent) {
		if (awtEvent == null)
			return null;

		return toAbstractEvent(awtEvent);
	}

	private static int getButtonMask(MouseEvent mevent) {
		if (SwingUtilities.isMiddleMouseButton(mevent))
			return InputEvent.BUTTON2_MASK;
		if (SwingUtilities.isRightMouseButton(mevent))
			return InputEvent.BUTTON3_MASK;

		return InputEvent.BUTTON1_MASK;
	}

	private static AbstractEvent toMouseEvent(MouseEvent event) {
		// 是JComponent，就轉成 Swing 下的元件滑鼠事件
		if (getEventSource(event) instanceof JComponent)
			return createJComponentMouseEvent(event);

		// 最少也是一個滑鼠事件
		return createPercentMouseEventData(event);
	}

	private static PercentMouseEventData createPercentMouseEventData(
			MouseEvent event) {
		Component comp = getEventSource(event);
		TemporaryComponentData data = new TemporaryComponentData(comp);
		return new PercentMouseEventData(data, event.getID(),
				getButtonMask(event), comp, event.getPoint());
	}

	private static AbstractEvent createJComponentMouseEvent(MouseEvent me) {
		try {
			// 轉成高階的滑鼠事件資料
			Component comp = getEventSource(me);
			Class<?> MouseEventDataClass = mapping(comp.getClass());
			for (int i = 0; i < MouseEventDataClass.getConstructors().length; i++) {
				Constructor<?> ct = MouseEventDataClass.getConstructors()[i];
				// 找到適當的建構子 - 參數一致
				if (ct.getParameterTypes().length == 5) {
					TemporaryComponentData source = new TemporaryComponentData(
							comp);
					Object[] args = { source, new Integer(me.getID()),
							new Integer(getButtonMask(me)), comp, me.getPoint() };
					return (AbstractEvent) ct.newInstance(args);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return createPercentMouseEventData(me);

	}

	private static Class<?> mapping(Class<?> from) {
		// 對不同 JComponent class 有不同的 mouse event 處理方式
		if (from == javax.swing.text.JTextComponent.class)
			return JTextEventData.class;
		if (from == javax.swing.JList.class)
			return JListEventData.class;
		if (from == javax.swing.JTable.class)
			return JTableEventData.class;
		if (from == javax.swing.table.JTableHeader.class)
			return JTableHeaderEventData.class;
		if (from == javax.swing.JTree.class)
			return JTreeEventData.class;
		if (from == javax.swing.JTabbedPane.class)
			return JTabbedPaneEventData.class;
		if (from == javax.swing.JSlider.class)
			return PercentMouseEventData.class;
		if (from == javax.swing.JScrollBar.class)
			return PercentMouseEventData.class;
		if (from == javax.swing.JPopupMenu.class)
			return CenterMouseEventData.class;
		// default 採用 Center MouseEvent
		return CenterMouseEventData.class;
	}

	/*
	 * Extract method from convertEventToInputEventData zws 2006/04/02
	 */
	private static AbstractEvent toKeyEvent(KeyEvent event) {
		// 直接就是 KeyEventData
		TemporaryComponentData data = new TemporaryComponentData(
				getEventSource(event));
		return new KeyEventData(data, event.getID(), event.getKeyCode(), event
				.getKeyChar());
	}

	private static AbstractEvent toAbstractEvent(AWTEvent event) {
		if (isAllowedMouseEvent(event))
			return toMouseEvent((MouseEvent) event);

		if (isAllowedKeyEvent(event))
			return toKeyEvent((KeyEvent) event);

		return null;
	}

	/**
	 * 是否為感興趣的 mouse event
	 * 
	 * @param event
	 * @return
	 */
	private static boolean isAllowedMouseEvent(AWTEvent event) {
		if (!(event instanceof MouseEvent))
			return false;

		if (event.getID() == MouseEvent.MOUSE_DRAGGED)
			return false;

		if (event.getID() == MouseEvent.MOUSE_ENTERED)
			return false;

		if (event.getID() == MouseEvent.MOUSE_EXITED)
			return false;

		if (event.getID() == MouseEvent.MOUSE_MOVED)
			return false;

		// 其它都可以
		return true;
	}

	/**
	 * 是否為感興趣的 key event
	 * 
	 * @param event
	 * @return
	 */
	private static boolean isAllowedKeyEvent(AWTEvent event) {
		if (!(event instanceof KeyEvent))
			return false;

		// 其它都可以
		return true;
	}

	private static Component getEventSource(AWTEvent event) {
		if (isAllowedMouseEvent(event)) {
			MouseEvent me = (MouseEvent) event;
			Component c = SwingUtilities.getDeepestComponentAt(me
					.getComponent(), me.getX(), me.getY());
			if (c != null)
				return c; // find it
		}

		if (isAllowedKeyEvent(event)) {
			KeyEvent ke = (KeyEvent) event;
			Component src = (Component) event.getSource();
			Component c = SwingUtilities.getDeepestComponentAt(ke
					.getComponent(), src.getX(), src.getY());
			if (c != null)
				return c; // find it
		}

		// 否則，就回傳 event 本身的 source
		return (Component) event.getSource();
	}

}
