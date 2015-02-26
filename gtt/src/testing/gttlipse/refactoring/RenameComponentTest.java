package testing.gttlipse.refactoring;

import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.RenameComponent;
import junit.framework.TestCase;

public class RenameComponentTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testIsExistSameName() {
		// initial
		MacroComponentNode macro = new MacroComponentNode();
		ComponentNode com1 = ComponentNode.create();
		ComponentNode com2 = ComponentNode.create();
		com1.setParent(macro);
		com1.setName("NAME");
		com2.setParent(macro);
		com2.setName("SAME");
		macro.add(com1);
		macro.add(com2);
		String newName = "SAME";
		RenameComponent rename = new RenameComponent(macro);
		
		// assert
		assertFalse(rename.isValid(com1, newName));
		com2.setName("CHANGE");
		assertTrue(rename.isValid(com1, newName));
	}
	
	public void testRename() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		MacroComponentNode macro = new MacroComponentNode();
		macro.setName("MACRO");
		MacroEventNode eve = new MacroEventNode();
		eve.setName("EVENT");
		ComponentNode com1 = ComponentNode.create();
		
		com1.setParent(macro);
		com1.setName("SAME");
		com1.setTitle("Title");
		com1.setType("JBUTTON");
		com1.setWinType("WIN");
		eve.setParent(macro);
		
		macro.add(com1);
		macro.add(eve);
		invisibleRoot.add(root);
		root.add(macro);
		
		ComponentEventNode comeve = new ComponentEventNode(com1);
		eve.add(comeve);
		

		String newName = "NEWNAME";
		RenameComponent rename = new RenameComponent(root);
//		rename.setRoot(root);
		
		// assert
		rename.rename(com1, newName);
		assertTrue(com1.getName().equals(newName));
		String path = com1.getPath().toString();
		assertEquals(path,  comeve.getComponent().getPath().toString());
	}
}
