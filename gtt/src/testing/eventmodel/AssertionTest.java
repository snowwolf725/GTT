package testing.eventmodel;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.Assertion.CompareOperator;

import java.lang.reflect.Method;

import junit.framework.TestCase;

public class AssertionTest extends TestCase {

	Assertion a;

	protected void setUp() throws Exception {
		super.setUp();
		a = new Assertion();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		a = null;
	}

	public void testClone() {
		Assertion a1 = new Assertion();
		Assertion a2 = a1.clone();
		assertEquals(a1, a2);
		assertFalse(a1.equals(new String()));
	}

	public void testClone2() {
		Assertion a1 = new Assertion();

		a1.getArguments().add(new Argument("int", "i"));
		a1.getArguments().add(new Argument("int", "j"));

		Assertion a2 = a1.clone();
		assertEquals(a1, a2);
	}

	public void testToString2() {
		a.setMessage(null);
		assertEquals(a.toString(), Assertion.NULL_METHOD);
	}

	public void testToString() {
		Class<?> c = a.getClass();
		assertEquals(c.getName(), "gtt.eventmodel.Assertion");

		try {
			Method m = c.getMethod("toString", new Class[] {});
			assertEquals(m.getName(), "toString");
			a.setMethod(m);
			assertEquals(a.toString(), "toString()");

			a.getArguments().add(new Argument("int", "a"));
			assertEquals(a.toString(), "toString(a)");
			a.getArguments().add(new Argument("int", "b"));
			assertEquals(a.toString(), "toString(a, b)");

			a.setValue("123.");
			assertEquals("toString(a, b)==123.", a.toString());

		} catch (SecurityException e) {
			assertTrue(false);
		} catch (NoSuchMethodException e) {
			assertTrue(false);
		}

	}

	public void testGetArgumentTypes() {
		assertEquals(a.getArguments().size(), 0);
		assertEquals(a.typeClasses().length, 0);

		try {
			a.getArguments().add(
					new Argument("gtt.eventmodel.Assertion", "i"));
			assertEquals(a.typeClasses().length, 1);
			assertEquals(a.typeClasses()[0].toString(),
					"class gtt.eventmodel.Assertion");
		} catch (Exception ep) {
			fail(ep.getMessage());
		}
	}

	public void testGetArgumentTypes2() {
		assertEquals(a.getArguments().size(), 0);
		assertEquals(a.typeClasses().length, 0);

		try {
			// 改試不存在的type
			a.getArguments().add(new Argument("NO_SUCH_TYPE", "i"));
			a.getArguments().add(new Argument("NO_SUCH_TYPE", "j"));
			assertEquals(a.typeClasses().length, 2);
			assertTrue(a.typeClasses()[0] == null);
			assertTrue(a.typeClasses()[1] == null);
		} catch (Exception ep) {
			fail(ep.toString());
		}
	}

	public void testSetMethodName() {
		assertEquals("", a.getMethodName());
		final String method_name = "METHOD_NAME";
		a.setMethodName(method_name);
		assertEquals(method_name, a.getMethodName());
	}

	public void testEquals_self() {
		assertTrue(a.equals(a));
	}

	public void testEquals() {
		Assertion a1 = a.clone();
		assertEquals(a1, a);

		// only msg different
		a1.setMessage("NO_SUCH_MESSAGE");
		assertFalse(a.equals(a1));

		// only value different
		a1 = a.clone();
		a1.setValue("NO_SUCH_VALUE");
		assertFalse(a.equals(a1));

		// only method different
		a1 = a.clone();
		a1.setMethod(a1.getClass().getMethods()[0]);
		assertFalse(a.equals(a1));
	}

	public void testGetOperatorString() {
		assertEquals("==", Assertion.getOperatorString(CompareOperator.EqualTo));
		assertEquals("!=", Assertion.getOperatorString(CompareOperator.NotEqual));
		assertEquals(">", Assertion.getOperatorString(CompareOperator.GreaterThan));
		assertEquals(">=", Assertion.getOperatorString(CompareOperator.GreaterThanOrEqual));
		assertEquals("<", Assertion.getOperatorString(CompareOperator.LessThan));
		assertEquals("<=", Assertion.getOperatorString(CompareOperator.LessThanOrEqual));

		assertEquals("isNotNull", Assertion.getOperatorString(CompareOperator.isNotNull));
		assertEquals("isNull", Assertion.getOperatorString(CompareOperator.isNull));
	}
	
	public void testSetCompareOperator() {
		a.setCompareOperator("==");
		assertEquals(CompareOperator.EqualTo, a.getCompareOperator());
		
		a.setCompareOperator("!=");
		assertEquals(CompareOperator.NotEqual, a.getCompareOperator());
		
		a.setCompareOperator(">");
		assertEquals(CompareOperator.GreaterThan, a.getCompareOperator());
		
		a.setCompareOperator(">=");
		assertEquals(CompareOperator.GreaterThanOrEqual, a.getCompareOperator());
		
		a.setCompareOperator("<");
		assertEquals(CompareOperator.LessThan, a.getCompareOperator());
		
		a.setCompareOperator("<=");
		assertEquals(CompareOperator.LessThanOrEqual, a.getCompareOperator());
		
		a.setCompareOperator("isNull");
		assertEquals(CompareOperator.isNull, a.getCompareOperator());
		
		a.setCompareOperator("isNotNull");
		assertEquals(CompareOperator.isNotNull, a.getCompareOperator());
		
	}
}
