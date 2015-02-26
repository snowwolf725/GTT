package gttlipse.refactoring.script;

import gtt.testscript.ReferenceMacroEventNode;
import gttlipse.fit.node.ReferenceFitNode;

public class ReplaceMacroReferenceVisitor extends GTTlipseScriptVisitor {
	private String _oldPath = "";
	private String _newPath = "";
	
	public ReplaceMacroReferenceVisitor() {}
	
	public ReplaceMacroReferenceVisitor(String oldPath, String newPath) {
		_oldPath = oldPath;
		_newPath = newPath;
	}
	
	public void setOldPath(String oldPath) {
		_oldPath = oldPath;
	}
	
	public void setNewPath(String newPath) {
		_newPath = newPath;
	}
	
	@Override
	public void visit(ReferenceMacroEventNode node) {
		String refPath = node.getRefPath();
		if(refPath.startsWith(_oldPath)) {
			refPath = replace(refPath, _oldPath, _newPath);
			node.setRefPath(refPath);
		}
	}
	
	@Override
	public void visit(ReferenceFitNode node) {
		String refPath = node.getReferencePath();
		if(refPath.startsWith(_oldPath)) {
			refPath = replace(refPath, _oldPath, _newPath);
			node.setRefPath(refPath);
		}
	}
	
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
}
