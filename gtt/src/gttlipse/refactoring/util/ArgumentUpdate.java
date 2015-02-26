package gttlipse.refactoring.util;

import java.util.Iterator;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gttlipse.fit.node.FitNode;
import gttlipse.refactoring.macro.MacroRefactorVisitor;

public class ArgumentUpdate extends MacroRefactorVisitor {
	private AbstractMacroNode _root = null;
	private Arguments _argList = null;
	private String _path = "";
	
	public ArgumentUpdate(AbstractMacroNode root) {
		_root = root;
	}
	
	public void updateArgument(Arguments args, String path) {
		if(args == null)
			return;
		if(path == null || path.equals(""))
			return;
		_argList = args;
		_path = path;
		
		_root.accept(this);
	}
	
	@Override
	public void visit(MacroEventCallerNode node) {
		//match path
		if(node.getReferencePath().equals(_path)) {
			//edit parameter
			Arguments list = node.getArguments();
			Arguments cloneList = _argList.clone();
			Iterator<Argument> ite = cloneList.iterator();
			while(ite.hasNext()) {
				Argument arg = ite.next();
				Argument a = list.find(arg.getName());
				// type and name match then set value
				if(a != null) {
					arg.setValue(a.getValue());
				}
			}
			node.setArguments(cloneList);
		}
	}

	@Override
	public void visit(FitNode node) {
		MacroEventCallerNode caller = node.getMacroEventCallerNode();
		caller.accept(this);
	}
}
