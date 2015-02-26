package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.ComponentReferenceUpdate;

import java.util.Iterator;


public class RenameComponent {
	private AbstractMacroNode _root = null;

	public RenameComponent(AbstractMacroNode root) {
		_root = root;
	}

	public boolean isValid(ComponentNode component, String name) {
		if (name.equals(""))
			return false;

		MacroComponentNode macroCom = (MacroComponentNode) component
				.getParent();

		Iterator<AbstractMacroNode> ite = macroCom.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			if (node instanceof ComponentNode) {
				if (node.getName().equals(name))
					return false;
			}
		}
		return true;
	}

	public void rename(ComponentNode component, String name) {
		// record old path
		String oldpath = component.getPath().toString();
		// rename and record new path
		component.setName(name);
		String newpath = component.getPath().toString();
		// replace reference
		updateReferences(oldpath, newpath);
	}

	private void updateReferences(String oldpath, String newpath) {
		AbstractReferenceUpdate replacer = new ComponentReferenceUpdate(_root);
		replacer.replace(oldpath, newpath);
	}
}
