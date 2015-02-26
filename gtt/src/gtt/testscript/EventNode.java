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
package gtt.testscript;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.testscript.visitor.ITestScriptVisitor;

public class EventNode extends AbstractNode {

	// @Override
	public void accept(ITestScriptVisitor v) {
		v.visit(this);
	}

	private IComponent m_Component = null;

	private IEvent m_Event = null;

	// package-private
	public EventNode(IComponent comp, IEvent event) {
		/*
		 * 使用 clone ，避免同時reference 到同一個物件
		 */
		m_Component = comp.clone();
		m_Event = event.clone();
	}

	/**
	 * clone 給複製貼上使用
	 */
	public EventNode clone() {
		return new EventNode(m_Component.clone(), m_Event.clone());
	}

	public String toString() {
		return m_Component.getName() + "." + m_Event.toString();
	}

	public IComponent getComponent() {
		return m_Component;
	}

	public IEvent getEvent() {
		return m_Event;
	}

	public void setComponent(IComponent c) {
		m_Component = c;
	}

	public void setEvent(IEvent e) {
		m_Event = e;
	}

	public String toDetailString() {
		String type = m_Component.getType();
		type = type.substring(type.lastIndexOf(".") + 1);

		StringBuilder result = new StringBuilder("(" + type + ")");
		result.append(m_Component.getName());
		result.append("." + m_Event.toString());
		return result.toString();
	}

	// / @display: Event_Name(Component_Name)
	public String toSimpleString() {
		return m_Event.getName() + "(" + m_Component.getName() + ")";
	}
}
