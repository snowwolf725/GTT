package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringInlineMacroComponentAction extends MacroViewAction {
	public RefactoringInlineMacroComponentAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().inlineMacroComponent();
	}
}
