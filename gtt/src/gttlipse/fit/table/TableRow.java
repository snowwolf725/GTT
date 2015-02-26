package gttlipse.fit.table;

import java.util.ArrayList;

public class TableRow {
	ArrayList<TableItem> m_row;

	public TableRow() {
		m_row = new ArrayList<TableItem>();
	}

	public void setRow(ArrayList<TableItem> row) {
		m_row = row;
	}

	public ArrayList<TableItem> getRow() {
		return m_row;
	}

	public TableItem get(int index) {
		if (m_row.size() < index)
			return null;
		return m_row.get(index);
	}

	public void add(TableItem item) {
		m_row.add(item);
	}

	public int getSize() {
		return m_row.size();
	}
}
