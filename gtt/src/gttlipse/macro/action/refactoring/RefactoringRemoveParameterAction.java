package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringRemoveParameterAction extends MacroViewAction {
	public RefactoringRemoveParameterAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().removeMacroEventParameter();
	}
}
