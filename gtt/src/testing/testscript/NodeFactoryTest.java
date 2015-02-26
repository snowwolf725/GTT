package testing.testscript;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.ViewAssertNode;
import junit.framework.TestCase;

public class NodeFactoryTest extends TestCase {

	NodeFactory factory;

	protected void setUp() throws Exception {
		super.setUp();
		factory = new NodeFactory();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		factory = null;
	}

	public void testCreateFolderNode() {
		FolderNode fn = (FolderNode) factory.createFolderNode("folder");

		assertEquals(fn.getClass().getName(), FolderNode.class.getName());

		assertEquals(fn.getName(), "folder");
		assertEquals(fn.size(), 0);

	}

	public void testCreateEventNode() {
		try {
			IComponent c = new MockComponent();
			IEvent e = new MockEvent();

			EventNode en = (EventNode) factory.createEventNode(c, e);

			assertEquals(en.getClass().getName(), EventNode.class.getName());

			assertEquals(en.getComponent(), c);
			assertEquals(en.getEvent(), e);
		} catch (Exception ep) {
			fail(ep.toString());
		}
	}

	public void testCreateViewAssertNode() {
		try {
			IComponent c = new MockComponent();
			Assertion a = new Assertion();

			ViewAssertNode vn = (ViewAssertNode) factory.createViewAssertNode(
					c, a);

			assertEquals(vn.getClass().getName(), ViewAssertNode.class
					.getName());
			assertEquals(vn.getComponent(), c);
			assertEquals(vn.getAssertion(), a);
		} catch (Exception ep) {
			fail(ep.toString());
		}

	}

	public void testCreateModelAssertNode() {
		Object obj = factory.createModelAssertNode();
		assertEquals(obj.getClass().getName(), ModelAssertNode.class.getName());
	}

	public void testCreateReferenceMacroEventNode() {
		Object obj = factory.createReferenceMacroEventNode(".");
		assertEquals(obj.getClass().getName(), ReferenceMacroEventNode.class
				.getName());
	}

	public void testCreateOracleNode() {
		Object obj = factory.createOracleNode();
		assertEquals(obj.getClass().getName(), OracleNode.class.getName());
	}
}
