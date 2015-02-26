package testing.gttlipse.refactoring;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.EditMacroEventParameter;
import junit.framework.TestCase;

public class EditParameterTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testEditParameter() {
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
		
		Arguments cloneList = argList.clone();
		cloneList.remove(cloneList.size() - 1);
		Argument arg3 = Argument.create("TYPE", "NAME3", "value3");
		cloneList.add(arg3);
		
		String path = event1.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		caller.setArguments(callerList);
		event2.add(caller);
		
		// assert
		EditMacroEventParameter editParameter = new EditMacroEventParameter(root);
		assertTrue(editParameter.isValid(event1.getArguments(), cloneList));
		editParameter.editParameter(event1, cloneList);
		Arguments list = event1.getArguments();
		assertTrue(list.size() == 2);
		assertTrue(list.get(0).getName().equals("NAME1"));
		assertTrue(list.get(1).getName().equals("NAME3"));
		list = caller.getArguments();
		assertTrue(list.size() == 2);
		assertTrue(list.get(0).getName().equals("NAME1"));
		assertTrue(list.get(0).getValue().equals("1"));
		assertTrue(list.get(1).getName().equals("NAME3"));
		assertTrue(list.get(1).getValue().equals("value3"));
	}
}
