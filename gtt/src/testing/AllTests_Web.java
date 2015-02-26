package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.web.ConvertPageToComponentTest;
import testing.web.SendEventTest;
import testing.web.HtmlTableTest;
import testing.web.WebControllerTest;

public class AllTests_Web {
	public static Test suite() {
		TestSuite suite = new TestSuite("Web");
		suite.addTestSuite(SendEventTest.class);
		suite.addTestSuite(ConvertPageToComponentTest.class);
		suite.addTestSuite(HtmlTableTest.class);
		suite.addTestSuite(WebControllerTest.class);		
		return suite;
	}
}
