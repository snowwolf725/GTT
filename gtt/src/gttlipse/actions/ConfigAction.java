/**
 * 
 */
package gttlipse.actions;

import gttlipse.scriptEditor.dialog.ConfigDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * @author SnowWolf
 *
 */
public class ConfigAction extends EnhancedAction implements
		IWorkbenchWindowActionDelegate {

	public ConfigAction(){
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		Shell shell = gttlipse.GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
//		PreferenceManager manager = GTTlipse.GTTlipse.getDefault().getWorkbench().getPreferenceManager();
//        PreferenceDialog dialog = new PreferenceDialog(shell, manager);
//		dialog.setSelectedNode("GTTlipse.preferences.GTTPreferencePage");
//		dialog.open();
		
		
		ConfigDialog dialog = new ConfigDialog(shell);
		dialog.open();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}

}
