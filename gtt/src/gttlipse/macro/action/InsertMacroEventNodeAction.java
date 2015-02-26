package gttlipse.macro.action;



public class InsertMacroEventNodeAction extends MacroViewAction  {
	public InsertMacroEventNodeAction() {
		super();
	}


	public void run() {
		this.getPresenter().insertMacroEventNode();
	}
}