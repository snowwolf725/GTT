/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.dialog.SelectProjectDialog;
import gttlipse.scriptEditor.views.GTTTestScriptView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.actions
 * 
 */
public class SelectProjectAction extends EnhancedAction implements
		IWorkbenchWindowActionDelegate {
	public void run() {
		GTTTestScriptView view = GTTlipse.showScriptView();
		m_TreeViewer = view.getTreeViewer();
		if (m_TreeViewer == null)
			return;
		SelectProjectDialog dialog = new SelectProjectDialog(m_TreeViewer
				.getControl().getShell());
		dialog.open();
		m_TreeViewer.refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		// TODO Auto-generated method stub
		run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}
}
