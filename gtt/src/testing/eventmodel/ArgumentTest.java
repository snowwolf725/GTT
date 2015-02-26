package testing.eventmodel;

import gtt.eventmodel.Argument;
import junit.framework.TestCase;

public class ArgumentTest extends TestCase {

	Argument a;

	String type = "TYPE";
	String name = "NAME";
	String value = "VALUE";

	protected void setUp() throws Exception {
		super.setUp();
		a = Argument.create(type, name, value);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		a = null;
	}

	public void testClone() {
		Argument arg = a.clone();
		assertNotSame(arg, a);

		assertEquals(arg.getType(), a.getType());
		assertEquals(arg.getName(), a.getName());
		assertEquals(arg.getValue(), a.getValue());
	}

	public void testGetType() {
		assertEquals(type, a.getType());
		final String other_type = "other_type";
		a.setType(other_type);
		assertEquals(other_type, a.getType());
	}

	public void testGetName() {
		assertEquals(name, a.getName());
		final String other_name = "other_name";
		a.setName(other_name);
		assertEquals(other_name, a.getName());
	}

	public void testSetValue() {
		assertEquals(value, a.getValue());
		final String other_value = "other_value";
		a.setValue(other_value);
		assertEquals(other_value, a.getValue());
	}

	public void testCreator() {
		Argument arg = Argument.create(type, name);
		assertNotNull(arg);
		assertEquals(type, arg.getType());
		assertEquals(name, arg.getName());
		assertFalse(value == arg.getValue());
	}

}
