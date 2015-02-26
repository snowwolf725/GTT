package gtt.macro.visitor;

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
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

public class StatisticNodesVistior extends MacroRefactorVisitor {
	private int m_CountEventTriggerNode = 0;
	private int m_CountFitStateAssertionNode = 0;
	private int m_CountFitNode = 0;
	private int m_CountSplitDataAsNameNode = 0;
	private int m_CountGenerateOrderNameNode = 0;
	private int m_CountFixNameNode = 0;
	private int m_CountFitAssertionNode = 0;
	private int m_CountComponentEventNode = 0;
	private int m_CountComponentNode = 0;
	private int m_CountMacroComponentNode = 0;
	private int m_CountModelAssertNode = 0;
	private int m_CountMacroEventCallerNode = 0;
	private int m_CountViewAssertNode = 0;
	private int m_CountNDefComponentNode = 0;
	private int m_CountBreakerNode = 0;
	private int m_CountMacroEventNode = 0;
	private int m_CountCommentNode = 0;
	private int m_CountSleeperNode = 0;
	private int m_CountOracleNode = 0;
	private int m_CountLaunchNode = 0;
	private int m_CountExistenceAssertNode = 0;
	private int m_CountSplitDataNode = 0;
	private int m_CountDynamicComponentNode = 0;
	private int m_CountIncludeNode = 0;
	private int m_CountDynamicComponentEventNode = 0;
	private int m_CountSystemNode = 0;

	@Override
	public void visit(EventTriggerNode node) {
		super.visit(node);
		m_CountEventTriggerNode++;
	}

	@Override
	public void visit(FitStateAssertionNode node) {
		super.visit(node);
		m_CountFitStateAssertionNode++;
	}

	@Override
	public void visit(FitNode node) {
		super.visit(node);
		m_CountFitNode++;
	}

	@Override
	public void visit(SplitDataAsNameNode node) {
		super.visit(node);
		m_CountSplitDataAsNameNode++;
	}

	@Override
	public void visit(GenerateOrderNameNode node) {
		super.visit(node);
		m_CountGenerateOrderNameNode++;
	}

	@Override
	public void visit(FixNameNode node) {
		super.visit(node);
		m_CountFixNameNode++;
	}

	@Override
	public void visit(FitAssertionNode node) {
		super.visit(node);
		m_CountFitAssertionNode++;
	}

	@Override
	public void visit(ComponentEventNode node) {
		super.visit(node);
		m_CountComponentEventNode++;
	}

	@Override
	public void visit(ComponentNode node) {
		super.visit(node);
		m_CountComponentNode++;
	}

	@Override
	public void visit(MacroComponentNode node) {
		super.visit(node);
		m_CountMacroComponentNode++;
	}

	@Override
	public void visit(MacroEventNode node) {
		super.visit(node);
		m_CountMacroEventNode++;
	}

	@Override
	public void visit(ModelAssertNode node) {
		super.visit(node);
		m_CountModelAssertNode++;
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		super.visit(node);
		m_CountMacroEventCallerNode++;
	}

	@Override
	public void visit(ViewAssertNode node) {
		super.visit(node);
		m_CountViewAssertNode++;
	}

	@Override
	public void visit(NDefComponentNode node) {
		super.visit(node);
		m_CountNDefComponentNode++;
	}

	@Override
	public void visit(BreakerNode node) {
		super.visit(node);
		m_CountBreakerNode++;
	}

	@Override
	public void visit(CommentNode node) {
		super.visit(node);
		m_CountCommentNode++;
	}

	@Override
	public void visit(SleeperNode node) {
		super.visit(node);
		m_CountSleeperNode++;
	}

	@Override
	public void visit(OracleNode node) {
		super.visit(node);
		m_CountOracleNode++;
	}

	@Override
	public void visit(LaunchNode node) {
		super.visit(node);
		m_CountLaunchNode++;
	}

	@Override
	public void visit(ExistenceAssertNode node) {
		super.visit(node);
		m_CountExistenceAssertNode++;
	}

	@Override
	public void visit(SplitDataNode node) {
		super.visit(node);
		m_CountSplitDataNode++;
	}

	@Override
	public void visit(DynamicComponentNode node) {
		super.visit(node);
		m_CountDynamicComponentNode++;
	}

	@Override
	public void visit(IncludeNode node) {
		super.visit(node);
		m_CountIncludeNode++;
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		super.visit(node);
		m_CountDynamicComponentEventNode++;
	}

	@Override
	public void visit(SystemNode node) {
		super.visit(node);
		m_CountSystemNode++;
	}
	
	public String getResult() {
		return 
		"Macro Component Node:\t" + m_CountMacroComponentNode + "\n" +
		"Component Node:\t\t" + m_CountComponentNode + "\n" +
		"Macro Event Node:\t\t" + m_CountMacroEventNode + "\n" +
		"Component Event Node:\t" + m_CountComponentEventNode + "\n" +
		"Macro Event Caller Node:\t" + m_CountMacroEventCallerNode + "\n" +
		"View Assert Node:\t\t" + m_CountViewAssertNode + "\n" +
		"Launch Node:\t\t" + m_CountLaunchNode + "\n" +
		"Include Node:\t\t" + m_CountIncludeNode + "\n" ;
	}

}
