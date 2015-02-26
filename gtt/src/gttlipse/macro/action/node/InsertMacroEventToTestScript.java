package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;

public class InsertMacroEventToTestScript extends MacroViewAction {
	public InsertMacroEventToTestScript() {
		super();
	}

	public void run() {
		getPresenter().insertMacroEventToTestScript();
	}
}