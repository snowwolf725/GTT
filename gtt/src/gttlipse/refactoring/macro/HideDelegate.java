package gttlipse.refactoring.macro;

import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.util.AbstractReferenceUpdate;
import gttlipse.refactoring.util.EventReferenceUpdate;

public class HideDelegate {
	private AbstractMacroNode _root = null;
	
	public HideDelegate(AbstractMacroNode root) {
		_root = root;
	}
	
	public boolean isValid(AbstractMacroNode caller, String name) {
		// check is caller
		if((caller instanceof MacroEventCallerNode) == false) {
			return false;
		}
		
		AbstractMacroNode localRoot = caller.getParent().getParent();
		
		// check name not empty
		if(name.equals(""))
			return false;
		
		// check same name
		for(int i = 0; i < localRoot.size(); i++) {
			if(localRoot.get(i).getName().equals(name)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void hideDelegate(AbstractMacroNode caller, String name) {
		// check is caller
		if((caller instanceof MacroEventCallerNode) == false) {
			return;
		}
		
		AbstractMacroNode middleMan = new MacroEventNode(name);
		// add to LocalRoot
		caller.getParent().getParent().add(middleMan);
		AbstractMacroNode cloneCaller = caller.clone();
		
		// check arguments
		checkArguments(cloneCaller, middleMan);
		String oldPath = ((MacroEventCallerNode) caller).getReferencePath();
		String newPath = middleMan.getPath().toString();
		
		// change path
		AbstractReferenceUpdate updater = new EventReferenceUpdate(_root);
		updater.replace(oldPath, newPath);
		
		// add caller to middle man
		middleMan.add(cloneCaller);
	}
	
	private void checkArguments(AbstractMacroNode caller, AbstractMacroNode event) {
		Arguments args = ((MacroEventCallerNode) caller).getArguments().clone();
		for(int i = 0; i < args.size(); i++) {
			// clear all argument value
			args.get(i).setValue("");
		}
		// set to event
		((MacroEventNode) event).setArguments(args);
		args = ((MacroEventCallerNode) caller).getArguments();
		for(int i = 0; i < args.size(); i++) {
			// set all argument value as parameter
			args.get(i).setValue("@" + args.get(i).getName());
		}
	}
}
