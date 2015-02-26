package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class DetectHierarchyAction extends MacroViewAction {
	public void run() {
		getPresenter().detectHierarchy();
	}
}
