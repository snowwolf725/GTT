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
package gtt.macro.macroStructure;

import gtt.eventmodel.IComponent;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ModelAssertNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;



public class MacroNodeFactory {
	public ComponentEventNode createComponentEventNode() {
		return new ComponentEventNode();
	}
	public ComponentEventNode createComponentEventNode(ComponentNode node) {
		return new ComponentEventNode(node);
	}
	public ComponentEventNode createComponentEventNode(String path) {
		return new ComponentEventNode(path);
	}
	
	public ComponentNode createComponentNode() {
		return ComponentNode.create();
	}
	
	public ComponentNode createComponentNode(IComponent ic) {
		return new ComponentNode(ic);
	}

	public MacroEventCallerNode createMacroEventCallerNode(String path) {
		return new MacroEventCallerNode(path);
	}

	public MacroComponentNode createMacroComponentNode(String name) {
		return MacroComponentNode.create(name);
	}

	public MacroEventNode createMacroEventNode() {
		return new MacroEventNode();
	}

	public ModelAssertNode createModelAssertNode() {
		return new ModelAssertNode();
	}

	public ViewAssertNode createViewAssertNode() {
		return new ViewAssertNode();
	}
	
	public ExistenceAssertNode createExistenceAssertNode() {
		return new ExistenceAssertNode();
	}
	
	public EventTriggerNode createEventTriggerNode() {
		return new EventTriggerNode();
	}
	
	public FitStateAssertionNode createFitStateAssertionNode() {
		return new FitStateAssertionNode();
	}

	public FitAssertionNode createFitAssertionNode() {
		return new FitAssertionNode();
	}

	public FitNode createFitNode() {
		return new FitNode();
	}

	public SplitDataAsNameNode createSplitDataAsNameNode() {
		return new SplitDataAsNameNode();
	}

	public GenerateOrderNameNode createGenerateOrderNameNode() {
		return new GenerateOrderNameNode();
	}

	public FixNameNode createFixNameNode() {
		return new FixNameNode();
	}
	
	public LaunchNode createLaunchNode() {
		return new LaunchNode();
	}
	
	public IncludeNode createIncludeNode() {
		return new IncludeNode();
	}
	
	public SplitDataNode createSplitDataNode(String name, String target) {
		return SplitDataNode.create(name, target);
	}
	
	public DynamicComponentNode createDynamicComponentNode() {
		return DynamicComponentNode.create();
	}
	
	public DynamicComponentNode createDynamicComponentNode(IComponent c) {
		return DynamicComponentNode.create(c);
	}
	
	public DynamicComponentEventNode createDynamicComponentEventNode() {
		return new DynamicComponentEventNode();
	}
	
	public SystemNode createSystemNode(String name) {
		return new SystemNode(name);
	}
}