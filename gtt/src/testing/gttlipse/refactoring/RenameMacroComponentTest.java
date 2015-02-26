package testing.gttlipse.refactoring;

import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.RenameMacroComponent;
import junit.framework.TestCase;

public class RenameMacroComponentTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testIsExistSameName() {
		// initial
		MacroComponentNode macro = new MacroComponentNode();
		MacroComponentNode macro2 = new MacroComponentNode();
		MacroComponentNode macro3 = new MacroComponentNode();
		MacroEventNode eve1 = new MacroEventNode();
		MacroEventNode eve2 = new MacroEventNode();
		macro.setName("AUT");
		macro2.setName("Macro");
		macro3.setName("NEW");
		eve1.setParent(macro);
		eve1.setName("NAME");
		eve2.setParent(macro2);
		eve2.setName("SAME");
		macro.add(eve1);
		macro2.add(eve2);
		macro.add(macro2);
		macro.add(macro3);
		String newName = "NEW";
		RenameMacroComponent rename = new RenameMacroComponent(macro);
		
		// assert
		assertFalse(rename.isValid(macro2, newName));
		macro3.setName("CHANGE");
		assertTrue(rename.isValid(macro2, newName));
	}
	
	public void testRename() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = new MacroComponentNode();
		MacroComponentNode macro2 = new MacroComponentNode();
		MacroComponentNode macro3 = new MacroComponentNode();
		MacroEventNode eve1 = new MacroEventNode();
		MacroEventNode eve2 = new MacroEventNode();
		root.setName("AUT");
		macro2.setName("Macro");
		macro3.setName("NEW");
		eve1.setName("NAME");
		eve2.setName("SAME");
		root.add(eve1);
		macro2.add(eve2);
		root.add(macro2);
		root.add(macro3);
		invisibleRoot.add(root);
		String newName = "NEWNAME";		
		MacroEventNode eveClone = eve2.clone();
		eveClone.setParent(eve2.getParent());
		macro2.setName("NEWNAME");
		String newPath = eveClone.getPath().toString();
		macro2.setName("Macro");
		String path = eve2.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		eve1.add(caller);
		RenameMacroComponent rename = new RenameMacroComponent(root);
		
		// assert
		rename.rename(macro2, newName);
		assertTrue(caller.getReferencePath().equals(newPath));
		assertTrue(macro2.getName().equals(newName));
	}
}
