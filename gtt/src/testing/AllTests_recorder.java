package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.recorder.EventAbstraction_KeyEvent_Test;
import testing.recorder.EventAbstraction_MouseEvent_Test;

public class AllTests_recorder {

	public static Test suite() {
		TestSuite suite = new TestSuite("gtt.recorder");
		suite.addTestSuite(EventAbstraction_MouseEvent_Test.class);
		suite.addTestSuite(EventAbstraction_KeyEvent_Test.class);

		return suite;
	}
}
