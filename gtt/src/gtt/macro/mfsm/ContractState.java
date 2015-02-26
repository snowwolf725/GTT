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

import gtt.macro.mfsm.ContractState;

import java.util.List;
import java.util.Vector;

public class ContractState {

	private List<String> m_predicates = new Vector<String>();

	public ContractState() {
	}

	public ContractState(ContractState s) {
		List<String> predicates = s.getPredicate();
		for (int i = 0; i < predicates.size(); i++) {
			m_predicates.add(predicates.get(i));
		}

	}

	public boolean equals(ContractState s) {
		if (m_predicates.size() != s.getPredicate().size())
			return false;
		for (int i = 0; i < m_predicates.size(); i++)
			if (!s.getPredicate().contains(m_predicates.get(i)))
				return false;
		return true;
	}

	public boolean add(String predicate) {
		for (int i = 0; i < m_predicates.size(); i++) {
			if (((String) m_predicates.get(i)).equals(predicate))
				return true;
		}
		return m_predicates.add(predicate);
	}

	public boolean remove(String predicate) {
		return m_predicates.remove(predicate);
	}

	public boolean isExist(String predicate) {
		for (int i = 0; i < m_predicates.size(); i++) {
			if (((String) m_predicates.get(i)).equals(predicate))
				return true;
		}
		return false;
	}

	public List<String> getPredicate() {
		return m_predicates;
	}

	public void print() {
		for (int i = 0; i < m_predicates.size(); i++) {
			System.out.print((String) m_predicates.get(i));
			if (i < m_predicates.size() - 1)
				System.out.print(",");
		}
	}

	public String getName() {
		String name = "(";
		for (int i = 0; i < m_predicates.size(); i++) {
			name += (String) m_predicates.get(i) + " ";
		}
		name += ")";
		return name;
	}
	
	public void setName(String name) {
		m_predicates.clear();
		m_predicates.add(name);
	}

	public String toString() {
		if (m_predicates.size() == 0)
			return "end";

		boolean existStart = false;
		String s = "";
		for (int i = 0; i < m_predicates.size(); i++) {
			if ((m_predicates.get(i)).equals("start"))
				existStart = true;

			if (i == 0)
				s += m_predicates.get(i);
			else
				s += "_" + m_predicates.get(i);
		}

		if (!existStart)
			return "end";

		return s;
	}

	public int size() {
		return m_predicates.size();
	}
}
