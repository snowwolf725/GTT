package gttlipse.macro.action;



public class DetectOuterUsageAction extends MacroViewAction  {
	public DetectOuterUsageAction() {
		super();
	}


	public void run() {
		getPresenter().detectOuterUsage();
	}
}
