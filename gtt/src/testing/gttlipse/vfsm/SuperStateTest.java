package testing.gttlipse.vfsm;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Dimension;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;

public class SuperStateTest extends TestCase {
	SuperState superstate;
	protected void setUp() throws Exception {
		superstate = new SuperState();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetSize() {
		superstate.setDimension(new Dimension(5,5));
		superstate.setCollapsed(false);
		assertEquals(new Dimension(5, 5), superstate.getDimension());
		superstate.setDimension(new Dimension(30,50));
		assertEquals(new Dimension(30,50), superstate.getDimension());
	}

	public void testSuperState() {
		assertEquals("XOR", superstate.getName());
		assertEquals("SUPERSTATE", superstate.getStateType());
		superstate.setCollapsed(true);
		assertEquals(new Dimension(160, 160), superstate.getDimension());
		superstate.setCollapsed(false);
		assertEquals(new Dimension(160, 160), superstate.getDimension());
	}

	public void testAddNodeNode() {
		State node1 = State.createDummyState();
		State node2 = State.createDummyState();
		assertEquals(0, superstate.getAll().size());
		superstate.addState(node1);
		assertEquals(1, superstate.getAll().size());
		superstate.addState(node2);
		assertEquals(2, superstate.getAll().size());
	}

	public void testAddNodeIntNode() {
		State node1 = State.createDummyState();
		State node2 = State.createDummyState();
		State node3 = State.createDummyState();
		assertEquals(0, superstate.getAll().size());
		superstate.addState(0, node1);
		assertEquals(1, superstate.getAll().size());
		superstate.addState(1, node2);
		assertEquals(2, superstate.getAll().size());
		superstate.addState(2, node3);
		assertEquals(3, superstate.getAll().size());
	}

	public void testRemoveNode() {
		State node = State.createDummyState();
		superstate.addState(node);
		superstate.addState(node);
		assertEquals(2, superstate.getAll().size());
		superstate.removeState(node);
		assertEquals(1, superstate.getAll().size());
		superstate.removeState(node);
		assertEquals(0, superstate.getAll().size());
	}

	public void testGetNodes() {
		State node = State.createDummyState();
		superstate.addState(node);
		assertEquals(1, superstate.getAll().size());
		superstate.getAll().clear();
		assertEquals(0, superstate.getAll().size());
		AbstractSuperState ss = new SuperState();
		ss.addState(node);
		superstate.addState(node);
		assertEquals(ss.getAll(), superstate.getAll());
	}

	public void testSetNodes() {
		List<State> nodelist = new ArrayList<State>();
		State node = State.createDummyState();
		nodelist.add(node);
		nodelist.add(node);
		superstate.setAllState(nodelist);
		assertEquals(nodelist, superstate.getAll());
	}

	public void testSetCollapsed() {
		superstate.setCollapsed(true);
		// always false
		assertFalse(superstate.getCollapsed());
		superstate.setCollapsed(false);
		// always false
		assertFalse(superstate.getCollapsed());
	}
}
