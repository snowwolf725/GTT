package gttlipse.refactoring.util;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gttlipse.fit.node.FitNode;

public class EventReferenceUpdate extends AbstractReferenceUpdate {
	public EventReferenceUpdate(AbstractMacroNode root) {
		super(root);
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		if(node.getReferencePath().equals(_oldPath)) {
			// replace path
			node.setReferencePath(_newPath);
		}
	}

	@Override
	public void visit(FitNode node) {
		MacroEventCallerNode caller = node.getMacroEventCallerNode();
		caller.accept(this);
	}
}
