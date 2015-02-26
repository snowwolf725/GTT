package testing.editor;

import gtt.editor.view.dialog.DummyDialog;
import gtt.editor.view.dialog.EventNodeDialog;
import gtt.editor.view.dialog.ModelAssertNodeDialog;
import gtt.editor.view.dialog.SwingScriptDialogFactory;
import gtt.editor.view.dialog.ViewAssertNodeDialog;
import junit.framework.TestCase;

public class SwingDialogFactoryTest extends TestCase {

	SwingScriptDialogFactory f;

	protected void setUp() throws Exception {
		super.setUp();
		f = new SwingScriptDialogFactory();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		f = null;
	}

	public void testCreateEventNodeDialog() {
		assertEquals(f.createEventNodeDialog().getClass(), EventNodeDialog.class);
	}

	public void testCreateFolderNodeDialog() {
		assertEquals(f.createFolderNodeDialog().getClass(),
				DummyDialog.DUMMY_DIALOG.getClass());
	}

	public void testCreateModelAssertNodeDialog() {
		assertEquals(f.createModelAssertNodeDialog().getClass(),
				ModelAssertNodeDialog.class);
	}

	public void testCreateViewAssertNodeDialog() {
		assertEquals(f.createViewAssertNodeDialog().getClass(), ViewAssertNodeDialog.class);
	}
}
