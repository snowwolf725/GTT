/**
 * 
 */
package gttlipse.scriptEditor.testScript;

import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

import java.util.ArrayList;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.TestScript
 * 
 */
public class ProjectNode extends CompositeNode {

	final static String DEFAULT_NAME = "ProjectNode";

	public ProjectNode() {
		this(DEFAULT_NAME);
	}

	public ProjectNode(String name) {
		super();
		this.setName(name);
	}

	public String addNewSourceFolder() {
		return addNewSourceFolder(nextNewFolderName());
	}

	public String addNewSourceFolder(String folderName) {
		SourceFolderNode child = new SourceFolderNode();
		child.setName(folderName);
		addChild(child);
		return child.getName();
	}

	public String nextNewFolderName() {
		return genSerialName("SourceFolder");
	}

	public BaseNode[] getMethodNodes() {
		BaseNode[] sourceFolderNodes = this.getChildren();
		ArrayList<BaseNode> methodNodes = new ArrayList<BaseNode>();

		for (int i = 0; i < sourceFolderNodes.length; i++) {
			if (sourceFolderNodes[i] instanceof SourceFolderNode) {
				BaseNode[] tempBaseNode = ((SourceFolderNode) sourceFolderNodes[i])
						.getTestMethods();
				for (int j = 0; j < tempBaseNode.length; j++) {
					methodNodes.add(tempBaseNode[j]);
				}
			}
		}

		return (BaseNode[]) methodNodes.toArray(new BaseNode[_children.size()]);
	}

	public ProjectNode clone() {
		ProjectNode newnode = new ProjectNode(m_nodeName);

		if (_children.size() == 0)
			return newnode; // hasn't children

		// has children
		newnode.setChildren(getChildren());
		return newnode;
	}

	public void accept(IGTTlipseScriptVisitor v) {
		v.visit(this);
	}

}
