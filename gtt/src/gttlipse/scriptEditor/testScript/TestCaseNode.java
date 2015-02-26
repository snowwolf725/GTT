/**
 * 
 */
package gttlipse.scriptEditor.testScript;

import gttlipse.scriptEditor.testScript.visitor.IGTTlipseScriptVisitor;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.TestScript
 * 
 */
public class TestCaseNode extends CompositeNode {

	public TestCaseNode() {
		this("TestCaseNode");
	}

	public TestCaseNode(String name) {
		super();
		this.setName(name);
	}

	public void addChild(TestMethodNode child) {
		_children.add(child);
		child.setParent(this);
	}

	public String addNewTestMethod() {
		TestMethodNode child = new TestMethodNode();
		child.setName(nextNewTestMethodName());
		addChild(child);
		return child.getName();
	}

	private static final String PREFIX_NAME = "testMethod";

	public String nextNewTestMethodName() {
		return genSerialName(PREFIX_NAME);
	}

	public TestCaseNode clone() {
		TestCaseNode node = new TestCaseNode(m_nodeName);
		
		if (size() == 0)
			return node;

		node.setChildren(getChildren());
		return node;
	}

	public void accept(IGTTlipseScriptVisitor v) {
		v.visit(this);
	}

}
