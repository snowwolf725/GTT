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
package gtt.eventmodel.web;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IDescriptorReader;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingEvent;

import java.io.InputStream;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//元件比較是依Type
class ComponentComparator implements Comparator<IComponent> {
	public int compare(IComponent obj1, IComponent obj2) {
		return obj1.getType().compareTo(obj2.getType());
	}
}
//事件比較是依Name，為了之後要排序用
class EventComparator implements Comparator<IEvent> {
	public int compare(IEvent obj1, IEvent obj2) {
		return obj1.getName().compareTo(obj2.getName());
	}
}
//class WebModel所做的是將xml檔讀入m_Components、m_Events、m_Scopes
public class WebModel implements IEventModel {

	private List<IComponent> m_Components = new LinkedList<IComponent>();

	public List<IComponent> getComponents() {
		// by clone
		return new LinkedList<IComponent>(m_Components);
	}

	private static final String DEFAULT_SCOPE = "WebComponent";

	private AbstractMap<String, LinkedList<IEvent>> m_Events = new HashMap<String, LinkedList<IEvent>>();

	private AbstractMap<String, String> m_Scopes = new HashMap<String, String>(); 

	//建構元，建立一個HashMap
	public WebModel() {
		m_Events.put(DEFAULT_SCOPE, new LinkedList<IEvent>());
	}

	//由filename讀入xml檔
	private IDescriptorReader createReader(String filename) {
		return new WebDescriptorReader(filename);
	}

	public boolean initialize(String desc_file) {
		IDescriptorReader reader = createReader(desc_file);
		reader.setModel(this);
		return reader.read();
	}

	public void addComponent(IComponent c) {
		if (c == null)
			return;
		if (getComponent(c.getType()) == null) {
			m_Components.add(c);
			// sort it
			Collections.sort(m_Components, new ComponentComparator());
		}
	}

	public void addEvent(IEvent e, String scope) {
		if (e == null)
			return;

		addEventScope(scope);
		List<IEvent> events = (List<IEvent>) (m_Events.get(scope));

		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent event = ite.next();
			if (event.equals(e))
				return;
		}
		events.add(e);

		// sort it
		Collections.sort(events, new EventComparator());
	}

	public IComponent getComponent(String type) {
		if (type == null)
			return null;

		Iterator<IComponent> ite = m_Components.iterator();
		while (ite.hasNext()) {
			IComponent comp = ite.next();
			if (comp.getType().equals(type))
				return comp.clone();
		}

		return null; // No such 'type' component
	}

	public int getComponentSize() {
		return m_Components.size();
	}

	public int getEventSize() {
		try {
			return ((List<IEvent>) m_Events.get(DEFAULT_SCOPE)).size();
		} catch (NullPointerException npe) {
			return 0;
		}
	}

	private static final List<IEvent> EMPTY_EVENTS = new LinkedList<IEvent>();

	public List<IEvent> getEvents(IComponent comp) {
		// by clone
		return new LinkedList<IEvent>( getEvents(getScope(comp)));
	}

	public IEvent getEvent(IComponent comp, int eid) {
		if (comp == null)
			return null;
		List<IEvent> events = getEvents(getScope(comp));
		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent ie = ite.next();
			if (ie.getEventId() == eid)
				return ie.clone();
		}
		return null; // non-found
	}

	public IEvent getEvent(IComponent comp, String ename) {
		if (comp == null)
			return null;
//		System.out.println(comp.getType()+","+ename+"==>");
		List<IEvent> events = getEvents(getScope(comp));
		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent ie = ite.next();
//			System.out.println(ie.getName());
			if (ie.getName().equals(ename))
				return ie.clone();
		}
		return null; // non-found
	}

	public List<IEvent> getEvents(String scope) {
		if (m_Events.containsKey(scope))
			return m_Events.get(scope);

		return EMPTY_EVENTS;
	}

	public void addEvent(IEvent c) {
		addEvent(c, DEFAULT_SCOPE);
	}

	public void addScope(IComponent comp, String scope) {
		scope = addEventScope(scope);
		m_Scopes.put(comp.getType(), scope);
	}

	private String addEventScope(String scope) {
		if (scope == "")
			scope = DEFAULT_SCOPE;

		if (!m_Events.containsKey(scope)) {
			m_Events.put(scope, new LinkedList<IEvent>());
		}

		return scope;
	}

	public int getScopeSize() {
		return m_Scopes.size();
	}

	public String getScope(IComponent comp) {
		try {
			return (String) m_Scopes.get(comp.getType());
		} catch (NullPointerException nep) {
			return null;
		}
	}

	public void clear() {
		m_Components.clear();
		m_Events.clear();
		m_Scopes.clear();
	}

	public IComponent createComponent(String type) {
		return SwingComponent.create(type);
	}

	public IEvent createEvent(int id, String name) {
		return SwingEvent.create(id, name);
	}
	
	public IComponent createDefaultComponent() {
		return SwingComponent.createDefault();
	}

	@Override
	public boolean initialize(InputStream desc_file) {
		// TODO Auto-generated method stub
		return false;
	}
}
