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
import gttlipse.GTTlipse;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;
import gttlipse.macro.view.BadSmellItem;
import gttlipse.macro.view.BadSmellListView;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

import java.util.Iterator;


public class LongScriptSmellVisitor extends MacroRefactorVisitor {
	private BadSmellListView m_view = GTTlipse.findBadSmellListView();
	private BadSmellItem m_item;
	private final int IMPORTANCE_YELLOW = 2;
	private final String BS_LARGE_COMPONENT = "Large Macro Component";
	private final String BS_LONG_EVENT = "Long Macro Event";
	private final String BS_LONG_ARGLIST = "Long Argument List";
	public final static int TYPE_MACROEVENT = 1;
	public final static int TYPE_MACROCOMP = 2;
	public final static int TYPE_ARG = 3;
	private int m_size = 0;
	private int m_type = 0;
	
	public void setType(int type){
		m_type = type;
	}
		
	public void setSizeLimit(int size){
		m_size = size;
	}

	@Override
	public void visit(ComponentEventNode node) {
		super.visit(node);
	}

	@Override
	public void visit(ComponentNode node) {
		super.visit(node);
	}

	@Override
	public void visit(MacroComponentNode node) {
		Iterator<AbstractMacroNode> ite = node.iterator();
		int usage = 0;
		int totalUsage = 0;
		
		// get child outer usage and total usage
		while (ite.hasNext()) {
			AbstractMacroNode child = ite.next();
			child.accept(this);
			usage += child.getBadSmellData().getBadSmellScore();
			totalUsage += child.getBadSmellData().getTotalBadSmellScore();
		}
		
		node.getBadSmellData().setBadSmellScore(usage);
		node.getBadSmellData().setTotalBadSmellScore(totalUsage);
		
		// detect self
		if(m_type == TYPE_MACROCOMP && node.size() > m_size) {
			node.getBadSmellData().setBadSmellScore(1);
			node.getBadSmellData().setTotalBadSmellScore(1);
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
			m_item = new BadSmellItem(BS_LARGE_COMPONENT, node.getPath().toString(), IMPORTANCE_YELLOW);
			m_item.addNode(node);
			m_view.addBadSmell(m_item);
		}
	}

	@Override
	public void visit(MacroEventNode node) {
		if(m_type == TYPE_MACROEVENT && node.size() > m_size) {
			node.getBadSmellData().setBadSmellScore(1);
			node.getBadSmellData().setTotalBadSmellScore(1);
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
			m_item = new BadSmellItem(BS_LONG_EVENT, node.getPath().toString(), IMPORTANCE_YELLOW);
			m_item.addNode(node);
			m_view.addBadSmell(m_item);
		} else if(m_type == TYPE_ARG && node.getArguments().size() > m_size) {
			node.getBadSmellData().setBadSmellScore(1);
			node.getBadSmellData().setTotalBadSmellScore(1);
			node.getBadSmellData().setRGB(node.getBadSmellData().COLOR_YELLOW);
			m_item = new BadSmellItem(BS_LONG_ARGLIST, node.getPath().toString(), IMPORTANCE_YELLOW);
			m_item.addNode(node);
			m_view.addBadSmell(m_item);
		}
	}

	@Override
	public void visit(ModelAssertNode node) {
		super.visit(node);
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		super.visit(node);
	}

	@Override
	public void visit(ViewAssertNode node) {
		super.visit(node);
	}

	@Override
	public void visit(NDefComponentNode node) {
		super.visit(node);
	}

	@Override
	public void visit(BreakerNode node) {

	}

	@Override
	public void visit(CommentNode node) {

	}

	@Override
	public void visit(SleeperNode node) {

	}

	@Override
	public void visit(OracleNode node) {

	}

	@Override
	public void visit(ExistenceAssertNode node) {

	}

	@Override
	public void visit(LaunchNode node) {

	}

	@Override
	public void visit(EventTriggerNode node) {

	}

	@Override
	public void visit(FitStateAssertionNode node) {

	}

	@Override
	public void visit(FitNode node) {

	}

	@Override
	public void visit(SplitDataAsNameNode node) {

	}

	@Override
	public void visit(GenerateOrderNameNode node) {

	}

	@Override
	public void visit(FixNameNode node) {

	}

	@Override
	public void visit(FitAssertionNode node) {
		
	}

	@Override
	public void visit(SplitDataNode node) {
		
	}

	@Override
	public void visit(DynamicComponentNode node) {
		
	}
	
	@Override
	public void visit(IncludeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DynamicComponentEventNode node) {
		
	}

	@Override
	public void visit(SystemNode node) {
		// TODO Auto-generated method stub
		
	}
}
