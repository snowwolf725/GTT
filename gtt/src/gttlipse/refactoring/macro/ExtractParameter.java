package gttlipse.refactoring.macro;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.refactoring.util.ArgumentUpdate;

public class ExtractParameter {
	private AbstractMacroNode _root = null;
	
	private static final String DYNAMIC_VALUE = "Dynamic Value";
	private static final String ASSERT_VALUE = "Assert Value";
	
	public ExtractParameter(AbstractMacroNode root) {
		_root = root;
	}
	
	public void extractParameter(AbstractMacroNode node, Arguments args) {
		// add to macro event arguments and clear value		
		Arguments eveArgs = ((MacroEventNode) node.getParent()).getArguments();
		for(int i = 0; i < args.size(); i++) {
			eveArgs.add(args.get(i).clone());
		}
		Arguments callerArgs = eveArgs.clone();
		
		// clear Event Arguments's value field
		for(int i = 0; i < eveArgs.size(); i++) {
			Argument arg = eveArgs.get(i);
			arg.setValue("");
		}
		
		// process node parameter
		processNode(node, args);

		// update reference arguments
		ArgumentUpdate updater = new ArgumentUpdate(_root);
		updater.updateArgument(callerArgs, ((MacroEventNode) node.getParent()).getPath().toString());
	}
	
	public boolean isValid(Arguments args, AbstractMacroNode node) {
		// value is not empty
		for(int i = 0; i < args.size(); i++) {
			if(args.get(i).getValue().equals("")) {
				return false;
			}
		}
		// argument name unique
		AbstractMacroNode event = node.getParent();
		Arguments eveArgs = ((IHaveArgument) event).getArguments();
		for(int i = 0; i < args.size(); i++) {
			Argument arg = eveArgs.find(args.get(i).getName());
			if(arg != null) {
				return false;
			}
		}
		return true;
	}
	
	private void processNode(AbstractMacroNode node, Arguments argsList) {
		if(node instanceof IHaveArgument) {
			// arguments
			Arguments args = ((IHaveArgument) node).getArguments();
			processEventArguments(args, argsList);
		}
		if(node instanceof ComponentEventNode) {
			// dynamic value
			if(checkValueUsable(((ComponentEventNode) node).getDyValue()) == true) {
				((ComponentEventNode) node).setDyValue("@" + DYNAMIC_VALUE);
			}
		}
		if(node instanceof ViewAssertNode) {
			// dynamic value
			if(checkValueUsable(((ViewAssertNode) node).getDyValue()) == true) {
				((ViewAssertNode) node).setDyValue("@" + DYNAMIC_VALUE);
			}
			// expect value
			if(checkValueUsable(((ViewAssertNode) node).getAssertion().getValue()) == true) {
				((ViewAssertNode) node).getAssertion().setValue("@" + ASSERT_VALUE);
			}
		}
	}
	
	private void processEventArguments(Arguments target, Arguments source) {
		// find same name argument
		for(int i = 0; i < target.size(); i++) {
			Argument arg = source.find(target.get(i).getName());
			if(arg != null) {
				target.get(i).setValue("@" + arg.getName());
			}
		}
	}
	
	public Arguments getArguments(AbstractMacroNode node) {
		Arguments extractArgs = new Arguments();
		Arguments eventArgs = null;
		if(node instanceof IHaveArgument) {
			eventArgs = ((IHaveArgument) node).getArguments();
			filterAllArgument(extractArgs, eventArgs);
		}
		if(node instanceof ViewAssertNode) {
			// add dynamic value
			String value = ((ViewAssertNode) node).getDyValue();
			addArgument(value, extractArgs, DYNAMIC_VALUE);
			// add expect value
			value = ((ViewAssertNode) node).getAssertion().getValue();
			addArgument(value, extractArgs, ASSERT_VALUE);
		}
		if(node instanceof ComponentEventNode) {
			// add dynamic value
			String value = ((ComponentEventNode) node).getDyValue();
			addArgument(value, extractArgs, DYNAMIC_VALUE);
		}
		
		return extractArgs;
	}
	
	private void filterAllArgument(Arguments target, Arguments source) {
		for(int i = 0; i < source.size(); i++) {
			// ignore parameter
			if(checkValueUsable(source.get(i).getValue()) == true) {				
				target.add(source.get(i).clone());
			}
		}
	}
	
	private void addArgument(String value, Arguments args, String name) {
		// do not add empty value and parameter
		if(checkValueUsable(value) == true) {
			Argument arg = Argument.create("String", name, value);
			args.add(arg);
		}
	}
	
	private boolean checkValueUsable(String value) {
		if(value.equals("") == false && value.indexOf("@") != 0)
			return true;
		return false;
	}
}
