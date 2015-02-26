package gttlipse.macro.action;



public class DeleteNodeAction extends MacroViewAction  {
	public DeleteNodeAction() {
		super();
	}


	public void run() {
		getPresenter().deleteMacroNode();
	}
}