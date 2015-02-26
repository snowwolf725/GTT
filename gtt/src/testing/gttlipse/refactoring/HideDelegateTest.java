package testing.gttlipse.refactoring;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.HideDelegate;
import junit.framework.TestCase;

public class HideDelegateTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testIsUsable() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = new MacroComponentNode();
		invisibleRoot.add(root);
		root.setName("AUT");
		
		MacroEventNode event1 = new MacroEventNode();
		event1.setName("Event1");
		MacroEventNode event2 = new MacroEventNode();
		event2.setName("Event2");
		root.add(event1);
		root.add(event2);
		
		MacroEventCallerNode caller = new MacroEventCallerNode(event1.getPath().toString());
		event2.add(caller);
		
		// test
		HideDelegate refactor = new HideDelegate(root);
		assertTrue(refactor.isValid(caller, "test"));
		assertFalse(refactor.isValid(caller, ""));
		assertFalse(refactor.isValid(caller, "Event2"));
	}
	
	public void testAddMiddleMan() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = new MacroComponentNode();
		invisibleRoot.add(root);
		root.setName("AUT");
		
		MacroEventNode event1 = new MacroEventNode();
		event1.setName("Event1");
		event1.setParent(root);
		MacroEventNode event2 = new MacroEventNode();
		event2.setName("Event2");
		event2.setParent(root);
		root.add(event1);
		root.add(event2);
		
		Arguments argList = new Arguments();
		Argument arg1 = Argument.create("TYPE", "NAME1", "value1");
		Argument arg2 = Argument.create("TYPE", "NAME2", "value2");
		argList.add(arg1);
		argList.add(arg2);		
		event1.setArguments(argList);
		Arguments callerList = argList.clone();
		callerList.setValue(0, "1");
		callerList.setValue(1, "2");
		
		String path = event1.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		caller.setArguments(callerList);
		event2.add(caller);
		
		String name = "MIDDLEMAN";
		
		// test
		assertTrue(root.size() == 2);
		HideDelegate refactor = new HideDelegate(root);
		refactor.hideDelegate(caller, name);
		assertTrue(root.size() == 3);
		assertFalse(caller.getReferencePath().equals(path));
		MacroEventNode middleMan = (MacroEventNode) root.get(root.size() - 1);
		assertTrue(caller.getReferencePath().equals(middleMan.getPath().toString()));
		MacroEventCallerNode midCaller = (MacroEventCallerNode) middleMan.get(0);
		Arguments args = midCaller.getArguments();
		assertTrue(args.get(0).getValue().equals("@" + args.get(0).getName()));
		assertTrue(args.get(1).getValue().equals("@" + args.get(1).getName()));
	}
}
