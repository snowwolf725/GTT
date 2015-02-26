package gttlipse.macro.action;



public class InsertIncludeNodeAction extends MacroViewAction  {
	public InsertIncludeNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertIncludeNode();
	}
}