/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.TestProject;
import gttlipse.actions.EnhancedAction;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class OpenFileAction extends EnhancedAction implements
		IWorkbenchWindowActionDelegate {

	/**
	 * 
	 */
	public OpenFileAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public OpenFileAction(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public OpenFileAction(String arg0, ImageDescriptor arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public OpenFileAction(String arg0, int arg1) {
		super(arg0, arg1);
	}

	public void run() {
		String fileexten[] = { "*", "*.gtt" };
		FileDialog fdialog = new FileDialog(m_TreeViewer.getControl()
				.getShell(), SWT.OPEN);
		fdialog.setFilterExtensions(fileexten);
		fdialog.open();
		if (fdialog.getFileName() == "")
			return;
		String filename = fdialog.getFilterPath() + "\\"
				+ fdialog.getFileName();
//		if (filename.matches("\\"))
		OpenFile(filename);
	}

	public void OpenFile(String filename) {
		TestProject.loadTestScript(filename);
		TestProject.updateScriptSync();

		m_TreeViewer.refresh();
	}

	public void dispose() {
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		// this.window = window;
	}

	public void run(IAction arg0) {
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}
}
