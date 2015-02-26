package gttlipse.macro.action;



public class EditNodeAction extends MacroViewAction  {
	public EditNodeAction() {
		super();
	}


	public void run() {
		getPresenter().editSelectedNode();
	}
}
