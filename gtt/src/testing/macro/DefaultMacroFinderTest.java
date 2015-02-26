package testing.macro;

import gtt.macro.DefaultMacroFinder;
import gtt.macro.MacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import junit.framework.TestCase;

public class DefaultMacroFinderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFindByName() {
		AbstractMacroNode root = makeMacroFixture();
		MacroFinder finder = new DefaultMacroFinder(root);

		assertNull(finder.findByName(""));
		assertNull(finder.findByName("MacroComponent")); // 不會找到root自已

		assertNotNull(finder.findByName("child1"));
		assertNotNull(finder.findByName("child2"));
		assertNotNull(finder.findByName("child3"));

		assertNull(finder.findByName("child4")); // no scuh child
	}

	public void testFindByPath() {
		AbstractMacroNode root = makeMacroFixture();
		MacroFinder finder = new DefaultMacroFinder(root);

		assertNull(finder.findByPath(""));
		assertNull(finder.findByPath("MacroComponent")); // 不會找到root自已

		assertNotNull(finder.findByPath("MacroComponent::child1"));
		assertNotNull(finder.findByPath("MacroComponent::child2"));
		assertNotNull(finder.findByPath("MacroComponent::child3"));

		assertNull(finder.findByPath("MacroComponent::child4")); // no scuh
																	// child
		assertNull(finder.findByPath("MacroComponent::child1::child2")); // no
																			// scuh
																			// child
		assertNull(finder.findByPath("MacroComponent::child1::child3")); // no
																			// scuh
																			// child

		assertNull(finder.findByPath("child1::child2")); // no scuh child
	}

	public void testFindByNameWithNodeType() {
		AbstractMacroNode root = makeMacroFixture();
		MacroFinder finder = new DefaultMacroFinder(root);

		assertNull(finder.findByName(null, MacroComponentNode.class));
		assertNull(finder.findByName("", MacroComponentNode.class));
		assertNull(finder
				.findByName("MacroComponent", MacroComponentNode.class)); // 不會找到root自已

		assertNotNull(finder.findByName("child1", MacroComponentNode.class));
		assertNotNull(finder.findByName("child2", MacroComponentNode.class));
		assertNotNull(finder.findByName("child3", MacroComponentNode.class));

		// 指定其它種type 會找不到
		assertNull(finder.findByName("child1", MacroEventNode.class));
		assertNull(finder.findByName("child2", MacroEventNode.class));
		assertNull(finder.findByName("child3", MacroEventNode.class));
	}

	public void testFindByPathWithNodeType() {
		AbstractMacroNode root = makeMacroFixture();
		MacroFinder finder = new DefaultMacroFinder(root);

		assertNull(finder.findByPath(null, MacroComponentNode.class));
		assertNull(finder.findByPath("", MacroComponentNode.class));
		assertNull(finder
				.findByPath("MacroComponent", MacroComponentNode.class)); // 不會找到root自已

		assertNotNull(finder.findByPath("MacroComponent::child1",
				MacroComponentNode.class));
		assertNotNull(finder.findByPath("MacroComponent::child2",
				MacroComponentNode.class));
		assertNotNull(finder.findByPath("MacroComponent::child3",
				MacroComponentNode.class));

		// 指定其它種type 會找不到
		assertNull(finder.findByPath("MacroComponent::child1",
				MacroEventNode.class));
		assertNull(finder.findByPath("MacroComponent::child2",
				MacroEventNode.class));
		assertNull(finder.findByPath("MacroComponent::child3",
				MacroEventNode.class));
	}

	public void testFindLocalRootOfNull() {
		assertNull(DefaultMacroFinder.findLocalRoot(null));
	}

	public void testFindLocalRootOfMacroComponent() {
		AbstractMacroNode root = MacroComponentNode.create("MacroComponent");
		AbstractMacroNode child1 = MacroComponentNode.create("child1");
		root.add(child1);

		assertNotNull(DefaultMacroFinder.findLocalRoot(child1));
		assertEquals(root, DefaultMacroFinder.findLocalRoot(child1));
	}

	public void testFindLocalRootOfComponentEvent() {
		AbstractMacroNode root = MacroComponentNode.create("MacroComponent");

		MacroEventNode me = MacroEventNode.create("MacroEvent");
		ComponentEventNode ce = new ComponentEventNode();
		root.add(me);
		me.add(ce);

		assertNotNull(DefaultMacroFinder.findLocalRoot(ce));
		assertEquals(root, DefaultMacroFinder.findLocalRoot(ce));
	}
	
	public void testFindComponentNodeByName() {
		AbstractMacroNode root = makeMacroFixture();
		MacroFinder finder = new DefaultMacroFinder(root);
		
		assertNull( finder.findComponentNodeByPath("MacroComponent") );
		assertNull( finder.findComponentNodeByPath("MacroComponent::child1") );
		
		assertNotNull( finder.findComponentNodeByPath("MacroComponent::child1::c1") );
		assertNotNull( finder.findComponentNodeByPath("MacroComponent::child1::c2") );
		assertNotNull( finder.findComponentNodeByPath("MacroComponent::child1::c3") );
		
		assertNull( finder.findComponentNodeByPath("MacroComponent::child1::c4") );
		assertNotNull( finder.findComponentNodeByPath("MacroComponent::child2::c4") );

	
		assertNull( finder.findComponentNodeByPath("MacroComponent::child1::c7") );
		assertNull( finder.findComponentNodeByPath("MacroComponent::child2::c7") );
		assertNotNull( finder.findComponentNodeByPath("MacroComponent::child3::c7") );
}

	private AbstractMacroNode makeMacroFixture() {
		AbstractMacroNode root = MacroComponentNode.create("MacroComponent");
		MacroComponentNode mc1 = MacroComponentNode.create("child1");
		root.add(mc1);
		mc1.add(ComponentNode.create("type", "c1"));
		mc1.add(ComponentNode.create("type", "c2"));
		mc1.add(ComponentNode.create("type", "c3"));

		MacroComponentNode mc2 = MacroComponentNode.create("child2");
		root.add(mc2);
		mc2.add(ComponentNode.create("type", "c4"));
		mc2.add(ComponentNode.create("type", "c4"));
		mc2.add(ComponentNode.create("type", "c6"));

		MacroComponentNode mc3 = MacroComponentNode.create("child3");
		root.add(mc3);
		mc3.add(ComponentNode.create("type", "c7"));
		mc3.add(ComponentNode.create("type", "c8"));
		mc3.add(ComponentNode.create("type", "c9"));

		return root;
	}

}
