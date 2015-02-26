package gttlipse.refactoring.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.util.RemoveMiddleManUpdate;

public class RemoveMiddleMan {
	private AbstractMacroNode _root = null;
	private String _path = "";
	private String _newPath = "";
	
	public RemoveMiddleMan(AbstractMacroNode root) {
		_root = root;
	}
	
	public String getOldPath() {
		return _path;
	}
	
	public String getNewPath() {
		return _newPath;
	}
	
	public boolean isValid(AbstractMacroNode node) {
		// middle man is macro event
		if((node instanceof MacroEventNode) == false) {
			return false;
		}
		
		// middle man only one child (caller)
		if(node.size() != 1) {
			return false;
		}
		else {
			// the child is macro event caller
			if((node.get(0) instanceof MacroEventCallerNode) == false) {
				return false;
			}
		}
		return true;
	}
	
	public void removeMiddleMan(AbstractMacroNode middleMan) {
		if(middleMan.size() == 0)
			return;
		AbstractMacroNode caller = middleMan.get(0);
		_newPath = ((MacroEventCallerNode) caller).getReferencePath();
		// handle path
		_path = middleMan.getPath().toString();
		// replace reference
		RemoveMiddleManUpdate updater = new RemoveMiddleManUpdate(_root);
		updater.update(caller, middleMan, _path);
		// remove middle man
		middleMan.getParent().remove(middleMan);
	}
}
