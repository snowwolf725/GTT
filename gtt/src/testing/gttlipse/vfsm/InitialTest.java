package testing.gttlipse.vfsm;

import junit.framework.TestCase;
import gttlipse.vfsmEditor.model.Initial;

public class InitialTest extends TestCase {

	Initial initial;
	protected void setUp() throws Exception {
		initial = new Initial();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetName() {
		initial.setName("Jason");
		assertEquals("Jason", initial.getName());
	}

	public void testSetName() {
		initial.setName("Jason");
		assertEquals("Jason", initial.getName());
	}

	public void testGetType() {
		assertEquals("INITIAL", initial.getStateType());
	}
}
