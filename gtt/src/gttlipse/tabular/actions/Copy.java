package gttlipse.tabular.actions;

import gttlipse.tabular.editors.MacroTabularEditor;
import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.ClipBoard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;


public class Copy extends TabularAction {

	@Override
	public void run(IAction action) {
		TabularEditor editor = findActiveTabularEditor();
		Object item = null;
		
		if (editor != null) {
			TableModel model = editor.getTableModel();
			item = model.getSelectedItem();
			
			// Copy the selected item
			if (editor instanceof MacroTabularEditor) {
				ClipBoard.setMacroItem(item);
			}
			else {
				ClipBoard.setScriptItem(item);
			}
		}
	}
	
	@Override
	public void dispose() {}

	@Override
	public void init(IWorkbenchWindow window) {}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}
}
