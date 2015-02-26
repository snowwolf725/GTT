package testing.gttlipse.refactoring;

import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.InlineMacroComponent;
import junit.framework.TestCase;

public class InlineMacroComponentTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testUsable() {
		// initial
		InvisibleRootNode inVisableRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		inVisableRoot.add(root);
		MacroComponentNode macro1 = MacroComponentNode.create("MACRO1");
		MacroComponentNode macro2 = MacroComponentNode.create("MACRO2");
		root.add(macro1);
		root.add(macro2);
		
		ComponentNode com = ComponentNode.create();
		com.setName("COM");
		macro1.add(com);
		
		ComponentNode com2 = ComponentNode.create();
		com2.setName("COM");
		macro2.add(com2);
		
		MacroEventNode event = new MacroEventNode("EVENT");
		macro2.add(event);
		ComponentEventNode comeve = new ComponentEventNode(com2);
		event.add(comeve);
		
		// assert
		InlineMacroComponent refactor = new InlineMacroComponent(root);
		assertFalse(refactor.isValid(macro2, macro1));
		
		com2.setName("COM2");
		assertTrue(refactor.isValid(macro2, macro1));
	}
	
	public void testInlineMacroComponent() {
		// initial
		InvisibleRootNode inVisableRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		inVisableRoot.add(root);
		MacroComponentNode macro1 = MacroComponentNode.create("MACRO1");
		MacroComponentNode macro2 = MacroComponentNode.create("MACRO2");
		root.add(macro1);
		root.add(macro2);
		
		ComponentNode com = ComponentNode.create();
		com.setName("COM");
		macro1.add(com);
		
		ComponentNode com2 = ComponentNode.create();
		com2.setName("COM2");
		macro2.add(com2);
		
		MacroEventNode event = new MacroEventNode("EVENT");
		macro2.add(event);
		ComponentEventNode comeve = new ComponentEventNode(com2);
		event.add(comeve);
		
		// assert
		InlineMacroComponent refactor = new InlineMacroComponent(root);
		assertTrue(refactor.isValid(macro2, macro1));
		assertTrue(macro1.size() == 1);
		assertTrue(macro2.size() == 2);
		assertTrue(macro1.getParent() == root);
		assertTrue(macro2.getParent() == root);
		
		refactor.inlineMacroComponent(macro2, macro1);
		assertTrue(macro1.size() == 3);
		assertTrue(macro2.size() == 0);
		assertTrue(macro1.getParent() == root);
		assertTrue(macro2.getParent() != root);
	}
}
