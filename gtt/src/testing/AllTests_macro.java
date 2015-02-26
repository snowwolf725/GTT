package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.macro.AbstractMacroNodeTest;
import testing.macro.BoxTableViewTest;
import testing.macro.ComponentEventNodeTest;
import testing.macro.ComponentNodeTest;
import testing.macro.DefaultMacroFinderTest;
import testing.macro.EBNFSaveVisitorTest;
import testing.macro.LaunchNodeTest;
import testing.macro.MacroComponentNodeTest;
import testing.macro.MacroEventNodeTest;
import testing.macro.MacroPathTest;
import testing.macro.MacroTransformTest;
import testing.macro.ModelAssertNodeTest;
import testing.macro.OracleNodeTest;
import testing.macro.ReferenceComponentTest;
import testing.macro.ViewAssertNodeTest;

public class AllTests_macro {

	public static Test suite() {
		TestSuite suite = new TestSuite("MacroComponent");
		suite.addTestSuite(ComponentNodeTest.class);
		suite.addTestSuite(MacroComponentNodeTest.class);
		suite.addTestSuite(ModelAssertNodeTest.class);
		suite.addTestSuite(ComponentEventNodeTest.class);
		suite.addTestSuite(MacroEventNodeTest.class);
		suite.addTestSuite(ViewAssertNodeTest.class);

		// AbstractMacroNode default behavior - zws 2007/07/09
		suite.addTestSuite(AbstractMacroNodeTest.class);

		// AbstractMacroNode default behavior - zws 2007/10/30
		suite.addTestSuite(MacroTransformTest.class);

		// test DefaultMacroFinder - zws 2008/05/15
		suite.addTestSuite(DefaultMacroFinderTest.class);

		// test Macro ReferenceNode - zws 2008/05/15
//		suite.addTestSuite(ReferenceNodeTest.class);

		// test Macro.View BoxTableViewTest - zws 2008/05/31
		suite.addTestSuite(BoxTableViewTest.class);

		// test MacroPath - zws 2008/05/17
		suite.addTestSuite(MacroPathTest.class);
		
		// test EBNFSaveVisitor - zws 2009/02/19
		suite.addTestSuite(EBNFSaveVisitorTest.class);

		//zws 2009/12/23
		suite.addTestSuite(OracleNodeTest.class);
		
		//zws 2010/04/05
		suite.addTestSuite(ReferenceComponentTest.class);
		
		//zws 2010/04/26
		suite.addTestSuite(LaunchNodeTest.class);
		

		return suite;
	}
}
