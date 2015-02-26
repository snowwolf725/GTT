package testing;

import junit.framework.Test;
import junit.framework.TestSuite;
import testing.gttlipse.vfsm.DiagramTest;
import testing.gttlipse.vfsm.FinalTest;
import testing.gttlipse.vfsm.GraphBuilderTest;
import testing.gttlipse.vfsm.InitialTest;
import testing.gttlipse.vfsm.NodeTest;
import testing.gttlipse.vfsm.StateTest;
import testing.gttlipse.vfsm.SuperStateTest;
import testing.gttlipse.vfsm.VFSMDiagramTest;

public class AllTests_VFSM {

	public static Test suite() {
		TestSuite suite = new TestSuite("VFSMEditor");
		suite.addTestSuite(DiagramTest.class);
		suite.addTestSuite(FinalTest.class);
		suite.addTestSuite(InitialTest.class);
		suite.addTestSuite(GraphBuilderTest.class);
		suite.addTestSuite(NodeTest.class);
		suite.addTestSuite(StateTest.class);
		suite.addTestSuite(SuperStateTest.class);

		// add by zwshen 2008/07/22
		suite.addTestSuite(VFSMDiagramTest.class);

		// add by zwshen 2008/07/23
		// comment by zws 2010/04/14
		//suite.addTestSuite(VFSMXmlReaderTest.class);

		// add by zwshen 2008/07/24
		// comment by zws 2010/04/14
		//suite.addTestSuite(VFSMXmlReader_forPorxyTest.class);
		
		return suite;
	}
}
