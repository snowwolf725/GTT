package testing.editor;

import gtt.editor.presenter.ITestScriptPresenter;
import gtt.editor.presenter.TestScriptPresenter;
import gtt.editor.view.JTreeTestScriptVisitor;
import gtt.editor.view.TreeNodeDataFactory;
import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;

import java.util.LinkedList;

import javax.swing.JTree;

import junit.framework.TestCase;

public class TestScriptPresenterTest extends TestCase {

	ITestScriptPresenter p;

	protected void setUp() throws Exception {
		super.setUp();
		p = new TestScriptPresenter();
		p.setView(new MockView());
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		p = null;
	}

	public void testTestScriptPresenter() {
		assertTrue(p.getModel() != null);
		assertEquals(p.getModel().getName(), TestScriptDocument.DEFAULT_NAME);
	}

	public void testGetDocument() {
		assertTrue(p.getModel() != null);
		assertEquals(p.getModel().getName(), TestScriptDocument.DEFAULT_NAME);
		p.setModel(new TestScriptDocument(new MockNode()));
		assertEquals(p.getModel().getName(), "");
		p.getModel().setName("MOCKDOCUMENT");
		assertEquals(p.getModel().getName(), "MOCKDOCUMENT");
	}

	public void testAcceptVisitor() {
		// fail("Not yet implemented");
	}

	public void testGetJTree() {
		assertTrue(p.getJTree() != null);

		JTree tree = new JTree();
		p.setJTree(tree);
		assertEquals(p.getJTree(), tree);
	}

	public void testDownNode() {
		assertFalse(p.downMoveNode());
	}

	public void testUpNode() {
		assertFalse(p.upMoveNode());
	}

	public void testDoInsertFolder() {
		assertFalse(p.insertFolderNode());
	}

	public void testDoInsertEventNode() {
		assertFalse(p.insertEventNode());
	}

	public void testDoInsertViewAssertNode() {
		assertFalse(p.insertModelAssertNode());
	}

	public void testDoInsertModelAssertNode() {
		assertFalse(p.insertViewAssertNode());
	}

	public void testRemoveNode() {
		assertFalse(p.removeNode());
	}
//
//	public void testModifyNode() {
//		assertFalse(p.modifyNode(new MockVisitor()));
//	}

	public void testAddNodes() {
		assertFalse(p.addNodes(new LinkedList<AbstractNode>()));
	}

	public void testTreeIterator() {
		JTreeTestScriptVisitor v = new JTreeTestScriptVisitor(new TreeNodeDataFactory(
				new MockView()));
		p.acceptVisitor(v);
		p.setJTree(v.createJTree());
	}

	public void testNewDocument() {
		assertTrue(p.getModel() != null);
		assertEquals(p.getModel().getName(), TestScriptDocument.DEFAULT_NAME);
//		p.newDocument();
//		assertTrue(p.getDocument() != null);
//		assertEquals(p.getDocument().getName(), TestScriptDocument.DEFAULT_NAME);
	}

}
