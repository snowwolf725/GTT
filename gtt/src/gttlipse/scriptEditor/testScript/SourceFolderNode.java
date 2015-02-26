/**
 * 
 */
package gttlipse.scriptEditor.testScript;

import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

import java.util.ArrayList;
import java.util.List;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.TestScript
 * 
 */
public class SourceFolderNode extends CompositeNode {

	private static String DEFAULT_NAME = "SourceFolder";

	public SourceFolderNode() {
		this(DEFAULT_NAME);
	}

	public SourceFolderNode(String name) {
		this.m_nodeName = name;
	}

	public void accept(IGTTlipseScriptVisitor v) {
		v.visit(this);
	}

	public String addNewPackageNode() {
		PackageNode child = new PackageNode();
		child.setName(nextNewPackageName());
		addChild(child);
		return child.getName();
	}

	public String addNewTestCase() {
		return addNewTestCase("Class");
	}

	public String addNewTestCase(String name) {
		TestCaseNode child = new TestCaseNode();
		child.setName(name);
		addChild(child);
		return child.getName();
	}

	public String nextNewPackageName() {
		return genSerialName("Package");
	}

	public String nextNewTestCaseName() {
		return genSerialName("Class");
	}

	public void addChild(BaseNode child) {
		if (!(child instanceof TestCaseNode) && !(child instanceof PackageNode))
			return;
		_children.add(child);
		child.setParent(this);
	}

	public BaseNode[] getTestMethods() {
		BaseNode[] Nodes = this.getChildren();
		List<BaseNode> methodNodes = new ArrayList<BaseNode>();

		for (int idx = 0; idx < Nodes.length; idx++) {
			if (Nodes[idx] instanceof PackageNode) {
				BaseNode[] tempBaseNode = ((PackageNode) Nodes[idx])
						.getTestMethods();
				for (int ct = 0; ct < tempBaseNode.length; ct++)
					methodNodes.add(tempBaseNode[ct]);
			}

			if (Nodes[idx] instanceof TestCaseNode) {
				BaseNode[] tempBaseNode = ((TestCaseNode) Nodes[idx])
						.getChildren();
				for (int ct = 0; ct < tempBaseNode.length; ct++)
					methodNodes.add(tempBaseNode[ct]);
			}
		}

		return (BaseNode[]) methodNodes.toArray(new BaseNode[_children.size()]);
	}

	public SourceFolderNode clone() {
		SourceFolderNode newnode = new SourceFolderNode(this.m_nodeName);
		if (_children.size() == 0)
			return newnode;

		newnode.setChildren(getChildren());
		return newnode;
	}
}
