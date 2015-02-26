package gttlipse.refactoring.util;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

public class RemoveMiddleManUpdate extends MacroRefactorVisitor {
	private AbstractMacroNode _root = null;
	private String _path = "";
	private AbstractMacroNode _caller = null;
	private AbstractMacroNode _middleMan = null;
	
	public RemoveMiddleManUpdate(AbstractMacroNode root) {
		_root = root;
	}
	
	public void update(AbstractMacroNode caller, AbstractMacroNode middleMan, String path) {
		_caller = caller;
		_middleMan = middleMan;
		_path = path;
		_root.accept(this);
	}
	
	@Override
	public void visit(MacroEventCallerNode node) {
		// check path and replace the old reference
		if(node.getReferencePath().equals(_path)) {
			int index = node.getParent().indexOf(node);
			// handle parameter
			AbstractMacroNode target = handleParameter(node);
			node.getParent().add(target.clone(), index);
			node.getParent().remove(node);
		}		
	}
	
	private AbstractMacroNode handleParameter(AbstractMacroNode node) {		
		Arguments middleManList = ((IHaveArgument) _middleMan).getArguments();		
		// no argument
		if(middleManList.size() == 0) {
			return _caller;
		}
		Arguments list = ((IHaveArgument) node).getArguments();		
		AbstractMacroNode cloneNode = _caller.clone();
		Arguments callerList = ((IHaveArgument) cloneNode).getArguments();
		
		for(int i = 0; i < callerList.size(); i++) {
			Argument arg = checkUseParameter(callerList.get(i).getValue(), middleManList);
			if(arg != null) {
				callerList.get(i).setValue(list.getValue(arg.getName()));
			}
		}
		
		((IHaveArgument) cloneNode).setArguments(callerList);
		return cloneNode;
	}
	
	private Argument checkUseParameter(String value, Arguments args) {
		if(value.indexOf("@") == 0) {
			String parameter = value.substring(1);
			Argument arg = args.find(parameter);
			if(arg != null)
				return arg;
		}		
		return null;
	}
}
