package testing.gttlipse.vfsm;

import junit.framework.TestCase;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;
import gttlipse.vfsmEditor.view.VFSMDiagram;

public class VFSMDiagramTest extends TestCase {

	VFSMDiagram m_Diagram;

	protected void setUp() throws Exception {
		super.setUp();
		m_Diagram = new VFSMDiagram();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		m_Diagram = null;
	}

	public void testConstruct() {
		assertNotNull(m_Diagram.getFSMRoot());
		// root 有兩個 child - main 及 FSM
		assertEquals(2, m_Diagram.getFSMRoot().size());
		assertEquals(VFSMDef.FSM_MAIN, m_Diagram.getFSMRoot().getAll().get(0)
				.getName());
		assertEquals(VFSMDef.FSM_FSM, m_Diagram.getFSMRoot().getAll().get(1)
				.getName());

		assertNotNull(m_Diagram.getFSMMain());
		// main中沒有任何 child
		assertEquals(0, m_Diagram.getFSMMain().getMainState().size());
		// declaration中沒有任何 child
		assertEquals(0, m_Diagram.getFSMDeclaration().size());
	}

	public void testAddDeclarationChildString() {
		// declaration中沒有任何 child
		assertEquals(0, m_Diagram.getFSMDeclaration().size());

		m_Diagram.addDeclarationChild("ss1");
		assertEquals(1, m_Diagram.getFSMDeclaration().size());

		// duplicate name
		m_Diagram.addDeclarationChild("ss1");
		assertEquals(2, m_Diagram.getFSMDeclaration().size());

		m_Diagram.addDeclarationChild("ss2");
		assertEquals(3, m_Diagram.getFSMDeclaration().size());

		// duplicate name
		m_Diagram.addDeclarationChild("ss2");
		assertEquals(4, m_Diagram.getFSMDeclaration().size());
	}

	public void testAddDeclarationChildNode() {
		// declaration中沒有任何 child
		assertEquals(0, m_Diagram.getFSMDeclaration().size());

		m_Diagram.addDeclarationChild(new SuperState());
		assertEquals(1, m_Diagram.getFSMDeclaration().size());
		m_Diagram.addDeclarationChild(new SuperState());
		assertEquals(2, m_Diagram.getFSMDeclaration().size());
		m_Diagram.addDeclarationChild(new SuperState());
		assertEquals(3, m_Diagram.getFSMDeclaration().size());
		m_Diagram.addDeclarationChild(new SuperState());
		assertEquals(4, m_Diagram.getFSMDeclaration().size());

		m_Diagram.getFSMDeclaration().clear();
		assertEquals(0, m_Diagram.getFSMDeclaration().size());
	}

	public void testRemoveDeclarationChild() {
		// declaration中沒有任何 child
		assertEquals(0, m_Diagram.getFSMDeclaration().size());

		State s1 = new SuperState();
		State s2 = new SuperState();
		State s3 = new SuperState();
		State s4 = new SuperState();
		m_Diagram.addDeclarationChild(s1);
		m_Diagram.addDeclarationChild(s2);
		m_Diagram.addDeclarationChild(s3);
		m_Diagram.addDeclarationChild(s4);
		assertEquals(4, m_Diagram.getFSMDeclaration().size());
		
		m_Diagram.removeDeclarationChild(s1);
		assertEquals(3, m_Diagram.getFSMDeclaration().size());
		m_Diagram.removeDeclarationChild(s1);
		assertEquals(3, m_Diagram.getFSMDeclaration().size());

		m_Diagram.removeDeclarationChild(s2);
		assertEquals(2, m_Diagram.getFSMDeclaration().size());
		m_Diagram.removeDeclarationChild(s3);
		assertEquals(1, m_Diagram.getFSMDeclaration().size());
		m_Diagram.removeDeclarationChild(s4);
		assertEquals(0, m_Diagram.getFSMDeclaration().size());
	}
	
	public void testFullName() {
		assertNotNull(m_Diagram.getFSMMain());
		AbstractSuperState main = m_Diagram.getFSMMain().getMainState();
		// main中沒有任何 child
		assertEquals(0, main.size());
		
		State is = new Initial();
		State fs = new Final();
		State s1 = new State("s1");
		State s2 = new State("s2");
		State s3 = new State("s3");
		
		main.addState(is);
		main.addState(fs);
		main.addState(s1);
		main.addState(s2);
		main.addState(s3);
		
		assertEquals("ROOT::Main::Initial", is.fullName());
		assertEquals("ROOT::Main::Final", fs.fullName());
		assertEquals("ROOT::Main::s1", s1.fullName());
		assertEquals("ROOT::Main::s2", s2.fullName());
		assertEquals("ROOT::Main::s3", s3.fullName());
		
		
	}

}
