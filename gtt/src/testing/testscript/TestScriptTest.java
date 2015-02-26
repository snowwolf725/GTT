package testing.testscript;

import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import junit.framework.TestCase;

public class TestScriptTest extends TestCase {
	
	AbstractNode m_Script;
	NodeFactory m_Factory = new NodeFactory();
	public void setUp() {
		 m_Script = m_Factory.createFolderNode("TESTING");
	}
	public void tearDown() {
		m_Script = null;
	}
	
	public void testEvent() {
		AbstractNode node = m_Factory.createFolderNode("child");
		assertEquals(m_Script.size(), 0);

		m_Script.add(node);
		assertEquals(m_Script.size(), 1);
		
		m_Script.remove(m_Script.size()-1);
		assertEquals(m_Script.size(), 0);
	}

}
