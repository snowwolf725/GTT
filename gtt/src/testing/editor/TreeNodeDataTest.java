package testing.editor;

import gtt.editor.view.ITestScriptView;
import gtt.editor.view.TreeNodeData;
import gtt.testscript.AbstractNode;
import junit.framework.TestCase;

public class TreeNodeDataTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testToString() {
		AbstractNode node = new MockNode();
		ITestScriptView view = new MockView();
		TreeNodeData d = new TreeNodeData(node, view);

//		assertEquals(d.toString(), node.toSimpleString());
		assertEquals(d.toString(), node.toString());
//		assertEquals(d.toString(), node.toDetailString());

//		assertEquals(d.toString(), TreeNodeData.ERROR_VIEW_LEVEL);
//		assertEquals(d.toString(), TreeNodeData.ERROR_VIEW_LEVEL);
//		assertEquals(d.toString(), TreeNodeData.ERROR_VIEW_LEVEL);
	}

	public void testGetter() {
		AbstractNode node = new MockNode();
		ITestScriptView view = new MockView();
		TreeNodeData d = new TreeNodeData(node, view);

		assertEquals(d.getData(), node);
		assertEquals(d.getView(), view);
	}

}
