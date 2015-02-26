package gttlipse.macro.action;


public class InsertExistenceAssertNodeAction extends MacroViewAction  {
	public InsertExistenceAssertNodeAction() {
		super();
	}

	public void run() {
		getPresenter().insertExistenceAssertNode();
	}
}