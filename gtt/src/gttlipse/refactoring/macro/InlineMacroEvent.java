package gttlipse.refactoring.macro;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.ViewAssertNode;

import java.util.Iterator;

public class InlineMacroEvent {	
	public InlineMacroEvent() {}
	
	public boolean isValid() {
		return true;
	}
	
	public void inlineMacroEvent(AbstractMacroNode caller) {
		AbstractMacroNode event = ((MacroEventCallerNode) caller).getReference();
		AbstractMacroNode parent = caller.getParent();
		Arguments args = ((IHaveArgument) caller).getArguments();
		
		if(event != null) {
			// get caller index
			int index = parent.indexOf(caller);
			// remove caller from parent
			parent.remove(index);			
			// copy node from macro event to parent
			Iterator<AbstractMacroNode> ite = event.iterator();
			while(ite.hasNext()) {
				AbstractMacroNode node = ite.next().clone();
				// check argument
				checkArgument(node, args);
				// add to parent
				parent.add(node, index);
				index++;
			}			
		}
	}
	
	private void checkArgument(AbstractMacroNode node, Arguments args) {
		if(node instanceof IHaveArgument) {
			IHaveArgument argNode = (IHaveArgument) node;
			Arguments list = argNode.getArguments();
			
			for(int i = 0; i < list.size(); i++) {
				String value = list.get(i).getValue();
				String parameter = getArgumentsValue(value, args);
				list.get(i).setValue(parameter);
			}
			argNode.setArguments(list);
		}
		if(node instanceof ViewAssertNode) {
			// expect value
			String expectValue = ((ViewAssertNode) node).getAssertion().getValue();
			String parameter = getArgumentsValue(expectValue, args);
			((ViewAssertNode) node).getAssertion().setValue(parameter);
			// dynamic value
			String value = ((ViewAssertNode) node).getDyValue();
			parameter = getArgumentsValue(value, args);
			((ViewAssertNode) node).setDyValue(parameter);
		}
		if(node instanceof ComponentEventNode) {
			// dynamic value
			String value = ((ComponentEventNode) node).getDyValue();
			String parameter = getArgumentsValue(value, args);
			((ComponentEventNode) node).setDyValue(parameter);
		}
	}
	
	private String getArgumentsValue(String value, Arguments args) {
		if (value.indexOf("@") == 0) {
			String parameter = value.substring(1);
			Argument arg = args.find(parameter);
			if(arg == null) {
				System.out.println("Argument not found.");
			}
			else {
				return arg.getValue();
			}
		}
		return value;
	}
}
