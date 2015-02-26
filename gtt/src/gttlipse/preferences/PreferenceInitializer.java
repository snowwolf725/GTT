package gttlipse.preferences;

import gttlipse.GTTlipse;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = GTTlipse.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_MODE, "PlayBack");
		store.setDefault(PreferenceConstants.P_SLEEPTIME, "100");
		store.setDefault(PreferenceConstants.P_LONGMACROEVENT, "15");
		store.setDefault(PreferenceConstants.P_LONGMACROCOM, "25");
		store.setDefault(PreferenceConstants.P_LONGPARLIST, "5");
		store.setDefault(PreferenceConstants.P_SHOTGUNSURGERY, "5");
		store.setDefault(PreferenceConstants.P_DUPLICATEEVENT_GREEN, "5");
		store.setDefault(PreferenceConstants.P_DUPLICATEEVENT_YELLOW, "10");
		store.setDefault(PreferenceConstants.P_DUPLICATEEVENT_RED, "15");
	}

}
