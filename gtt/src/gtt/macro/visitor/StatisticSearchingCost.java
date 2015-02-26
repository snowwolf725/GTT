package gtt.macro.visitor;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

public class StatisticSearchingCost extends MacroRefactorVisitor {
	private double m_totalSearchingCost = 0;
	
	private double m_CountComponentNode = 0;
	
	private AbstractMacroNode m_root = null;

	@Override
	public void visit(ComponentNode node) {
		super.visit(node);
		m_CountComponentNode++;
		AbstractMacroNode parent = node.getParent();
		do {
			if(parent instanceof InvisibleRootNode)
				return;
			m_totalSearchingCost += ((double)parent.size()) / 2.0;
			if(parent == m_root)
				return;
			parent = parent.getParent();
		} while(parent != null);
	}

	public String getResult() {
		return	"The number of component nodes:\t"	+ m_CountComponentNode +	"\n"	+
			 	"Total Searching Cost:\t\t" 	 	+ m_totalSearchingCost +	"\n"	+
				"Average Searching Cost:\t\t"		+ m_totalSearchingCost/m_CountComponentNode;
	}
	
	public void setRoot(AbstractMacroNode root) {
		m_root = root;
	}
}
