package testing.gttlipse.refactoring;

import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.MoveMacroComponent;
import junit.framework.TestCase;

public class MoveMacroComponentTest extends TestCase {
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
		
		MacroComponentNode macro3 = MacroComponentNode.create("MACRO3");
		macro1.add(macro3);
		MacroComponentNode macro4 = MacroComponentNode.create("MACRO1");
		macro2.add(macro4);
		
		// test usable
		MoveMacroComponent refactor = new MoveMacroComponent(root);
		
		// assert
		assertFalse(refactor.isValid(macro1, macro3));
		assertFalse(refactor.isValid(macro1, macro2));
		macro4.setName("MACRO4");
		assertTrue(refactor.isValid(macro1, macro2));
	}
	
	public void testMove() {
		// initial
		InvisibleRootNode inVisableRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		inVisableRoot.add(root);
		MacroComponentNode macro1 = MacroComponentNode.create("MACRO1");
		MacroComponentNode macro2 = MacroComponentNode.create("MACRO2");
		root.add(macro1);
		root.add(macro2);
		
		MacroComponentNode macro3 = MacroComponentNode.create("MACRO3");
		macro1.add(macro3);
		
		MacroEventNode event1 = new MacroEventNode("EVENT1");
		macro3.add(event1);
		
		MacroEventNode event2 = new MacroEventNode("EVENT2");
		macro2.add(event2);
		
		MacroEventCallerNode caller = new MacroEventCallerNode(event1.getPath().toString());
		event2.add(caller);		
		
		// move macro1 from root to macro2
		MoveMacroComponent refactor = new MoveMacroComponent(root);
		
		// assert
		assertTrue(root.size() == 2);
		assertTrue(macro2.size() == 1);
		
		String path = caller.getReferencePath();
		
		refactor.moveMacroComponent(macro1, macro2);
		
		assertTrue(root.size() == 1);
		assertTrue(macro2.size() == 2);
		assertFalse(caller.getReferencePath().equals(path));
		path = event1.getPath().toString();
		assertTrue(caller.getReferencePath().equals(path));
	}
}
