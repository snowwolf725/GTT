package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringAddParameterAction extends MacroViewAction {
	public RefactoringAddParameterAction() {
		super();
	}
	
	public void run() {
		getPresenter().addMacroEventParameter();
	}
}
