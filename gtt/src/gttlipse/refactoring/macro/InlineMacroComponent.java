package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroPath;
import gttlipse.fit.node.FitNode;

import java.util.Iterator;
import java.util.Vector;


public class InlineMacroComponent {
	private AbstractMacroNode _root = null;
	private Vector<String> _oldPath = null;
	private Vector<String> _newPath = null;
	
	public InlineMacroComponent(AbstractMacroNode root) {
		_root = root;
		_oldPath = new Vector<String>();
		_newPath = new Vector<String>();
	}
	
	public Vector<String> getOldPath() {
		return _oldPath;
	}
	
	public Vector<String> getNewPath() {
		return _newPath;
	}
	
	public boolean isValid(AbstractMacroNode source, AbstractMacroNode target) {
		Iterator<AbstractMacroNode> ite = target.iterator();
		Iterator<AbstractMacroNode> iter = source.iterator();
		
		// check target are not invisibleRoot
		if(target == null || (target instanceof InvisibleRootNode) == true) {
			return false;
		}
		
		// check source are not ancestry
		AbstractMacroNode n = target.getParent();
		while(n != null && (n instanceof InvisibleRootNode) == false) {
			if(n == source) {
				return false;
			}
			n = n.getParent();
		}
		
		// check not same name
		while(ite.hasNext()) {
			AbstractMacroNode child = ite.next();
			iter = source.iterator();
			while(iter.hasNext()) {
				if(child.getName().equals(iter.next().getName())) {
					return false;
				}
			}
		}		
		return true;
	}
	
	public void inlineMacroComponent(AbstractMacroNode source, AbstractMacroNode target) {
		// inline source to target
		while(source.size() != 0) {
			AbstractMacroNode node = source.get(0);
			if(node instanceof ComponentNode) {
				handleComponent(node, target);
			}
			if(node instanceof MacroEventNode) {
				handleMacroEvent(node, target);
			}
			if(node instanceof MacroComponentNode) {
				handleMacroComponent(node, target);
			}
			if(node instanceof FitNode) {
				handleFitNode(node, target);
			}	
		}
		// remove source
		source.getParent().remove(source);
	}
	
	private void handleComponent(AbstractMacroNode node, AbstractMacroNode target) {
		// move component
		MoveComponent refactor = new MoveComponent(_root);
		refactor.moveComponent(node, target);
	}
	
	private void handleMacroEvent(AbstractMacroNode node, AbstractMacroNode target) {
		// move event
		MoveMacroEvent refactor = new MoveMacroEvent(_root);
		refactor.move(node, target);
		
		// set path
		Vector<String> newPath = refactor.getNewPath();
		Vector<String> oldPath = refactor.getOldPath();
		for(int i = 0; i < oldPath.size(); i++) {
			setPath(newPath.get(i), oldPath.get(i));
		}
	}
	
	private void handleMacroComponent(AbstractMacroNode node, AbstractMacroNode target) {
		// move macro component
		MoveMacroComponent refactor = new MoveMacroComponent(_root);
		refactor.moveMacroComponent(node, target);
		
		// set path
		setPath(refactor.getNewPath(), refactor.getOldPath());
	}
	
	private void handleFitNode(AbstractMacroNode node, AbstractMacroNode target) {
		String path = target.getPath().toString();
		path = path + MacroPath.PATH_SEPARATOR + node.getName();
		String fitPath = path;
		String oldPath = node.getPath().toString();
		target.add(node);
		
		setPath(fitPath, oldPath);
	}
	
	private void setPath(String newPath, String oldPath) {
		_newPath.add(newPath);
		_oldPath.add(oldPath);
	}
}
