package testing;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test Suite of BasicComponentModel project
 * 
 * @author zwshen
 * 
 */
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("GTT All Tests");
		suite.addTest(AllTests_recorder.suite());
		suite.addTest(AllTests_testscript.suite());
		suite.addTest(AllTests_eventmodel.suite());
		suite.addTest(AllTests_gttlipse.suite());
		// suite.addTest(AllTests_FSM.suite());
		suite.addTest(AllTests_editor.suite());
		suite.addTest(AllTests_macro.suite());
		suite.addTest(AllTests_VFSM.suite());
		// for tester assert
		suite.addTest(AllTests_testassert.suite());
		// for swing test
		suite.addTest(AllTests_tester.suite());
		suite.addTest(AllTests_Fit.suite());

		// zwshen 2009/11/12 for test case generation
		suite.addTest(AllTests_testcasegen.suite());
		
		// zwshen 2009/11/12  for refactoring function
		suite.addTest(AllTests_refactoring.suite());

		// zwshen 2009/12/18  for gtt.oracle.*
		suite.addTest(AllTests_oracle.suite());

		// zwshen 2010/04/26 for gtt.runner.*	
		suite.addTest(AllTests_runner.suite());
		
		// 2010/09/30 fix and add new test by loveshoo
		suite.addTest(AllTests_Web.suite());
		
		// uhsing 2010/10/23 for GTTlipse.tabular
		suite.addTest(AllTests_tabular.suite());

		return suite;
	}
}
