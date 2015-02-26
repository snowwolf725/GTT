package gttlipse.refactoring.macro;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.refactoring.util.ArgumentUpdate;

import java.util.Iterator;


public class InlineParameter {
	private AbstractMacroNode _root = null;
	
	public InlineParameter(AbstractMacroNode root) {
		_root = root;
	}
	
	public boolean isValid(Arguments args) {
		// value is not empty
		for(int i = 0; i < args.size(); i++) {
			if(args.get(i).getValue().equals("")) {
				return false;
			}
		}		
		return true;
	}
	
	public void inlineParameter(AbstractMacroNode event, Arguments args) {
		Arguments eventArgs = ((MacroEventNode) event).getArguments();
		removeEventArgument(eventArgs, args);
		
		processChild(event, args);
		
		// update reference arguments
		ArgumentUpdate updater = new ArgumentUpdate(_root);
		updater.updateArgument(eventArgs.clone(), event.getPath().toString());
	}
	
	private void removeEventArgument(Arguments eventArgs, Arguments args) {
		for(int i = 0; i < eventArgs.size(); i++) {
			for(int j = 0; j < args.size(); j++) {
				if(eventArgs.get(i).getName().equals(args.get(j).getName())) {
					eventArgs.remove(i);
					i--;
					break;
				}
			}
		}
	}
	
	private void processChild(AbstractMacroNode event, Arguments args) {
		Iterator<AbstractMacroNode> iter = event.iterator();
		while(iter.hasNext()) {
			AbstractMacroNode node = iter.next();
			if(node instanceof IHaveArgument) {
				processArguments(((IHaveArgument) node).getArguments(), args);
			}
			if(node instanceof ViewAssertNode) {
				// dynamic value
				String value = getActualValue(((ViewAssertNode) node).getDyValue(), args);
				((ViewAssertNode) node).setDyValue(value);
				// expect value
				value = getActualValue(((ViewAssertNode) node).getAssertion().getValue(), args);
				((ViewAssertNode) node).getAssertion().setValue(value);
			}
			if(node instanceof ComponentEventNode) {
				// dynamic value
				String value = getActualValue(((ComponentEventNode) node).getDyValue(), args);
				((ComponentEventNode) node).setDyValue(value);
			}
		}
	}
	
	private void processArguments(Arguments target, Arguments source) {
		for(int i = 0; i < target.size(); i++) {
			String value = getActualValue(target.get(i).getValue(), source);
			target.setValue(i, value);
		}
	}
	
	private String getActualValue(String value, Arguments source) {
		if(value.indexOf("@") == 0) {
			String parameter = value.substring(1);
			Argument arg = source.find(parameter);
			if(arg != null) {
				value = arg.getValue();
			}
		}
		
		return value;
	}
}
