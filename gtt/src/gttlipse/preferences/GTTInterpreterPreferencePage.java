package gttlipse.preferences;

import gttlipse.GTTlipse;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class GTTInterpreterPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public GTTInterpreterPreferencePage() {
		super(GRID);
		setPreferenceStore(GTTlipse.getDefault().getPreferenceStore());
	}

	@Override
	public void createFieldEditors() {
		RadioGroupFieldEditor interMode= new RadioGroupFieldEditor(PreferenceConstants.P_MODE,
						"Interpreter Mode", 1, new String[][] { 
						{ "&PlayBack", "PlayBack" },
						{ "&Collect Component Info", "Collect Component Info" },
						{ "Collect &Test Oracle", "Collect Test Oracle" }}, getFieldEditorParent());
		interMode.loadDefault();
		addField(interMode);
		
		StringFieldEditor sleepTime = new StringFieldEditor(
				PreferenceConstants.P_SLEEPTIME, "Default Sleep Time:",
				getFieldEditorParent());
		sleepTime.loadDefault();
		addField(sleepTime);
	}


	@Override
	public void init(IWorkbench workbench) {
	}

}