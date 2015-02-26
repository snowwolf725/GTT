package android4gtt.scriptEditor.action;

import gttlipse.scriptEditor.actions.ILauncher;

import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;

import com.android.ide.eclipse.adt.internal.launch.junit.AndroidJUnitLaunchShortcut;

@SuppressWarnings("restriction")
public class AndroidJUnitLauncher implements ILauncher {

	@Override
	public JUnitLaunchShortcut getLauncher() {
		return new AndroidJUnitLaunchShortcut();
	}

	@Override
	public int getPlatformID() {
		return 3;
	}

}
