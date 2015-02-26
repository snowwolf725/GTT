package testing.editor;

import gtt.editor.view.JTreeTestScriptVisitor;
import gtt.editor.view.TreeNodeDataFactory;
import gtt.eventmodel.Assertion;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.ModelAssertNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.ViewAssertNode;

import javax.swing.JTree;

import junit.framework.TestCase;
import testing.testscript.MockComponent;
import testing.testscript.MockEvent;

public class JTreeVisitorTest extends TestCase {

	JTreeTestScriptVisitor v;
	MockView view;
	protected void setUp() throws Exception {
		super.setUp();
		view = new MockView();
		v = new JTreeTestScriptVisitor( new TreeNodeDataFactory(view));
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		view = null;
		v = null;
	}

	public void testVisitFolderNode() {
		NodeFactory f = new NodeFactory();
		AbstractNode n = f.createFolderNode("folder");
		n.add(f.createFolderNode("sub1"));
		n.add(f.createFolderNode("sub2"));
		n.add(f.createFolderNode("sub3"));
		n.add(f.createFolderNode("sub4"));
		
		v.visit( (FolderNode) n);
		JTree tree = v.createJTree();
		assertEquals(tree.getModel().getChildCount(tree.getModel().getRoot()), 4);
	}

	public void testVisitEventNode() {
		NodeFactory f = new NodeFactory();
		v.visit( (FolderNode) f.createFolderNode("root"));
		
		AbstractNode n = f.createEventNode(new MockComponent(), new MockEvent() );
		v.visit( (EventNode) n);
		JTree tree = v.createJTree();
		assertEquals(tree.getModel().getChildCount(tree.getModel().getRoot()), 1);
	}

	public void testVisitViewAssertNode() {
		NodeFactory f = new NodeFactory();
		v.visit( (FolderNode) f.createFolderNode("root"));
		
		AbstractNode n = f.createViewAssertNode(new MockComponent(), new Assertion() );
		v.visit( (ViewAssertNode) n);
		JTree tree = v.createJTree();
		assertEquals(tree.getModel().getChildCount(tree.getModel().getRoot()), 1);
	}

	public void testVisitModelAssertNode() {
		NodeFactory f = new NodeFactory();
		v.visit( (FolderNode) f.createFolderNode("root"));
		
		AbstractNode n = f.createModelAssertNode();
		v.visit( (ModelAssertNode) n);
		JTree tree = v.createJTree();
		assertEquals(tree.getModel().getChildCount(tree.getModel().getRoot()), 1);
	}
}
