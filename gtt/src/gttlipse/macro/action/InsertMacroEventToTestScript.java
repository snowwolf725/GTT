package gttlipse.macro.action;

public class InsertMacroEventToTestScript extends MacroViewAction {
	public InsertMacroEventToTestScript() {
		super();
	}

	public void run() {
		getPresenter().insertMacroEventToTestScript();
	}
}