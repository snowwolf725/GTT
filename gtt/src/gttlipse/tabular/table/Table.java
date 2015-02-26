package gttlipse.tabular.table;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class Table implements IEditorInput {

	private ArrayList<TableRow> _rows = null;
	private String _name = "";
	
	public Table() {
		_rows = new ArrayList<TableRow>();
	}
	
	public Table(String name) {
		this();
		_name = name;
	}
	
	public void clear() {
		_rows.clear();
	}
	
	public TableRow getRow(int index) {
		if (index < 0 || index >= _rows.size()) {
			return null;
		}
		return _rows.get(index);
	}
	
	public int getRowCount() {
		return _rows.size();
	}
	
	public int getColumnCount() {
		return ((TableRow)_rows.get(0)).getItemCount();
	}
	
	public void addRow(TableRow row) {
		_rows.add(row);
	}
	
	public boolean addRow(int index, TableRow row) {
		if (index < 0 || index > _rows.size()) {
			return false;
		}
		_rows.add(index, row);
		return true;
	}
	
	public boolean removeRow(int index) {
		if (index < 0 || index >= _rows.size()) {
			return false;
		}
		_rows.remove(index);
		return true;
	}
	
	public boolean addColumn(int index) {
		if (index < 0 || index > getColumnCount()) {
			return false;
		}
		
		// Insert item to every row at the specified position
		for (int i = 0; i < _rows.size(); i++) {
			TableRow row = (TableRow)_rows.get(i);
			row.addItem(index, new TableItem(""));
		}
		return true;
	}
	
	public boolean removeColumn(int index) {
		if (index < 0 || index >= getColumnCount()) {
			return false;
		}
		
		// Remove item from every row at the specified position
		for (int i = 0; i < _rows.size(); i++) {
			TableRow row = (TableRow)_rows.get(i);
			row.removeItem(index);
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return _name;
	}
}
