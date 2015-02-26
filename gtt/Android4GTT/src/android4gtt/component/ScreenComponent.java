package android4gtt.component;

import com.jayway.android.robotium.solo.Solo;

import gtt.eventmodel.IComponent;

public class ScreenComponent {
	
	private IComponent component = null;
	private Solo m_solo = null;
	
	public ScreenComponent(IComponent comp, Solo solo) {
		component = comp;
		m_solo = solo;
	}

	public String getName() {
		return component.getName();
	}
	
	public String getText() {
		return component.getText();
	}
	
	public boolean searchText(String text) {
		return m_solo.searchText(text);
	}
}
