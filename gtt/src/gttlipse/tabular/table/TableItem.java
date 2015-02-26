package gttlipse.tabular.table;

public class TableItem {

	private String _content;
	
	public TableItem() {
		_content = "";
	}
	
	public TableItem(String content) {
		_content = content;
	}
	
	public void setContent(String content) {
		_content = content;
	}
	
	public String getContent() {
		return _content;
	}
	
	@Override
	public String toString() {
		return _content;
	}
}
