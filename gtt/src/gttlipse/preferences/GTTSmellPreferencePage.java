package gttlipse.preferences;

import gttlipse.GTTlipse;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class GTTSmellPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public GTTSmellPreferencePage() {
		super(GRID);
		setPreferenceStore(GTTlipse.getDefault().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		
	}

	@Override
	protected void createFieldEditors() {
		StringFieldEditor longMacroEvent = new StringFieldEditor(
				PreferenceConstants.P_LONGMACROEVENT, "Long Macro Event:",
				getFieldEditorParent());
		longMacroEvent.loadDefault();
		addField(longMacroEvent);
		
		StringFieldEditor longMacroCom = new StringFieldEditor(
				PreferenceConstants.P_LONGMACROCOM, "Long Macro Component:",
				getFieldEditorParent());
		longMacroCom.loadDefault();
		addField(longMacroCom);
		
		StringFieldEditor longParameterList = new StringFieldEditor(
				PreferenceConstants.P_LONGPARLIST, "Long Parameter List:",
				getFieldEditorParent());
		longParameterList.loadDefault();
		addField(longParameterList);
		
		StringFieldEditor shotgunSurgery = new StringFieldEditor(
				PreferenceConstants.P_SHOTGUNSURGERY, "Shotgun Surgery:",
				getFieldEditorParent());
		shotgunSurgery.loadDefault();
		addField(shotgunSurgery);
		
		StringFieldEditor duplicateEventGreen = new StringFieldEditor(
				PreferenceConstants.P_DUPLICATEEVENT_GREEN, "Duplicate Event Green:",
				getFieldEditorParent());
		duplicateEventGreen.loadDefault();
		addField(duplicateEventGreen);
		
		StringFieldEditor duplicateEventYellow = new StringFieldEditor(
				PreferenceConstants.P_DUPLICATEEVENT_YELLOW, "Duplicate Event Yellow:",
				getFieldEditorParent());
		duplicateEventYellow.loadDefault();
		addField(duplicateEventYellow);
		
		StringFieldEditor duplicateEventRed = new StringFieldEditor(
				PreferenceConstants.P_DUPLICATEEVENT_RED, "Duplicate Event Red:",
				getFieldEditorParent());
		duplicateEventRed.loadDefault();
		addField(duplicateEventRed);
	}

}
