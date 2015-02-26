package testing.gttlipse.refactoring;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.InlineMacroEvent;
import junit.framework.TestCase;

public class InlineMacroEventTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCheckUsable() {
//		// initial
//		MacroComponentNode root = new MacroComponentNode();
//		MacroEventNode event = new MacroEventNode();
//		event.setName("EVENT1");
//		MacroEventNode event2 = new MacroEventNode();
//		event2.setName("EVENT2");
//		root.add(event);
//		root.add(event2);
//		MacroComponentNode macro = new MacroComponentNode();
//		root.add(macro);
//		MacroEventNode event3 = new MacroEventNode();
//		event3.setName("EVENT3");
//		macro.add(event3);
//		
//		
//		ComponentEventNode comEve1 = new ComponentEventNode();
//		ComponentEventNode comEve2 = new ComponentEventNode();
//		ComponentEventNode comEve3 = new ComponentEventNode();
//		
//		event.add(comEve1);
//		event.add(comEve2);
//		event.add(comEve3);
//		
//		String path = event.getPath().toString();
//		path = GTTlipse.macroEditor.view.MacroPresenter.checkReferencePath(path);
//		MacroEventCallerNode caller = new MacroEventCallerNode(path);
//		event2.add(caller);
//		MacroEventCallerNode cloneCaller = caller.clone();
//		event3.add(cloneCaller);
//		
//		InlineMacroEvent refactor = new InlineMacroEvent();
//		
//		// assert
//		refactor.setCaller(cloneCaller);
//		refactor.checkUsable();
//		assertFalse(refactor.isUsable());
//		refactor.setCaller(caller);
//		refactor.checkUsable();
//		assertTrue(refactor.isUsable());
	}
	
	public void testInlineMacroEvent() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = new MacroComponentNode();
		invisibleRoot.add(root);
		MacroEventNode event = new MacroEventNode();
		event.setName("EVENT");
		root.add(event);
		MacroEventNode event2 = new MacroEventNode();
		event2.setName("EVENT2");
		root.add(event2);
		Arguments list = new Arguments();
		list.add(Argument.create("TYPE", "name", "value"));
		event.setArguments(list);
		Arguments cloneList = list.clone();
		cloneList.get(0).setName("change");
		event2.setArguments(cloneList);
		
		
		Arguments comList = new Arguments();
		comList.add(Argument.create("TYPE", "NAME", "@name"));
		ComponentEventNode comEve1 = new ComponentEventNode();
		comEve1.setArguments(comList);
		ComponentEventNode comEve2 = new ComponentEventNode();
		ComponentEventNode comEve3 = new ComponentEventNode();
		
		event.add(comEve1);
		event.add(comEve2);
		event.add(comEve3);
		
		String path = event.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		event2.add(caller);
		Arguments cloneComList = comList.clone();
		cloneComList.get(0).setName("name");
		cloneComList.get(0).setValue("@change");
		caller.setArguments(cloneComList);
		
		InlineMacroEvent refactor = new InlineMacroEvent();
		
		// assert
		assertTrue(root.size() == 2);
		assertTrue(event.size() == 3);
		assertTrue(event2.size() == 1);
		refactor.inlineMacroEvent(caller);
		assertTrue(comEve1.getParent() == event);
		assertTrue(comEve2.getParent() == event);
		assertTrue(comEve3.getParent() == event);
		assertTrue(root.size() == 2);
		assertTrue(event.size() == 3);
		assertTrue(event2.size() == 3);
		assertTrue(event2.get(0) instanceof ComponentEventNode);
		ComponentEventNode eventNode = (ComponentEventNode) event2.get(0);
		Argument arg = eventNode.getArguments().get(0);
		assertTrue(arg.getName().equals("NAME"));
		assertTrue(arg.getValue().equals("@change"));
	}
}
