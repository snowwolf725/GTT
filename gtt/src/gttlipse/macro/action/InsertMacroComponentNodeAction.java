package gttlipse.macro.action;



public class InsertMacroComponentNodeAction extends MacroViewAction  {
	public InsertMacroComponentNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertMacroComponentNode();
	}
}