package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;



public class RefactoringRenameWindowTitleAction extends MacroViewAction  {
	public RefactoringRenameWindowTitleAction() {
		super();
	}

	public void run() {
		this.getPresenter().renameWindowTitle();
	}
}