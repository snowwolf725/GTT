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

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.NDefComponentNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class MacroCoverageAnalysis {

	private Map<IComponent, Vector<IEvent>> m_coverage = new HashMap<IComponent, Vector<IEvent>>();
	private Map<String, Vector<String>> m_macroCoverage = new HashMap<String, Vector<String>>();

	public void setCoverage(Map<IComponent, Vector<IEvent>> coverage) {
		m_coverage = coverage;
	}

	public void setMacroCoverage(Map<String, Vector<String>> coverage) {
		m_macroCoverage = coverage;
	}

	public void computeCoverage(MacroDocument doc) {
		clearCoverage((MacroComponentNode) doc.getMacroScript());

		forComponentCoverage(doc);

		forMacroComponentCoverage(doc);
	}

	private AbstractMacroNode getNodeByComponent(MacroComponentNode parent,
			IComponent com) {
		Iterator<AbstractMacroNode> ite = parent.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode tmp = (AbstractMacroNode) ite.next();

			if (tmp instanceof MacroComponentNode) {
				AbstractMacroNode n = getNodeByComponent(
						(MacroComponentNode) tmp, com);
				if (n != null)
					return n;
			}

			if (tmp instanceof ComponentNode) {
				ComponentNode comp = (ComponentNode) tmp;
				if (comp.getComponent().equals(com))
					return comp;
			}
		}
		return null;
	}

	private AbstractMacroNode getMacroComponentNodeByName(
			MacroComponentNode node, String name) {
		Iterator<AbstractMacroNode> ite = node.iterator();

		if (node.getName().equals(name))
			return node;

		while (ite.hasNext()) {
			AbstractMacroNode tmp = (AbstractMacroNode) ite.next();
			if (!(tmp instanceof MacroComponentNode))
				continue;

			AbstractMacroNode n = getMacroComponentNodeByName(
					(MacroComponentNode) tmp, name);
			if (n != null)
				return n;
		}

		return null;
	}

	private void forMacroComponentCoverage(MacroDocument doc) {
		MacroComponentNode root = (MacroComponentNode) doc.getMacroScript();

		Set<?> keysOfMacroComponent = m_macroCoverage.keySet();
		Iterator<?> ite = keysOfMacroComponent.iterator();

		while (ite.hasNext()) {
			String keyName = (String) ite.next();
			AbstractMacroNode node = getMacroComponentNodeByName(root, keyName);
			if (node == null)
				continue;
			// ¥u­pºâ MacroComponentNode
			if (!(node instanceof MacroComponentNode))
				continue;

			EventCoverage coverage = ((MacroComponentNode) node)
					.getEventCoverage();

			Vector<String> events = m_macroCoverage.get(keyName);
			for (int i = 0; i < events.size(); i++) {
				String e = events.get(i);
				// event cover
				coverage.setCover(e, true);
			}
		}
	}

	private void forComponentCoverage(MacroDocument doc) {
		MacroComponentNode root = (MacroComponentNode) doc.getMacroScript();

		Set<?> keys = m_coverage.keySet();
		Iterator<?> ite = keys.iterator();

		while (ite.hasNext()) {
			IComponent keyName = (IComponent) ite.next();

			AbstractMacroNode node = getNodeByComponent(root, keyName);
			if (node == null) {
				NDefComponentNode ndef = new NDefComponentNode(keyName);
				MacroUtil.insertNode(root, ndef);

				Vector<IEvent> events = m_coverage.get(keyName);
				EventCoverage coverage = ndef.getEventCoverage();
				for (int i = 0; i < events.size(); i++) {
					String e = events.get(i).getName();
					coverage.addNeedToCover(e, true);
					coverage.setCover(e, true);
				}

				continue;
			}

			if (node instanceof ComponentNode) {
				EventCoverage coverage = ((ComponentNode) node)
						.getEventCoverage();
				Vector<IEvent> events = m_coverage.get(keyName);
				for (int i = 0; i < events.size(); i++) {
					String eventName = events.get(i).getName();
					coverage.setCover(eventName, true);
				}
			}
		}
	}

	// private void dumpCoverage(MacroComponentNode parent) {
	// Iterator<AbstractMacroNode> ite = parent.iterator();
	// System.out.println("[Coverage]");
	// while (ite.hasNext()) {
	// AbstractMacroNode tmp = (AbstractMacroNode) ite.next();
	//
	// if (tmp instanceof MacroComponentNode) {
	// dumpCoverage((MacroComponentNode) tmp);
	// }
	//
	// if (tmp instanceof ComponentNode) {
	// ComponentNode n = (ComponentNode) tmp;
	// System.out.println("\t" + n.getName());
	// n.getEventCoverage().print();
	// }
	// }
	// }

	private void clearCoverage(MacroComponentNode parent) {
		parent.getEventCoverage().clear();
		for (int i = 0; i < parent.size(); i++) {
			AbstractMacroNode child = parent.get(i);
			if (child instanceof MacroComponentNode) {
				// ((MacroComponentNode)child).getEventCoverage().clear();
				clearCoverage((MacroComponentNode) child);
				continue;
			}

			if (child instanceof ComponentNode) {
				((ComponentNode) child).getEventCoverage().clear();
				continue;
			}
		}
	}
}
