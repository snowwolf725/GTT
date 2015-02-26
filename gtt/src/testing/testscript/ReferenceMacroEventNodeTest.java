package testing.testscript;

import gtt.testscript.NodeFactory;
import gtt.testscript.ReferenceMacroEventNode;
import junit.framework.TestCase;

public class ReferenceMacroEventNodeTest extends TestCase {

	ReferenceMacroEventNode _node;
	String path = "MacroComponent:MacroEvent";

	protected void setUp() throws Exception {
		super.setUp();
		_node = new NodeFactory().createReferenceMacroEventNode(path);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		_node = null;
	}

	class MyMockVisitor extends MockVisitor {

		boolean m_hasVisit = false;

		public boolean hasVisit() {
			return m_hasVisit;
		}

		public void visit(ReferenceMacroEventNode node) {
			m_hasVisit = true;
		}

	}

	public void testAccept() {
		MyMockVisitor mock_visitor = new MyMockVisitor();

		_node.accept(mock_visitor);
		assertTrue(mock_visitor.hasVisit());
	}

	public void testToSimpleString() {
		assertEquals(path, _node.toSimpleString());
	}

	public void testToDetailString() {
		assertEquals(path, _node.toDetailString());
	}

	public void testGetRefPath() {
		assertEquals(path, _node.getRefPath());
	}

	public void testSetRefPath() {
		String other_path = "path1:path2;path3";
		_node.setRefPath(other_path);
		assertEquals(other_path, _node.toDetailString());
	}

	public void testToString() {
		assertEquals(path, _node.toString());
	}

	public void testClone() {
		
		ReferenceMacroEventNode ref = _node.clone();
		assertNotSame(ref, _node);
		
		assertEquals(ref.getRefPath(), _node.getRefPath());
	}

}
