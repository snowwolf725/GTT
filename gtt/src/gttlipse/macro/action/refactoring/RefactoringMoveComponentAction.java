package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringMoveComponentAction extends MacroViewAction {
	public RefactoringMoveComponentAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().moveComponent();
	}
}
