package gttlipse.macro.action;



public class CopyNodeAction extends MacroViewAction  {
	public CopyNodeAction() {
		super();
	}

	public void run() {
		getPresenter().doCopy();
	}
}