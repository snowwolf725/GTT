package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.eventmodel.SwingAsserterTest;
import testing.testscript.LaunchNodeTest;
import testing.testscript.AbstractNodeTest;
import testing.testscript.EventNodeTest;
import testing.testscript.FolderNodeTest;
import testing.testscript.ModelAssertNodeTest;
import testing.testscript.NodeFactoryTest;
import testing.testscript.OracleNodeTest;
import testing.testscript.ReferenceMacroEventNodeTest;
import testing.testscript.TestScriptDocumentTest;
import testing.testscript.TestScriptTest;
import testing.testscript.ViewAssertNodeTest;

public class AllTests_testscript {

	public static Test suite() {
		TestSuite suite = new TestSuite("TestScript");
		suite.addTestSuite(NodeFactoryTest.class);

		suite.addTestSuite(AbstractNodeTest.class);
		suite.addTestSuite(EventNodeTest.class);
		suite.addTestSuite(ViewAssertNodeTest.class);
		suite.addTestSuite(ModelAssertNodeTest.class);
		suite.addTestSuite(FolderNodeTest.class);
		suite.addTestSuite(LaunchNodeTest.class);
		suite.addTestSuite(ReferenceMacroEventNodeTest.class);
		suite.addTestSuite(OracleNodeTest.class);
		
		suite.addTestSuite(TestScriptTest.class);
		suite.addTestSuite(TestScriptDocumentTest.class);
		suite.addTestSuite(SwingAsserterTest.class);

		return suite;
	}
}
