package testing.gttlipse.vfsm;

import junit.framework.TestCase;
import gttlipse.vfsmEditor.model.Final;

public class FinalTest extends TestCase {

	Final _final;
	protected void setUp() throws Exception {
		_final = new Final();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetName() {
		_final.setName("Jason");
		assertEquals("Jason", _final.getName());
		_final.setName("Wang");
		assertEquals("Wang", _final.getName());
	}

	public void testSetName() {
		_final.setName("Jason");
		assertEquals("Jason", _final.getName());
		_final.setName("Wang");
		assertEquals("Wang", _final.getName());
	}

	public void testGetType() {
		assertEquals("FINAL",_final.getStateType());
	}

}
