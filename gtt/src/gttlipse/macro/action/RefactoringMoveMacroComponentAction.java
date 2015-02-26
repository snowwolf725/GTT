package gttlipse.macro.action;


public class RefactoringMoveMacroComponentAction extends MacroViewAction {
	public RefactoringMoveMacroComponentAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().moveMacroComponent();
	}
}
