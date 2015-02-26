package testing.gttlipse.testScript;

import junit.framework.TestCase;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

public class ClassNodeTest extends TestCase {
	private TestCaseNode classnode;

	protected void setUp() throws Exception {
		super.setUp();
		classnode=new TestCaseNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'GTTlipse.TestScript.TestCaseNode.clean()'
	 */
	public void testClean() {
		TestMethodNode method=new TestMethodNode();
		classnode.addChild(method);
		assertEquals(1,classnode.size());
		classnode.clearChildren();
		assertEquals(0,classnode.size());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.TestCaseNode.addChild(TestMethodNode)'
	 */
	public void testAddChildTestMethodNode() {
		TestMethodNode method=new TestMethodNode();
		classnode.addChild(method);
		assertEquals(1,classnode.size());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.TestCaseNode.addNewNode()'
	 */
	public void testAddNewNode() {
		classnode.addNewTestMethod();
		assertEquals(1,classnode.size());
	}
	
	public void testClone() {
		TestCaseNode classnode1 = classnode.clone();
		assertEquals("TestCaseNode",classnode.getName());
		assertNotSame(classnode,classnode1);
	}

}
