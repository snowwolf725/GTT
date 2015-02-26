package gttlipse.macro.action;



public class RefactoringRenameWindowTitleAction extends MacroViewAction  {
	public RefactoringRenameWindowTitleAction() {
		super();
	}

	public void run() {
		this.getPresenter().renameWindowTitle();
	}
}