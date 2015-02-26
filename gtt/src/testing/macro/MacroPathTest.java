package testing.macro;

import gtt.macro.macroStructure.AbstractMacroNode;
import junit.framework.TestCase;

public class MacroPathTest extends TestCase {

	AbstractMacroNode node;
	
	protected void setUp() throws Exception {
		super.setUp();
		node = new MockAbstractMacroNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testGetComponentPath() {
		assertNotNull(node.getPath());
		assertEquals(node.getPath().list().size(), 1);
		assertEquals(node.getPath().list().get(0).toString(),
				"MockAbstractMacroNode");
		assertEquals(node.getPath().toString(), "MockAbstractMacroNode");

		node.setName("path1");
		assertEquals(node.getPath().list().get(0).toString(), "path1");
		assertEquals(node.getPath().toString(), "path1");
	}

	public void testGetComponentPath2() {
		MockAbstractMacroNode p = new MockAbstractMacroNode();
		p.setName("parent");
		p.add(node);

		assertNotNull(node.getPath());
		assertEquals(node.getPath().list().size(), 2);
		assertEquals(node.getPath().list().get(0).toString(), "parent");
		assertEquals(node.getPath().list().get(1).toString(),
				"MockAbstractMacroNode");
		assertEquals(node.getPath().toString(), "parent::MockAbstractMacroNode");

		MockAbstractMacroNode p2 = new MockAbstractMacroNode();
		p2.setName("parent2");
		p2.add(p);

		assertNotNull(node.getPath());
		assertEquals(node.getPath().list().size(), 3);
		assertEquals(node.getPath().list().get(0).toString(), "parent2");
		assertEquals(node.getPath().list().get(1).toString(), "parent");
		assertEquals(node.getPath().list().get(2).toString(),
				"MockAbstractMacroNode");
		assertEquals(node.getPath().toString(), "parent2::parent::MockAbstractMacroNode");

		assertNotNull(p.getPath());
		assertEquals(p.getPath().list().size(), 2);
		assertEquals(p.getPath().list().get(0).toString(), "parent2");
		assertEquals(p.getPath().list().get(1).toString(), "parent");
		assertEquals(p.getPath().toString(), "parent2::parent");
	}

}
