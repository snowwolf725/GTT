package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.eventmodel.ArgumentListTest;
import testing.eventmodel.ArgumentTest;
import testing.eventmodel.AssertionTest;
import testing.eventmodel.SwingAsserterTest;
import testing.eventmodel.SwingComponentTest;
import testing.eventmodel.SwingEventTest;
import testing.eventmodel.SwingModelTest;

public class AllTests_eventmodel {

	public static Test suite() {
		TestSuite suite = new TestSuite("EventModel");
		suite.addTestSuite(SwingComponentTest.class);
		suite.addTestSuite(SwingEventTest.class);
		suite.addTestSuite(AssertionTest.class);
		suite.addTestSuite(ArgumentTest.class);

		suite.addTestSuite(SwingModelTest.class);
		suite.addTestSuite(SwingAsserterTest.class);

		// a composition object for managing arguments
		suite.addTestSuite(ArgumentListTest.class);

		return suite;
	}
}
