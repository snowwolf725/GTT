package testing.gttlipse.refactoring;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.InvisibleRootNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.refactoring.macro.RenameMacroWindowTitle;
import gttlipse.refactoring.script.RenameScriptWindowTitle;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import junit.framework.TestCase;

public class RenameWindowTitleTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testMacroRenameWindowTitle() {
		// initial
		InvisibleRootNode invisibleRoot = new InvisibleRootNode();
		MacroComponentNode root = MacroComponentNode.create("AUT");
		MacroComponentNode macro1 = MacroComponentNode.create("MACRO1");
		invisibleRoot.add(root);
		root.add(macro1);
		ComponentNode com1 = ComponentNode.create();
		ComponentNode com2 = ComponentNode.create();
		ComponentNode com3 = ComponentNode.create();
		ComponentNode com4 = ComponentNode.create();
		com1.setName("COM1");
		com1.setTitle("TITLE1");
		com1.setWinType("TYPE1");
		com2.setName("COM2");
		com2.setTitle("TITLE1");
		com2.setWinType("TYPE1");
		com3.setName("COM3");
		com3.setTitle("TITLE1");
		com3.setWinType("TYPE1");
		com4.setName("COM4");
		com4.setTitle("TITLE1");
		com4.setWinType("TYPE2");
		root.add(com1);
		macro1.add(com2);
		macro1.add(com3);
		macro1.add(com4);
		String title = "TITLE1";
		String type = "TYPE1";
		String name = "NAME";
		
		RenameMacroWindowTitle refactor = new RenameMacroWindowTitle(macro1);
		refactor.renameWindowTitle(title, type, name);
		
		// assert
		assertFalse(com1.getTitle().equals(name));
		assertTrue(com2.getTitle().equals(name));
		assertTrue(com3.getTitle().equals(name));
		assertFalse(com4.getTitle().equals(name));
	}
	
	public void testScriptRenameWindowTitle() {
		ProjectNode project = new ProjectNode();
		project.setName("PROJECT");
		SourceFolderNode sourceFoler = new SourceFolderNode("SOURCEFOLDER");
		project.addChild(sourceFoler);
		TestCaseNode class1 = new TestCaseNode("CLASS1");
		TestCaseNode class2 = new TestCaseNode("CLASS2");
		sourceFoler.addChild(class1);
		sourceFoler.addChild(class2);
		TestMethodNode method1 = new TestMethodNode("METHOD1");
		TestMethodNode method2 = new TestMethodNode("METHOD2");
		class1.addChild(method1);
		class2.addChild(method2);
		TestScriptDocument doc1 = TestScriptDocument.create();
		TestScriptDocument doc2 = TestScriptDocument.create();
		method1.addDocument(doc1);
		method2.addDocument(doc2);
		NodeFactory factory = new NodeFactory();
		IComponent com1 = SwingComponent.createDefault();
		IComponent com2 = SwingComponent.createDefault();
		IComponent com3 = SwingComponent.createDefault();
		IComponent com4 = SwingComponent.createDefault();
		com1.setName("COM1");
		com1.setTitle("TITLE1");
		com1.setWinType("TYPE1");
		com2.setName("COM2");
		com2.setTitle("TITLE1");
		com2.setWinType("TYPE1");
		com3.setName("COM3");
		com3.setTitle("TITLE1");
		com3.setWinType("TYPE1");
		com4.setName("COM4");
		com4.setTitle("TITLE1");
		com4.setWinType("TYPE2");
		IEvent event = SwingEvent.create(1, "name");
		EventNode event1 = factory.createEventNode(com1, event);
		EventNode event2 = factory.createEventNode(com2, event);
		EventNode event3 = factory.createEventNode(com3, event);
		EventNode event4 = factory.createEventNode(com4, event);
		doc1.insertNode(doc1.getScript(), event1);
		doc2.insertNode(doc2.getScript(), event2);
		doc2.insertNode(doc2.getScript(), event3);
		doc2.insertNode(doc2.getScript(), event4);
		
		String title = "TITLE1";
		String type = "TYPE1";
		String name = "NAME";
		
		RenameScriptWindowTitle refactor = new RenameScriptWindowTitle();
		refactor.setName(name);
		refactor.setTitle(title);
		refactor.setWindowType(type);
		refactor.setRoot(class2);
		refactor.renameWindowTitle();
		com1 = event1.getComponent();
		com2 = event2.getComponent();
		com3 = event3.getComponent();
		com4 = event4.getComponent();
		
		// assert
		assertFalse(com1.getTitle().equals(name));
		assertTrue(com2.getTitle().equals(name));
		assertTrue(com3.getTitle().equals(name));
		assertFalse(com4.getTitle().equals(name));
	}
}
