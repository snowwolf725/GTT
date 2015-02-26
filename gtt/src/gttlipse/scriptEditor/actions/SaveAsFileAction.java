/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.TestProject;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.testScript.io.XmlTestScriptSaveVisitor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class SaveAsFileAction extends EnhancedAction {

	/**
	 * 
	 */
	public SaveAsFileAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public SaveAsFileAction(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public SaveAsFileAction(String arg0, ImageDescriptor arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public SaveAsFileAction(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		String fileexten[] = { "*", "*.gtt" };
		FileDialog fdialog = new FileDialog(m_TreeViewer.getControl().getShell(), SWT.SAVE);
		fdialog.setFilterExtensions(fileexten);
		fdialog.open();
		String filepath = fdialog.getFilterPath() + "\\" + fdialog.getFileName();

		XmlTestScriptSaveVisitor v = new XmlTestScriptSaveVisitor();
		TestProject.getProject().accept(v);
		v.saveFile(filepath);
		m_TreeViewer.refresh();
	}

}
