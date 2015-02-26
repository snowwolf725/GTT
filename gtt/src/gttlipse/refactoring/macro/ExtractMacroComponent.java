package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroPath;
import gttlipse.fit.node.FitNode;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class ExtractMacroComponent {
	private AbstractMacroNode _root = null;
	private Vector<String> _oldPath = null;
	private Vector<String> _newPath = null;
	
	public ExtractMacroComponent(AbstractMacroNode root) {
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
	
	public boolean isValid(List<AbstractMacroNode> nodes, AbstractMacroNode target, String name) {
		if(nodes.size() == 0) {
			return false;
		}
			
		AbstractMacroNode parent = nodes.get(0).getParent();
		// check not at sub macro component
		AbstractMacroNode tarParent = target.getParent();
		while(tarParent != null) {
			if(tarParent == parent) {
				return false;
			}
			tarParent = tarParent.getParent();
		}
		
		AbstractMacroNode n = null;
		Iterator<AbstractMacroNode> ite = nodes.iterator();
		n = ite.next().getParent();
		
		// check same parent
		while(ite.hasNext()) {
			if(n != ite.next().getParent()) {
				return false;
			}
		}
		
		// check not duplicate name
		for(int i = 0; i < target.size(); i++) {
			if(target.get(i).getName().equals(name)) {
				return false;
			}
		}		
		return true;
	}
	
	public void extractMacroComponent(List<AbstractMacroNode> nodes, AbstractMacroNode target, String name) {
		// initial new parent
		MacroComponentNode newParent = MacroComponentNode.create(name);
		target.add(newParent);
		
		// handle and move nodes
		List<String> path = newParent.getPath().list();
		Iterator<AbstractMacroNode> ite = nodes.iterator();
		while(ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			if(node instanceof MacroEventNode) {
				handleMacroEvent(node, newParent);
			}
			if(node instanceof MacroComponentNode) {
				handleMacroComponent(node, newParent);
			}
			if(node instanceof ComponentNode) {
				handleComponent(node, newParent);
			}
			if(node instanceof FitNode) {
				handleFitNode(node, path);
				newParent.add(node);
			}
		}
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
	
	private void handleComponent(AbstractMacroNode node, AbstractMacroNode target) {
		// move component
		MoveComponent refactor = new MoveComponent(_root);
		refactor.moveComponent(node, target);
	}
	
	private void handleMacroComponent(AbstractMacroNode node, AbstractMacroNode target) {		
		// move macro component
		MoveMacroComponent refactor = new MoveMacroComponent(_root);
		refactor.moveMacroComponent(node, target);
		
		// set path
		setPath(refactor.getNewPath(), refactor.getOldPath());
	}
	
	private void handleFitNode(AbstractMacroNode node, List<String> path) {
		String spath = node.getPath().toString();
		spath = spath + MacroPath.PATH_SEPARATOR + node.getName();
		String fitPath = spath;
		
		String oldPath = node.getPath().toString();
		
		setPath(fitPath, oldPath);
	}

	private void setPath(String newPath, String oldPath) {
		_newPath.add(newPath);
		_oldPath.add(oldPath);
	}
}
