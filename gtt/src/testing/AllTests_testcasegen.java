package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.gttlipse.testcasegen.ConsecutiveCoverageTest;
import testing.gttlipse.testcasegen.ConsecutiveSetTest;
import testing.gttlipse.testcasegen.StateCoverageTest;
import testing.gttlipse.testcasegen.TransitionCoverageTest;

public class AllTests_testcasegen {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test Case Generation");
		// add by zwshen 2009/11/12
		suite.addTestSuite(StateCoverageTest.class);
		// add by zwshen 2008/07/23
		suite.addTestSuite(TransitionCoverageTest.class);
		// add by zwshen 2008/07/23
		suite.addTestSuite(ConsecutiveCoverageTest.class);
		// add by zwshen 2008/07/23
		suite.addTestSuite(ConsecutiveSetTest.class);
		
		return suite;
	}
}
