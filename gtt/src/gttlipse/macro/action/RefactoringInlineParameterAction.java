package gttlipse.macro.action;


public class RefactoringInlineParameterAction extends MacroViewAction {
	public RefactoringInlineParameterAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().inlineMacroEventParameter();
	}
}
