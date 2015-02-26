package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;

public class RenameMacroWindowTitle extends MacroRefactorVisitor {
	private String _title = "";
	private String _winType = "";
	private String _name = "";
	private AbstractMacroNode _root = null;

	public RenameMacroWindowTitle(AbstractMacroNode root) {
		_root = root;
	}

	public void renameWindowTitle(String title, String winType, String name) {
		_title = title;
		_winType = winType;
		_name = name;
		_root.accept(this);
	}

	@Override
	public void visit(ComponentNode node) {
		if (!node.getTitle().equals(_title))
			return;
		if (!node.getWinType().equals(_winType))
			return;
		node.setTitle(_name);
	}
}
