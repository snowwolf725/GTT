package testing.gttlipse.refactoring;

import java.util.Vector;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.MoveMacroEvent;
import junit.framework.TestCase;

public class MoveMacroEventTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCheckable() {
		MacroComponentNode parent = new MacroComponentNode();
		ComponentNode com = ComponentNode.create();
		com.setName("TESTCOM");
		parent.add(com);
		MacroEventNode event = new MacroEventNode();
		event.setName("TEST");
		MacroEventNode event2 = new MacroEventNode();
		event2.setName("TEST");
		parent.add(event2);
		String name = "NAME";
		
		Vector<AbstractMacroNode> events = new Vector<AbstractMacroNode>();
		events.add(event);
		MoveMacroEvent refactor = new MoveMacroEvent(parent);
		assertFalse(refactor.isValid(events, parent));
		event2.setName(name);
		assertTrue(refactor.isValid(events, parent));
	}
	
	public void testMove() {
		// initial
		InvisibleRootNode inVisableRoot = new InvisibleRootNode();
		MacroComponentNode root = new MacroComponentNode();
		inVisableRoot.add(root);
		root.setName("ROOT");
		MacroEventNode event = new MacroEventNode();
		event.setName("EVENT");
		MacroEventNode event2 = new MacroEventNode();
		event2.setName("EVENT");
		root.add(event);
		MacroComponentNode parent = new MacroComponentNode();
		root.add(parent);
		parent.setName("PARENT");
		parent.add(event2);
		
		ComponentNode com = ComponentNode.create();
		com.setName("com");
		root.add(com);
		ComponentEventNode comEve1 = new ComponentEventNode(com);
//		comEve1.setName(com.getName());
		
		event.add(comEve1);
		
		String path = event.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		event2.add(caller);
		
		// assert
		MoveMacroEvent refactor = new MoveMacroEvent(root);
		assertTrue(parent.size() == 1);
		assertTrue(root.size() == 3);
		assertTrue(event.getParent() == root);
		refactor.move(event, parent);
		assertTrue(parent.size() == 2);
		assertTrue(root.size() == 2);
		assertTrue(event.getParent() == parent);
		path = event.getPath().toString();
		String refPath = caller.getReferencePath();
		assertTrue(refPath.equals(path));
	}
}
