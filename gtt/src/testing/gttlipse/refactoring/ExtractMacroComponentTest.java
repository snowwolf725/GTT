package testing.gttlipse.refactoring;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.refactoring.macro.ExtractMacroComponent;

import java.util.Vector;

import junit.framework.TestCase;

public class ExtractMacroComponentTest extends TestCase {
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
		ViewAssertNode viewAssert1 = new ViewAssertNode(com2.getName());
		event1.add(comEve1);
		event1.add(viewAssert1);
		event2.add(comEve2);
		MacroEventNode event3 = new MacroEventNode("EVENT3");
		macro3.add(event3);
		MacroEventNode event4 = new MacroEventNode("EVENT4");
		macro1.add(event4);
		String path = event1.getPath().toString();
		MacroEventCallerNode caller1 = new MacroEventCallerNode(path);
		event4.add(caller1);
		path = event3.getPath().toString();
		MacroEventCallerNode caller2 = new MacroEventCallerNode(path);
		event4.add(caller2);
		
		Vector<AbstractMacroNode> nodes = new Vector<AbstractMacroNode>();
		nodes.add(event1);
		nodes.add(com1);
		nodes.add(macro3);
		
		String name = "NAME";
		
		// assert
		ExtractMacroComponent refactor = new ExtractMacroComponent(root);
		
		assertTrue(refactor.isValid(nodes, macro1, name));
		name = "MACRO2";
		assertFalse(refactor.isValid(nodes, macro1, name));
	}
	
	public void testExtractMacroComponent() {
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
		event4.add(caller1);
		path = event3.getPath().toString();
		MacroEventCallerNode caller2 = new MacroEventCallerNode(path);
		event4.add(caller2);
		
		Vector<AbstractMacroNode> nodes = new Vector<AbstractMacroNode>();
		nodes.add(event1);
		nodes.add(com1);
		nodes.add(macro3);
		
		String name = "NAME";
		
		// assert
		ExtractMacroComponent refactor = new ExtractMacroComponent(root);
		
		assertTrue(macro1.size() == 2);
		assertTrue(macro2.size() == 5);
		String oldPath1 = caller1.getReferencePath();
		String oldPath2 = caller2.getReferencePath();
		
		refactor.extractMacroComponent(nodes, macro1, name);
		
		assertTrue(macro1.size() == 3);
		assertTrue(macro2.size() == 2);
		AbstractMacroNode node = macro1.get(2);
		assertTrue(node.getName().equals(name));
		assertTrue(node.size() == 3);
		assertFalse(caller1.getReferencePath().equals(oldPath1));
		assertFalse(caller2.getReferencePath().equals(oldPath2));
	}
}
