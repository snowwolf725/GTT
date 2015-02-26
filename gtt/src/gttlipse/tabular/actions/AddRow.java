package gttlipse.tabular.actions;

import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.table.TableModel;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;


public class AddRow extends TabularAction {

	public AddRow() {};
	
	@Override
	public void run(IAction action) {
		TabularEditor editor = findActiveTabularEditor();
		
		if (editor != null) {
			TableModel model = editor.getTableModel();
			model.addRow();
			editor.refresh();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}

	@Override
	public void dispose() {}

	@Override
	public void init(IWorkbenchWindow window) {}
}
