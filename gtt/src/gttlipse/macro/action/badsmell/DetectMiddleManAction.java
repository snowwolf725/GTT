package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class DetectMiddleManAction extends MacroViewAction {
	public void run() {
		getPresenter().detectMiddleMan();
	}
}
