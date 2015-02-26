package gttlipse.web.loadtesting;

import java.util.Vector;

import gttlipse.web.loadtesting.view.LoadTestingResultItem;;

public class LoadTestingResultCollector {
	
	private Vector<LoadTestingResultItem> m_items = null;
	
	private final static LoadTestingResultCollector m_Instance = new LoadTestingResultCollector();
	
	public LoadTestingResultCollector() {
	}
	
	public static LoadTestingResultCollector instance() {
		return m_Instance;
	}
	
	public LoadTestingResultCollector getCollector() {
		if(m_Instance == null) {
			return new LoadTestingResultCollector();
		}
		return m_Instance;
	}
	
	public Vector<LoadTestingResultItem> getItems() {
		if(m_items == null) {
			return new Vector<LoadTestingResultItem>();
		}
		return m_items;
	}
	
	public void addItem(LoadTestingResultItem item) {
		m_items.add(item);
	}
	
	public void removeItem(LoadTestingResultItem item) {
		m_items.remove(item);
	}
	
	public void clearAll() {
		m_items.clear();
	}
}
