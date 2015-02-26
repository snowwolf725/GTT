package gttlipse.refactoring.macro;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ExtractMacroEvent {
	private Vector<String> _args = null;
	
	public ExtractMacroEvent() {}
	
	public boolean isValid(List<AbstractMacroNode> nodes, AbstractMacroNode target, String name) {
		if(nodes.size() == 0) {
			return false;
		}
		
		AbstractMacroNode node = null;
		Iterator<AbstractMacroNode> ite = nodes.iterator();
		node = ite.next().getParent();
		
		// check same parent
		while(ite.hasNext()) {
			if(node != ite.next().getParent()) {
				return false;
			}
		}
		
		// check continuous
		MacroEventNode event = (MacroEventNode) node;
		ite = nodes.iterator();
		AbstractMacroNode temp = ite.next();
		int index = event.indexOf(temp);
		while(ite.hasNext()) {
			if((index + 1) != event.indexOf(ite.next())) {
				return false;
			}
			index++;
		}
		
		for(int i = 0; i < target.size(); i++) {
			if(target.get(i).getName().equals(name)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void extractMacroEvent(List<AbstractMacroNode> nodes, String name) {
		// initial new parent
		AbstractMacroNode oldParent = nodes.get(0).getParent();
		AbstractMacroNode newParent = MacroEventNode.create(name);
		newParent.setParent(oldParent.getParent());
		oldParent.getParent().add(newParent);
		
		// check argument
		Arguments list = ((IHaveArgument) oldParent).getArguments();
		_args = new Vector<String>();
		Iterator<Argument> arg = list.iterator();
		while(arg.hasNext()) {
			_args.add(arg.next().getName());
		}
		list = checkArgument(nodes);
		((IHaveArgument) newParent).setArguments(list);
		
		// get first element index
		Iterator<AbstractMacroNode> ite = nodes.iterator();
		AbstractMacroNode firstNode = ite.next();
		int index = oldParent.indexOf(firstNode);
		
		// move nodes to new parent
		ite = nodes.iterator();
		while(ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			newParent.add(node);
		}		
		
		// add reference to old parent
		String path = newParent.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		oldParent.add(caller, index);
		Arguments cloneList = list.clone();
		for(int i = 0; i < cloneList.size(); i++) {
			String value = "@" + cloneList.get(i).getName();
			cloneList.get(i).setValue(value);
		}
		caller.setArguments(cloneList);
	}
	
	private Arguments checkArgument(List<AbstractMacroNode> nodes) {
		Arguments list = new Arguments();
		
		Iterator<AbstractMacroNode> ite = nodes.iterator();
		// find node use parameter or not
		while(ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			if(node instanceof IHaveArgument) {
				IHaveArgument argNode = (IHaveArgument) node;
				Arguments temp = argNode.getArguments();
				
				for(int i = 0; i < temp.size(); i++) {
					String value = temp.get(i).getValue();
					// check use parameter
					checkUseParameter(value, temp.get(i).getType(), list);
				}
			}
			// Check ViewAssertNode expect value and dynamic value
			if(node instanceof ViewAssertNode) {
				String expectValue = ((ViewAssertNode) node).getAssertion().getValue();
				// check use parameter
				checkUseParameter(expectValue, "String", list);
				String value = ((ViewAssertNode) node).getDyValue();
				// check use parameter
				checkUseParameter(value, "String", list);
			}
			// Check ComponentNode dynamic value
			if(node instanceof ComponentEventNode) {
				String value = ((ComponentEventNode) node).getDyValue();
				// check use parameter
				checkUseParameter(value, "String", list);
			}
		}		
		return list;
	}
	
	private boolean checkInArguments(String name) {
		for(int i = 0; i < _args.size(); i++) {
			if(_args.get(i).equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private void checkUseParameter(String name, String type, Arguments list) {
		if (name.indexOf("@") == 0) {
			String value = name.substring(1);
			if(checkInArguments(value)) {
				// if find, then do nothing.
				Argument arg = list.find(value);
				// when not exist
				if(arg == null) {
					arg = Argument.create(type, value, "");
					list.add(arg);
				}
			}
		}
	}
}
