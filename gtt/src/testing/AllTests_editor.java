package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.editor.ComponentDialogFactoryTest;
import testing.editor.DummyDialogTest;
import testing.editor.GUILayoutReverserTest;
import testing.editor.JTreeVisitorTest;
import testing.editor.RunnerUtilTest;
import testing.editor.SwingDialogFactoryTest;
import testing.editor.TestScriptPresenterTest;
import testing.editor.TreeNodeDataFactoryTest;
import testing.editor.TreeNodeDataTest;

public class AllTests_editor {
	public static Test suite() {
		TestSuite suite = new TestSuite("TestScriptEditor");
		
		suite.addTestSuite(TreeNodeDataTest.class);
		suite.addTestSuite(TreeNodeDataFactoryTest.class);

		suite.addTestSuite(JTreeVisitorTest.class);

		suite.addTestSuite(DummyDialogTest.class);
		suite.addTestSuite(ComponentDialogFactoryTest.class);
		suite.addTestSuite(SwingDialogFactoryTest.class);

		suite.addTestSuite(TestScriptPresenterTest.class);
		
		// add by zws 2007/10/30
		suite.addTestSuite(GUILayoutReverserTest.class);
		
		// add by zws 2008/04/28
		suite.addTestSuite(RunnerUtilTest.class);
		
		
		return suite;
	}
}
