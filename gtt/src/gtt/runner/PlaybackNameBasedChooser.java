package gtt.runner;

import java.awt.Component;

import org.netbeans.jemmy.ComponentChooser;

public class PlaybackNameBasedChooser implements ComponentChooser {
	private String name;

	public PlaybackNameBasedChooser(String componentName) {
		name = componentName;
	}

	public boolean checkComponent(Component aComponent) {
		String theName = aComponent.getName();
		return (theName != null) && theName.equals(name);
	}

	public String getDescription() {
		return "Matches Components named \"" + name + "\"";
	}
}
