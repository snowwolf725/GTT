package testing.editor;

import gtt.editor.view.TreeNodeData;
import gtt.editor.view.TreeNodeDataFactory;
import junit.framework.TestCase;

public class TreeNodeDataFactoryTest extends TestCase {
	

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateData() {
		MockView v = new MockView();
		MockNode n = new MockNode();
		TreeNodeDataFactory f = new TreeNodeDataFactory(v);
		TreeNodeData d =f.createData(n); 
		assertTrue(d != null);
		assertEquals(d.getData(), n);
		assertEquals(d.getView(), v);
	}

}
