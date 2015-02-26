package gttlipse.fit.dialog;

import org.eclipse.swt.widgets.Combo;

public class DialogDefinition {
	final static public String DEFAULTWINDOWTYPE = "javax.swing.JFrame";
	final static public String SWINGPATH = "javax.swing.";
	final static public String DEFAULTCOMPONENTTYPE = "javax.swing.JButton";
	
	static public String[] windowTypeList = {
		"javax.swing.JFrame",
		"javax.awt.Frame",
		"javax.awt.Dialog"
	};
	
	static public String[] windowTitleList = {
		"Calculator",
		"Crossword Sage",
		"Sudoku",
		"RTF Word Processor",
		"¿é¤J",
		"¶}±Ò",
		"Àx¦s"
	};
	
	static public void initWindowTypeCombo(Combo combo) {
		for(int i = 0; i < DialogDefinition.windowTypeList.length; i++) {
			combo.add(DialogDefinition.windowTypeList[i]);
		}
	}
	
	static public void initWindowTitleCombo(Combo combo) {
		for(int i = 0; i < DialogDefinition.windowTitleList.length; i++) {
			combo.add(DialogDefinition.windowTitleList[i]);
		}
	}
}
