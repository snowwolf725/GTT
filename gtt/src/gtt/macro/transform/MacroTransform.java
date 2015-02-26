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
package gtt.macro.transform;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.macro.DefaultMacroFinder;
import gtt.macro.MacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroNodeFactory;
import gtt.oracle.OracleData;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;

public class MacroTransform implements IMacroTransform {

	MacroTransform() {
	}

	static IMacroTransform m_Instance = new MacroTransform();

	// SINGLETON
	public static IMacroTransform instance() {
		return m_Instance;
	}

	public AbstractNode transform(AbstractMacroNode root) {
		// throw Exception("Not Implement Yet.");
		return null;
	}

	/***************************************************************************
	 * test script -> macro
	 */
	MacroNodeFactory m_MacroFactory = new MacroNodeFactory();

	public AbstractMacroNode transform(AbstractNode root) {

		MacroComponentNode result_mc = m_MacroFactory
				.createMacroComponentNode("MACRO");
		MacroEventNode result_me = m_MacroFactory.createMacroEventNode();
		result_me.setName("AllTests");

		processAbstractNode(result_mc, result_me, root);

		if (root.isContainer()) {
			// 加 macro event
			if (result_me.size() > 0)
				result_mc.get(0).add(result_me);
			return result_mc.get(0); //
		} else {
			// 加 macro event
			if (result_me.size() > 0)
				result_mc.add(result_me);
			return result_mc; //
		}
	}

	private void processAbstractNode(MacroComponentNode mc, MacroEventNode me,
			AbstractNode node) {
		/*
		 * NOTE: OracleNode 是FodlerNode 的subclass 必須要先比FolderNode 先判斷特殊處理，否則
		 * 會被FolderNode擋住
		 */
		if (node instanceof gtt.testscript.OracleNode) {
			// OracleNode 轉換
			OracleData d = ((gtt.testscript.OracleNode) node).getOracleData();

			gtt.macro.macroStructure.OracleNode n2 = new gtt.macro.macroStructure.OracleNode(
					d);
			me.add(n2);
			return;
		}

		if (node instanceof FolderNode) {
			FolderNode fn = (FolderNode) node;

			// macro component
			MacroComponentNode ff = m_MacroFactory.createMacroComponentNode(fn
					.getName());

			// macro event
			MacroEventNode fe = m_MacroFactory.createMacroEventNode();
			fe.setName(fn.getName());

			mc.add(ff);
			// 遞回處理folder node
			processFolderNode(ff, fe, fn);

			if (fe.size() > 0)
				ff.add(fe);

			return;
		}
		if (node instanceof EventNode) {
			tansformEventNodeToComponentEventNode(mc, me, (EventNode) node);
		} else if (node instanceof gtt.testscript.ViewAssertNode) {
			// future work
		} else if (node instanceof gtt.testscript.ModelAssertNode) {
			// future work
		}
	}

	private void processFolderNode(MacroComponentNode mc, MacroEventNode me,
			FolderNode f) {
		for (int i = 0; i < f.size(); i++) {
			processAbstractNode(mc, me, f.get(i));
		}

	}

	private void tansformEventNodeToComponentEventNode(MacroComponentNode mc,
			MacroEventNode me, EventNode event) {

		IComponent ic = event.getComponent();

		MacroFinder finder = new DefaultMacroFinder(mc);
		ComponentNode cn = finder.findComponentNodeByName(ic.getName());
		if (cn == null) {
			cn = m_MacroFactory.createComponentNode(ic);
			mc.add(cn);
		}

		// 將 event 的部份加到 macro event
		ComponentEventNode cen = m_MacroFactory.createComponentEventNode(cn
				.getPath().toString());
		IEvent ie = event.getEvent();
		cen.setEvent(ie.getName(), ie.getEventId());

		// add arguments
		cen.setArguments(ie.getArguments().clone());

		me.add(cen);
	}
}
