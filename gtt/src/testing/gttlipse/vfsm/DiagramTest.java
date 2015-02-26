package testing.gttlipse.vfsm;

import junit.framework.TestCase;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.State;

public class DiagramTest extends TestCase {

	Diagram diagram;

	protected void setUp() throws Exception {
		diagram = new Diagram();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddNode() {
		State node = State.createDummyState();
		assertTrue(diagram.getMainState().getAll().isEmpty());
		diagram.getMainState().addState(node);
		assertFalse(diagram.getMainState().getAll().isEmpty());
	}

	public void testRemoveNode() {
		State node = State.createDummyState();
		assertTrue(diagram.getMainState().getAll().isEmpty());
		diagram.getMainState().addState(node);
		assertFalse(diagram.getMainState().getAll().isEmpty());
		diagram.removeNode(node);
		assertTrue(diagram.getMainState().getAll().isEmpty());
	}

	public void testGetParent() {
		assertTrue(true);
	}

	public void testGetNodes() {
		State node = State.createDummyState();
		assertTrue(diagram.getMainState().getAll().size() == 0);
		diagram.getMainState().addState(node);
		assertTrue(diagram.getMainState().getAll().size() == 1);
		diagram.removeNode(node);
		assertTrue(diagram.getMainState().getAll().size() == 0);
	}

	public void testClearNodes() {
		State node = State.createDummyState();
		assertTrue(diagram.getMainState().getAll().size() == 0);
		diagram.getMainState().addState(node);
		assertTrue(diagram.getMainState().getAll().size() == 1);
		diagram.getMainState().clear();
		assertTrue(diagram.getMainState().getAll().size() == 0);
	}

}
