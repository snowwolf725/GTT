package gttlipse.macro.action;


public class RefactoringHideDelegateAction extends MacroViewAction {
	public RefactoringHideDelegateAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().hideDelegate();
	}
}
