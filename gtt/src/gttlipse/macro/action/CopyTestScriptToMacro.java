package gttlipse.macro.action;



public class CopyTestScriptToMacro extends MacroViewAction  {
	public CopyTestScriptToMacro() {
		super();
	}

	public void run() {
		getPresenter().copyTestScriptToMacro();
	}
}