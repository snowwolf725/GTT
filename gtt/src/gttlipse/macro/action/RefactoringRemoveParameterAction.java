package gttlipse.macro.action;


public class RefactoringRemoveParameterAction extends MacroViewAction {
	public RefactoringRemoveParameterAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().removeMacroEventParameter();
	}
}
