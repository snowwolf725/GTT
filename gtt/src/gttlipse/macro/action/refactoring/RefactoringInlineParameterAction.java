package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringInlineParameterAction extends MacroViewAction {
	public RefactoringInlineParameterAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().inlineMacroEventParameter();
	}
}
