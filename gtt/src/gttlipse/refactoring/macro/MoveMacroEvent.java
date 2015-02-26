package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.EventReferenceUpdate;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


public class MoveMacroEvent {
	private AbstractMacroNode _root = null;
	private Vector<String> _oldPath = null;
	private Vector<String> _newPath = null;
	
	public MoveMacroEvent(AbstractMacroNode root) {
		_root = root;
	}
	
	public boolean isValid(List<AbstractMacroNode> events, AbstractMacroNode target) {
		// check all macro event
		Iterator<AbstractMacroNode> iter = events.iterator();
		while(iter.hasNext()) {
			if((iter.next() instanceof MacroEventNode) == false) {
				return false;
			}
		}
		
		// check not same name
		Iterator<AbstractMacroNode> parentIte = target.iterator();
		while(parentIte.hasNext()) {
			AbstractMacroNode check = parentIte.next();
			Iterator<AbstractMacroNode> ite = events.iterator();
			while(ite.hasNext()) {
				if(check.getName().equals(ite.next().getName())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Vector<String> getOldPath() {
		return _oldPath;
	}
	
	public Vector<String> getNewPath() {
		return _newPath;
	}
	
	public void move(List<AbstractMacroNode> events, AbstractMacroNode target) {
		// handle path and add event to new parent
		_oldPath = new Vector<String>();
		_newPath = new Vector<String>();
		
		AbstractReferenceUpdate replacer = new EventReferenceUpdate(_root);
		Iterator<AbstractMacroNode> ite = events.iterator();
		while(ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			// record old path
			String oldPath = node.getPath().toString();
			_oldPath.add(oldPath);
			//new path
			target.add(node);
			String newPath = node.getPath().toString();
			_newPath.add(newPath);
			replacer.replace(oldPath, newPath);
		}		
		
		if(_oldPath.size() != _newPath.size()) {
			System.out.println("Move Macro Event path size wrong.");
		}
	}
	
	public void move(AbstractMacroNode event, AbstractMacroNode target) {
		// handle path and add event to new parent
		_oldPath = new Vector<String>();
		_newPath = new Vector<String>();
		
		AbstractReferenceUpdate replacer = new EventReferenceUpdate(_root);
		
		// record old path
		String oldPath = event.getPath().toString();
		_oldPath.add(oldPath);
		//new path
		target.add(event);
		String newPath = event.getPath().toString();
		_newPath.add(newPath);
		replacer.replace(oldPath, newPath);
		
		if(_oldPath.size() != _newPath.size()) {
			System.out.println("Move Macro Event path size wrong.");
		}
	}
}
