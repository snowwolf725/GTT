package gttlipse.refactoring.macro;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gttlipse.refactoring.util.ArgumentUpdate;

import java.util.Iterator;


public class EditMacroEventParameter {
	private AbstractMacroNode _root = null;
	
	public EditMacroEventParameter(AbstractMacroNode node) {
		_root = node;
	}
	
	public boolean isValid(Arguments oldArgs, Arguments newArgs) {
		if(oldArgs.size() > newArgs.size()) {
			return true;			
		}
		
		// check duplicate name
		for(int i = oldArgs.size(); i < newArgs.size(); i++) {
			if(oldArgs.find(newArgs.get(i).getName()) != null) {
				return false;
			}
		}
		return true;
	}
	
	public void editParameter(AbstractMacroNode event, Arguments argList) {
		String path = event.getPath().toString();
		checkEventParameter(event, argList);
		ArgumentUpdate updater = new ArgumentUpdate(_root);
		updater.updateArgument(argList, path);
	}
	
	private void checkEventParameter(AbstractMacroNode event, Arguments argList) {
		Arguments list = ((IHaveArgument) event).getArguments();
		Arguments cloneList = argList.clone();
		Iterator<Argument> ite = cloneList.iterator();
		while(ite.hasNext()) {
			Argument arg = ite.next();
			Argument a = list.find(arg.getName());
			if(a == null) {
				arg.setValue("");
			}
		}
		((IHaveArgument) event).setArguments(cloneList);
	}
}
