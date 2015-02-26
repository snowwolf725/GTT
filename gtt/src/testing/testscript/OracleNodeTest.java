package testing.testscript;

import gtt.eventmodel.Assertion;
import gtt.oracle.IOracleHandler;
import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.OracleNode;
import gtt.testscript.ViewAssertNode;

import java.util.Iterator;

import junit.framework.TestCase;

public class OracleNodeTest extends TestCase {

	OracleNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = new NodeFactory().createOracleNode();
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

		public void visit(OracleNode node) {
			m_hasVisit = true;
		}
	}

	public void testAccept() {
		MyMockVisitor mock_visitor = new MyMockVisitor();

		node.accept(mock_visitor);
		assertTrue(mock_visitor.hasVisit());
	}

	public void testAddAbstractNode() {
		assertFalse(node.add(null));
		assertEquals(0, node.size());

		ViewAssertNode va;
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(1, node.size());

		// 只能加入 ViewAssertNode
		NodeFactory nf = new NodeFactory();
		assertFalse(node.add(nf.createFolderNode("")));
		assertEquals(1, node.size());

		assertFalse(node.add(nf.createLaunchNode("", "")));
		assertEquals(1, node.size());

		assertFalse(node.add(nf.createEventNode(new MockComponent(),
				new MockEvent())));
		assertEquals(1, node.size());

		assertFalse(node.add(nf.createModelAssertNode()));
		assertEquals(1, node.size());

		assertFalse(node.add(nf.createOracleNode()));
		assertEquals(1, node.size());

		assertFalse(node.add(nf.createReferenceMacroEventNode("path")));
		assertEquals(1, node.size());

		// 可以加 ViewAssertNode
		assertTrue(node.add(nf.createViewAssertNode(new MockComponent(),
				new Assertion())));
		assertEquals(2, node.size());
	}

	public void testRemoveAbstractNode() {
		assertFalse(node.add(null));
		assertEquals(0, node.size());

		ViewAssertNode va;
		va = new ViewAssertNode();
		// add a ViewAssertNode
		assertTrue(node.add(va));
		assertEquals(1, node.size());

		// remove it
		assertFalse(node.remove(null));
		assertTrue(node.remove(va));
		assertEquals(0, node.size());

		// add again
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(1, node.size());
	}

	public void testToSimpleString() {
		assertEquals(node.getName(), node.toSimpleString());
	}

	public void testToDetailString() {
		assertEquals(node.getName(), node.toDetailString());
	}

	public void testGetName() {
		assertEquals("TestOracle", node.getName());
	}
	public void testClone() {
		OracleNode n = node.clone();
		assertNotSame(n, node);
		assertEquals(n.getOracleData().getLevel(), node.getOracleData()
				.getLevel());
	}

	public void testRemoveAllNode() {
		assertFalse(node.add(null));
		assertEquals(0, node.size());

		ViewAssertNode va;
		// add a ViewAssertNode
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(1, node.size());
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(2, node.size());
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(3, node.size());

		// remove all
		node.removeAll();
		assertEquals(0, node.size());
	}

	public void testIterator() {
		ViewAssertNode va;
		// add a ViewAssertNode
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(1, node.size());
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(2, node.size());
		va = new ViewAssertNode();
		assertTrue(node.add(va));
		assertEquals(3, node.size());

		Iterator<AbstractNode> ite = node.iterator();
		assertNotNull(ite);

		int ct = 0;
		while (ite.hasNext()) {
			ite.next();
			ct++;
		}
		assertEquals(3, ct);
	}

	public void testClone_withLotOfViewAssertNode() {

		ViewAssertNode va;
		MockComponent mc = new MockComponent();
		Assertion a = new Assertion();
		// add a ViewAssertNode
		va = new ViewAssertNode(mc, a);
		assertTrue(node.add(va));
		assertEquals(1, node.size());
		va = new ViewAssertNode(mc, a);
		assertTrue(node.add(va));
		assertEquals(2, node.size());
		va = new ViewAssertNode(mc, a);
		assertTrue(node.add(va));
		assertEquals(3, node.size());

		OracleNode n = node.clone();
		assertNotSame(n, node);
		assertEquals(n.getOracleData().getLevel(), node.getOracleData().getLevel());
		assertEquals(n.size(), node.size());
	}

	public void testGetChildren() {
		Object[] objs = node.getChildren();
		assertEquals(0, objs.length);
	}

	public void testOracleLevel() {
		assertEquals(0, IOracleHandler.Level.APPLICATION_LEVEL.ordinal());
		assertEquals(1, IOracleHandler.Level.WINDOW_LEVEL.ordinal());
		assertEquals(2, IOracleHandler.Level.COMPONENT_LEVEL.ordinal());
	}

	public void testEventType() {
		assertEquals(0, IOracleHandler.EventType.DEFAULT.ordinal());
		assertEquals(1, IOracleHandler.EventType.USER_SELECTED.ordinal());
		assertEquals(2, IOracleHandler.EventType.ALL.ordinal());
	}

}
