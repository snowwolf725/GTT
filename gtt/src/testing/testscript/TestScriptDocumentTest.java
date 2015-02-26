package testing.testscript;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import junit.framework.TestCase;

public class TestScriptDocumentTest extends TestCase {

	private TestScriptDocument m_tsDoc;

//	private IEventModel m_model = EventModelFactory.getDefault();

	NodeFactory m_Factory = new NodeFactory();

	protected void setUp() throws Exception {
		super.setUp();
		m_tsDoc = TestScriptDocument.create();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		m_tsDoc = null;
	}

	public void testInsertNode() {
		assertFalse(m_tsDoc.insertNode(null, null));
		assertFalse(m_tsDoc.insertNode(m_tsDoc.getScript(), null));

	}

	public void testRemoveNode2() {
		assertFalse(m_tsDoc.removeNode(null, null));
		assertEquals(m_tsDoc.getScript().size(), 0);
		assertFalse(m_tsDoc.removeNode(null, -1));
		assertFalse(m_tsDoc.removeNode(null, 0));
		assertFalse(m_tsDoc.removeNode(null, 1));
	}

	public void testRemoveNode3() {
		assertEquals(m_tsDoc.getScript().size(), 0);
		assertFalse(m_tsDoc.removeNode(null));

		AbstractNode f = m_Factory.createFolderNode("f");
		assertFalse(m_tsDoc.removeNode(f));
		m_tsDoc.insertNode(m_tsDoc.getScript(), f);

		assertTrue(m_tsDoc.removeNode(f)); // remove ok
		assertFalse(m_tsDoc.removeNode(f)); // non-exist

	}

	IComponent createComponent() {
		return SwingComponent.createDefault();
	}

	IEvent createEvent() {
		return SwingEvent.create(0, "");
	}

	public void testAddEventNode() {
		AbstractNode e = m_Factory.createEventNode(createComponent(),
				createEvent());
		assertEquals(m_tsDoc.getScript().size(), 0);

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), e));
		assertEquals(m_tsDoc.getScript().size(), 1);
		assertEquals(m_tsDoc.getScript(), e.getParent());

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), e));
		assertEquals(m_tsDoc.getScript().size(), 1);

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), e));
		assertEquals(m_tsDoc.getScript().size(), 1);

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), e));
		assertEquals(m_tsDoc.getScript().size(), 1);
	}

	public void testRemoveNode() {
		assertEquals(m_tsDoc.getScript().size(), 0);

		AbstractNode e1 = m_Factory.createEventNode(createComponent(),
				createEvent());
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), e1));
		assertEquals(m_tsDoc.getScript().size(), 1);
		assertEquals(m_tsDoc.getScript(), e1.getParent());

		AbstractNode e2 = m_Factory.createEventNode(createComponent(),
				createEvent());
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), e2));
		assertEquals(m_tsDoc.getScript().size(), 2);
		assertEquals(m_tsDoc.getScript(), e2.getParent());

		assertFalse(m_tsDoc.removeNode(m_tsDoc.getScript(), -1));
		assertFalse(m_tsDoc.removeNode(m_tsDoc.getScript(), 2));
		assertFalse(m_tsDoc.removeNode(m_tsDoc.getScript(), 3));
		assertTrue(m_tsDoc.removeNode(m_tsDoc.getScript(), 1)); // remove e2
		assertFalse(m_tsDoc.removeNode(m_tsDoc.getScript(), 1)); // no index
		// 1
		assertFalse(m_tsDoc.removeNode(m_tsDoc.getScript(), e2)); // no e2
		assertEquals(m_tsDoc.getScript().size(), 1);

		assertTrue(m_tsDoc.removeNode(m_tsDoc.getScript(), 0)); // e1 移掉
		assertEquals(m_tsDoc.getScript().size(), 0);
		assertFalse(m_tsDoc.removeNode(m_tsDoc.getScript(), 0)); // no index
		// 0
		assertFalse(m_tsDoc.removeNode(m_tsDoc.getScript(), e1)); // no e1
	}

	public void testExchangeNode2() {
		assertEquals(m_tsDoc.getScript().size(), 0);
		assertFalse(m_tsDoc.swapNode(null, null));

		AbstractNode f1 = m_Factory.createFolderNode("f1");
		assertFalse(m_tsDoc.swapNode(f1, null));
		assertFalse(m_tsDoc.swapNode(null, f1));

		m_tsDoc.insertNode(m_tsDoc.getScript(), f1);
		assertFalse(m_tsDoc.swapNode(f1, null));
		assertFalse(m_tsDoc.swapNode(null, f1));

		AbstractNode f2 = m_Factory.createFolderNode("f2");
		m_tsDoc.insertNode(m_tsDoc.getScript(), f2);
		assertFalse(m_tsDoc.swapNode(f2, null));
		assertFalse(m_tsDoc.swapNode(null, f2));
		assertTrue(m_tsDoc.swapNode(f2, f1));
		assertTrue(m_tsDoc.swapNode(f1, f2));

		AbstractNode f3 = m_Factory.createFolderNode("f3");
		m_tsDoc.insertNode(f2, f3);
		// 不同parent下的兩個node無法做swap
		assertFalse(m_tsDoc.swapNode(f2, f3));
		assertFalse(m_tsDoc.swapNode(f2, f3));
		assertFalse(m_tsDoc.swapNode(f1, f3));
		assertFalse(m_tsDoc.swapNode(f3, f1));

	}

	/**
	 * Exchange two childrens within a FolderNode
	 */
	public void testExchangeNode() {
		assertEquals(m_tsDoc.getScript().size(), 0);

		AbstractNode f1 = m_Factory.createFolderNode("f1");
		AbstractNode f2 = m_Factory.createFolderNode("f2");
		AbstractNode f3 = m_Factory.createFolderNode("f3");

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f1));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f2));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f3));
		assertEquals(f1.getParent().indexOf(f1), 0);
		assertEquals(f2.getParent().indexOf(f2), 1);
		assertEquals(f3.getParent().indexOf(f3), 2);

		// do exchange
		assertTrue(m_tsDoc.swapNode(f1, f2));

		assertEquals(f1.getParent().indexOf(f1), 1);
		assertEquals(f2.getParent().indexOf(f2), 0);
		assertEquals(f3.getParent().indexOf(f3), 2);

		// do exchange
		assertTrue(m_tsDoc.swapNode(f1, f3));

		assertEquals(f1.getParent().indexOf(f1), 2);
		assertEquals(f2.getParent().indexOf(f2), 0);
		assertEquals(f3.getParent().indexOf(f3), 1);
	}

	public void testUpNode() {
		assertEquals(m_tsDoc.getScript().size(), 0);
		assertFalse(m_tsDoc.upNode(null));

		AbstractNode f1 = m_Factory.createFolderNode("f1");
		// 未加入的node無法做up
		assertFalse(m_tsDoc.upNode(f1));
		AbstractNode f2 = m_Factory.createFolderNode("f2");
		// 未加入的node無法做up
		assertFalse(m_tsDoc.upNode(f2));
		AbstractNode f3 = m_Factory.createFolderNode("f3");
		// 未加入的node無法做up
		assertFalse(m_tsDoc.upNode(f3));
		AbstractNode f4 = m_Factory.createFolderNode("f4");
		// 未加入的node無法做up
		assertFalse(m_tsDoc.upNode(f4));
		AbstractNode f5 = m_Factory.createFolderNode("f5");
		// 未加入的node無法做up
		assertFalse(m_tsDoc.upNode(f5));
		AbstractNode f6 = m_Factory.createFolderNode("f6");
		// 未加入的node無法做up
		assertFalse(m_tsDoc.upNode(f6));

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f1));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f2));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f3));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f4));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f5));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f6));
		assertEquals(f1.getParent().indexOf(f1), 0);
		assertEquals(f2.getParent().indexOf(f2), 1);
		assertEquals(f3.getParent().indexOf(f3), 2);
		assertEquals(f4.getParent().indexOf(f4), 3);
		assertEquals(f5.getParent().indexOf(f5), 4);
		assertEquals(f6.getParent().indexOf(f6), 5);

		// do up
		assertTrue(m_tsDoc.upNode(f5));
		assertEquals(f1.getParent().indexOf(f1), 0);
		assertEquals(f2.getParent().indexOf(f2), 1);
		assertEquals(f3.getParent().indexOf(f3), 2);
		assertEquals(f4.getParent().indexOf(f4), 4);
		assertEquals(f5.getParent().indexOf(f5), 3);
		assertEquals(f6.getParent().indexOf(f6), 5);
		// do up
		assertTrue(m_tsDoc.upNode(f5));
		assertEquals(f5.getParent().indexOf(f5), 2);
		// do up
		assertTrue(m_tsDoc.upNode(f5));
		assertEquals(f5.getParent().indexOf(f5), 1);
		// do up
		assertTrue(m_tsDoc.upNode(f5));
		assertEquals(f5.getParent().indexOf(f5), 0);
		// do up fail
		assertFalse(m_tsDoc.upNode(f5));
		assertEquals(f5.getParent().indexOf(f5), 0);

		assertEquals(f1.getParent().indexOf(f1), 1);
		assertEquals(f2.getParent().indexOf(f2), 2);
		assertEquals(f3.getParent().indexOf(f3), 3);
		assertEquals(f4.getParent().indexOf(f4), 4);
		assertEquals(f5.getParent().indexOf(f5), 0);
		assertEquals(f6.getParent().indexOf(f6), 5);
	}

	public void testDownNode() {
		assertEquals(m_tsDoc.getScript().size(), 0);
		assertFalse(m_tsDoc.downNode(null));

		AbstractNode f1 = m_Factory.createFolderNode("f1");
		// 未加入的node無法做down
		assertFalse(m_tsDoc.downNode(f1));
		AbstractNode f2 = m_Factory.createFolderNode("f2");
		// 未加入的node無法做down
		assertFalse(m_tsDoc.downNode(f2));
		AbstractNode f3 = m_Factory.createFolderNode("f3");
		// 未加入的node無法做down
		assertFalse(m_tsDoc.downNode(f3));
		AbstractNode f4 = m_Factory.createFolderNode("f4");
		// 未加入的node無法做down
		assertFalse(m_tsDoc.downNode(f4));
		AbstractNode f5 = m_Factory.createFolderNode("f5");
		// 未加入的node無法做down
		assertFalse(m_tsDoc.downNode(f5));
		AbstractNode f6 = m_Factory.createFolderNode("f6");
		// 未加入的node無法做down
		assertFalse(m_tsDoc.downNode(f6));

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f1));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f2));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f3));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f4));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f5));
		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), f6));
		assertEquals(f1.getParent().indexOf(f1), 0);
		assertEquals(f2.getParent().indexOf(f2), 1);
		assertEquals(f3.getParent().indexOf(f3), 2);
		assertEquals(f4.getParent().indexOf(f4), 3);
		assertEquals(f5.getParent().indexOf(f5), 4);
		assertEquals(f6.getParent().indexOf(f6), 5);

		// do down
		assertTrue(m_tsDoc.downNode(f1));
		assertEquals(f1.getParent().indexOf(f1), 1);
		assertEquals(f2.getParent().indexOf(f2), 0);
		assertEquals(f3.getParent().indexOf(f3), 2);
		assertEquals(f4.getParent().indexOf(f4), 3);
		assertEquals(f5.getParent().indexOf(f5), 4);
		assertEquals(f6.getParent().indexOf(f6), 5);

		// do down
		assertTrue(m_tsDoc.downNode(f1));
		assertEquals(f1.getParent().indexOf(f1), 2);

		// do down
		assertTrue(m_tsDoc.downNode(f1));
		assertEquals(f1.getParent().indexOf(f1), 3);

		// do down
		assertTrue(m_tsDoc.downNode(f1));
		assertEquals(f1.getParent().indexOf(f1), 4);

		// do down
		assertTrue(m_tsDoc.downNode(f1));
		assertEquals(f1.getParent().indexOf(f1), 5);

		// do down fails
		assertFalse(m_tsDoc.downNode(f1));
		assertEquals(f1.getParent().indexOf(f1), 5);

		assertEquals(f1.getParent().indexOf(f1), 5);
		assertEquals(f2.getParent().indexOf(f2), 0);
		assertEquals(f3.getParent().indexOf(f3), 1);
		assertEquals(f4.getParent().indexOf(f4), 2);
		assertEquals(f5.getParent().indexOf(f5), 3);
		assertEquals(f6.getParent().indexOf(f6), 4);
	}

	public void testHasChildren() {
		assertFalse(m_tsDoc.hasChildren());

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), m_Factory
				.createFolderNode("f")));

		assertTrue(m_tsDoc.hasChildren());
	}

	public void testGetChildren() {
		assertEquals(m_tsDoc.getChildren().length, 0);

		AbstractNode e = m_Factory.createFolderNode("f");

		assertTrue(m_tsDoc.insertNode(m_tsDoc.getScript(), e));
		assertEquals(m_tsDoc.getChildren().length, 1);
		assertEquals(m_tsDoc.getChildren()[0], e);
	}

	public void testSetName() {
		String docname = "document";
		m_tsDoc.setName(docname);
		assertEquals(m_tsDoc.getName(), docname);
		String othername = "others";
		m_tsDoc.setName(othername);
		assertEquals(m_tsDoc.getName(), othername);
		assertFalse(m_tsDoc.getName().equals(docname));
	}

	public void testClone() {
		m_tsDoc.setName("clone");

		TestScriptDocument doc2 = m_tsDoc.clone();
		assertEquals(m_tsDoc.getName(), doc2.getName());
		assertTrue(m_tsDoc.getScript() != doc2.getScript());
		assertTrue(m_tsDoc.getScript().size() == doc2.getScript().size());
		assertTrue(m_tsDoc != doc2);
	}

	public void testCreateEmpty() {
		TestScriptDocument d = TestScriptDocument.create();
		assertFalse(d.hasChildren());
		assertEquals(d.getName(), "TEST_SCRIPT");
	}

}
