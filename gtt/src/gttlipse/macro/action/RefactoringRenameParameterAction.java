package gttlipse.macro.action;



public class RefactoringRenameParameterAction extends MacroViewAction  {
	public RefactoringRenameParameterAction() {
		super();
	}

	public void run() {
		this.getPresenter().renameMacroEventParameter();
	}
}