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
package gtt.macro.visitor;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.NDefComponentNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.SystemNode;
import gtt.macro.macroStructure.ViewAssertNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Macro Reuse 計算每個macro event被呼的次數 2009/03/20
 *
 * @author zwshen
 */
public class MacroReuseStatisticsVisitor implements IMacroStructureVisitor {

	class Stat {
		String event;
		int count = 0;
		int size = 0;
	}

	HashMap<String, Stat> m_map = new HashMap<String, Stat>();

	public String toString() {
		Set<String> set = m_map.keySet();
		Iterator<String> ite = set.iterator();

		StringBuilder sb = new StringBuilder("");
		int tot_ct = 0;
		int tot_size = 0;
		while (ite.hasNext()) {
			String e = ite.next();
			Stat s = m_map.get(e);
			int tt = s.size * s.count;
			sb.append(s.event + "\t" + s.size + "\t" + s.count + "\t" + tt
					+ "\n");

			tot_ct += s.count;
			tot_size += tt;
		}
		sb.append(tot_size + "\t" + tot_ct);
		return sb.toString();
	}

	public MacroReuseStatisticsVisitor() {
	}

	public void visit(ComponentEventNode node) {
	}

	public void visit(ComponentNode node) {
	}

	public void visit(MacroComponentNode node) {
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}
	}

	public void visit(MacroEventNode node) {
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}
	}

	public void visit(ModelAssertNode node) {
	}

	public void visit(MacroEventCallerNode node) {

		String path = node.getReferencePath();
		if (!m_map.containsKey(path)) {
			if(node.getReference()==null) {
				System.out.println("null path " + path);
				return;
			}
			Stat s = new Stat();
			s.event = path;
			s.count = 1;
			s.size = cal(node);
			m_map.put(path, s);
		} else {
			// 多call 一次
			Stat s = m_map.get(path);
			s.count++;
		}
	}

	
	int cal(MacroEventCallerNode node) {
		return cal((MacroEventNode) node.getReference());
	}

	int cal(MacroEventNode node) {
		int r = 0;
		for(int i=0;i<node.size();i++) {
			AbstractMacroNode n = node.get(i);
			if(n instanceof MacroEventCallerNode)
				r += cal((MacroEventCallerNode)n);
			else 
				r++; // 事件+1
		}

		return r;
	}
	public void visit(ViewAssertNode node) {
	}

	public void visit(ExistenceAssertNode node) {
	}
	
	public void visit(NDefComponentNode node) {
	}

	@Override
	public void visit(BreakerNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(CommentNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SleeperNode node) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void visit(OracleNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(LaunchNode node) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void visit(SplitDataNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(DynamicComponentNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(IncludeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}
}
