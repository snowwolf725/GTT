package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;



public class RefactoringRenameAction extends MacroViewAction  {
	public RefactoringRenameAction() {
		super();
	}

	public void run() {
		this.getPresenter().rename();
	}
}