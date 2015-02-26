package testing.gttlipse.refactoring;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.InlineParameter;
import junit.framework.TestCase;

public class InlineParameterTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInlineParameter() {
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
		Argument arg1 = Argument.create("TYPE", "name1", "value1");
		Argument arg2 = Argument.create("TYPE", "name2", "value2");
		argList.add(arg1);
		argList.add(arg2);		
		event1.setArguments(argList);
		
		ComponentNode com = ComponentNode.create();
		root.add(com);
		ComponentEventNode comeve = new ComponentEventNode(com);
		Arguments args = new Arguments();
		Argument arg3 = Argument.create("TYPE", "ARG1", "V3");
		Argument arg4 = Argument.create("TYPE", "ARG2", "V4");
		args.add(arg3);
		args.add(arg4);
		comeve.setArguments(args);
		comeve.setDyValue("@name1");
		event1.add(comeve);
		
		Arguments callerList = argList.clone();
		callerList.setValue(0, "1");
		callerList.setValue(1, "2");
		String path = event1.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		caller.setArguments(callerList);
		event2.add(caller);
		
		Arguments inlineArgs = new Arguments();
		Argument inArg = arg1.clone();
		inArg.setValue("");
		inlineArgs.add(inArg);
		
		// test
		InlineParameter refactor = new InlineParameter(root);
		assertFalse(refactor.isValid(inlineArgs));
		inArg.setValue("V1");
		inlineArgs.clear();
		inlineArgs.add(inArg);		
		assertTrue(refactor.isValid(inlineArgs));
		
		refactor.inlineParameter(event1, inlineArgs);
		assertTrue(args.get(0).getValue().equals("V3"));
		assertTrue(args.get(1).getValue().equals("V4"));
		assertTrue(comeve.getDyValue().equals("V1"));
		assertTrue(caller.getArguments().size() == 1);
	}
}
