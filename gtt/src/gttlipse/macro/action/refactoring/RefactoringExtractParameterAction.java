package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringExtractParameterAction extends MacroViewAction {
	public RefactoringExtractParameterAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().extractMacroEventParameter();
	}
}
