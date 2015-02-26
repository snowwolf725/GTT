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
package gtt.macro;

import gtt.eventmodel.AbstractEvent;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.util.Pair;
import gttlipse.macro.dialog.UsedColors;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.graphics.Color;


public class EventCoverage {
	private Object m_component = null;

	private Map<String, Pair> m_coverageMap = new LinkedHashMap<String, Pair>();

	public EventCoverage(Object component) {
		m_component = component;
		setup();
	}

	// public void print() {
	// Set<String> keys = m_coverage.keySet();
	// Iterator<String> ite = keys.iterator();
	// while (ite.hasNext()) {
	// String event = (String) ite.next();
	// Pair value = m_coverage.get(event);
	// if (value.first.equals(true)) {
	// System.out.print("  " + event);
	// if (value.second.equals(true))
	// System.out.println(" -> V");
	// else
	// System.out.println(" -> X");
	// }
	// }
	// }

	public void setup() {
		m_coverageMap.clear();
		if (m_component instanceof IComponent) {
			// 取得該 swing component的event list
			String swingComponent = ((IComponent) m_component).getType();

			final IEventModel model = EventModelFactory.getDefault();
			List<IEvent> eventList = model.getEvents(SwingComponent
					.create(swingComponent));

			TreeSet<String> sset_events = new TreeSet<String>();
			Iterator<IEvent> eite = eventList.iterator();

			// sort events
			while (eite.hasNext()) {
				AbstractEvent _event = (AbstractEvent) eite.next();
				sset_events.add(_event.getName());
			}

			Iterator<String> eite_sset = sset_events.iterator();
			while (eite_sset.hasNext()) {
				String event = (String) eite_sset.next();
				Pair p = new Pair(false, false);
				m_coverageMap.put(event, p);
			}
		}

		if (m_component instanceof MacroComponentNode) {
			MacroComponentNode node = (MacroComponentNode) m_component;
			for (int i = 0; i < node.size(); i++) {
				AbstractMacroNode child = node.get(i);
				if (child instanceof MacroEventNode) {
					Pair p = new Pair(false, false);
					m_coverageMap.put(((MacroEventNode) child).getName(), p);
				}
			}
			return;
		}
	}

	public Set<String> getEvents() {
		return m_coverageMap.keySet();
	}

	public Set<String> getNeedToCoverSet() {
		Set<String> s = new TreeSet<String>();

		Set<String> keys = m_coverageMap.keySet();
		Iterator<String> ite = keys.iterator();

		while (ite.hasNext()) {
			String key = (String) ite.next();
			Pair p = m_coverageMap.get(key);
			if (p.first.equals(true)) {
				s.add(key);
			}
		}
		return s;
	}

	public int getNeedToCoverSize() {
		Set<String> s = getNeedToCoverSet();
		return s.size();
	}

	public int getCoverSize() {
		Set<String> s = new TreeSet<String>();

		Set<String> keys = m_coverageMap.keySet();
		Iterator<String> ite = keys.iterator();

		while (ite.hasNext()) {
			String key = (String) ite.next();
			Pair p = m_coverageMap.get(key);
			if (p.first.equals(true) && p.second.equals(true)) {
				s.add(key);
			}
		}
		return s.size();
	}

	public boolean isNeedToCover(String event) {
		if (!m_coverageMap.containsKey(event))
			return false;

		Pair p = m_coverageMap.get(event);
		return p.first.equals(true);
	}

	public boolean isCover(String event) {
		if (!m_coverageMap.containsKey(event))
			return false;

		Pair p = m_coverageMap.get(event);
		return p.second.equals(true);
	}

	public boolean addNeedToCover(String event, boolean isNeedToCover) {
		if (event.equals(""))
			return false;

		if (!m_coverageMap.containsKey(event)) {
			Pair p = new Pair(false, false);
			m_coverageMap.put(event, p);
		}

		Pair p = m_coverageMap.get(event);
		p.first = isNeedToCover;

		return true;
	}

	public boolean removeNeedToCover(String event) {
		if (event.equals(""))
			return false;
		if (!m_coverageMap.containsKey(event))
			return true;
		m_coverageMap.remove(event);
		return true;
	}

	public boolean setCover(String event, boolean isCover) {
		if (!m_coverageMap.containsKey(event))
			return false;

		Pair p = m_coverageMap.get(event);
		p.second = isCover;

		return true;
	}

	public double getCoverage() {
		int cnt_u = 0, cnt_d = 0;

		Set<String> keys = m_coverageMap.keySet();
		Iterator<String> ite = keys.iterator();

		while (ite.hasNext()) {
			String key = (String) ite.next();
			Pair p = m_coverageMap.get(key);
			if (p.first.equals(true)) {
				cnt_d++;
				if (p.second.equals(true))
					cnt_u++;
			}
		}

		if (cnt_d == 0) {
			if (cnt_u == 0)
				return 0;
			else
				return cnt_u * 100;
		}

		return cnt_u * 100 / cnt_d;
	}

	public String getCoverageString() {
		return "Coverage:" + getCoverage() * 100 + "%";
	}

	public void clear() {
		Iterator<String> ite = m_coverageMap.keySet().iterator();

		while (ite.hasNext()) {
			String key = (String) ite.next();
			Pair p = m_coverageMap.get(key);
			p.second = false;
		}
	}

	public Color getRenderColor(String event) {
		if (!isNeedToCover(event))
			return UsedColors.WHITE_COLOR;
		if (isCover(event))
			return UsedColors.GREEN_COLOR;
		return UsedColors.RED_COLOR;
	}
}
