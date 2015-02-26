package gttlipse.refactoring.util;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ViewAssertNode;

public class ComponentReferenceUpdate extends AbstractReferenceUpdate {
	
	public ComponentReferenceUpdate(AbstractMacroNode root) {
		super(root);
	}

	@Override
	public void visit(ComponentEventNode node) {
		// full path matching
		if (node.getComponentPath().equals(_oldPath)) {
			node.setComponentPath(_newPath);
			return;
		}
	}

	@Override
	public void visit(ViewAssertNode node) {
		if (node.getComponentPath().equals(_oldPath)) {
			node.setComponentPath(_newPath);
		}
	}
}
