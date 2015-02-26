package gttlipse.macro.action;


public class FocusOnPointAction extends MacroViewAction {
	public FocusOnPointAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().focusOnPoint();
	}
}
