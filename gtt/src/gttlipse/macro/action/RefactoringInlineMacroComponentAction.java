package gttlipse.macro.action;


public class RefactoringInlineMacroComponentAction extends MacroViewAction {
	public RefactoringInlineMacroComponentAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().inlineMacroComponent();
	}
}
