package gttlipse.macro.action;


public class RefactoringMoveComponentAction extends MacroViewAction {
	public RefactoringMoveComponentAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().moveComponent();
	}
}
