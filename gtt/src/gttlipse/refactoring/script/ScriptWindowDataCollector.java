package gttlipse.refactoring.script;

import gtt.eventmodel.IComponent;
import gtt.testscript.EventNode;
import gtt.testscript.ViewAssertNode;
import gttlipse.refactoring.util.ComponentWindow;

public class ScriptWindowDataCollector extends GTTlipseScriptVisitor {
	private ComponentWindow _window = null;
	
	public ScriptWindowDataCollector() {
		_window = new ComponentWindow();
	}
	
	public ComponentWindow getComponentWindow() {
		return _window;
	}
	
	@Override
	public void visit(EventNode node) {
		IComponent com = node.getComponent();
		_window.addWindowData(com.getTitle(), com.getWinType());
	}

	@Override
	public void visit(ViewAssertNode node) {
		IComponent com = node.getComponent();
		_window.addWindowData(com.getTitle(), com.getWinType());
	}
}
