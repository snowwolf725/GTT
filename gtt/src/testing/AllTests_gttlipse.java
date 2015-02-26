package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.gttlipse.testScript.ClassNodeTest;
import testing.gttlipse.testScript.InvisibleRootNodeTest;
import testing.gttlipse.testScript.NodeTest;
import testing.gttlipse.testScript.TestMethodNodeTest;
import testing.gttlipse.testScript.TestPackageNodeTest;
import testing.gttlipse.testScript.TestProjectTest;

public class AllTests_gttlipse {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for GTTlipse.TestScript");
		//$JUnit-BEGIN$
		//Test Script
		suite.addTestSuite(TestProjectTest.class);	
		suite.addTestSuite(TestPackageNodeTest.class);
		suite.addTestSuite(ClassNodeTest.class);
		suite.addTestSuite(TestMethodNodeTest.class);
//		suite.addTestSuite(ConvertorTest.class);
		suite.addTestSuite(InvisibleRootNodeTest.class);
		suite.addTestSuite(NodeTest.class);
		//$JUnit-END$
		return suite;
	}

}
