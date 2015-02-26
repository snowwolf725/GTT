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
/*
 * Created on 2005/3/12 by 哲銘
 */
package gtt.macro.macroStructure;

import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.CompositeMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gtt.util.Pair;
import gttlipse.fit.node.FitNode;
import gttlipse.macro.dialog.EditDialogFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



public class MacroComponentNode extends CompositeMacroNode {
	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}
	
	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	public MacroComponentNode() {
		this("");
	}

	public static MacroComponentNode create(String name) {
		return new MacroComponentNode(name);
	}

	MacroComponentNode(String name) {
		super(name);
	}

	public MacroComponentNode(MacroComponentNode target) {
		super(target.getName());

		if (target.size() == 0)
			return;

		Iterator<AbstractMacroNode> ite = target.iterator();
		while (ite.hasNext()) {
			add(ite.next().clone());
		}
	}

	public String toString() {
		return getName();
	}

	public MacroComponentNode clone() {
		return new MacroComponentNode(this);
	}

	public List<MacroEventNode> getMacroEvents() {
		List<MacroEventNode> v = new LinkedList<MacroEventNode>();
		Iterator<AbstractMacroNode> ite = iterator();
		while (ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			if (node instanceof MacroEventNode) {
				v.add((MacroEventNode) node);
			}
		}
		return v;
	}

	public boolean equals(Object o) {
		if (!(o instanceof MacroComponentNode))
			return false;

		MacroComponentNode n = (MacroComponentNode) o;

		return n.getName().equals(m_Name);
	}

	@Override
	public boolean isAllowedAdd(AbstractMacroNode node) {
		// MacroComponentNode 可允許新增的子節點類型
		if (node instanceof MacroComponentNode)
			return true;
		if (node instanceof ComponentNode)
			return true;
		if (node instanceof DynamicComponentNode)
			return true;
		if (node instanceof MacroEventNode)
			return true;
		if (node instanceof FitNode)
			return true;
		if (node instanceof IncludeNode)
			return true;		
		return false;
	}

	// ///////////////////////////////////////////////////////////
	// 以下為跟 Coverage 有關的程式碼
	// 或許需要refactor至其它class 上 -zwshen 2008/05/19
	// ///////////////////////////////////////////////////////////

	private EventCoverage m_coverage = new EventCoverage(this);

	public EventCoverage getEventCoverage() {
		return m_coverage;
	}

	public Pair getTotalComponentCoverage() {
		int covered = 0, total = 0;

		for (int i = 0; i < this.size(); i++) {
			AbstractMacroNode child = this.get(i);

			if (child instanceof MacroComponentNode) {
				MacroComponentNode node = (MacroComponentNode) child;
				Pair p = node.getTotalEventCoverage();
				covered += Integer.parseInt(p.first.toString());
				total += Integer.parseInt(p.second.toString());
				continue;
			}

			if (child instanceof ComponentNode) {
				ComponentNode node = (ComponentNode) child;

				if (node.getEventCoverage().getNeedToCoverSize() > 0) {
					if (node.getEventCoverage().getCoverSize() > 0)
						covered += 1;
					total += 1;
				}

				continue;
			}

		}

		return new Pair(covered, total);
	}

	public Pair getTotalEventCoverage() {
		int covered = 0, total = 0;

		for (int i = 0; i < this.size(); i++) {
			AbstractMacroNode child = this.get(i);

			if (child instanceof MacroComponentNode) {
				MacroComponentNode node = (MacroComponentNode) child;
				Pair p = node.getTotalEventCoverage();
				covered += Integer.parseInt(p.first.toString());
				total += Integer.parseInt(p.second.toString());
				continue;
			}

			if (child instanceof ComponentNode) {
				ComponentNode node = (ComponentNode) child;
				covered += node.getEventCoverage().getCoverSize();
				total += node.getEventCoverage().getNeedToCoverSize();
				continue;
			}
		}

		return new Pair(covered, total);
	}

	public Pair getTotalMacroEventCoverage() {
		int covered = 0, total = 0;

		for (int i = 0; i < this.size(); i++) {
			AbstractMacroNode child = this.get(i);

			if (child instanceof MacroComponentNode) {
				MacroComponentNode node = (MacroComponentNode) child;

				Pair p = node.getTotalMacroEventCoverage();
				covered += Integer.parseInt(p.first.toString());
				total += Integer.parseInt(p.second.toString());
			}
		}
		covered += this.getEventCoverage().getCoverSize();
		total += this.getEventCoverage().getNeedToCoverSize();

		return new Pair(covered, total);
	}

	public Pair getTotalMacroComponentCoverage() {
		int covered = 0, total = 0;

		for (int i = 0; i < this.size(); i++) {
			AbstractMacroNode child = this.get(i);

			if (child instanceof MacroComponentNode) {
				MacroComponentNode node = (MacroComponentNode) child;

				Pair p = node.getTotalMacroComponentCoverage();
				covered += Integer.parseInt(p.first.toString());
				total += Integer.parseInt(p.second.toString());

				if (node.getEventCoverage().getCoverSize() > 0)
					covered += 1;
				total += 1;
			}
		}
		return new Pair(covered, total);
	}
//
//	@Override
//	public String getOuterUsageInfo() {
//		// return outer usage infomation
//		return " : " + getOuterUtilityRate() + "%" + " (" + getOuterUsage() + " / " + getTotalUsage() + ")";
//	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_MACRO_COMPONENT_NODE;
	}

}
