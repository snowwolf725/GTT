package gttlipse.tabular.actions;

import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.table.TableModel;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;


public class RemoveRow extends TabularAction {

	@Override
	public void run(IAction action) {
		TabularEditor editor = findActiveTabularEditor();
		
		if (editor != null) {
			TableModel model = editor.getTableModel();
			model.removeRow();
			editor.refresh();
			model.synchronize();
		}
	}
	
	@Override
	public void dispose() {}

	@Override
	public void init(IWorkbenchWindow window) {}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}
}
