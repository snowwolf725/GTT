package testing.macro;

import gtt.eventmodel.Argument;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import junit.framework.TestCase;

public class ComponentEventNodeTest extends TestCase {

	ComponentEventNode node;

	protected void setUp() throws Exception {
		super.setUp();
		ComponentNode cn = ComponentNode.create("JButton", "button1");
		node = new ComponentEventNode(cn);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testClone() {
		ComponentEventNode n2 = node.clone();
		assertEquals(node.getComponentPath(), n2.getComponentPath());
		assertEquals(node.getPath(), n2.getPath());
	}

	public void testSetEventType() {
		node.setEvent("type", 1);
		assertEquals(node.getEventType(), "type");
		assertEquals(node.getEventID(), 1);

		node.setEvent("type2", 2);
		assertEquals(node.getEventType(), "type2");
		assertEquals(node.getEventID(), 2);
	}

	public void testAddEventData() {
		assertEquals(node.getArguments().size(), 0);
		Argument arg = Argument.create("type", "name", "value");
		node.getArguments().add(arg);
		assertEquals(node.getArguments().size(), 1);
		
		Argument a = node.getArguments().get(0);
		assertEquals("type", a.getType());
		assertEquals("name", a.getName());
		assertEquals("value", a.getValue());
	}

	public void testGetEventDataValue() {
		assertEquals(node.getArguments().getValue("name1"), null);
		assertEquals(node.getArguments().getValue("name2"), null);
		assertEquals(node.getArguments().getValue("name3"), null);
	}


//	public void testGetComClassType() {
//		assertEquals(node.getComponentType(), "JButton");
//	}
//
//	public void testGetComName() {
//		assertEquals(node.getComponentName(), "button1");
//	}

}
