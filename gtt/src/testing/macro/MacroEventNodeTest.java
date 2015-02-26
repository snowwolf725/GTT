package testing.macro;

import gtt.eventmodel.Argument;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import junit.framework.TestCase;

public class MacroEventNodeTest extends TestCase {

	MacroEventNode node;
	Argument arg = Argument.create("type", "name", "value");
	Argument arg2 = Argument.create("type2", "name2", "value2");
	Argument arg3 = Argument.create("a", "b", "c");


	protected void setUp() throws Exception {
		super.setUp();
		node = new MacroEventNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testSetName() {
		assertEquals(node.getName(), "MacroEvent");
		node.setName("123");
		assertEquals(node.getName(), "123");
	}

	public void testAddArgument() {
		assertEquals(node.getArguments().size(), 0);
		node.getArguments().add(arg);
		assertEquals(node.getArguments().size(), 1);
		assertTrue(node.getArguments().get(0) != null);
		assertEquals(node.getArguments().get(0).getName(), "name");
		assertEquals(node.getArguments().get(0).getType(), "type");
		assertEquals(node.getArguments().get(0).getValue(), "value");

		node.getArguments().add(arg2);
		assertEquals(node.getArguments().size(), 2);

		assertTrue(node.getArguments().get(0) != null);
		assertEquals(node.getArguments().get(0).getName(), "name");
		assertEquals(node.getArguments().get(0).getType(), "type");
		assertEquals(node.getArguments().get(0).getValue(), "value");

		assertTrue(node.getArguments().get(1) != null);
		assertEquals(node.getArguments().get(1).getName(), "name2");
		assertEquals(node.getArguments().get(1).getType(), "type2");
		assertEquals(node.getArguments().get(1).getValue(), "value2");
	}

	public void testGetArguments() {
		assertEquals(node.getArguments().size(), 0);
		assertEquals(node.getArguments().size(), 0);

		node.getArguments().add(arg);
		assertEquals(node.getArguments().size(), 1);
		assertEquals(node.getArguments().size(), 1);

		node.getArguments().add(arg2);
		assertEquals(node.getArguments().size(), 2);
		assertEquals(node.getArguments().size(), 2);

		node.getArguments().clear();
		assertEquals(node.getArguments().size(), 0);
		assertEquals(node.getArguments().size(), 0);
	}

	public void testGetArgument() {
		assertEquals(node.getArguments().size(), 0);

		node.getArguments().add(arg);
		assertEquals(node.getArguments().size(), 1);
		assertEquals(node.getArguments().get(-1), null);
		assertTrue(node.getArguments().get(0) != null);
		assertEquals(node.getArguments().get(0).getName(), "name");
		assertEquals(node.getArguments().get(0).getType(), "type");
		assertEquals(node.getArguments().get(0).getValue(), "value");
		assertEquals(node.getArguments().get(1), null);
		assertEquals(node.getArguments().get(2), null);

		node.getArguments().add(arg2);
		assertEquals(node.getArguments().size(), 2);
		assertEquals(node.getArguments().get(-1), null);
		assertTrue(node.getArguments().get(0) != null);
		assertEquals(node.getArguments().get(0).getName(), "name");
		assertEquals(node.getArguments().get(0).getType(), "type");
		assertEquals(node.getArguments().get(0).getValue(), "value");
		assertTrue(node.getArguments().get(1) != null);
		assertEquals(node.getArguments().get(1).getName(), "name2");
		assertEquals(node.getArguments().get(1).getType(), "type2");
		assertEquals(node.getArguments().get(1).getValue(), "value2");
		assertEquals(node.getArguments().get(2), null);
	}

	public void testToString() {
		assertEquals(node.getArguments().size(), 0);
		node.setName("event");

		assertEquals(node.toString(), "event()");

		node.getArguments().add(arg);
		assertEquals(node.getArguments().size(), 1);
		assertEquals(node.toString(), "event(name)");

		node.getArguments().add(arg2);
		assertEquals(node.getArguments().size(), 2);
		assertEquals("event(name, name2)", node.toString());

		node.getArguments().add(arg3);
		assertEquals(3, node.getArguments().size());
		assertEquals( "event(name, name2, b)", node.toString());
	}

	public void testCopy() {
		MacroEventNode node2 = new MacroEventNode();
		node2.setName("node");

		node2 = node.clone();
		assertEquals(node.getName(), node2.getName());
		assertEquals(node.getArguments().size(), node2.getArguments().size());

		node.getArguments().add(Argument.create("t1", "n1", "v1"));
		node.getArguments().add(Argument.create("t2", "n2", "v2"));
		node.getArguments().add(Argument.create("t3", "n3", "v3"));

		node2 = node.clone();
		assertEquals(node.getName(), node2.getName());
		assertEquals(node.getArguments().size(), node2.getArguments().size());
		assertEquals(node.getArguments().get(0).getName(), node2.getArguments().get(0)
				.getName());
		assertEquals(node.getArguments().get(0).getType(), node2.getArguments().get(0)
				.getType());
		assertEquals(node.getArguments().get(0).getValue(), node2.getArguments().get(0)
				.getValue());
		assertEquals(node.getArguments().get(1).getName(), node2.getArguments().get(1)
				.getName());
		assertEquals(node.getArguments().get(1).getType(), node2.getArguments().get(1)
				.getType());
		assertEquals(node.getArguments().get(1).getValue(), node2.getArguments().get(1)
				.getValue());
		assertEquals(node.getArguments().get(2).getName(), node2.getArguments().get(2)
				.getName());
		assertEquals(node.getArguments().get(2).getType(), node2.getArguments().get(2)
				.getType());
		assertEquals(node.getArguments().get(2).getValue(), node2.getArguments().get(2)
				.getValue());
	}

	public void testIsContainer() {
		assertTrue(node.isContainer());
	}
	
	public void testEquals() {
		MacroEventNode me1 = new MacroEventNode("name1");
		MacroEventNode me2 = new MacroEventNode("name2");

		assertEquals("name1", me1.getPath().toString());
		assertEquals("name2", me2.getPath().toString());
		assertFalse(me1.equals(me2));		
	}
	
	public void testEquals2() {
		MacroComponentNode mc = MacroComponentNode.create("ROOT");
		MacroEventNode me1 = new MacroEventNode("name1");
		MacroEventNode me2 = new MacroEventNode("name2");
		mc.add(me1);
		mc.add(me2);

		assertEquals("ROOT::name1", me1.getPath().toString());
		assertEquals("ROOT::name2", me2.getPath().toString());
		assertFalse(me1.equals(me2));		
	}

}
