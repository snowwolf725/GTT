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

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingEvent;
import gtt.recorder.atje.AbstractEvent;
import gtt.recorder.atje.CenterMouseEventData;
import gtt.recorder.atje.KeyEventCompositeData;
import gtt.recorder.atje.KeyEventData;
import gtt.recorder.atje.MouseEventData;
import gtt.recorder.atje.TemporaryComponentData;
import gtt.tester.swing.SwingTesterTag;
import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Translate AbstractEvent into EventNode
 * 
 * @author zwshen
 */

public class SwingEventFactory {
	public static AbstractNode forJComponentMouseEvent(MouseEventData ae,
			int default_event) {
		int eid = default_event;
		if (ae.getEventID() == MouseEvent.MOUSE_PRESSED)
			eid = SwingTesterTag.PRESS_MOUSE;
		if (ae.getEventID() == MouseEvent.MOUSE_RELEASED)
			eid = SwingTesterTag.RELEASE_MOUSE;
		JComponentEventData ce = new JComponentEventData(toIComponent(ae
				.getComponentData()), eid);
		ce.setModifiers(ae.getModifier());
		ce.setX(ae.getX());
		ce.setY(ae.getY());
		ce.setClickCount(ae.getClickCount());
		return toEventNode(ce);
	}

	public static AbstractNode forJComponentKeyEvent(AbstractEvent e) {
		// 轉換成鍵盤事件
		if (e instanceof KeyEventCompositeData) {
			IComponent ic = SwingEventFactory
					.toIComponent(e.getComponentData());
			return toEventNode(new JTextComponentEventData(ic,
					SwingTesterTag.TYPE_TEXT_NO_POS, 0,
					((KeyEventCompositeData) e).getKeyString()));
		}
		if (e instanceof KeyEventData) {
			IComponent ic = SwingEventFactory
					.toIComponent(e.getComponentData());
			JComponentEventData ce = new JComponentEventData(ic,
					SwingTesterTag.TYPE_KEY);
			if (e.getEventID() == KeyEvent.KEY_PRESSED)
				ce.setEventID(SwingTesterTag.PRESS_KEY);
			if (e.getEventID() == KeyEvent.KEY_RELEASED)
				ce.setEventID(SwingTesterTag.RELEASE_KEY);
			ce.setChar(((KeyEventData) e).getKeyChar());
			ce.setModifiers(((KeyEventData) e).getKeyCode());
			return toEventNode(ce);
		}

		return null;
	}

	public static AbstractNode convert(AbstractEvent ae) {
		// if (ae instanceof JListMouseEventData)
		// return forJListEvent((MouseEventData) ae);
		// if (ae instanceof JTableMouseEventData)
		// return forJTableEvent((MouseEventData) ae);
		// if (ae instanceof JTabbedPaneMouseEventData)
		// return forJTabbedPaneEvent((MouseEventData) ae);
		// if (ae instanceof JTreeMouseEventData)
		// return forJTreeEvent((MouseEventData) ae);
		if (ae instanceof KeyEventData)
			return forJComponentKeyEvent(ae);
		if (ae instanceof CenterMouseEventData)
			return forJComponentMouseEvent((MouseEventData) ae,
					SwingTesterTag.PUSH);
		// if (ae instanceof DefaultMouseEventData)
		return forJComponentMouseEvent((MouseEventData) ae,
				SwingTesterTag.CLICK_MOUSE);
		// return null; // non-found
	}

	// //////////////////////////////////////////////////////////////////////
	static IEventModel m_EventModel = EventModelFactory.getDefault();

	private static IEvent toIEvent(IComponent ic, JComponentEventData ae) {
		IEvent ie = m_EventModel.getEvent(ic, ae.getEventID());
		if (ie != null)
			return ie;
		return SwingEvent.create(ae.getEventID(), ae.getIDString());
	}

	// 將ComponentData (old) 轉成 IComponent (new)
	private static IComponent toIComponent(TemporaryComponentData cd) {
		IComponent ic = m_EventModel.createDefaultComponent();
		ic.setIndex(cd.getIndex());
		ic.setIndexOfSameName(cd.getIndexOfSameName());
		ic.setName(cd.getName());
		ic.setText(cd.getText());
		ic.setTitle(cd.getTitle());
		if (cd.getType() != null)
			ic
					.setType(cd.getType().toString().replaceFirst("class", "")
							.trim());

		if (cd.getWinType() != null)
			ic.setWinType(cd.getWinType().toString().replaceFirst("class", "")
					.trim());

		return ic;
	}

	private static AbstractNode toEventNode(JComponentEventData je) {
		NodeFactory m_NodeFactory = new NodeFactory();

		IComponent c = je.getComponent();
		IEvent e = SwingEventFactory.toIEvent(c, je);

		// arguments
		try {
			if (je.getChar() != ' ')
				e.getArguments().find("Char").setValue("" + je.getChar());
		} catch (NullPointerException npe) {
			// 沒有支援的argument 就不用設定值了
		}

		try {
			if (je.getModifier() != 0)
				e.getArguments().find("Modifier").setValue(
						"" + je.getModifier());
		} catch (NullPointerException npe) {
			// 沒有支援的argument 就不用設定值了
		}

		return m_NodeFactory.createEventNode(c, e);
	}

}
