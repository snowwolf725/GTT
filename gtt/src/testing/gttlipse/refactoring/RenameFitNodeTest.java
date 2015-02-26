package testing.gttlipse.refactoring;

import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.node.FitNode;
import gttlipse.refactoring.macro.RenameFitNode;
import junit.framework.TestCase;

public class RenameFitNodeTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testIsExistSameName() {
		// initial
		MacroComponentNode macro = new MacroComponentNode();
		MacroEventNode eve1 = new MacroEventNode();
		MacroEventNode eve2 = new MacroEventNode();
		FitNode fit = new FitNode();
		fit.setParent(macro);
		fit.setName("FIT");
		eve1.setParent(macro);
		eve1.setName("NAME");
		eve2.setParent(macro);
		eve2.setName("SAME");
		macro.add(eve1);
		macro.add(eve2);
		macro.add(fit);
		String newName = "SAME";
		RenameFitNode rename = new RenameFitNode();
		
		// assert
		assertFalse(rename.isValid(fit, newName));
		eve2.setName("CHANGE");
		assertTrue(rename.isValid(fit, newName));
	}
	
	public void testRename() {
		// initial
		MacroComponentNode macro = new MacroComponentNode();
		MacroEventNode eve1 = new MacroEventNode();
		MacroEventNode eve2 = new MacroEventNode();
		FitNode fit = new FitNode();
		fit.setParent(macro);
		fit.setName("FIT");
		eve1.setParent(macro);
		eve1.setName("NAME");
		eve2.setParent(macro);
		eve2.setName("SAME");
		macro.add(eve1);
		macro.add(eve2);
		macro.add(fit);
		String newName = "SAME";
		RenameFitNode rename = new RenameFitNode();
		
		// assert
		rename.rename(fit, newName);
		assertTrue(fit.getName().equals(newName));
	}
}
