package gttlipse.refactoring.util;

import gtt.macro.macroStructure.AbstractMacroNode;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

public abstract class AbstractReferenceUpdate extends MacroRefactorVisitor {
	protected String _oldPath = "";
	protected String _newPath = "";
	protected AbstractMacroNode _root = null;

	public AbstractReferenceUpdate() {
	}

	public AbstractReferenceUpdate(AbstractMacroNode root) {
		_root = root;
	}

	final public void replace(String oldPath, String newPath) {
		if(_root == null)
			return;
		if (oldPath == "")
			return;
		if (newPath == "")
			return;
		_oldPath = oldPath;
		_newPath = newPath;
		
		_root.accept(this);
	}

}
