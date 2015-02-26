package gttlipse.refactoring.macro;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.fit.node.FitNode;

public class RenameParameter extends MacroRefactorVisitor {
	private Arguments _argList = null;
	private String _path = "";
	private AbstractMacroNode _root = null;

	public RenameParameter(AbstractMacroNode root) {
		_root = root;
	}

	public void renameParameter(AbstractMacroNode event, Arguments args) {
		_path = event.getPath().toString();
		_argList = args;
		
		renameChildrenArgument(event);
		renameEventArgumentList((IHaveArgument) event);
		
		_root.accept(this);
	}

	private void renameChildrenArgument(AbstractMacroNode event) {
		Arguments list = ((IHaveArgument) event).getArguments();
		// find event's children
		for (int i = 0; i < event.size(); i++) {
			if (event.get(i) instanceof IHaveArgument) {
				IHaveArgument node = (IHaveArgument) event.get(i);
				Arguments tempList = node.getArguments();
				for (int j = 0; j < tempList.size(); j++) {
					String value = getNewParameterName(tempList.get(j).getValue(), list);
					tempList.setValue(j, value);					
				}
				node.setArguments(tempList);
			}
			if(event.get(i) instanceof ViewAssertNode) {
				// expect value
				ViewAssertNode asNode = (ViewAssertNode) event.get(i);
				String value = asNode.getAssertion().getValue();
				String parameter = getNewParameterName(value, list);
				asNode.getAssertion().setValue(parameter);
				// dynamic value
				value = asNode.getDyValue();
				parameter = getNewParameterName(value, list);
				asNode.setDyValue(parameter);
			}
			if(event.get(i) instanceof ComponentEventNode) {
				ComponentEventNode ceNode = (ComponentEventNode) event.get(i);
				// dynamic value
				String value = ceNode.getDyValue();
				String parameter = getNewParameterName(value, list);
				ceNode.setDyValue(parameter);
			}
		}
	}
	
	private String getNewParameterName(String value, Arguments list) {
		if (value.indexOf("@") == 0) {
			String parameter = value.substring(1);
			for(int i = 0; i < list.size(); i++) {
				if(list.get(i).getName().equals(parameter)) {
					return "@" + _argList.get(i).getName();
				}
			}
		}
		
		return value;
	}

	private void renameEventArgumentList(IHaveArgument node) {
		Arguments list = node.getArguments();

		for (int i = 0; i < list.size(); i++) {
			if (_argList.get(i).getName().equals(list.get(i).getName()) == false) {
				list.get(i).setName(_argList.get(i).getName());
			}
		}
		node.setArguments(list);
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		if (node.getReferencePath().equals(_path)) {
			renameEventArgumentList(node);
		}
	}

	@Override
	public void visit(FitNode node) {
		MacroEventCallerNode caller = node.getMacroEventCallerNode();
		caller.accept(this);
	}

}
