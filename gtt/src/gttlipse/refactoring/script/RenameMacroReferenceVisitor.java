package gttlipse.refactoring.script;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.testscript.ReferenceMacroEventNode;
import gttlipse.fit.node.ReferenceFitNode;

public class RenameMacroReferenceVisitor extends GTTlipseScriptVisitor {
	private String _oldPath = "";
	private String _newPath = "";
	private boolean _isFull = true;
	
	public RenameMacroReferenceVisitor(AbstractMacroNode node, String name) {
		handlePath(node, name);
	}	
	
	public void setIsFullPath(boolean isFull) {
		_isFull = isFull;
	}
	
	private void handlePath(AbstractMacroNode node, String name) {
		String oldName = node.getName();
		_oldPath = node.getPath().toString();
		node.setName(name);
		_newPath = node.getPath().toString();
		node.setName(oldName);
	}

	@Override
	public void visit(ReferenceMacroEventNode node) {
		if(node.getRefPath().startsWith(_oldPath)) {
			// avoid change macro component also change macro event
			if(_isFull == false) {
				if((node.getRefPath().length() != _oldPath.length())) {
					String path = replace(node.getRefPath(), _oldPath, _newPath);
					node.setRefPath(path);
				}
			}
			else {
				if(node.getRefPath().length() == _oldPath.length()) {
					String path = replace(node.getRefPath(), _oldPath, _newPath);
					node.setRefPath(path);
				}
			}
		}
	}
	
	@Override
	public void visit(ReferenceFitNode node) {
		if(node.getReferencePath().startsWith(_oldPath)) {
			// avoid change macro component also change fit node
			if(_isFull == false) {
				if((node.getReferencePath().length() != _oldPath.length())) {
					String path = replace(node.getReferencePath(), _oldPath, _newPath);
					node.setRefPath(path);
				}
			}
			else {
				if(node.getReferencePath().length() == _oldPath.length()) {
					String path = replace(node.getReferencePath(), _oldPath, _newPath);
					node.setRefPath(path);
				}
			}
		}
	}
	
	public String replace(String original, String pattern, String replace) {
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
}
