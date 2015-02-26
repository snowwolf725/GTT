package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.EventReferenceUpdate;
import gttlipse.refactoring.util.MacroComponentReferenceUpdate;

public class RefactoringFacade {
	AbstractMacroNode m_Root;

	public RefactoringFacade(AbstractMacroNode root) {
		m_Root = root;
	}

	public boolean doRename(AbstractMacroNode node, String name) {

		if (node instanceof ComponentNode) {
			// for ComponentNode
			ComponentNode c = (ComponentNode) node;
			RenameComponent r = new RenameComponent(m_Root);
			if (r.isValid(c, name) == false)
				return false;
			r.rename(c, name);
			return true;
		}

		if (node instanceof MacroComponentNode) {
			// for ComponMacroComponentNodeentNode
			MacroComponentNode c = (MacroComponentNode) node;
			RenameMacroComponent r = new RenameMacroComponent(m_Root);
			if (r.isValid(c, name) == false)
				return false;
			r.rename(c, name);
			return true;
		}

		if (node instanceof MacroEventNode) {
			// for ComponMacroComponentNodeentNode
			MacroEventNode c = (MacroEventNode) node;
			RenameMacroEvent r = new RenameMacroEvent(m_Root);
			if (r.isValid(c, name) == false)
				return false;
			r.rename(c, name);
			return true;
		}

		return false;
	}

	public boolean updateReferencePath(String oldpath, String newpath) {
		AbstractReferenceUpdate replacer = new MacroComponentReferenceUpdate(m_Root);
		replacer.replace(oldpath, newpath);

		
		replacer = new EventReferenceUpdate(m_Root);
		replacer.replace(oldpath, newpath);
		return true;
		
//		if (node instanceof ComponentNode) {
//			// for ComponentNode
//			MoveComponent r = new MoveComponent(node, parent);
//			r.setRoot(m_Root);
//			r.moveComponent();
//			return true;
//		}
//
//		if (node instanceof MacroComponentNode) {
//			MoveMacroComponent r = new MoveMacroComponent(node, parent);
//			r.setRoot(m_Root);
//
//			r.moveMacroComponent();
//			return true;
//		}
//
//		if (node instanceof MacroEventNode) {
//			MoveMacroEvent r = new MoveMacroEvent(node, parent);
//			r.setRoot(m_Root);
//			
//			r.move();
//			return true;
//		}

//		return false;
	}

}
