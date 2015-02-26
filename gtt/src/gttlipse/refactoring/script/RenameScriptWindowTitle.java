package gttlipse.refactoring.script;

import gtt.eventmodel.IComponent;
import gtt.testscript.EventNode;
import gtt.testscript.ViewAssertNode;
import gttlipse.scriptEditor.testScript.BaseNode;

public class RenameScriptWindowTitle extends GTTlipseScriptVisitor {
	private String _title = "";
	private String _winType = "";
	private String _name = "";
	private BaseNode _root = null;
	
	public RenameScriptWindowTitle() {}
	
	public void setTitle(String title) {
		_title = title;
	}

	public void setWindowType(String winType) {
		_winType = winType;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setRoot(BaseNode root) {
		_root = root;
	}
	
	public void renameWindowTitle() {
		_root.accept(this);
	}
	
	@Override
	public void visit(EventNode node) {
		IComponent com = node.getComponent();
		renameTitle(com);
	}

	@Override
	public void visit(ViewAssertNode node) {
		IComponent com = node.getComponent();
		renameTitle(com);
	}
	
	private void renameTitle(IComponent com) {
		if(com.getTitle().equals(_title)) {
			if(com.getWinType().equals(_winType)) {
				com.setTitle(_name);
			}
		}
	}
}
