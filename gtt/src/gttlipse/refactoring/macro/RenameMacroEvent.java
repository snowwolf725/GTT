package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.node.FitNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.EventReferenceUpdate;

import java.util.Iterator;


public class RenameMacroEvent {
	private AbstractMacroNode _root = null;

	public RenameMacroEvent(AbstractMacroNode root) {
		_root = root;
	}

	public boolean isValid(MacroEventNode event, String name) {
		if (name.equals(""))
			return false;

		MacroComponentNode macroCom = (MacroComponentNode) event.getParent();

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

	public void rename(MacroEventNode event, String name) {
		String oldPath = event.getPath().toString();
		event.setName(name);
		String newPath = event.getPath().toString();

		updateReference(oldPath, newPath);
	}

	private void updateReference(String oldPath, String newPath) {
		AbstractReferenceUpdate replacer = new EventReferenceUpdate(_root);
		replacer.replace(oldPath, newPath);
	}
}
