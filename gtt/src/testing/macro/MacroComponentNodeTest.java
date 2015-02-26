package testing.macro;

import gtt.macro.macroStructure.MacroComponentNode;
import junit.framework.TestCase;

public class MacroComponentNodeTest extends TestCase {

	MacroComponentNode node;

	String name = "MACRO";

	protected void setUp() throws Exception {
		super.setUp();
		node = MacroComponentNode.create(name);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testSize() {
		// 不多不少，只會是2個
//		assertEquals(node.size(), 2);
	}

	public void testIsContainer() {
		assertTrue(node.isContainer());
	}

	public void testSetName() {
		assertEquals(node.getName(), name);
		node.setName("name2");
		assertEquals(node.getName(), "name2");
	}

	public void testGetName() {
		assertEquals(node.getName(), name);
	}

	public void testToString() {
		assertEquals(node.toString(), name);
	}

//	public void testIdentificationName() {
//		assertEquals(node.identifier(), "Macro::" + node.getName());
//
//	}

	public void testClone() {
		MacroComponentNode n2 = node.clone();
		assertEquals(node.getName(), n2.getName());
	}

//	public void testGetComponentBranch() {
//		assertTrue(node.getComponentBranch() != null);
//	}
//
//	public void testGetEventBranch() {
//		assertTrue(node.getEventBranch() != null);
//	}

	public void testGetMacroEventNode() {
		assertEquals(node.getParent(), null);
	}

}
