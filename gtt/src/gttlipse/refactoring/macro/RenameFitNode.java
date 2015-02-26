package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.node.FitNode;

import java.util.Iterator;


public class RenameFitNode {	
	public RenameFitNode () {}
	
	public boolean isValid(AbstractMacroNode node, String name) {
		if(name.equals(""))
			return false;
		
		AbstractMacroNode macroCom = node.getParent();
		
		Iterator<AbstractMacroNode> ite = macroCom.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode n = ite.next();
			if(n instanceof FitNode || n instanceof MacroComponentNode || n instanceof MacroEventNode) {
				if(n.getName().equals(name))
					return false;
			}			
		}
		return true;
	}
	
	public void rename(AbstractMacroNode node, String name) {
		node.setName(name);
	}
}
