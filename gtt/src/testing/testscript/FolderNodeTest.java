package testing.testscript;

import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import junit.framework.TestCase;

public class FolderNodeTest extends TestCase {

	FolderNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = (FolderNode) new NodeFactory().createFolderNode("folder");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}
	class MyMockVisitor extends MockVisitor {

		boolean m_hasVisit = false;

		public boolean hasVisit() {
			return m_hasVisit;
		}

		public void visit(FolderNode node) {
			m_hasVisit = true;
		}

	}

	public void testAccept() {
		MyMockVisitor mock_visitor = new MyMockVisitor();

		node.accept(mock_visitor);
		assertTrue(mock_visitor.hasVisit());
	}
	
	
	public void testAddAbstractNode() {
		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);

		assertFalse(node.add(null));

		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);

		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);

		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);

		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);
	}
	
	public void testAddAbstractNode_negative() {
		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);
		assertFalse(node.add(null));

		assertTrue(node.add(f.createFolderNode("1"),-1));
		assertTrue(node.add(f.createFolderNode("2"),-2));
		assertTrue(node.add(f.createFolderNode("3"),-3));
		assertTrue(node.add(f.createFolderNode("4"),-99));
		assertEquals(node.size(), 4);
		assertEquals( ((FolderNode)node.get(0)).getName(), "4");
		assertEquals( ((FolderNode)node.get(1)).getName(), "3");
		assertEquals( ((FolderNode)node.get(2)).getName(), "2");
		assertEquals( ((FolderNode)node.get(3)).getName(), "1");
	}

	public void testGet() {
		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);
		assertFalse(node.add(null));

		FolderNode add = (FolderNode) f.createFolderNode("1");
		assertTrue(node.add(add));
		assertEquals(node.size(), 1);
		assertEquals(node.get(-1), null);
		assertEquals(node.get(0), add);
		assertEquals(node.get(1), null);

		FolderNode add2 = (FolderNode) f.createFolderNode("1");
		assertTrue(node.add(add2));
		assertEquals(node.size(), 2);
		assertEquals(node.get(-1), null);
		assertEquals(node.get(0), add);
		assertEquals(node.get(1), add2);
		assertEquals(node.get(2), null);
	}

	public void testAddAbstractNodeInt() {

		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);

		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);
		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);
		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);
		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);

		assertFalse(node.add(null, 2));

		FolderNode add = (FolderNode) f.createFolderNode("insert");
		assertTrue(node.add(add, 2));
		assertTrue(node.get(0) != add);
		assertTrue(node.get(1) != add);
		assertEquals(node.get(2), add);
		assertTrue(node.get(3) != add);
		assertTrue(node.get(4) != add);
		assertTrue(node.get(5) == null);
	}

	public void testRemoveInt() {
		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);

		assertFalse(node.remove(-1));
		assertFalse(node.remove(0));
		assertFalse(node.remove(1));

		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);
		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);
		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);
		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);

		assertTrue(node.remove(3));
		assertEquals(node.size(), 3);
		assertTrue(node.remove(2));
		assertEquals(node.size(), 2);
		assertTrue(node.remove(1));
		assertEquals(node.size(), 1);
		assertTrue(node.remove(0));
		assertEquals(node.size(), 0);
		assertFalse(node.remove(-1));
		assertEquals(node.size(), 0);
	}

	public void testRemoveAbstractNode() {

		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);

		assertFalse(node.remove(null));

		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);
		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);
		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);
		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);

		FolderNode add = (FolderNode) f.createFolderNode("insert");
		FolderNode add2 = (FolderNode) f.createFolderNode("insert");

		node.add(add, 2);
		assertEquals(node.size(), 5);
		assertFalse(node.remove(add2));

		assertTrue(node.remove(add));
		assertEquals(node.size(), 4);
	}

	public void testIndexOf() {
		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);

		assertFalse(node.remove(null));

		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);
		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);
		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);
		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);

		// non-exist
		FolderNode add2 = (FolderNode) f.createFolderNode("insert2");
		assertFalse(node.remove(add2));

		FolderNode add = (FolderNode) f.createFolderNode("insert");

		assertTrue(node.add(add));
		assertEquals(node.indexOf(add), 4);
		assertTrue(node.remove(add));
		assertEquals(node.indexOf(add), -1);

		assertTrue(node.add(add, 0));
		assertEquals(node.indexOf(add), 0);
		assertTrue(node.remove(add));
		assertEquals(node.indexOf(add), -1);

		assertTrue(node.add(add, 1));
		assertEquals(node.indexOf(add), 1);
		assertTrue(node.remove(add));
		assertEquals(node.indexOf(add), -1);

		assertTrue(node.add(add, 2));
		assertEquals(node.indexOf(add), 2);
		assertTrue(node.remove(add));
		assertEquals(node.indexOf(add), -1);

		assertTrue(node.add(add, 3));
		assertEquals(node.indexOf(add), 3);
		assertTrue(node.remove(add));
		assertEquals(node.indexOf(add), -1);

		assertTrue(node.add(add, 4));
		assertEquals(node.indexOf(add), 4);
		assertTrue(node.remove(add));
		assertEquals(node.indexOf(add), -1);

	}

	public void testIsContainer() {
		assertTrue(node.isContainer());
	}

	public void testToSimpleString() {
		assertEquals(node.toSimpleString(), node.getName());
	}

	public void testToDetailString() {
		assertEquals(node.toDetailString(), node.getName() + " (" + node.size()
				+ ")");
	}

	public void testSetName() {
		node.setName("testSetName");
		assertEquals(node.getName(), "testSetName");
	}

	public void testRemoveAllNode() {

		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);
		assertFalse(node.removeAll());

		assertFalse(node.remove(null));

		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);
		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);
		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);
		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);

		assertTrue(node.removeAll());
		assertEquals(node.size(), 0);
	}

	public void testToString() {
		assertEquals(node.toString(), node.getName() + " (" + node.size() + ")");
	}

	public void testClone() {
		FolderNode f2 = node.clone();
		assertEquals(node.getName(), f2.getName());
		assertEquals(node.size(), f2.size());
	}

	public void testClone2() {
		NodeFactory f = new NodeFactory();
		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);
		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);
		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);
		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);

		FolderNode f2 = node.clone();
		assertEquals(node.getName(), f2.getName());
		assertEquals(node.size(), f2.size());
		assertEquals(f2.size(), 4);

		// child 要一模一樣
		assertEquals(node.get(0).size(), f2.get(0).size());
		assertEquals(node.get(1).size(), f2.get(1).size());
		assertEquals(node.get(2).size(), f2.get(2).size());
		assertEquals(node.get(3).size(), f2.get(3).size());
	}

	public void testGetChildren() {
		assertEquals(node.getChildren().length, 0);
		
		NodeFactory f = new NodeFactory();
		assertEquals(node.size(), 0);
		assertFalse(node.removeAll());

		assertFalse(node.remove(null));

		assertTrue(node.add(f.createFolderNode("1")));
		assertEquals(node.size(), 1);
		assertTrue(node.add(f.createFolderNode("2")));
		assertEquals(node.size(), 2);
		assertTrue(node.add(f.createFolderNode("3")));
		assertEquals(node.size(), 3);
		assertTrue(node.add(f.createFolderNode("4")));
		assertEquals(node.size(), 4);

		assertEquals(node.getChildren().length, 4);
	}

}
