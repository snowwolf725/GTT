package testing.testscript;

import gtt.testscript.LaunchNode;
import gtt.testscript.NodeFactory;
import junit.framework.TestCase;

public class LaunchNodeTest extends TestCase {

	LaunchNode _node;
	String CLASS_NAME = "AutClassName";
	String CLASS_PATH = ".;c:\\;d:\\";

	protected void setUp() throws Exception {
		super.setUp();
		_node = new NodeFactory().createLaunchNode(CLASS_NAME, CLASS_PATH);
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

		public void visit(LaunchNode node) {
			m_hasVisit = true;
		}
	
	}
	
	public void testAccept() {
		MyMockVisitor mock_visitor = new MyMockVisitor();

		_node.accept(mock_visitor);
		assertTrue(mock_visitor.hasVisit());
	}

	public void testClone() {
		LaunchNode obj = null;
		assertNotSame(obj, _node);

		obj = (LaunchNode) _node.clone();

		assertNotSame(obj, _node);
		assertEquals(obj.getClassName(), CLASS_NAME);
		assertEquals(obj.getClassPath(), CLASS_PATH);

		assertEquals(obj.getArgument(), _node.getArgument());
		assertEquals(obj.getClassName(), _node.getClassName());
		assertEquals(obj.getClassPath(), _node.getClassPath());
	}

	public void testToSimpleString() {
		assertEquals(_node.toSimpleString(), CLASS_NAME);
	}

	public void testToDetailString() {
		assertEquals(_node.toDetailString(), CLASS_NAME);
	}

	public void testToString() {
		assertEquals(_node.toString(), CLASS_NAME);
	}

	public void testGetClassname() {
		assertEquals(CLASS_NAME, _node.getClassName());
		String ANOTHER_CLASS_NAME = "ANOTHER_CLASS_NAME";
		_node.setClassName(ANOTHER_CLASS_NAME);
		assertEquals(ANOTHER_CLASS_NAME, _node.getClassName());
	}

	public void testSetClasspath() {
		assertEquals(CLASS_PATH, _node.getClassPath());
		String ANOTHER_CLASS_PATH = "ANOTHER_CLASS_PATH;path2;path3;path4";
		_node.setClassPath(ANOTHER_CLASS_PATH);
		assertEquals(ANOTHER_CLASS_PATH, _node.getClassPath());
	}

	public void testGetArgument() {
		assertEquals("", _node.getArgument());
		String argument = "{\"LayoutChanged\"}";
		_node.setArgument(argument);
		assertEquals(argument, _node.getArgument());
	}
}
