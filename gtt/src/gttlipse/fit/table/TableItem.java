package gttlipse.fit.table;

public class TableItem {
	String m_text;
	int m_color;

	public TableItem() {	
	}

	public TableItem(String text, int color) {
		m_text = text;
		m_color = color;
	}

	public void setText(String text) {
		m_text = text;
	}

	public String getText() {
		return m_text;
	}

	public void setColor(int color) {
		m_color = color;
	}

	public int getColor() {
		return m_color;
	}

	public String toString(){
		return m_text;
	}
}
