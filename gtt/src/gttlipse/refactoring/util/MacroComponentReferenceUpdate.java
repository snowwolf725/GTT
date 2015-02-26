package gttlipse.refactoring.util;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.fit.node.FitNode;

public class MacroComponentReferenceUpdate extends AbstractReferenceUpdate {
	public MacroComponentReferenceUpdate(AbstractMacroNode root) {
		super(root);
	}
	
//	public MacroComponentReferenceUpdate(String oldPath, String newPath) {
//		super(oldPath, newPath);
//	}
//	
	private String replace(String original, String pattern, String replace) {
	    int start = 0;
	    int index = 0;
	    StringBuffer result = new StringBuffer();
	    
	    // replace first pattern
    	result.append(original.substring(start, index));
    	result.append(replace);
    	start = index + pattern.length();    	
	    result.append(original.substring(start));
	    
	    return result.toString();
	}

	@Override
	public void visit(MacroEventCallerNode node) {
		String path = node.getReferencePath();
		if(path.startsWith(_oldPath)) {
			if(path.length() > _oldPath.length()) {
				// replace reference
				path = replace(path, _oldPath, _newPath);
				node.setReferencePath(path);
			}
		}
	}

	@Override
	public void visit(FitNode node) {
		MacroEventCallerNode caller = node.getMacroEventCallerNode();
		caller.accept(this);
	}

	@Override
	public void visit(ComponentEventNode node) {
		String path = node.getComponentPath();
		if(path.startsWith(_oldPath)) {
			if(path.length() > _oldPath.length()) {
				// replace reference
				path = replace(path, _oldPath, _newPath);
				node.setComponentPath(path);
			}
		}
	}

	@Override
	public void visit(ViewAssertNode node) {
		String path = node.getComponentPath();
		if(path.startsWith(_oldPath)) {
			if(path.length() > _oldPath.length()) {
				// replace reference
				path = replace(path, _oldPath, _newPath);
				node.setComponentPath(path);
			}
		}
	}

}
