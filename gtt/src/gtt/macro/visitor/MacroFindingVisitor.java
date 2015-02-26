package gtt.macro.visitor;

import java.util.ArrayList;
import java.util.Iterator;

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

public class MacroFindingVisitor implements IMacroStructureVisitor {

	private ArrayList<String> _list = new ArrayList<String>();
	
	public ArrayList<String> getResultList() {
		return _list;
	}
	
	@Override
	public void visit(ComponentEventNode node) {}

	@Override
	public void visit(ComponentNode node) {
		// Insert component name into list
		_list.add(node.getName());
	}

	@Override
	public void visit(DynamicComponentNode node) {
		// Insert component name into list
		_list.add(node.getName());
	}
	
	@Override
	public void visit(MacroComponentNode node) {
		// Insert macro component name into list
		_list.add(node.getName());
		
		// Traverse macro structure
		Iterator<AbstractMacroNode> it = node.iterator();
		while (it.hasNext()) {
			((AbstractMacroNode)it.next()).accept(this);
		}
	}

	@Override
	public void visit(MacroEventNode node) {}

	@Override
	public void visit(ModelAssertNode node) {}

	@Override
	public void visit(MacroEventCallerNode node) {}

	@Override
	public void visit(ViewAssertNode node) {}

	@Override
	public void visit(NDefComponentNode node) {}

	@Override
	public void visit(BreakerNode node) {}

	@Override
	public void visit(CommentNode node) {}

	@Override
	public void visit(SleeperNode node) {}

	@Override
	public void visit(OracleNode node) {}

	@Override
	public void visit(ExistenceAssertNode node) {}

	@Override
	public void visit(LaunchNode node) {}
	
	@Override
	public void visit(SplitDataNode node) {}

	@Override
	public void visit(DynamicComponentEventNode node) {}

	@Override
	public void visit(IncludeNode node) {}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}
}
