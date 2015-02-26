package testing.gttlipse.vfsm;

import junit.framework.TestCase;
import gttlipse.vfsmEditor.io.VFSMXmlReader;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.ProxySuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.view.VFSMDiagram;

public class VFSMXmlReader_forPorxyTest extends TestCase {

	VFSMDiagram m_Diagram;

	protected void setUp() throws Exception {
		super.setUp();
		m_Diagram = new VFSMDiagram();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		m_Diagram = null;
	}

	public void testOpenFile() {
		String testfile = "test2.vfsm";

		VFSMXmlReader reader = new VFSMXmlReader();
		assertTrue(reader.read(testfile));

		VFSMDiagram newDiagram = reader.getVFSMDiagram();
		assertNotNull(newDiagram.getFSMRoot());
		forTestMainPart(newDiagram.getFSMMain().getMainState());
		forTestDeclarationPart(newDiagram.getFSMDeclaration());
	}

	private void forTestDeclarationPart(AbstractSuperState declar) {
		assertNotNull(declar);
		assertEquals("FSM", declar.getName());
		assertEquals(160, declar.getDimension().width);
		assertEquals(160, declar.getDimension().height);
		assertEquals(0, declar.getLocation().x);
		assertEquals(0, declar.getLocation().y);

		assertEquals(1, declar.size());
	}

	private void forTestMainPart(AbstractSuperState main) {
		assertNotNull(main);
		assertEquals("Main", main.getName());
		assertEquals(800, main.getDimension().width);
		assertEquals(800, main.getDimension().height);

		assertEquals(0, main.getLocation().x);
		assertEquals(0, main.getLocation().y);

		assertEquals(1, main.size());
		
		// proxy super state
		State s = main.getChildrenByName("DSS");
		assertNotNull(s);

		ProxySuperState dss = (ProxySuperState)s;
		// for real
		assertEquals(3, dss.getRealState().size());
		assertNotNull(dss.getRealState().getChildrenByName("State"));
		assertNotNull(dss.getRealState().getChildrenByName("State1"));
		assertNotNull(dss.getRealState().getChildrenByName("State2"));
		
		// for extra
		assertNotNull(dss.getChildrenByName("Initial"));
		assertNotNull(dss.getChildrenByName("Final"));
		assertNotNull(dss.getChildrenByName("State3"));
		// test connections
		assertEquals(1, dss.getChildrenByName("Initial").getOutgoingConnections().size());
		assertEquals(1, dss.getChildrenByName("State3").getOutgoingConnections().size());
		
		
		
	}

}
