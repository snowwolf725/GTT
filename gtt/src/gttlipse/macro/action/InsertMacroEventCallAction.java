package gttlipse.macro.action;



public class InsertMacroEventCallAction extends MacroViewAction  {
	public InsertMacroEventCallAction() {
		super();
	}


	public void run() {
		getPresenter().insertMacroEventCallNode();
	}
}