/**
 * 
 */
package testing.gttlipse.testScript;

import junit.framework.TestCase;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.PackageNode;

/**
 * @author SnowWolf
 * 
 * created first in project testing.GTTlipse.TestScript
 * 
 */
public class TestPackageNodeTest extends TestCase {
	private PackageNode packagenode;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		packagenode = new PackageNode();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link GTTlipse.scriptEditor.TestScript.PackageNode#addChild(GTTlipse.scriptEditor.TestScript.Node)}.
	 */
	public void testAddChild() {
		packagenode.addChild(new TestCaseNode());
		assertEquals("TestCaseNode",packagenode.getChildrenAt(0).getName());
		packagenode.addChild(new PackageNode());
		assertEquals("TestPackageNode",packagenode.getChildrenAt(1).getName());
	}

	/**
	 * Test method for {@link GTTlipse.scriptEditor.TestScript.PackageNode#addNewTestCase()}.
	 */
	public void testAddNewCaseNode() {
		packagenode.addNewTestCase();
		assertEquals("TestCase1",packagenode.getChildrenAt(0).getName());
	}

	/**
	 * Test method for {@link GTTlipse.scriptEditor.TestScript.PackageNode#addNewPackage()}.
	 */
	public void testAddNewPackageNode() {
		packagenode.addNewPackage();
		assertEquals("TestPackageNode1",packagenode.getChildrenAt(0).getName());
	}

	/**
	 * Test method for {@link GTTlipse.scriptEditor.TestScript.PackageNode#clone()}.
	 */
	public void testClone() {
		PackageNode package1 = packagenode.clone();
		assertEquals("TestPackageNode",package1.getName());
		assertNotSame(packagenode,package1);
	}

}
