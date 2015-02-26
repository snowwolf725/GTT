/**
 * 
 */
package gttlipse.scriptEditor.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class GotoScriptActionFromObject implements IObjectActionDelegate {
	private ISelection m_selection;
	

	/**
	 * 
	 */
	public GotoScriptActionFromObject() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		// TODO Auto-generated method stub
		if (!(m_selection instanceof IStructuredSelection))
			return;
		IStructuredSelection structured = (IStructuredSelection) m_selection;
		if (!(structured.getFirstElement() instanceof IResource))
			return;
		IResource resource = (IResource) structured.getFirstElement();
		
		GoToScript gotoscript = new GoToScript();
		gotoscript.gotoScriptFromResource(resource);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		m_selection = selection;
	}

}
