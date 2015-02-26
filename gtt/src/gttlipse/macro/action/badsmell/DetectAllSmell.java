package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class DetectAllSmell extends MacroViewAction {
	public void run() {
		getPresenter().detectAllBadsmell();
	}
}
