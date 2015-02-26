package testing.macro;

import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.macroStructure.ComponentNode;

import javax.swing.JComponent;

import junit.framework.TestCase;

public class ComponentNodeTest extends TestCase {

	ComponentNode node;
	SwingComponent data;
//	ComponentData cd;

	protected void setUp() throws Exception {
		super.setUp();
		data = SwingComponent.createDefault();
		data.setName("SwingComponent");
		data.setType("javax.swing.JComponent");
		node = ComponentNode.create(data);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testGetName() {
		assertEquals(node.getName(), data.getName());
	}

	public void testToString() {
		assertEquals(node.toString(), data.getName() + ":JComponent");
	}

	public void testAddNodeAbstractMacroNode() {
		assertFalse(node.add(null));
	}

	public void testAddNodeAbstractMacroNodeInt() {
		assertFalse(node.add(null, -1));
		assertFalse(node.add(null, 0));
		assertFalse(node.add(null, 1));
		assertFalse(node.add(null, 2));
	}

	public void testClone() {
		ComponentNode n2 = node.clone();
		assertEquals(node.getName(), n2.getName());
		assertEquals(node.getComponent(), n2.getComponent());

		ComponentNode n3 = node.clone();
		assertEquals(node.getName(), n3.getName());
		assertEquals(node.getComponent(), n3.getComponent());

		assertEquals(n2.getName(), n3.getName());
		assertEquals(n2.getComponent(), n3.getComponent());
}

	public void testGetComponentData() {
		assertEquals(node.getComponent(), data);
	}

	public void testGetClassType() {
		assertEquals(node.getType(), "javax.swing.JComponent");
	}

	public void testGetComponentClass() {
		assertEquals(node.getComponentClass(), JComponent.class);
	}

}
