package gttlipse.macro.action;



public class InsertModelAssertNodeAction extends MacroViewAction  {
	public InsertModelAssertNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertModelAssertNode();
	}
}