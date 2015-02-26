package gttlipse.fit.table;

import gttlipse.fit.view.GTTFitViewDefinition;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;


public class FitTable {
	String m_fixtureType;
	String m_tableName;
	ArrayList<TableItem> m_columnElement;
	ArrayList<TableRow> m_rowElement;

	public FitTable() {
		m_columnElement = new ArrayList<TableItem>();
		m_rowElement = new ArrayList<TableRow>();
	}

	public void setFixtureType(String type) {
		m_fixtureType = type;
	}

	public String getFixtureType() {
		return m_fixtureType;
	}

	public void setTableName(String name) {
		m_tableName = name;
	}

	public String getTableName() {
		return m_tableName;
	}

	public void addColumnElement(TableItem element) {
		m_columnElement.add(element);
	}

	public void addRowElement(TableRow element) {
		m_rowElement.add(element);
	}

	public ArrayList<TableItem> getColumentElement() {
		return m_columnElement;
	}

	public TableRow getRowElement(int index) {
		if (m_rowElement.size() < index) {
			System.err.println("row index out of Bound at fitTable");
			return null;
		}
		return m_rowElement.get(index);
	}

	public ArrayList<TableRow> getRowElement() {
		return m_rowElement;
	}

	public String getItem(String columnName, TableRow row) {
		for (int i = 0; i < m_columnElement.size(); i++) {
			if ((m_columnElement.get(i)).getText().compareTo(columnName) == 0)
				return row.get(
						(m_columnElement.indexOf(m_columnElement.get(i))))
						.getText();
		}
		return "";
	}

	public String getItem(String columnName, ArrayList<TableItem> column) {
		for (int i = 0; i < m_columnElement.size(); i++) {
			if ((m_columnElement.get(i)).getText().compareTo(columnName) == 0)
				return column.get(
						(m_columnElement.indexOf(m_columnElement.get(i))))
						.getText();
		}
		return "";
	}

	public int getColumnCount() {
		return m_columnElement.size();
	}

	public int getRowCount() {
		return m_rowElement.size();
	}

	public boolean addColumnSize() {
		try {
			m_columnElement.add(new TableItem("", SWT.COLOR_WHITE));
			for (int i = 0; i < m_rowElement.size(); i++) {
				m_rowElement.get(i).add(new TableItem("", SWT.COLOR_WHITE));
			}
			return true;
		} catch (Exception e) {
			System.err.println("Fit Table: Add Column Size ERROR!!");
			return false;
		}
	}

	public boolean subColumnSize() {
		int index = m_columnElement.size() - 1;
		if (index <= GTTFitViewDefinition.TableNameTextColumn)
			return false;
		return removeColumn(index);
	}

	public boolean removeColumn(int index) {
		if (index > m_columnElement.size() - 1
				|| index <= GTTFitViewDefinition.TableNameTextColumn)
			return false;
		m_columnElement.remove(index);
		for (int i = 0; i < m_rowElement.size(); i++) {
			m_rowElement.get(i).getRow().remove(index);
		}
		return true;
	}

	public boolean addRowSize() {
		try {
			TableRow row = new TableRow();
			for (int i = 0; i < m_columnElement.size(); i++)
				row.add(new TableItem("", SWT.COLOR_WHITE));
			m_rowElement.add(row);
			return true;
		} catch (Exception e) {
			System.err.println("Fit Table: Add Column Size ERROR!!");
			return false;
		}
	}

	public boolean subRowSize() {
		if (m_rowElement.size() <= 1)
			return false;
		return removeRow(m_rowElement.size() - 1);
	}

	public boolean removeRow(int index) {
		if (index > m_rowElement.size())
			return false;
		m_rowElement.remove(index);
		return true;
	}

	public String columnToString() {
		String columnString = "";
		Iterator<TableItem> ite = m_columnElement.iterator();
		while (ite.hasNext()) {
			TableItem item = (TableItem) ite.next();
			if (item.getText().compareTo("") == 0)
				columnString += (FitTableDefinition.EMPTY
						+ FitTableDefinition.COLOR_SPLIT + item.getColor());
			else
				columnString += (item.getText()
						+ FitTableDefinition.COLOR_SPLIT + item.getColor());
			if (ite.hasNext())
				columnString += FitTableDefinition.ITEM_SPLIT;
		}

		return columnString;
	}

	public String rowToString() {
		String item = "";
		String rowString = "";

		Iterator<TableRow> iterator = m_rowElement.iterator();
		while (iterator.hasNext()) {
			TableRow row = (TableRow) iterator.next();
			for (int i = 0; i < getColumnCount(); i++) {
				if (row.get(i).getText().compareTo("") == 0)
					item += (FitTableDefinition.EMPTY
							+ FitTableDefinition.COLOR_SPLIT + row.get(i)
							.getColor());
				else
					item += (row.get(i).getText()
							+ FitTableDefinition.COLOR_SPLIT + row.get(i)
							.getColor());
				if (i < getColumnCount() - 1)
					item += FitTableDefinition.ITEM_SPLIT;
			}
			if (iterator.hasNext())
				item += FitTableDefinition.ROW_SPLIT;
			rowString += item;
			item = "";
		}
		rowString += "";

		return rowString;
	}

	public void clearTable() {
		m_fixtureType = "";
		m_tableName = "";
		m_columnElement.clear();
		m_rowElement.clear();
	}

}
