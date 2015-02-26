/**
 *
 */
package testing.gttlipse.vfsm;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import gttlipse.GTTlipse;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.State;

/**
 * @author Jason Wang
 *
 */
public class NodeTest extends TestCase {

	State m_State;

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		m_State = State.createDummyState();
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testsetDeclarationFlag() {
		m_State.setDeclaration(true);
		assertTrue(m_State.isDeclaration());
		m_State.setDeclaration(false);
		assertFalse(m_State.isDeclaration());
	}

	public void testgetDeclarationFlag() {
		m_State.setDeclaration(true);
		assertTrue(m_State.isDeclaration());
		m_State.setDeclaration(false);
		assertFalse(m_State.isDeclaration());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#setDimension(org.eclipse.draw2d.geometry.Dimension)}
	 * .
	 */
	public void testSetSize() {
		/* for setSize(Dimension d) */
		Dimension d = new Dimension(100, 100);
		m_State.setDimension(d);
		assertEquals(d, m_State.getDimension());
		/* for setSize(int w, int y) */
		m_State.setDimension(50, 50);
		assertEquals(50, m_State.getDimension().width);
		assertEquals(50, m_State.getDimension().height);
	}

	/**
	 * Test method for {@link GTTlipse.VFSMEditor.model.Node#getDimension()}.
	 */
	public void testGetSize() {
		Dimension d = new Dimension(200, 200);
		m_State.setDimension(d);
		assertEquals(d, m_State.getDimension());
	}

	public void testGetParent() {
		State pnode = State.createDummyState();
		m_State.setParent(pnode);
		assertEquals(pnode, m_State.getParent());
	}

	public void testSetParent() {
		State pnode = State.createDummyState();
		m_State.setParent(pnode);
		assertEquals(pnode, m_State.getParent());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#addInput(GTTlipse.VFSMEditor.model.Connection)}
	 * .
	 */
	public void testAddInput() {
		assertEquals(0, m_State.getIncomingConnections().size());

		State sourceNode = State.createDummyState();

		Connection c = Connection.create(sourceNode, m_State);
		assertEquals(1, m_State.getIncomingConnections().size());

		m_State.addInput(c); // 重複再加一次
		assertEquals(1, m_State.getIncomingConnections().size());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#addOutput(GTTlipse.VFSMEditor.model.Connection)}
	 * .
	 */
	public void testAddOutput() {
		assertEquals(0, m_State.getOutgoingConnections().size());
		State targetNode = State.createDummyState();
		Connection c = Connection.create(m_State, targetNode);
		assertEquals(1, m_State.getOutgoingConnections().size());

		m_State.addOutput(c); // 重複再加一次
		assertEquals(1, m_State.getOutgoingConnections().size());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#getIncomingConnections()}.
	 */
	public void testGetIncomingConnections() {
		assertEquals(0, m_State.getIncomingConnections().size());
		State sourceNode1 = State.createDummyState();
		State sourceNode2 = State.createDummyState();
		State sourceNode3 = State.createDummyState();
		sourceNode1.setName("state1");
		sourceNode2.setName("state2");
		sourceNode3.setName("state3");

		Connection.create(sourceNode1, m_State);
		assertEquals(1, m_State.getIncomingConnections().size());

		Connection.create(sourceNode2, m_State);
		assertEquals(2, m_State.getIncomingConnections().size());

		Connection.create(sourceNode3, m_State);
		assertEquals(3, m_State.getIncomingConnections().size());

		// node.addInput(c1);
		// node.addInput(c2);
		// node.addInput(c3);
		// assertEquals(6, node.getIncomingConnections().size());
	}

	public void testSetIncomingConnections() {
		assertEquals(0, m_State.getIncomingConnections().size());
		List<Connection> inputs = new ArrayList<Connection>(5);
		State sourceNode1 = State.createDummyState();
		State sourceNode2 = State.createDummyState();
		State sourceNode3 = State.createDummyState();
		sourceNode1.setName("state1");
		sourceNode2.setName("state2");
		sourceNode3.setName("state3");

		Connection c1 = Connection.create(sourceNode1, m_State);
		Connection c2 = Connection.create(sourceNode2, m_State);
		Connection c3 = Connection.create(sourceNode3, m_State);
		inputs.add(c1);
		inputs.add(c2);
		inputs.add(c3);
		assertEquals(3, m_State.getIncomingConnections().size());

		State node2 = State.createDummyState();
		assertEquals(0, node2.getIncomingConnections().size());
		// node2.setIncomingConnections(inputs);
		// assertEquals(3, node2.getIncomingConnections().size());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#getOutgoingConnections()}.
	 */
	public void testGetOutgoingConnections() {
		assertEquals(0, m_State.getOutgoingConnections().size());
		State targetNode1 = State.createDummyState();
		Connection.create(m_State, targetNode1);
		assertEquals(1, m_State.getOutgoingConnections().size());

		// node.addOutput(c1);
		// assertEquals(2, node.getOutgoingConnections().size());
	}

	public void testSetOutgoingConnections() {
		assertEquals(0, m_State.getOutgoingConnections().size());
		State targetNode1 = State.createDummyState();
		Connection.create(m_State, targetNode1);
		assertEquals(1, m_State.getOutgoingConnections().size());
		// node.addOutput(c1);
		// assertEquals(2, node.getOutgoingConnections().size());

		State node2 = State.createDummyState();
		node2.setName("state2");
		// node2.setOutgoingConnections(m_State.getOutgoingConnections());
		// assertEquals(1, node2.getOutgoingConnections().size());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#removeInput(GTTlipse.VFSMEditor.model.Connection)}
	 * .
	 */
	public void testRemoveInput() {
		assertEquals(0, m_State.getIncomingConnections().size());
		State sourceNode1 = State.createDummyState();
		State sourceNode2 = State.createDummyState();
		State sourceNode3 = State.createDummyState();
		sourceNode1.setName("state1");
		sourceNode2.setName("state2");
		sourceNode3.setName("state3");

		Connection c1 = Connection.create(sourceNode1, m_State);
		assertEquals(1, m_State.getIncomingConnections().size());

		Connection.create(sourceNode2, m_State);
		assertEquals(2, m_State.getIncomingConnections().size());

		Connection.create(sourceNode3, m_State);
		assertEquals(3, m_State.getIncomingConnections().size());
		// node.addInput(c1);
		// node.addInput(c2);
		// node.addInput(c3);

		// assertEquals(6, node.getIncomingConnections().size());
		// because node have 2 c1, remove 1, exist the other 1.
		m_State.removeInput(c1);
		assertEquals(2, m_State.getIncomingConnections().size());
		// if remove another one, the list not exist any c1.
		m_State.removeInput(c1);
		assertEquals(2, m_State.getIncomingConnections().size());
		// if want remove inexistence connection like c1, the list can't be
		// remove .
		m_State.removeInput(c1);
		assertEquals(2, m_State.getIncomingConnections().size());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#removeOutput(GTTlipse.VFSMEditor.model.Connection)}
	 * .
	 */
	public void testRemoveOutput() {
		assertEquals(0, m_State.getOutgoingConnections().size());
		State targetNode1 = State.createDummyState();
		Connection c1 = Connection.create(m_State, targetNode1);
		assertEquals(1, m_State.getOutgoingConnections().size());
		// node.addOutput(c1);
		// assertEquals(2, node.getOutgoingConnections().size());

		m_State.removeOutput(c1);
		assertEquals(0, m_State.getOutgoingConnections().size());

		m_State.removeOutput(c1);
		assertEquals(0, m_State.getOutgoingConnections().size());

	}

	/**
	 * Test method for {@link GTTlipse.VFSMEditor.model.Node#isVisible()}.
	 */
	public void testIsVisible() {
		m_State.setVisible(true);
		assertEquals(true, m_State.isVisible());
		m_State.setVisible(false);
		assertEquals(false, m_State.isVisible());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#setVisible(boolean)}.
	 */
	public void testSetVisible() {
		m_State.setVisible(true);
		assertEquals(true, m_State.isVisible());
		m_State.setVisible(false);
		assertEquals(false, m_State.isVisible());
	}

	/**
	 * Test method for {@link GTTlipse.VFSMEditor.model.Node#getName()}.
	 */
	public void testGetName() {
		m_State.setName("jason");
		assertEquals("jason", m_State.getName());
		m_State.setName("wang");
		assertEquals("wang", m_State.getName());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#setNodeName(java.lang.String)}.
	 */
	public void testSetName() {
		m_State.setName("jason");
		assertEquals("jason", m_State.getName());
		m_State.setName("wang");
		assertEquals("wang", m_State.getName());
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#setLocation(org.eclipse.draw2d.geometry.Point)}
	 * .
	 */
	public void testSetLocation() {
		m_State.setLocation(new Point(0, 0));
		assertEquals(new Point(0, 0), m_State.getLocation());
		m_State.setLocation(new Point(10, 10));
		assertEquals(new Point(10, 10), m_State.getLocation());

		m_State.setLocation(0, 0);
		assertEquals(new Point(0, 0), m_State.getLocation());
		m_State.setLocation(10, 10);
		assertEquals(new Point(10, 10), m_State.getLocation());
	}

	/**
	 * Test method for {@link GTTlipse.VFSMEditor.model.Node#getLocation()}.
	 */
	public void testGetLocation() {
		m_State.setLocation(new Point(0, 0));
		assertEquals(new Point(0, 0), m_State.getLocation());
		m_State.setLocation(new Point(10, 10));
		assertEquals(new Point(10, 10), m_State.getLocation());
	}

	/**
	 * Test method for {@link GTTlipse.VFSMEditor.model.Node#getEditableValue()}
	 * .
	 */
	public void testGetEditableValue() {
		assert (m_State.getEditableValue() != null);
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#getPropertyDescriptors()}.
	 */
	public void testGetPropertyDescriptors() {
		assert (m_State.getPropertyDescriptors() != null);
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#getPropertyValue(java.lang.Object)}
	 * .
	 */
	public void testGetPropertyValue() {
		m_State.setName("jason");
		assertEquals("jason", m_State.getPropertyValue("NAME"));
		m_State.setLocation(new Point(50, 50));
		assertEquals(new Point(50, 50), m_State.getPropertyValue("LOCATION"));
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#isPropertySet(java.lang.Object)}.
	 */
	public void testIsPropertySet() {
		assertTrue(m_State.isPropertySet("NAME"));
		assertTrue(m_State.isPropertySet("LOCATION"));
		assertTrue(m_State.isPropertySet("SIZE"));
		assertTrue(m_State.isPropertySet("INPUTS"));
		assertTrue(m_State.isPropertySet("OUTPUTS"));
		assertTrue(m_State.isPropertySet("VISIBLE"));
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#resetPropertyValue(java.lang.Object)}
	 * .
	 */
	public void testResetPropertyValue() {
		m_State.resetPropertyValue("NAME");
		assertEquals("", m_State.getPropertyValue("NAME"));
		m_State.resetPropertyValue("LOCATION");
		assertEquals(new Point(0, 0), m_State.getPropertyValue("LOCATION"));
	}

	/**
	 * Test method for
	 * {@link GTTlipse.VFSMEditor.model.Node#setPropertyValue(java.lang.Object, java.lang.Object)}
	 * .
	 */
	public void testSetPropertyValue() {
		m_State.setPropertyValue("NAME", "jason");
		assertEquals("jason", m_State.getPropertyValue("NAME"));
	}

	/**
	 * Test method for {@link GTTlipse.VFSMEditor.model.Node#getStateType()}.
	 */
	public void testGetType() {
		assertEquals("STATE", m_State.getStateType());
	}

}
