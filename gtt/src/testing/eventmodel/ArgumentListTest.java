package testing.eventmodel;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import junit.framework.TestCase;

public class ArgumentListTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testArgu() {
		Arguments e = new Arguments();
		assertEquals(e.size(), 0);
		assertFalse(e.remove(-1));
		assertFalse(e.remove(0));
		assertFalse(e.remove(1));

		Argument arg;
		arg = Argument.create("int", "sleeptime", "100");
		e.add(arg);
		assertEquals(e.size(), 1);

		arg = Argument.create("double", "sleeptime", "100");
		e.add(arg);
		assertEquals(e.size(), 2);

		arg = Argument.create("long", "sleeptime", "100");
		e.add(arg);
		assertEquals(e.size(), 3);

		arg = Argument.create("String", "sleeptime", "100");
		e.add(arg);
		assertEquals(e.size(), 4);

		assertFalse(e.remove(-1));
		assertEquals(e.size(), 4);
		assertFalse(e.remove(e.size()));
		assertEquals(e.size(), 4);
		assertTrue(e.remove(e.size() - 1));
		assertEquals(e.size(), 3);
		assertTrue(e.remove(1));
		assertEquals(e.size(), 2);

		e.clear();
		assertEquals(0, e.size());
	}

	public void testArgu2() {
		Arguments e = new Arguments();
		Argument arg = Argument.create("int", "sleep", "100");
		e.add(arg);
		assertEquals(e.size(), 1);

		Argument arg2 = e.get(0);
		assertEquals(arg2.getType(), "int");
		assertEquals(arg2.getName(), "sleep");
		assertEquals(arg2.getValue(), "100");
		assertEquals(arg.getType(), arg2.getType());
		assertEquals(arg.getName(), arg2.getName());
		assertEquals(arg.getValue(), arg2.getValue());
	}

	public void testGetArgu() {
		Arguments e1 = new Arguments();

		assertNull(e1.find("i"));
		assertNull(e1.find("j"));
		assertNull(e1.get(-1));
		assertNull(e1.get(0));
		assertNull(e1.get(1));
		assertNull(e1.get(2));

		// 把计
		e1.add(new Argument("int", "i"));
		e1.add(new Argument("int", "j"));
		assertNotNull(e1.find("i"));
		assertNotNull(e1.find("j"));

		assertNull(e1.get(-1));
		assertEquals(e1.get(0).getName(), "i");
		assertEquals(e1.get(1).getName(), "j");
		assertNull(e1.get(2));
	}

	public void testGetArguValue() {
		Arguments e1 = new Arguments();

		// 把计
		e1.add(new Argument("int", "i"));
		e1.add(new Argument("int", "j"));
		assertNotNull(e1.find("i"));
		assertNotNull(e1.find("j"));

		assertFalse(e1.setValue(-1, "0"));
		assertFalse(e1.setValue(2, "0"));
		assertTrue(e1.setValue(0, "99"));
		assertEquals(e1.get(0).getValue(), "99");

		assertTrue(e1.setValue(1, "123"));
		assertEquals(e1.get(1).getValue(), "123");
	}

	public void testToString() {
		Arguments e1 = new Arguments();
		assertEquals(e1.toString(), "()");

		// 把计
		e1.add(new Argument("int", "i"));
		assertEquals("(i)", e1.toString());
		e1.setValue(0, "123");
		assertEquals(e1.toString(), "(123)");

		// 把计
		e1.add(new Argument("int", "j"));
		assertEquals("(123, j)", e1.toString());
		e1.setValue(1, "999");
		assertEquals(e1.toString(), "(123, 999)");

		// 把计 - ⊿Τvalue 碞陪ボname
		e1.clear();
		e1.add(new Argument("int", "name1"));
		assertEquals("(name1)", e1.toString());
		e1.setValue(0, "value");
		assertEquals(e1.toString(), "(value)");

	}

	public void testGetValue() {
		Arguments e1 = new Arguments();
		assertEquals(e1.getValue("i"), null);
		assertEquals(e1.getValue("j"), null);

		// 把计
		e1.add(new Argument("int", "i"));
		assertEquals(e1.getValue("i"), null);
		e1.setValue(0, "123");
		assertEquals(e1.getValue("i"), "123");
	}

	public void testClone() {
		Arguments e1 = new Arguments();
		Arguments e2 = e1.clone();

		// 把计
		e1.add(new Argument("int", "i"));
		e1.add(new Argument("int", "j"));

		assertNull(e2.find("i"));
		assertNull(e2.find("j"));
		e2 = e1.clone();
		assertEquals(e1.get(0).getName(), e2.get(0).getName());
		assertEquals(e1.get(1).getName(), e2.get(1).getName());
	}

}
