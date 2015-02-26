package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.node.FitNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.MacroComponentReferenceUpdate;

import java.util.Iterator;


public class RenameMacroComponent {
	private AbstractMacroNode _root = null;

	public RenameMacroComponent(AbstractMacroNode root) {
		_root = root;
	}

	public boolean isValid(MacroComponentNode comp, String name) {
		if (name.equals(""))
			return false;
		if (_root == comp)
			return true;

		MacroComponentNode macroCom = (MacroComponentNode) comp.getParent();

		Iterator<AbstractMacroNode> ite = macroCom.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			if (node instanceof FitNode || node instanceof MacroComponentNode
					|| node instanceof MacroEventNode) {
				if (node.getName().equals(name))
					return false;
			}
		}
		return true;
	}

	public void rename(MacroComponentNode comp, String name) {
		// rename and record new path
		String oldPath = comp.getPath().toString();
		comp.setName(name);
		String newPath = comp.getPath().toString();

		// replace reference
		updateReferences(oldPath, newPath);
	}

	private void updateReferences(String oldPath, String newPath) {
		AbstractReferenceUpdate replacer = new MacroComponentReferenceUpdate(
				_root);
		replacer.replace(oldPath, newPath);
	}
}
