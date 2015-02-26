package gttlipse.macro.action;



public class RefactoringRenameAction extends MacroViewAction  {
	public RefactoringRenameAction() {
		super();
	}

	public void run() {
		this.getPresenter().rename();
	}
}