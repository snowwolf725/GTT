package gttlipse.macro.action;


public class RefactoringAddParameterAction extends MacroViewAction {
	public RefactoringAddParameterAction() {
		super();
	}
	
	public void run() {
		getPresenter().addMacroEventParameter();
	}
}
