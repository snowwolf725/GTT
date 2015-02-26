package testing.macro;

import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ComponentReferenceImpl;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import junit.framework.TestCase;

public class ReferenceComponentTest extends TestCase {

	ComponentReferenceImpl ref;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetPath() {
		ref = new ComponentReferenceImpl();
		assertEquals(null, ref.getComponent());
		assertEquals(null, ref.getReferencePath());

		ref = new ComponentReferenceImpl("widget");
		assertEquals(null, ref.getComponent());
		assertEquals("widget", ref.getReferencePath());

	}

	public void testUpdate() {
		/*
		 * mc widget1 me ce1
		 */
		ref = new ComponentReferenceImpl("mc::widget1");

		assertFalse(ref.lookupComponent(null));

		MacroComponentNode mc = MacroComponentNode.create("mc");
		assertFalse(ref.lookupComponent(mc));

		ComponentNode widget1 = ComponentNode.create("type", "widget1");
		MacroEventNode me = new MacroEventNode("me");
		mc.add(widget1);
		mc.add(me);

		ComponentEventNode ce1 = new ComponentEventNode("widget1");
		me.add(ce1);

		assertTrue(ref.lookupComponent(ce1));
		assertEquals("mc::widget1", ref.getReferencePath());
	}

}
