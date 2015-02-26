package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;

public class ClearBadSmellAction extends MacroViewAction {
	public void run() {
		getPresenter().clearBadSmell();
	}
}
