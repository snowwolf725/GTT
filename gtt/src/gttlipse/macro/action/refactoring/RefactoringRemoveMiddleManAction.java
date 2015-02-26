package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringRemoveMiddleManAction extends MacroViewAction {
	public RefactoringRemoveMiddleManAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().removeMiddleMan();
	}
}
