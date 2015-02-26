package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.oracle.LoadAssertMethodTest;
import testing.oracle.OracleDataTest;

public class AllTests_oracle {
	public static Test suite() {
		TestSuite suite = new TestSuite("gtt.oracle");
		suite.addTestSuite(LoadAssertMethodTest.class);
		suite.addTestSuite(OracleDataTest.class);
		
		
		return suite;
	}
}
