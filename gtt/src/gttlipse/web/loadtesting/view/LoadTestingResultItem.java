package gttlipse.web.loadtesting.view;

import java.util.Vector;

public class LoadTestingResultItem {
	private String m_type;

	private String m_path;
	
	private long m_time;
	
	private Vector<LoadTestingResultItem> m_items = new Vector<LoadTestingResultItem>();
	
	LoadTestingResultItem() {
		setType("InvisibleRoot");
	}
	
	public LoadTestingResultItem(String type, String path, long time) {
		m_type = type;
		m_path = path;
		m_time = time;
	}
	
	public void setImportance(long time) {
		m_time = time;
	}
	
	public long getTime() {
		return m_time;
	}
	
	public String getType() {
		return m_type;
	}
	
	public String getPath() {
		return m_path;
	}
	
	public void setPath(String m_path) {
		this.m_path = m_path;
	}
	
	public void setType(String m_type) {
		this.m_type = m_type;
	}
	
	public void add(LoadTestingResultItem item){
		m_items.add(item);
	}
	
	public void clear(){
		m_items.clear();
	}
	
	public String toString(){
		return getType();
	}
	
	public Object[] getChildren() {
		return m_items.toArray();
	}
	
	public boolean hasChildren() {
		return m_items.isEmpty()?false:true;
	}
	
}
