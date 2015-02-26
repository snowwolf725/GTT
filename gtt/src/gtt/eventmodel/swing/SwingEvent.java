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
package gtt.eventmodel.swing;

/**
 * Created on 2006/08/01
 * @author zwshen
 */
import gtt.eventmodel.AbstractEvent;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IEvent;

public class SwingEvent extends AbstractEvent {

	private int eventID;

	private String evetName;

	public int getEventId() {
		return eventID;
	}

	public void setEventId(int id) {
		eventID = id;
	}

	public String getName() {
		return evetName;
	}

	public void setName(String name) {
		evetName = name;
	}

	private SwingEvent(int id, String name) {
		super(id, name);
		eventID = id;
		evetName = name;
	}

	public static SwingEvent create(int id, String name) {
		return new SwingEvent(id, name);
	}

	private Arguments arguments = new Arguments();

	public IEvent clone() {
		SwingEvent e = create(getEventId(), getName());
		e.setArguments(arguments.clone());

		return e;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SwingEvent))
			return false;

		SwingEvent e = (SwingEvent) o;

		return e.getName().equals(evetName) && e.getEventId() == eventID;
	}

	public String toString() {
		StringBuilder result = new StringBuilder(evetName);
		result.append(arguments.toString());
		return result.toString();
	}

	public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments list) {
		arguments = list.clone();
	}
}
