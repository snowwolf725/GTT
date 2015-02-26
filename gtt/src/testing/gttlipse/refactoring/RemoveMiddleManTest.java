package testing.gttlipse.refactoring;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.refactoring.macro.RemoveMiddleMan;
import junit.framework.TestCase;

public class RemoveMiddleManTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testUsable() {
		// initial		
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		MacroComponentNode macro1 = MacroComponentNode.create("MACRO1");
		MacroComponentNode macro2 = MacroComponentNode.create("MACRO2");
		MacroComponentNode macro3 = MacroComponentNode.create("MACRO3");
		invisibleRoot.add(root);
		root.add(macro1);
		macro1.add(macro2);
		macro2.add(macro3);
		MacroEventNode event1 = new MacroEventNode("EVENT1");
		MacroEventNode event2 = new MacroEventNode("EVENT2");
		ComponentNode com1 = ComponentNode.create();
		com1.setName("COM1");
		ComponentNode com2 = ComponentNode.create();
		com1.setName("COM2");
		macro2.add(event1);
		macro2.add(event2);
		macro2.add(com1);
		macro2.add(com2);
		ComponentEventNode comEve1 = new ComponentEventNode(com1);
		ComponentEventNode comEve2 = new ComponentEventNode(com1);
		ViewAssertNode viewAssert1 = new ViewAssertNode(com2);
		event1.add(comEve1);
		event1.add(viewAssert1);
		event2.add(comEve2);
		MacroEventNode event3 = new MacroEventNode("EVENT3");
		macro3.add(event3);
		MacroEventNode event4 = new MacroEventNode("EVENT4");
		macro1.add(event4);
		String path = event1.getPath().toString();
		MacroEventCallerNode caller1 = new MacroEventCallerNode(path);
		event3.add(caller1);
		path = event3.getPath().toString();
		MacroEventCallerNode caller2 = new MacroEventCallerNode(path);
		event4.add(caller2);
		MacroEventNode event5 = new MacroEventNode("EVENT5");
		path = event4.getPath().toString();
		MacroEventCallerNode caller3 = new MacroEventCallerNode(path);
		event5.add(caller3);
		macro1.add(event5);
		
		// assert
		RemoveMiddleMan refactor = new RemoveMiddleMan(root);
		assertTrue(refactor.isValid(event4));
		assertFalse(refactor.isValid(event1));
	}
	
	public void testRemoveMiddleMan() {
		// initial		
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		MacroComponentNode macro1 = MacroComponentNode.create("MACRO1");
		MacroComponentNode macro2 = MacroComponentNode.create("MACRO2");
		MacroComponentNode macro3 = MacroComponentNode.create("MACRO3");
		invisibleRoot.add(root);
		root.add(macro1);
		macro1.add(macro2);
		macro2.add(macro3);
		MacroEventNode event1 = new MacroEventNode("EVENT1");
		MacroEventNode event2 = new MacroEventNode("EVENT2");
		ComponentNode com1 = ComponentNode.create();
		com1.setName("COM1");
		ComponentNode com2 = ComponentNode.create();
		com1.setName("COM2");
		macro2.add(event1);
		macro2.add(event2);
		macro2.add(com1);
		macro2.add(com2);
		ComponentEventNode comEve1 = new ComponentEventNode(com1);
		ComponentEventNode comEve2 = new ComponentEventNode(com1);
		ViewAssertNode viewAssert1 = new ViewAssertNode(com2);
		event1.add(comEve1);
		event1.add(viewAssert1);
		event2.add(comEve2);
		MacroEventNode event3 = new MacroEventNode("EVENT3");
		macro3.add(event3);
		MacroEventNode event4 = new MacroEventNode("EVENT4");
		macro1.add(event4);
		// argument
		Arguments args = new Arguments();
		Argument arg1 = Argument.create("TYPE", "N1", "");
		Argument arg2 = Argument.create("TYPE", "N2", "");
		args.add(arg1);
		args.add(arg2);
		event3.setArguments(args.clone());
		String path = event1.getPath().toString();
		MacroEventCallerNode caller1 = new MacroEventCallerNode(path);
		event3.add(caller1);
		path = event3.getPath().toString();
		MacroEventCallerNode caller2 = new MacroEventCallerNode(path);
		args.get(0).setValue("@M1");
		args.get(1).setValue("TEST");
		caller2.setArguments(args.clone());
		event4.add(caller2);
		args.get(0).setName("M1");
		args.remove(1);
		event4.setArguments(args.clone());
		MacroEventNode event5 = new MacroEventNode("EVENT5");
		path = event4.getPath().toString();
		MacroEventCallerNode caller3 = new MacroEventCallerNode(path);
		args.get(0).setValue("TTT");
		caller3.setArguments(args.clone());
		event5.add(caller3);
		macro1.add(event5);
		
		// assert
		path = event3.getPath().toString();
		MacroEventCallerNode verifyCaller = (MacroEventCallerNode) event5.get(0);
		assertFalse(verifyCaller.getReferencePath().equals(path));
		RemoveMiddleMan refactor = new RemoveMiddleMan(root);
		refactor.removeMiddleMan(event4);
		assertTrue(event4.getParent() == null);
		verifyCaller = (MacroEventCallerNode) event5.get(0);
		assertTrue(verifyCaller.getReferencePath().equals(path));
		assertTrue(verifyCaller.getArguments().size() == 2);
		assertTrue(verifyCaller.getArguments().get(0).getValue().equals("TTT"));
		assertTrue(verifyCaller.getArguments().get(1).getValue().equals("TEST"));
	}
}
