package gttlipse.tabular.actions;

import gttlipse.GTTlipse;
import gttlipse.tabular.editors.TabularEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


public abstract class TabularAction extends Action implements IWorkbenchWindowActionDelegate {

	public TabularAction() {
		super();
	}
	
	protected TabularEditor findActiveTabularEditor() {
		// Get the reference of the active editor
		IEditorPart editor = GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow()
		 							 .getActivePage().getActiveEditor();
		if (editor instanceof TabularEditor) {
			return (TabularEditor)editor;
		}
		return null;
	}
}
