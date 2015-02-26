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
 * Created on 2005/4/11
 */
package gtt.macro.macroStructure;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.BreakerNode;
import gtt.macro.macroStructure.CommentNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.CompositeMacroNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroContract;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.OracleNode;
import gtt.macro.macroStructure.SleeperNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.macro.visitor.IMacroFitVisitor;
import gtt.macro.visitor.IMacroStructureVisitor;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.macro.dialog.EditDialogFactory;

import java.util.Iterator;


public class MacroEventNode extends CompositeMacroNode implements IHaveArgument {

	public void accept(IMacroStructureVisitor v) {
		v.visit(this);
	}

	public void accept(IMacroFitVisitor v) {
		v.visit(this);
	}

	public MacroEventNode() {
		super("MacroEvent");
	}

	public MacroEventNode(String name) {
		super(name);
	}

	public static MacroEventNode create(String name) {
		return new MacroEventNode(name);
	}
	
	public static MacroEventNode createDefault() {
		return new MacroEventNode("MacroEvent");
	}

	public MacroEventNode(MacroEventNode target) {
		super(target.getName());
		arguments = target.getArguments().clone();
		contract = target.getContract().clone();
		copyChild(target);
	}

	public MacroEventNode clone() {
		return new MacroEventNode(this);
	}

	public String toString() {
		return getName() + arguments.formSignature();
	}

	private void copyChild(MacroEventNode mevent) {
		removeAll();
		Iterator<AbstractMacroNode> ite = mevent.iterator();
		while (ite.hasNext()) {
			add((ite.next()).clone());
		}
	}

	@Override
	protected boolean isAllowedAdd(AbstractMacroNode node) {
		return (node instanceof ComponentEventNode)
				|| (node instanceof DynamicComponentEventNode)
				|| (node instanceof MacroEventCallerNode)
				|| (node instanceof ViewAssertNode)
				|| (node instanceof ExistenceAssertNode)
				|| (node instanceof ModelAssertNode)
				|| (node instanceof SleeperNode)
				|| (node instanceof CommentNode)
				|| (node instanceof BreakerNode)
				|| (node instanceof OracleNode)
				|| (node instanceof LaunchNode)
				|| (node instanceof EventTriggerNode)
				|| (node instanceof FitAssertionNode)
				|| (node instanceof FitStateAssertionNode)
				|| (node instanceof SplitDataNode);
	}

	private Arguments arguments = new Arguments();

	public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}

	private MacroContract contract = new MacroContract();

	public MacroContract getContract() {
		return contract;
	}

	public void setContract(MacroContract c) {
		contract = c.clone();
	}

	// uhsing 2010/12/27 (For customized table header) begin
	private boolean _isAutoParsing = false;
	
	public void setAutoState(boolean state) {
		_isAutoParsing = state;
	}
	
	public boolean isAutoParsing() {
		return _isAutoParsing;
	}
	// end
	
	// SnowWolf725 2011/2/11 children 不用判斷是否相等?
	public boolean equals(Object obj) {
		if (this == obj)
			return true; // the same refernece
		if (!(obj instanceof MacroEventNode))
			return false;
		MacroEventNode me = (MacroEventNode) obj;

		if (!getName().equals(me.getName()))
			return false;

		return getPath().equals(me.getPath());
	}

	@Override
	public int getNodeID() {
		return EditDialogFactory.NID_MACRO_EVENT_NODE;
	}
}
