package gttlipse.refactoring.macro;

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
import gtt.macro.visitor.IMacroFitVisitor;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;

import java.util.Iterator;


public abstract class MacroRefactorVisitor implements IMacroFitVisitor {
	public MacroRefactorVisitor() {}

	@Override
	public void visit(EventTriggerNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FitStateAssertionNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FitNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SplitDataAsNameNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(GenerateOrderNameNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FixNameNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FitAssertionNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ComponentEventNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ComponentNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MacroComponentNode node) {
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}
	}

	@Override
	public void visit(MacroEventNode node) {
		Iterator<AbstractMacroNode> ite = node.iterator();
		while (ite.hasNext()) {
			ite.next().accept(this);
		}
	}

	@Override
	public void visit(ModelAssertNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MacroEventCallerNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ViewAssertNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NDefComponentNode node) {
		// TODO Auto-generated method stub

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
	public void visit(ExistenceAssertNode node) {
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
