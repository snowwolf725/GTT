package testing.gttlipse.vfsm;

import junit.framework.TestCase;

import org.eclipse.draw2d.geometry.Dimension;

import gttlipse.vfsmEditor.model.State;

public class StateTest extends TestCase {

	State state;
	protected void setUp() throws Exception {
		super.setUp();
		state = new State();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetSize() {
		state.setDimension(new Dimension(100,100));
		assertEquals(new Dimension(100,100), state.getDimension());
	}

	public void testGetSize() {
		state.setDimension(new Dimension(100,100));
		assertEquals(new Dimension(100,100), state.getDimension());
	}

	public void testGetName() {
		state.setName("jason");
		assertEquals("jason", state.getName());
	}

	public void testSetName() {
		state.setName("jason");
		assertEquals("jason", state.getName());
	}

	public void testGetType() {
		assertEquals("STATE", state.getStateType());
	}

}
