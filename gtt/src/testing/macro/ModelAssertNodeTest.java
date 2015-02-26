package testing.macro;

import gtt.macro.macroStructure.ModelAssertNode;
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

	public void testClone() {
		ModelAssertNode n2 = node.clone();
		assertEquals(node.getClassUrl(), n2.getClassUrl());
		assertEquals(node.getPath(), n2.getPath());
		assertEquals(node.getMethod(), n2.getMethod());
	}

	public void testGetClassUrl() {
		assertEquals(node.getClassUrl(), null);
	}

	public void testSetClassUrlAndMethod() {
		assertEquals(node.getClassUrl(), null);
		assertEquals(node.getMethod(), null);

		node.setInfo("url", "method");
		assertEquals(node.getClassUrl(), "url");
		assertEquals(node.getMethod(), "method");
	}

}
