package testing;

import testing.gttlipse.tabular.TableRowTest;
import testing.gttlipse.tabular.TableTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests_tabular {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tabular");

		suite.addTestSuite(TableTest.class);
		suite.addTestSuite(TableRowTest.class);
		
		return suite;
	}

}
