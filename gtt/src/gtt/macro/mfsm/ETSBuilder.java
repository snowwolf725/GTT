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
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.mfsm.ContractEvent;
import gtt.macro.mfsm.ContractParser;
import gtt.macro.mfsm.ContractState;
import gtt.macro.mfsm.ContractTransition;
import gtt.macro.mfsm.ConverETStoGraph;
import gtt.macro.mfsm.IMacroContractFSM;
import gtt.macro.mfsm.MacroContractFSM;
import gtt.macro.mfsm.NameGenerator;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;


public class ETSBuilder {
	private Vector<ContractEvent> MEs;
	private IMacroContractFSM m_mets;
	private AbstractMacroNode m_script = null;
	private ConverETStoGraph m_convertor = null;

	public static final int ANY_LEVEL = -1;

	private int allowed_level = ANY_LEVEL;

	public ETSBuilder(AbstractMacroNode script) {
		m_script = script;
	}

	public IGraph getGraph() {
		return m_convertor.getGraph();
	}

	public void saveGraph(String filepath) {
		m_convertor.saveGraph(filepath);
	}
	
	public void saveSimpleGraph(String filepath) {
		NameGenerator ng= new NameGenerator();
		Iterator<Vertex> ite = m_convertor.getGraph().vertices().iterator();
		while(ite.hasNext()) {
			Vertex v = ite.next();
			ContractState s = (ContractState)v.getUserObject();
			s.setName(ng.getNextName());
		}
		
		m_convertor.saveGraph(filepath);
	}


	private void createEvent(AbstractMacroNode node) {

		if (!(node instanceof MacroComponentNode))
			return;

		for (int i = 0; i < node.size(); i++) {
			// 將所有macro event 轉成 event contract
			AbstractMacroNode child = (AbstractMacroNode) node.get(i);

			if (child instanceof MacroComponentNode)
				createEvent(child);

			if (child instanceof MacroEventNode) {
				MacroEventNode me = (MacroEventNode) child;
				if (me.getContract().getPostCondition() == ""
						&& me.getContract().getPreCondition() == "")
					continue;
				if (allowed_level == ANY_LEVEL
						|| me.getContract().getLevel() == allowed_level)
					MEs.add(translateToContractEvent(me));
			}
		}
	}

	private ContractEvent translateToContractEvent(MacroEventNode event) {
		ContractParser parser = new ContractParser();
		Predicate p = parser.doParse(event.getContract().getPreCondition());
		Predicate act = parser.doParse(event.getContract().getAction());
		Predicate q = parser.doParse(event.getContract().getPostCondition());

		return new ContractEvent(event, p, act, q);
	}

	public void build() {
		build(ANY_LEVEL);
	}

	public void build(int level) {
		allowed_level = level;

		MEs = new Vector<ContractEvent>();
		m_mets = new MacroContractFSM();
		m_convertor = new ConverETStoGraph();

		createEvent(m_script);
		createETS();

		// mets to IGraph
		m_convertor.buildGraph(m_mets);
	}

	private void createETS() {
		// init...
		ContractState initState = new ContractState();
		initState.add("start");

		Stack<ContractState> to_visit = new Stack<ContractState>();
		to_visit.push(initState);
		m_mets.addState(initState);

		// build...
		while (!to_visit.empty()) {
			ContractState currentState = (ContractState) to_visit.pop();
			for (int i = 0; i < MEs.size(); i++) {
				if (!currentState.isExist("start"))
					continue;
				ContractEvent me = MEs.get(i);

				// pre-condition
				if (me.getPreCondition() == null)
					continue;
				if (!me.getPreCondition().entry(currentState))
					continue; // 這個contract event 不能套

				ContractState newState = new ContractState(currentState);
				// Action
				if(me.getAction()!=null)
					me.getAction().entry(newState);

				if (me.getPostCondition() == null)
					continue;

				// post condition 不為真就不建立這個結果
				if (!me.getPostCondition().entry(newState))
					continue;

				ContractState tmp = m_mets.isExist(newState);
				if (tmp == null) {
					m_mets.addState(newState);
					to_visit.push(newState);
					m_mets.addTranstion(new ContractTransition(currentState,
							me, newState));
					System.out.println("ADD STATE: " + newState.toString());
				} else {
					m_mets.addTranstion(new ContractTransition(currentState,
							me, tmp));
				}
			}
		}
	}
}


class NameGenerator {
	private char m_nextName = 'N';
	int m_counter = 1;

	public NameGenerator() {
	}

	public String getNextName() {
		String n = m_nextName + String.valueOf(m_counter);
		m_counter++;
		return n;
	}
}

class SimpleGraph {

	private Map<String, String> m_map = new HashMap<String, String>();

	public Map<String, String> getMap() {
		return m_map;
	}

	public void changeVertexUsingShortName(IGraph graph) {
		NameGenerator nameGenerator = new NameGenerator();

		Iterator<Vertex> ite = graph.vertices().iterator();
		while (ite.hasNext()) {
			Vertex v = ite.next();
			ContractState n = (ContractState) v.getUserObject();
			String name = "";
			if (m_map.containsKey(n.getName())) {
				name = m_map.get(n.getName());
			} else {
				name = nameGenerator.getNextName();
				m_map.put(n.getName(), name);
			}
			n.setName(name);

		}
	}
}