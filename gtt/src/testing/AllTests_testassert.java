package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.testassert.AssertMethodTest;

public class AllTests_testassert {

	public static Test suite() {
		TestSuite suite = new TestSuite("GTT.testassert");

		suite.addTestSuite(AssertMethodTest.class);

		return suite;
	}
}
