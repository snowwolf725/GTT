package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.gttlipse.refactoring.EditParameterTest;
import testing.gttlipse.refactoring.ExtractMacroComponentTest;
import testing.gttlipse.refactoring.ExtractMacroEventTest;
import testing.gttlipse.refactoring.ExtractParameterTest;
import testing.gttlipse.refactoring.HideDelegateTest;
import testing.gttlipse.refactoring.InlineMacroComponentTest;
import testing.gttlipse.refactoring.InlineMacroEventTest;
import testing.gttlipse.refactoring.MoveComponentTest;
import testing.gttlipse.refactoring.MoveMacroComponentTest;
import testing.gttlipse.refactoring.MoveMacroEventTest;
import testing.gttlipse.refactoring.RemoveMiddleManTest;
import testing.gttlipse.refactoring.RenameComponentTest;
import testing.gttlipse.refactoring.RenameFitNodeTest;
import testing.gttlipse.refactoring.RenameMacroComponentTest;
import testing.gttlipse.refactoring.RenameMacroEventTest;
import testing.gttlipse.refactoring.RenameParameterTest;
import testing.gttlipse.refactoring.RenameWindowTitleTest;
import testing.gttlipse.refactoring.TestScriptComponentRenameTest;

public class AllTests_refactoring {
	public static Test suite() {
		TestSuite suite = new TestSuite("Refactoring");
		
		suite.addTestSuite(RenameComponentTest.class);
		suite.addTestSuite(RenameMacroComponentTest.class);
		suite.addTestSuite(RenameMacroEventTest.class);
		suite.addTestSuite(RenameFitNodeTest.class);
		suite.addTestSuite(TestScriptComponentRenameTest.class);
		suite.addTestSuite(EditParameterTest.class);
		suite.addTestSuite(ExtractMacroEventTest.class);
		suite.addTestSuite(RenameParameterTest.class);
		suite.addTestSuite(InlineMacroEventTest.class);
		suite.addTestSuite(MoveMacroEventTest.class);
		suite.addTestSuite(ExtractMacroComponentTest.class);
		suite.addTestSuite(RenameWindowTitleTest.class);
		suite.addTestSuite(RemoveMiddleManTest.class);
		suite.addTestSuite(MoveComponentTest.class);
		suite.addTestSuite(MoveMacroComponentTest.class);
		suite.addTestSuite(InlineMacroComponentTest.class);
		suite.addTestSuite(HideDelegateTest.class);
		suite.addTestSuite(ExtractParameterTest.class);
		
		
		return suite;
	}
}
