package testing.gttlipse.testScript;

import java.util.List;

import junit.framework.TestCase;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.ProjectNode;

public class NodeTest extends TestCase {
	private CompositeNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node=new CompositeNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testAddChild() {
		CompositeNode newnode = new CompositeNode();
		node.addChild(newnode);
		assertEquals(node,newnode.getParent());
	}
	
	public void testCloneComposisteNode() {
		CompositeNode newnode = new CompositeNode();
		assertNull(newnode.clone());		
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.setName(String)'
	 */
	public void testSetName() {
		node.setName("test123");
		String value=node.getName();
		assertEquals("test123",value);
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.toString()'
	 */
	public void testToString() {
		node.setName("test123");
		String value=node.toString();
		assertEquals("test123",value);
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.setParent(Node)'
	 */
	public void testSetParent() {
		CompositeNode parent=new CompositeNode();
		parent.setName("test123");
		node.setParent(parent);
		assertEquals("test123",node.getParent().getName());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.removeChild(Node)'
	 */
	public void testRemoveChild() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		assertEquals(1,parent.size());
		parent.removeChild(node);
		assertEquals(0,parent.size());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.getChildrenCount()'
	 */
	public void testGetChildrenCount() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		assertEquals(1,parent.size());
		parent.removeChild(node);
		assertEquals(0,parent.size());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.getChildren()'
	 */
	public void testGetChildren() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		BaseNode nodes[]=parent.getChildren();
		assertEquals("test123",nodes[0].getName());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.getChildrenAt(int)'
	 */
	public void testGetChildrenAt() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		assertEquals("test123",parent.getChildrenAt(0).getName());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.getChildrenByName(String)'
	 */
	public void testGetChildrenByName() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		assertEquals("test123",parent.getChildrenByName("test123").getName());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.hasChildren()'
	 */
	public void testHasChildren() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		assertEquals(true,parent.hasChildren());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.moveChildrenFront(Node)'
	 */
	public void testMoveChildrenFront() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		CompositeNode node2=new CompositeNode();
		node2.setName("test456");
		parent.addChild(node2);
		assertEquals("test123",parent.getChildrenAt(0).getName());
		parent.moveChildFront(node2);
		assertEquals("test456",parent.getChildrenAt(0).getName());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.moveChildrenRear(Node)'
	 */
	public void testMoveChildrenRear() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		CompositeNode node2=new CompositeNode();
		node2.setName("test456");
		parent.addChild(node2);
		assertEquals("test123",parent.getChildrenAt(0).getName());
		parent.moveChildRear(node);
		assertEquals("test456",parent.getChildrenAt(0).getName());
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.checkName(String)'
	 */
	public void testCheckName() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		assertEquals(false,parent.checkName("test123"));
		parent.removeChild(node);
		assertEquals(true,parent.checkName("test1234"));
	}

	/*
	 * Test method for 'GTTlipse.TestScript.Node.clean()'
	 */
	public void testClean() {
		CompositeNode parent=new CompositeNode();
		node.setName("test123");
		parent.addChild(node);
		assertEquals(1,parent.size());
		parent.clearChildren();
		assertEquals(0,parent.size());
	}
	
	public void testGetFullPath() {
		ProjectNode project = new ProjectNode();
		String foldername = project.addNewSourceFolder();
		BaseNode anode = project.getChildrenByName(foldername);
		List<String> paths = anode.getFullPath();
		assertEquals(paths.toString(),"[SourceFolder1]");
	}

}
