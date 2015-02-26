package testing.testscript;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import junit.framework.TestCase;

public class EventNodeTest extends TestCase {

	EventNode node;

	protected void setUp() throws Exception {
		super.setUp();
		IComponent c = new MockComponent();
		IEvent e = new MockEvent();
		node = (EventNode) new NodeFactory().createEventNode(c, e);
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

		public void visit(EventNode node) {
			m_hasVisit = true;
		}

	}

	public void testAccept() {
		MyMockVisitor mock_visitor = new MyMockVisitor();

		node.accept(mock_visitor);
		assertTrue(mock_visitor.hasVisit());
	}

	public void testIsContainer() {
		assertFalse(node.isContainer());
	}

	public void testToSimpleString() {
		assertEquals(node.toSimpleString(), MockEvent.NAME + "("
				+ MockComponent.NAME + ")");
	}

//	public void testToDetailString() {
//		assertEquals("(" + MockComponent.TYPE + ")" + MockComponent.NAME + "."
//				+ MockEvent.NAME + "()", node.toDetailString());
//
//		IEvent e = SwingEvent.create(99, "push");
//		ArgumentList args = e.getArgumentList();
//		args.addArgu(Argument.create("int", "i", null));
//		args.addArgu(Argument.create("long", "j", null));
//		args.addArgu(Argument.create("String", null));
//		args.addArgu(Argument.create("String", null));
//		node.setEvent(e);
//
//		assertEquals(node.toDetailString(), "(" + MockComponent.TYPE + ")"
//				+ MockComponent.NAME + "." + e.getName()
//				+ "(NULL:i, NULL:j, NULL:null, NULL:null)");
//	}

//	public void testToDetailString2() {
//		IComponent c = new MockComponent();
//		IEvent e = SwingEvent.create(99, "push");
//		ArgumentList args = e.getArgumentList();
//		args.addArgu(Argument.create("int", "i", "100"));
//		args.addArgu(Argument.create("long", "j", "99"));
//		args.addArgu(Argument.create("String", "k"));
//		args.addArgu(Argument.create("String", "m"));
//		node = (EventNode) new NodeFactory().createEventNode(c, e);
//
//		assertEquals("(TYPE)NAME.push(100:i, 99:j, NULL:k, NULL:m)", node
//				.toDetailString());
//	}

	public void testClone() {
		EventNode node2 = node.clone();
		assertEquals(node.getComponent(), node2.getComponent());
		assertEquals(node.getEvent(), node2.getEvent());
	}

	public void testToString() {
		assertEquals(node.toString(), MockComponent.NAME + "."
				+ MockEvent.TO_STRING);
	}

	public void testSetComponent() {
		IComponent c = new MockComponent();
		assertTrue(node.getComponent() != c);
		node.setComponent(c);
		assertTrue(node.getComponent() == c);
	}

	public void testSetEvent() {
		IEvent e = new MockEvent();
		assertTrue(node.getEvent() != e);
		node.setEvent(e);
		assertTrue(node.getEvent() == e);
	}

}
