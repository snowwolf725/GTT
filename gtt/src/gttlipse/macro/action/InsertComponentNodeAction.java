package gttlipse.macro.action;



public class InsertComponentNodeAction extends MacroViewAction  {
	public InsertComponentNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertComponentNode(null);
	}
}
