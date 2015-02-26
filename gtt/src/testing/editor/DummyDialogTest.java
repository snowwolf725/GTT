package testing.editor;

import gtt.editor.view.dialog.DummyDialog;
import junit.framework.TestCase;

public class DummyDialogTest extends TestCase {

	DummyDialog dialog;

	protected void setUp() throws Exception {
		super.setUp();
		dialog = new DummyDialog();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		dialog = null;
	}

	public void testSetNode() {
		assertEquals(dialog.getNode(), null);
		assertFalse(dialog.setnode);
		dialog.setNode(null);
		assertEquals(dialog.getNode(), null);
		assertTrue(dialog.setnode);
	}

	public void testAppear() {
		assertFalse(dialog.appear);
		dialog.appear();
		assertTrue(dialog.appear);
		dialog.appear();
		assertTrue(dialog.appear);
	}

}
