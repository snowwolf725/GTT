package testing.gttlipse.testScript;

import junit.framework.TestCase;
import gttlipse.TestProject;
import gttlipse.scriptEditor.testScript.InvisibleRootNode;
import gttlipse.scriptEditor.testScript.ProjectNode;

public class InvisibleRootNodeTest extends TestCase {
	private InvisibleRootNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = TestProject.getInstance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddChild() {
		ProjectNode suitnode = new ProjectNode("TestSuitNode");
		node.addChild(suitnode);

		assertEquals("(not set)", node.getChildrenAt(0).getName());
	}

}
