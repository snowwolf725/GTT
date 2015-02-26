package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class DetectLongMacroEventAction extends MacroViewAction {
	public void run() {
		getPresenter().detectLongMacroEvent();
	}
}
