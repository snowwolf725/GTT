package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.gttlipse.fit.GenerateOrderNameNodeProcessorTest;
import testing.gttlipse.fit.StringPreprocessorTest;

public class AllTests_Fit {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for GTTlipse.Fit");
		suite.addTestSuite(StringPreprocessorTest.class);
		suite.addTestSuite(GenerateOrderNameNodeProcessorTest.class);
		return suite;
	}
}
