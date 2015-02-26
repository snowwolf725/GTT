package gttlipse.fit;

import gttlipse.fit.node.FitNode;
import gttlipse.fit.table.FitTable;
import gttlipse.fit.table.TableLoader;
import gttlipse.fit.table.TableSaver;

public class FitInfoCollector {
	String m_tablePath;
	FitData m_fitdata = null;
	FitNode m_fitNode = null;

	public FitInfoCollector() {
		m_fitdata = new FitData();
	}

	public FitData getFitData() {
		return m_fitdata;
	}

	public void collectAll(FitNode node) {
		m_fitNode = node;
		m_tablePath = node.getProjectRoot() + node.getTableFilePath();
		m_fitdata.setFitTable(collectTable(m_tablePath));
	}

	private FitTable collectTable(String tableFilePath) {
		return TableLoader.read(tableFilePath, m_fitNode.getFixtureType());
	}

	public String getProjectRoot(String path) {
		path = path.substring(0, path.lastIndexOf("/") - 1);
		path = path.substring(0, path.lastIndexOf("/"));
		return path;
	}

	public boolean updateTable() {
		try {
			TableSaver m_saver = new TableSaver();
			m_saver.save(m_fitdata.getFitTable(), m_tablePath);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}
}
