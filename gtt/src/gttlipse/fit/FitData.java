package gttlipse.fit;

import gttlipse.fit.table.FitTable;

public class FitData {
	FitTable m_table = null;
	
	public FitData() {
	}

	public FitTable getFitTable() {
		return m_table;
	}
	
	public void setFitTable(FitTable table) {
		m_table = table;
	}
}
