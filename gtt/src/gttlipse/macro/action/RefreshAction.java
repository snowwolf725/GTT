package gttlipse.macro.action;



public class RefreshAction extends MacroViewAction  {
	public RefreshAction() {
		super();
	}

	public void run() {
		getPresenter().refresh();
	}
}