package gttlipse.macro.action;



public class CutNodeAction extends MacroViewAction  {
	public CutNodeAction() {
		super();
	}

	
	public void run() {
		getPresenter().doCut();
	}
}