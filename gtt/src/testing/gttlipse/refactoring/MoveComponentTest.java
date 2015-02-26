package testing.gttlipse.refactoring;

import java.util.Vector;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.MoveComponent;
import junit.framework.TestCase;

public class MoveComponentTest extends TestCase {
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
		macro1.add(event);
		ComponentEventNode comeve = new ComponentEventNode(com);
		event.add(comeve);
		
		Vector<AbstractMacroNode> coms = new Vector<AbstractMacroNode>();
		coms.add(com);
		// test usable
		MoveComponent refactor = new MoveComponent(root);
		
		// assert
		assertFalse(refactor.isValid(coms, macro2));
		com2.setName("OTHER");
		assertTrue(refactor.isValid(coms, macro2));
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
		
		ComponentNode com = ComponentNode.create();
		com.setName("COM");
		macro1.add(com);
		MacroEventNode event = new MacroEventNode("EVENT");
		macro1.add(event);
		ComponentEventNode comeve = new ComponentEventNode(com);
		event.add(comeve);
		
		// move com from macro1 to macro2
		MoveComponent refactor = new MoveComponent(root);
		Vector<AbstractMacroNode> coms = new Vector<AbstractMacroNode>();
		coms.add(com);
		
		// assert
		assertTrue(macro2.size() == 0);
		assertTrue(macro1.size() == 2);
		String path = com.getPath().toString();
		
		refactor.moveComponent(coms, macro2);
		
		assertTrue(macro2.size() == 1);
		assertTrue(macro1.size() == 1);
		String newPath = com.getPath().toString();
		assertFalse(newPath.equals(path));
		assertTrue(comeve.getComponentPath().equals(newPath));
	}
}
