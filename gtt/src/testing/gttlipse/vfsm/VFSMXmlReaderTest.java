package testing.gttlipse.vfsm;

import junit.framework.TestCase;
import gttlipse.vfsmEditor.io.VFSMXmlReader;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.view.VFSMDiagram;

public class VFSMXmlReaderTest extends TestCase {

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
		String testfile = "test1.vfsm";

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
		assertEquals(80, declar.getDimension().width);
		assertEquals(20, declar.getDimension().height);

		assertEquals(0, declar.getLocation().x);
		assertEquals(0, declar.getLocation().y);

		assertEquals(0, declar.size());

	}

	private void forTestMainPart(AbstractSuperState main) {
		assertNotNull(main);
		assertEquals("Main", main.getName());
		assertEquals(800, main.getDimension().width);
		assertEquals(800, main.getDimension().height);

		assertEquals(0, main.getLocation().x);
		assertEquals(0, main.getLocation().y);

		assertEquals(6, main.size());

		assertNotNull(main.getChildrenByName("Initial0"));
		assertNotNull(main.getChildrenByName("State2"));
		assertNotNull(main.getChildrenByName("State3"));
		assertNotNull(main.getChildrenByName("State4"));
		assertNotNull(main.getChildrenByName("State5"));
		assertNotNull(main.getChildrenByName("Final1"));

		assertEquals(1, main.getChildrenByName("Initial0")
				.getOutgoingConnections().size());
		assertEquals(1, main.getChildrenByName("State2")
				.getOutgoingConnections().size());
		assertEquals(1, main.getChildrenByName("State3")
				.getOutgoingConnections().size());
		assertEquals(1, main.getChildrenByName("State4")
				.getOutgoingConnections().size());
		assertEquals(1, main.getChildrenByName("State5")
				.getOutgoingConnections().size());

	}

}
