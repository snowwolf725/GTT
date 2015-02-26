package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.MacroComponentReferenceUpdate;

import java.util.Iterator;


public class MoveMacroComponent {
	private AbstractMacroNode _root = null;
	private String _oldPath = "";
	private String _newPath = "";
	
	public MoveMacroComponent(AbstractMacroNode root) {
		_root = root;
	}
	
	public String getOldPath() {
		return _oldPath;
	}
	
	public String getNewPath() {
		return _newPath;
	}
	
	public boolean isValid(AbstractMacroNode source, AbstractMacroNode target) {		
		// parent are not in sub macro component
		AbstractMacroNode parent = target;
		while(parent != null) {
			if(parent == source) {
				return false;
			}
			parent = parent.getParent();
		}
		
		// not exist same name
		Iterator<AbstractMacroNode> ite = target.iterator();
		while(ite.hasNext()) {
			if(ite.next().getName().equals(source.getName())) {
				return false;
			}				
		}		
		return true;
	}
	
	public void moveMacroComponent(AbstractMacroNode source, AbstractMacroNode target) {
		_oldPath = source.getPath().toString();
		// move to new parent
		target.add(source);
		_newPath = source.getPath().toString();
		// replace reference
		AbstractReferenceUpdate replacer = new MacroComponentReferenceUpdate(_root);
		replacer.replace(_oldPath, _newPath);
	}
}
