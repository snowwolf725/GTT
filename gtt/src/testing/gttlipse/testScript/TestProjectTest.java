package testing.gttlipse.testScript;

import junit.framework.TestCase;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.ProjectNode;

public class TestProjectTest extends TestCase {
	private ProjectNode projectnode;

	protected void setUp() throws Exception {
		super.setUp();
		projectnode=new ProjectNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testAddChild() {
		TestCaseNode classnode=new TestCaseNode();
		classnode.setName("test123");
		projectnode.addChild(classnode);
		assertEquals("test123",projectnode.getChildrenAt(0).getName());
	}

}
