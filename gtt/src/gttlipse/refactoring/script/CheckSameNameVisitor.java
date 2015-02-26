package gttlipse.refactoring.script;

import gtt.eventmodel.IComponent;
import gtt.testscript.EventNode;
import gtt.testscript.ViewAssertNode;

public class CheckSameNameVisitor extends GTTlipseScriptVisitor {
	private IComponent _component = null;
	private boolean _sameName = false;
	private String _oldName = "";
	
	public CheckSameNameVisitor() {}
	
	public CheckSameNameVisitor(IComponent component) {
		_component = component;
	}
	
	public void setComponet(IComponent component) {
		_component = component;
	}
	
	public boolean isSameName() {
		return _sameName;
	}
	
	public void setOldName(String name) {
		_oldName = name;
	}

	@Override
	public void visit(EventNode node) {
		IComponent component = node.getComponent();
		
		if(_component.equals(component)) {
			if(_component.getName().equals(_oldName) == false)
				_sameName = true;
		}
	}

	@Override
	public void visit(ViewAssertNode node) {
		IComponent component = node.getComponent();
		
		if(_component.equals(component)) {
			if(_component.getName().equals(_oldName) == false)
				_sameName = true;
		}
	}
}
