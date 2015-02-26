package testing.testscript;

import gtt.testscript.AbstractNode;
import gtt.testscript.visitor.ITestScriptVisitor;
import junit.framework.TestCase;

public class AbstractNodeTest extends TestCase {

	class MockAbstractNode extends AbstractNode {

		@Override
		public void accept(ITestScriptVisitor v) {
		}

		@Override
		public String toDetailString() {
			return null;
		}

		@Override
		public String toSimpleString() {
			return null;
		}

		@Override
		public AbstractNode clone() {
			return null;
		}

	}

	MockAbstractNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = new MockAbstractNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testAddAbstractNode() {
		assertFalse(node.add(null));
	}

	public void testGet() {
		assertTrue(node.get(-1) == null);
		assertTrue(node.get(0) == null);
		assertTrue(node.get(1) == null);
	}

	public void testAddAbstractNodeInt() {
		assertFalse(node.add(null, -1));
		assertFalse(node.add(null, 0));
		assertFalse(node.add(null, 1));
	}

	public void testRemoveInt() {
		assertFalse(node.remove(-1));
		assertFalse(node.remove(0));
		assertFalse(node.remove(1));
	}

	public void testRemoveAbstractNode() {
		assertFalse(node.remove(null));
	}

	public void testIndexOf() {
		assertEquals(node.indexOf(node), -1);
	}

	public void testSize() {
		assertEquals(node.size(), 0);
	}

	public void testHasChildren() {
		assertFalse(node.hasChildren());
	}

	public void testGetChildren() {
		assertEquals(0, node.getChildren().length);
	}

	public void testIsContainer() {
		assertFalse(node.isContainer());
	}

	public void testGetParent() {
		assertEquals(null, node.getParent());
	}

	public void testClone() {
		assertEquals(null, node.clone());
	}

	public void testToSimpleString() {
		assertEquals(null, node.toSimpleString());
	}

	public void testToDetailString() {
		assertEquals(null, node.toDetailString());
	}
}
