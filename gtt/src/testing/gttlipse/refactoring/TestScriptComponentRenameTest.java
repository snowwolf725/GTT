package testing.gttlipse.refactoring;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.ViewAssertNode;
import gttlipse.refactoring.script.CheckSameNameVisitor;
import gttlipse.refactoring.script.RefactoringRenameVisitor;
import junit.framework.TestCase;

public class TestScriptComponentRenameTest extends TestCase {
	private NodeFactory _factory = null;
	private CheckSameNameVisitor _checkVisitor = null;
	private RefactoringRenameVisitor _renameVisitor = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		_factory = new NodeFactory();
		_checkVisitor = new CheckSameNameVisitor();
		_renameVisitor = new RefactoringRenameVisitor();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testVisitEventNode() {
		SwingComponent com = SwingComponent.createComponent("type", "winType", "title", "name", "text", 0, 0);
		SwingComponent oldCom = com.clone();
		_checkVisitor.setComponet(oldCom.clone());
		_renameVisitor.setRenameComponent(oldCom.clone());
		SwingEvent event = SwingEvent.create(1, "name");
		EventNode node = _factory.createEventNode(com, event);
		
		//check same name and not same name
		_checkVisitor.visit(node);
		assertTrue(_checkVisitor.isSameName());
		com.setName("12");
		node.setComponent(com);
		_checkVisitor = new CheckSameNameVisitor();
		_checkVisitor.setComponet(oldCom.clone());
		_checkVisitor.visit(node);
		assertTrue(!_checkVisitor.isSameName());
		
		//rename
		_renameVisitor.setComponent(com.clone());
		_renameVisitor.visit(node);
		SwingComponent newCom = (SwingComponent) node.getComponent();
		assertTrue(newCom.match(oldCom));
	}
	
	public void testVisitViewAssertNode() {
		SwingComponent com = SwingComponent.createComponent("type", "winType", "title", "name", "text", 0, 0);
		SwingComponent oldCom = com.clone();
		_checkVisitor.setComponet(oldCom.clone());
		_renameVisitor.setRenameComponent(oldCom.clone());
		Assertion a = new Assertion();
		ViewAssertNode node = _factory.createViewAssertNode(com, a);
		
		//check same name and not same name
		_checkVisitor.visit(node);
		assertTrue(_checkVisitor.isSameName());
		com.setName("12");
		node.setComponent(com);
		_checkVisitor = new CheckSameNameVisitor();
		_checkVisitor.setComponet(oldCom.clone());
		_checkVisitor.visit(node);
		assertTrue(!_checkVisitor.isSameName());
		
		//rename
		_renameVisitor.setComponent(com.clone());
		_renameVisitor.visit(node);
		SwingComponent newCom = (SwingComponent) node.getComponent();
		assertTrue(newCom.match(oldCom));
	}
}
