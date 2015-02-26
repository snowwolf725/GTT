package testing.gttlipse.testScript;

import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import junit.framework.TestCase;
import gttlipse.scriptEditor.testScript.TestMethodNode;

public class TestMethodNodeTest extends TestCase {
	private TestMethodNode methodnode;

	protected void setUp() throws Exception {
		super.setUp();
		methodnode=new TestMethodNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddChildTestScriptDocument() {
		NodeFactory factory = new NodeFactory();
		AbstractNode root = factory.createFolderNode("Root");
		TestScriptDocument doc = new TestScriptDocument(root);
		doc.setName("test123");
		methodnode.addDocument(doc);
		assertEquals("test123",methodnode.getDocAt(0).getName());
	}
	
	public void testAddNewNode() {
		TestScriptDocument doc = methodnode.addInteractionSequence();
		assertEquals("Interaction Sequence",doc.getName());
	}
	
	public void testAddNewDoc(){
		TestScriptDocument doc = methodnode.addNewDoc("test1");
		assertEquals("test1",doc.getName());
	}
	
	public void testRemoveDocument() {
		TestScriptDocument doc = methodnode.addNewDoc("test1");
		methodnode.addNewDoc("test2");
		methodnode.removeDoc(doc);
		assertEquals("test2",methodnode.getDocAt(0).getName());
	}
	
	public void testGetDocuments() {
		methodnode.addNewDoc("test1");
		methodnode.addNewDoc("test2");
		TestScriptDocument docs[]=methodnode.getDocuments();
		assertEquals(2,docs.length);
	}
	
	public void testGetDocByName() {
		methodnode.addNewDoc("test1");
		methodnode.addNewDoc("test2");
		methodnode.addNewDoc("test1");
		TestScriptDocument doc1 = methodnode.getDocByName("test1", 1);
		TestScriptDocument doc2 = methodnode.getDocByName("test1", 2);
		TestScriptDocument doc3 = methodnode.getDocByName("test2", 1);
		assertEquals("test1",doc1.getName());
		assertEquals("test1",doc2.getName());
		assertEquals("test2",doc3.getName());
	}
	
	public void testGetDocAt() {
		methodnode.addNewDoc("test1");
		methodnode.addNewDoc("test2");
		TestScriptDocument doc1 = methodnode.getDocAt(0);
		TestScriptDocument doc2 = methodnode.getDocAt(1);
		assertEquals("test1",doc1.getName());
		assertEquals("test2",doc2.getName());
	}
	
	public void testClone() {
		TestMethodNode methodnode2 = methodnode.clone();
		assertEquals("TestMethodNode",methodnode2.getName());
		assertNotSame(methodnode,methodnode2);
	}
	
	public void testMoveChildrenFront() {
		methodnode.addNewDoc("test2");
		TestScriptDocument doc2 = methodnode.addNewDoc("test1");
		methodnode.moveChildrenFront(doc2);
		assertEquals("test1",methodnode.getDocAt(0).getName());
		assertEquals("test2",methodnode.getDocAt(1).getName());
	}
	
	public void testMoveChildrenRear() {
		TestScriptDocument doc1 = methodnode.addNewDoc("test2");
		methodnode.addNewDoc("test1");
		methodnode.moveChildrenRear(doc1);
		assertEquals("test1",methodnode.getDocAt(0).getName());
		assertEquals("test2",methodnode.getDocAt(1).getName());
	}
	
	public void testIndexOf() {
		TestScriptDocument doc1 = methodnode.addNewDoc("test2");
		TestScriptDocument doc2 = methodnode.addNewDoc("test1");
		int index1 = methodnode.indexOf(doc1);
		int index2 = methodnode.indexOf(doc2);
		assertEquals(1,index1);
		assertEquals(2,index2);
	}
}
