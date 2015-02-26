package gttlipse.fit.view;

import de.kupzog.ktable.KTable;

public class ViewInfo {
	int m_columnCount;
	int m_rowCount;
	String m_tableName;
	String m_fixtureType;
	KTable m_table;
	FitTableViewModel m_tableModel;
	
	public ViewInfo() {
		m_columnCount = 0;
		m_rowCount = 0;
		m_table = null;
	}

	public void setTableSize(int col, int row) {
		m_columnCount = col;
		m_rowCount = row;
	}
	
	public int getColumnCount() {
		return m_columnCount;
	}
	
	public int getRowCount() {
		return m_rowCount;
	}

	public void setTableName(String name) {
		m_tableName = name;
	}

	public String getTableName() {
		return m_tableName;
	}

	public void setFixtureType(String type) {
		m_fixtureType = type;
	}

	public String getFixtureType() {
		return m_fixtureType;
	}
	
	public void setKTable(KTable table) {
		m_table = table;
	}
	
	public KTable getKTable() {
		return m_table;
	}

	public FitTableViewModel createTableModel() {
		if(m_tableModel == null) {
			m_tableModel = new FitTableViewModel(m_table, m_columnCount);
			m_table.setModel(m_tableModel);
		}
		return m_tableModel;
	}
}
