package gttlipse.refactoring.script;

import gtt.eventmodel.IComponent;
import gtt.testscript.EventNode;
import gtt.testscript.ViewAssertNode;

public class RefactoringRenameVisitor extends GTTlipseScriptVisitor {	
	private IComponent _renameComponent = null;
	private IComponent _component = null;
	
	public RefactoringRenameVisitor() {}
	
	public RefactoringRenameVisitor(IComponent component) {
		_component = component;
	}
	
	public void setComponent(IComponent component) {
		_component = component;
	}
	
	public void setRenameComponent(IComponent component) {
		_renameComponent = component;
	}
	
	@Override
	public void visit(EventNode node) {
		IComponent component = node.getComponent();
		
		if(_component.match(component)) {
			node.setComponent(copyInfo(node.getComponent()));
		}
	}

	@Override
	public void visit(ViewAssertNode node) {
		IComponent component = node.getComponent();
		
		if(_component.match(component)) {
			node.setComponent(copyInfo(node.getComponent()));
		}
	}
	
	private IComponent copyInfo(IComponent com) {
		IComponent newCom = com.clone();
		newCom.setName(_renameComponent.getName());
		newCom.setType(_renameComponent.getType());
		newCom.setWinType(_renameComponent.getWinType());
		
		return newCom;
	}
}
