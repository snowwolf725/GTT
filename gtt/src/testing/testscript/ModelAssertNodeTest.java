package testing.testscript;

import gtt.testscript.ModelAssertNode;

import java.lang.reflect.Method;

import junit.framework.TestCase;

public class ModelAssertNodeTest extends TestCase {

	ModelAssertNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = new ModelAssertNode();
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

		public void visit(ModelAssertNode node) {
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
		assertEquals(node.toSimpleString(), ModelAssertNode.NULL_STRING);
	}

	public void testToDetailString() {
		assertEquals(node.toDetailString(), ModelAssertNode.NULL_STRING);
	}

	public void testToString() {
		assertEquals(node.toString(), ModelAssertNode.NULL_STRING);

		Method m = (node.getClass().getDeclaredMethods())[0];
		node.setAssertMethod(m);

		assertEquals(node.toString(), m.toString());
	}

	public void testClone() {
		node.setClassPath("classpath");
		ModelAssertNode mn = node.clone();
		assertEquals(node.getClassPath(), mn.getClassPath());
		assertEquals(node.getAssertMethod(), mn.getAssertMethod());
	}

	public void testSetClassPath() {
		assertTrue(node.getClassPath() == null);
		node.setClassPath("classpath");
		assertEquals(node.getClassPath(), "classpath");
	}

	public void testSetAssertMethod() {
		Method m = (node.getClass().getDeclaredMethods())[0];
		assertTrue(node.getAssertMethod() == null);
		node.setAssertMethod(m);
		assertEquals(node.getAssertMethod(), m);
	}

}
