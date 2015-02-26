package gttlipse.macro.action;



public class InsertComponentEventNodeAction extends MacroViewAction  {
	public InsertComponentEventNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertComponentEventNode();
	}
}