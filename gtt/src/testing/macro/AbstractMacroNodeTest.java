package testing.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import junit.framework.TestCase;

public class AbstractMacroNodeTest extends TestCase {

	AbstractMacroNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = new MockAbstractMacroNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testSetName() {
		assertEquals(node.getName(), "MockAbstractMacroNode");

		node.setName("path1");
		assertEquals(node.getName(), "path1");
	}

	public void testGetParent() {
		assertNull(node.getParent());

		MockAbstractMacroNode p = new MockAbstractMacroNode();
		p.setName("parent");
		p.add(node);
		assertNotNull(node.getParent());
		assertEquals(node.getParent(), p);
	}

	public void testGetInt() {
		assertNull(node.get(0));
	}

	public void testAddAbstractMacroNode() {
		assertFalse(node.add(null));
	}

	public void testAddAbstractMacroNodeInt() {
		assertFalse(node.add(null, -1));
		assertFalse(node.add(null, 0));
		assertFalse(node.add(null, 1));
	}

	public void testRemoveInt() {
		assertFalse(node.remove(-1));
		assertFalse(node.remove(0));
		assertFalse(node.remove(1));
	}

	public void testRemoveAbstractMacroNode() {
		assertFalse(node.remove(null));
	}

	public void testRemoveAll() {
		assertFalse(node.removeAll());
	}

	public void testIsContainer() {
		assertFalse(node.isContainer());
	}

	public void testSize() {
		assertEquals(node.size(), 0);
	}

	public void testHasChildren() {
		assertFalse(node.hasChildren());
	}

	public void testGetChildren() {
		assertEquals(node.getChildren().length, 0);
	}

	public void testIndexOf() {
		assertEquals(node.indexOf(null), -1);
	}

	public void testClone() {
		assertNotNull(node.clone());
	}

}
