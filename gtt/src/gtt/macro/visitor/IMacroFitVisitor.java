package gtt.macro.visitor;

import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;

public interface IMacroFitVisitor extends IMacroStructureVisitor {

	public void visit(EventTriggerNode node);

	public void visit(FitStateAssertionNode node);

	public void visit(FitNode node);

	public void visit(SplitDataAsNameNode node);

	public void visit(GenerateOrderNameNode node);

	public void visit(FixNameNode node);

	public void visit(FitAssertionNode node);

}
