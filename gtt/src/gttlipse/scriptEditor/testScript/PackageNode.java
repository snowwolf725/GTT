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
 *         created first in project GTTlipse.TestScript
 * 
 */
public class PackageNode extends CompositeNode {

	final static String DEFAULT_NAME = "TestPackageNode";

	public PackageNode() {
		this(DEFAULT_NAME);
	}

	public PackageNode(String name) {
		super();
		this.setName(name);
	}

	public void addChild(BaseNode child) {
		if (!(child instanceof TestCaseNode) && !(child instanceof PackageNode))
			return;

		_children.add(child);
		child.setParent(this);
	}

	public String addNewTestCase() {
		TestCaseNode child = new TestCaseNode();
		child.setName(nextNewTestCaseName());
		addChild(child);
		return child.getName();
	}

	public String addNewPackage() {
		PackageNode child = new PackageNode();
		child.setName(nextNewPackageName());
		addChild(child);
		return child.getName();
	}

	public String nextNewPackageName() {
		return genSerialName("TestPackageNode");
	}

	public String nextNewTestCaseName() {
		return genSerialName("TestCase");
	}

	public BaseNode[] getTestMethods() {
		List<BaseNode> results = new ArrayList<BaseNode>();

		BaseNode[] classNodes = getChildren();

		for (int i = 0; i < classNodes.length; i++) {
			if (!(classNodes[i] instanceof TestCaseNode))
				continue;

			BaseNode[] methods = ((TestCaseNode) classNodes[i]).getChildren();
			for (int j = 0; j < methods.length; j++)
				results.add(methods[j]);
		}

		return (BaseNode[]) results.toArray(new BaseNode[results.size()]);
	}

	public PackageNode clone() {
		PackageNode newnode = new PackageNode(this.m_nodeName);
		if (_children.size() == 0)
			return newnode;

		newnode.setChildren(getChildren());
		return newnode;
	}

	public void accept(IGTTlipseScriptVisitor v) {
		v.visit(this);
	}
}
