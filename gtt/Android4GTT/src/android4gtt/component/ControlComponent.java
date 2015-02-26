package android4gtt.component;

import com.jayway.android.robotium.solo.Solo;

import gtt.eventmodel.IComponent;

public class ControlComponent {
	
	private IComponent component = null;
	private Solo m_solo = null;
	
	public ControlComponent(IComponent comp, Solo solo) {
		component = comp;
		m_solo = solo;
	}

	public String getName() {
		return component.getName();
	}
	
	public String getText() {
		return component.getText();
	}
	
	public String getCurrentActivityName() {
		return m_solo.getCurrentActivity().getClass().getSimpleName();
	}
}
