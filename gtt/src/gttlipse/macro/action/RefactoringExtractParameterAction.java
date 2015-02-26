package gttlipse.macro.action;


public class RefactoringExtractParameterAction extends MacroViewAction {
	public RefactoringExtractParameterAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().extractMacroEventParameter();
	}
}
