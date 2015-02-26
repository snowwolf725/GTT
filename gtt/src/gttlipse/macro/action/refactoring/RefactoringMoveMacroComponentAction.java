package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;


public class RefactoringMoveMacroComponentAction extends MacroViewAction {
	public RefactoringMoveMacroComponentAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().moveMacroComponent();
	}
}
