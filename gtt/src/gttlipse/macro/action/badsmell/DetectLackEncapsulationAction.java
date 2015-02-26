package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class DetectLackEncapsulationAction extends MacroViewAction {
	public void run() {
		getPresenter().detectLackEncapsulation();
	}
}
