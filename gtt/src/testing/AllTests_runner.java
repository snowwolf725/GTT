package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.runner.LaunchInformationTest;

public class AllTests_runner {

	public static Test suite() {
		TestSuite suite = new TestSuite("gtt.runner");
		suite.addTestSuite(LaunchInformationTest.class);

		
		return suite;
	}
}
