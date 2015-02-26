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

import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.mfsm.ContractState;
import gtt.macro.mfsm.ContractTransition;
import gtt.macro.mfsm.IMacroContractFSM;
import gtt.util.Pair;
import gttlipse.testgen.graph.AdjacencyListGraph;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class ConverETStoGraph {
	private IGraph m_graph = new AdjacencyListGraph();

	public ConverETStoGraph() {
	}

	public IGraph getGraph() {
		return m_graph;
	}

	public void buildGraph(IMacroContractFSM mets) {
		if (mets == null)
			return;

		List<ContractState> states = mets.getStates();
		// add vertexs
		for (int i = 0; i < states.size(); i++) {
			ContractState s = states.get(i);
			m_graph.addVertex(s);

			// start vertex
			if (s.size() == 1 && s.isExist("start")) {
				m_graph.setStart(m_graph.getVertex(s));
			}

		}

		List<ContractTransition> edges = mets.getTransitions();
		// add edges
		for (int i = 0; i < edges.size(); i++) {
			ContractTransition tran = edges.get(i);

			Vertex from = m_graph.getVertex(tran.getFrom());
			Vertex to = m_graph.getVertex(tran.getTo());

			if (from == null || to == null)
				continue;

			m_graph.addEdge(from, to, tran.getEvent().getMacroEvent());
		}

	}

	@SuppressWarnings("unchecked")
	private void addLabel(Vector<Pair> v, ContractState s, String event) {
		for (int i = 0; i < v.size(); i++) {
			Pair p = v.get(i);
			if (p.first == null)
				continue;

			ContractState ss = (ContractState) p.first;

			if (!ss.equals(s))
				continue;

			// 找到就串接在後面
			Vector<String> events = (Vector<String>) p.second;
			if (!events.contains(event)) {
				events.add(event);
				p.second = events;
			}
			return;
		}

		// 找不到，就新增
		Vector<String> events = new Vector<String>();
		events.add(event);
		Pair p = new Pair(s, events);
		v.add(p);
	}

	@SuppressWarnings("unchecked")
	private String getLabel(Vector<Pair> v, ContractState s) {
		for (int i = 0; i < v.size(); i++) {
			Pair p = (Pair) v.get(i);
			ContractState ss = (ContractState) p.first;

			if (ss == null)
				continue;
			if (ss.equals(s)) {
				Vector<String> events = (Vector<String>) p.second;
				String label = "";
				for (int j = 0; j < events.size(); j++) {
					if (j == 0)
						label += events.get(j);
					else
						label += "\\n" + events.get(j);
				}
				return label;
			}
		}

		return "";
	}

	public void saveGraph(String filepath) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileOutputStream(filepath));
			writer.print("digraph G\n{\n");
			String ets = getDigraph();
			writer.print(ets);
			writer.print("}");
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String getDigraph() {
		StringBuilder output = new StringBuilder("");
		Iterator<Vertex> iteAllFrom = m_graph.vertices().iterator();
		while (iteAllFrom.hasNext()) {
			Vertex from = iteAllFrom.next();
			Iterator<Vertex> iteAllTo = m_graph.outAdjacentVertices(from);

			Vector<Pair> v = new Vector<Pair>();

			while (iteAllTo.hasNext()) {
				Vertex to = iteAllTo.next();
				Iterator<Edge> iteAllEvent = m_graph.outIncidentEdges(from);

				while (iteAllEvent.hasNext()) {
					Edge e = iteAllEvent.next();
					Vertex dest = e.destination();
					if (((ContractState) dest.getUserObject())
							.equals((ContractState) to.getUserObject())) {
						addLabel(v, (ContractState) to.getUserObject(),
								((MacroEventNode) e.getUserObject()).getName());
					}
				}
			}

			for (int i = 0; i < v.size(); i++) {
				output .append(((ContractState) from.getUserObject()).toString());
				output.append( " -> ");

				Pair p = (Pair) v.get(i);
				ContractState s = (ContractState) p.first;
				String label = getLabel(v, s);
				output.append(s.toString() + " " + "[label=\"" + label + "\"];\n");
			}
		}
		return output.toString();
	}
}
