package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class DetectLongMacroCompAction extends MacroViewAction {
	public void run() {
		getPresenter().detectLongMacroComp();
	}
}
