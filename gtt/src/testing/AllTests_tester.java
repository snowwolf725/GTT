package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.tester.ButtonTesterTest;
import testing.tester.PlaybackTest;

public class AllTests_tester {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tester");

		suite.addTestSuite(PlaybackTest.class);
		suite.addTestSuite(ButtonTesterTest.class);

		return suite;
	}
}
