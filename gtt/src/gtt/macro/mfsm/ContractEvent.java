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
package gtt.macro.mfsm;

import gtt.macro.mfsm.predicate.Predicate;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.mfsm.ContractEvent;

class ContractEvent {
	private MacroEventNode m_MacroEvent;
	private Predicate m_precondition;
	private Predicate m_action;
	private Predicate m_postcondition;

	// public Event(MacroEventConditionNode macroEvent, AbstractPredicate pre,
	// AbstractPredicate post) {
	public ContractEvent(MacroEventNode macroEvent, Predicate pre,
			Predicate action, Predicate post) {
		m_MacroEvent = macroEvent;
		m_precondition = pre;
		m_action = action;
		m_postcondition = post;
	}

	public String getEventName() {
		return m_MacroEvent.getName();
	}

	public MacroEventNode getMacroEvent() {
		return m_MacroEvent;
	}

	public Predicate getPreCondition() {
		return m_precondition;
	}

	public Predicate getAction() {
		return m_action;
	}

	public Predicate getPostCondition() {
		return m_postcondition;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true; // the same refernece
		if (!(obj instanceof ContractEvent))
			return false;
		ContractEvent e = (ContractEvent) obj;

		return m_MacroEvent.equals(e.getMacroEvent());
	}
}
