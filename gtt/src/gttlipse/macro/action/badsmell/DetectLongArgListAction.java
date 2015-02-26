package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class DetectLongArgListAction extends MacroViewAction {
	public void run() {
		getPresenter().detectLongArg();
	}
}
