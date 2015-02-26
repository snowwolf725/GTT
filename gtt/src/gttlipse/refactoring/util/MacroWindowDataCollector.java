package gttlipse.refactoring.util;

import gtt.eventmodel.IComponent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

public class MacroWindowDataCollector extends MacroRefactorVisitor {
	private ComponentWindow _window = null;
	private AbstractMacroNode _root = null;
	
	public MacroWindowDataCollector() {
		_window = new ComponentWindow();
	}
	
	public void setRoot(AbstractMacroNode root) {
		_root = root;
	}
	
	public void collect() {
		_root.accept(this);
	}
	
	public ComponentWindow getComponentWindow() {
		return _window;
	}
	
	@Override
	public void visit(ComponentNode node) {
		IComponent com = node.getComponent();
		_window.addWindowData(com.getTitle(), com.getWinType());
	}
}
