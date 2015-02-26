package testing.gttlipse.refactoring;

import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.refactoring.macro.RenameMacroEvent;
import junit.framework.TestCase;

public class RenameMacroEventTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testIsExistSameName() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		invisibleRoot.add(root);
		MacroComponentNode macro = new MacroComponentNode();
		root.add(macro);
		MacroEventNode eve1 = new MacroEventNode();
		MacroEventNode eve2 = new MacroEventNode();
		eve1.setParent(macro);
		eve1.setName("NAME");
		eve2.setParent(macro);
		eve2.setName("SAME");
		macro.add(eve1);
		macro.add(eve2);
		String newName = "SAME";
		RenameMacroEvent rename = new RenameMacroEvent(root);

		// assert
		assertFalse(rename.isValid(eve1, newName));
		eve2.setName("CHANGE");
		assertTrue(rename.isValid(eve1, newName));
	}

	public void testRename() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		invisibleRoot.add(root);
		MacroComponentNode macro = new MacroComponentNode();
		root.add(macro);
		MacroEventNode eve1 = new MacroEventNode();
		MacroEventNode eve2 = new MacroEventNode();
		eve1.setParent(macro);
		eve1.setName("NAME");
		eve2.setParent(macro);
		eve2.setName("ANOTHER");
		macro.add(eve1);
		macro.add(eve2);
		String newName = "SAME";
		MacroEventNode eveClone = eve1.clone();
		eveClone.setParent(eve1.getParent());
		eveClone.setName(newName);
		String newPath = eveClone.getPath().toString();
		String path = eve1.getPath().toString();
		MacroEventCallerNode caller = new MacroEventCallerNode(path);
		eve2.add(caller);
		RenameMacroEvent rename = new RenameMacroEvent(macro);

		// assert
		rename.rename(eve1, newName);
		assertTrue(caller.getReferencePath().equals(newPath));
		assertTrue(eve1.getName().equals(newName));
		assertFalse(eve2.getName().equals(newName));
	}
}
