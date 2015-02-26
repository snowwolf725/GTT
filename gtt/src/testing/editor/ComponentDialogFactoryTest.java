package testing.editor;

import gtt.editor.view.dialog.ComponentDialog;
import gtt.editor.view.dialog.ComponentDialogFactory;
import gtt.editor.view.dialog.DummyDialog;
import gtt.editor.view.dialog.FolderNodeDialog;
import junit.framework.TestCase;

public class ComponentDialogFactoryTest extends TestCase {

	ComponentDialogFactory f;

	protected void setUp() throws Exception {
		super.setUp();
		f = new ComponentDialogFactory();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		f = null;
	}

	public void testCreateEventNodeDialog() {
		assertEquals(f.createEventNodeDialog().getClass(),
				ComponentDialog.class);
	}

	public void testCreateFolderNodeDialog() {
		assertEquals(f.createFolderNodeDialog().getClass(),
				FolderNodeDialog.class);
	}

	public void testCreateModelAssertNodeDialog() {
		assertEquals(f.createModelAssertNodeDialog().getClass(),
				DummyDialog.DUMMY_DIALOG.getClass());
	}

	public void testCreateViewAssertNodeDialog() {
		assertEquals(f.createViewAssertNodeDialog().getClass(),
				ComponentDialog.class);
	}

}
