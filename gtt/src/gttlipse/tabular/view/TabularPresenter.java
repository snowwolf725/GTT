package gttlipse.tabular.view;

import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.provider.HeaderProvider;
import gttlipse.tabular.table.TableModel;

import java.util.ArrayList;


public abstract class TabularPresenter {

	protected String _title;
	protected TableModel _model = null;
	protected TabularEditor _editor = null;
	protected HeaderProvider _headerProvider = null;
	protected ArrayList<String> _row = new ArrayList<String>();
	
	protected abstract void modifyTableModel(Object node);
	
	protected abstract TabularEditor findEditor();
	
	final protected void fillTableModel(Object[] row) {
		_model.addRow(row);
	}
	
	final protected void refresh() {
		// Update the table model and refresh the view of the editor
		_model.initialize();
		_editor.refresh();
	}
	
	// Template method pattern
	final public void initialize(Object node, int index) {
		// Use the name of the node as the editor's title
		_title = node.toString();
		
		// Find and open the tabular editor
		_editor = findEditor();
		
		// Update the title of the editor
		_editor.setEditorTitle(_title);
		
		// Get the table model of the editor
		_model = _editor.getTableModel();
		_model.setRowCount(TableConstants.DEFAULT_ROW_COUNT);
		_model.setName(_title);
		_model.markError(index);
		
		// Get the header provider
		_headerProvider = _model.getHeaderProvider();
		
		// Modify the table model
		modifyTableModel(node);
		
		// Update the model
		refresh();
	}
}
