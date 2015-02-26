package gttlipse.vfsmCoverageAnalyser.model;

import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.ProjectNode;

public class EventSetInvisibleRootNode extends CompositeNode{
	
	public void addChild(ProjectNode child) {
		if (_children.size() > 0)
			return; 
		_children.add(child);
		child.setParent(this);
	}
}
