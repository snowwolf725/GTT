package testing.testscript;

import gtt.eventmodel.IComponent;

/**
 * mock object for IComponent
 * 
 * @author zwshen
 * 
 */
public class MockComponent implements IComponent {

	public IComponent clone() {
		MockComponent mc = new MockComponent();
		mc.setName(getName());
		return mc;
	}

	public int getIndex() {
		return 0;
	}

	public int getIndexOfSameName() {
		return 0;
	}

	public static String NAME = "WIDGET";
	public String m_Name = NAME;

	public String getName() {
		return m_Name;
	}

	public static final String TEXT = "TEXT";

	public String getText() {
		return TEXT;
	}

	public String getTitle() {
		return null;
	}

	public static final String TYPE = "TYPE";

	public String getType() {
		return TYPE;
	}

	public String getWinType() {
		return null;
	}

	public void setIndex(int idx) {
	}

	public void setIndexOfSameName(int idx) {

	}

	public void setName(String name) {
		m_Name = name;
	}

	public void setText(String text) {

	}

	public void setTitle(String title) {

	}

	public void setType(String type) {

	}

	public void setWinType(String type) {

	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof MockComponent))
			return false;

		return getName().equals(((MockComponent) o).getName());
	}

	@Override
	public boolean match(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

}
