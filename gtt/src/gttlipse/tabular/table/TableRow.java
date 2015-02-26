package gttlipse.tabular.table;

import java.util.ArrayList;

public class TableRow {

	private ArrayList<TableItem> _row = null;
	
	public TableRow() {
		_row = new ArrayList<TableItem>();
	}
	
	public void addItem(TableItem item) {
		_row.add(item);
	}
	
	public boolean addItem(int index, TableItem item) {
		if (index < 0 || index > _row.size()) {
			return false;
		}
		_row.add(index, item);
		return true;
	}
	
	public boolean removeItem(int index) {
		if (index < 0 || index >= _row.size()) {
			return false;
		}
		_row.remove(index);
		return true;
	}
	
	public TableItem getItem(int index) {
		if (index < 0 || index >= _row.size()) {
			return null;
		}
		return _row.get(index);
	}
	
	public Object[] getItems() {
		return _row.toArray();
	}
	
	public int getItemCount() {
		return _row.size();
	}
}
