package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.ComponentReferenceUpdate;

import java.util.Iterator;
import java.util.List;


public class MoveComponent {
	private AbstractMacroNode _root = null;

	public MoveComponent(AbstractMacroNode root) {
		_root = root;
	}

	public boolean isValid(List<AbstractMacroNode> nodes, AbstractMacroNode target) {
		// check move to macro component
		if ((target instanceof MacroComponentNode) == false) {
			return false;
		}

		// check all component
		Iterator<AbstractMacroNode> iter = nodes.iterator();
		while (iter.hasNext()) {
			if ((iter.next() instanceof ComponentNode) == false) {
				return false;
			}
		}

		// check no same name exist
		Iterator<AbstractMacroNode> ite = target.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode check = ite.next();
			Iterator<AbstractMacroNode> nodeIte = nodes.iterator();
			while (nodeIte.hasNext()) {
				if (check.getName().equals(nodeIte.next().getName())) {
					return false;
				}
			}
		}
		return true;
	}

	public void moveComponent(List<AbstractMacroNode> nodes, AbstractMacroNode target) {
		Iterator<AbstractMacroNode> ite = nodes.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			moveComponent(node, target);
		}
	}
	
	public void moveComponent(AbstractMacroNode node, AbstractMacroNode target) {
		AbstractReferenceUpdate replacer = new ComponentReferenceUpdate(_root);
		
		// record old path
		String oldPath = node.getPath().toString();

		// move component
		target.add(node);
		
		// get new path
		String newPath = node.getPath().toString();
		
		// replace new path
		replacer.replace(oldPath, newPath);
	}
}
