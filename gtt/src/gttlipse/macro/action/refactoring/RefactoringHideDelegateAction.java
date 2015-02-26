package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringHideDelegateAction extends MacroViewAction {
	public RefactoringHideDelegateAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().hideDelegate();
	}
}
