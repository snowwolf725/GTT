package testing.macro;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroNodeFactory;
import gtt.macro.transform.IMacroTransform;
import gtt.macro.transform.MacroTransform;
import gtt.testscript.AbstractNode;
import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import junit.framework.TestCase;
import testing.testscript.MockComponent;
import testing.testscript.MockEvent;

public class MacroTransformTest extends TestCase {

	IMacroTransform transform;

	protected void setUp() throws Exception {
		super.setUp();
		transform = MacroTransform.instance();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		transform = null;
	}

	public void testTransformAbstractMacroNode() {
		AbstractMacroNode root = new MacroNodeFactory()
				.createMacroComponentNode("123");
		assertNull(transform.transform(root));
	}

	public void testTransformAbstractNode() {
		NodeFactory factory = new NodeFactory();
		AbstractNode root = factory.createFolderNode("root");
		AbstractMacroNode tRoot = transform.transform(root);

		assertNotNull(tRoot);
		assertEquals(tRoot.getName(), "root");
		assertEquals(tRoot.size(), 0);

	}

	public void testTransformAbstractNode_withLengthEvents() {
		NodeFactory factory = new NodeFactory();
		IComponent c = new MockComponent();
		IEvent e = new MockEvent();

		AbstractNode root = factory.createFolderNode("root");

		c.setName("c1");
		root.add(factory.createEventNode(c, e));

		c.setName("c2");
		root.add(factory.createEventNode(c, e));

		c.setName("c3");
		root.add(factory.createEventNode(c, e));

		c.setName("c4");
		root.add(factory.createEventNode(c, e));

		AbstractMacroNode tRoot = transform.transform(root);
		assertNotNull(tRoot);
		assertEquals(tRoot.getName(), "root");
		assertEquals(tRoot.size(), 5);

		// 5 elements - 4 component node, 1 macro event
		assertEquals(tRoot.get(0).getClass(), ComponentNode.class);
		assertEquals(tRoot.get(1).getClass(), ComponentNode.class);
		assertEquals(tRoot.get(2).getClass(), ComponentNode.class);
		assertEquals(tRoot.get(3).getClass(), ComponentNode.class);
		assertEquals(tRoot.get(4).getClass(), MacroEventNode.class);

		// macro events
		MacroEventNode me = (MacroEventNode) tRoot.get(4);
		assertEquals(me.size(), 4);
		assertEquals(me.get(0).getClass(), ComponentEventNode.class);
		assertEquals(me.get(1).getClass(), ComponentEventNode.class);
		assertEquals(me.get(2).getClass(), ComponentEventNode.class);
		assertEquals(me.get(3).getClass(), ComponentEventNode.class);
	}

	public void testTransformAbstractNode_withMoreFolderNodes() {
		NodeFactory factory = new NodeFactory();

		AbstractNode root = factory.createFolderNode("root");
		root.add(factory.createFolderNode("folder1"));
		root.add(factory.createFolderNode("folder2"));
		root.add(factory.createFolderNode("folder3"));

		AbstractMacroNode tRoot = transform.transform(root);
		assertNotNull(tRoot);

		assertEquals(tRoot.getName(), "root");
		assertEquals(tRoot.size(), 3);

		assertEquals(tRoot.get(0).getName(), "folder1");
		assertEquals(tRoot.get(0).size(), 0);
		assertEquals(tRoot.get(1).getName(), "folder2");
		assertEquals(tRoot.get(1).size(), 0);
		assertEquals(tRoot.get(2).getName(), "folder3");
		assertEquals(tRoot.get(2).size(), 0);
	}

	public void testTransformAbstractNode_withMoreFolderAndEvent() {
		NodeFactory factory = new NodeFactory();
		IComponent c = new MockComponent();
		IEvent e = new MockEvent();

		AbstractNode root = factory.createFolderNode("root");
		FolderNode fn;

		// empty folder
		fn = factory.createFolderNode("folder0");
		root.add(fn);

		// 1 element folder
		fn = factory.createFolderNode("folder1");
		fn.add(factory.createEventNode(c, e));
		root.add(fn);

		// 2 elements folder
		fn = factory.createFolderNode("folder2");
		fn.add(factory.createEventNode(c, e));
		fn.add(factory.createEventNode(c, e));
		root.add(fn);

		// 3 elements empty folder
		fn = factory.createFolderNode("folder3");
		fn.add(factory.createEventNode(c, e));
		fn.add(factory.createEventNode(c, e));
		fn.add(factory.createEventNode(c, e));
		root.add(fn);

		AbstractMacroNode tRoot = transform.transform(root);
		assertNotNull(tRoot);

		assertEquals("root", tRoot.getName());
		assertEquals(4, tRoot.size());

		assertEquals("folder0", tRoot.get(0).getName());
		assertEquals(0, tRoot.get(0).size(), 0);

		assertEquals("folder1", tRoot.get(1).getName());
		assertEquals(2, tRoot.get(1).size());
		assertEquals(MacroEventNode.class, tRoot.get(1).get(1).getClass());
		assertEquals(1, tRoot.get(1).get(1).size());

		assertEquals("folder2", tRoot.get(2).getName());
		assertEquals(2, tRoot.get(2).size());
		assertEquals(MacroEventNode.class, tRoot.get(2).get(1).getClass());
		assertEquals(2, tRoot.get(2).get(1).size());

		assertEquals("folder3", tRoot.get(3).getName());
		assertEquals(2, tRoot.get(3).size());
		assertEquals(MacroEventNode.class, tRoot.get(3).get(1).getClass());
		assertEquals(3, tRoot.get(3).get(1).size());
	}

	public void testTransformAbstractNode_EventNode() {
		NodeFactory factory = new NodeFactory();
		IComponent c = new MockComponent();
		IEvent e = new MockEvent();

		AbstractNode root = factory.createEventNode(c, e);

		AbstractMacroNode tRoot = transform.transform(root);
		assertNotNull(tRoot);
		assertEquals(tRoot.getName(), "MACRO");
		assertEquals(tRoot.size(), 2);

		// 有1個 ComponentNode (MockComponent)
		assertEquals(tRoot.get(0).getClass(), ComponentNode.class);
		assertEquals(tRoot.get(0).getName(), c.getName());
		assertEquals("MACRO::WIDGET", tRoot.get(0).getPath().toString());
		assertEquals(0, tRoot.get(0).size());

		// 有1個 MacroEventNode
		assertEquals(tRoot.get(1).getClass(), MacroEventNode.class);
		assertEquals(tRoot.get(1).getName(), "AllTests");
		assertEquals(tRoot.get(1).size(), 1);

		// MacroEvent下會有一個 ComponentEvent
		assertEquals(tRoot.get(1).get(0).getClass(), ComponentEventNode.class);
		ComponentEventNode en = (ComponentEventNode) tRoot.get(1).get(0);
		// en.setComponentPath("Macro::NAME");
		assertEquals("MACRO::WIDGET", en.getComponentPath());
		assertEquals("WIDGET", en.getName());
		assertEquals(0, en.size());
	}

	public void testTransformOracleNode() {
		NodeFactory factory = new NodeFactory();

		AbstractNode root = factory.createFolderNode("root");
		AbstractNode f;
		f = factory.createFolderNode("folder1");
		gtt.testscript.OracleNode on = factory.createOracleNode();
		f.add(on);
		root.add(f);

		AbstractMacroNode tRoot = transform.transform(root);
		assertNotNull(tRoot);

		assertEquals(tRoot.getName(), "root");
		assertEquals(tRoot.size(), 1);

		assertEquals(tRoot.get(0).getName(), "folder1"); // macro component
		assertEquals(tRoot.get(0).size(), 1);
		assertEquals("folder1", tRoot.get(0).get(0).getName()); // macro event
		assertEquals(tRoot.get(0).get(0).size(), 1);
		assertEquals("TestOracle", tRoot.get(0).get(0).get(0).getName());

		gtt.macro.macroStructure.OracleNode on2 = (gtt.macro.macroStructure.OracleNode) tRoot
				.get(0).get(0).get(0);
		// reference 資料不一樣
		assertNotSame(on.getOracleData(), on2.getOracleData());
		// uuid 一樣
		assertEquals(on.getOracleData().getUUID(), on2.getOracleData()
				.getUUID());

	}
}
