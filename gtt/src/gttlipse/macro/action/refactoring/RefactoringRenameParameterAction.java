package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;



public class RefactoringRenameParameterAction extends MacroViewAction  {
	public RefactoringRenameParameterAction() {
		super();
	}

	public void run() {
		this.getPresenter().renameMacroEventParameter();
	}
}