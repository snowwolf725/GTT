package gttlipse.scriptEditor.actions;

import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;

public interface ILauncher {

	abstract public JUnitLaunchShortcut getLauncher(); 
	
	abstract public int getPlatformID();
}
