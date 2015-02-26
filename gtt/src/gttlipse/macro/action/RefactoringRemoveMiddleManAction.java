package gttlipse.macro.action;


public class RefactoringRemoveMiddleManAction extends MacroViewAction {
	public RefactoringRemoveMiddleManAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().removeMiddleMan();
	}
}
