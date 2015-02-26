package gttlipse.macro.action.badsmell;

import gttlipse.macro.action.MacroViewAction;



public class DetectOuterUsageAction extends MacroViewAction  {
	public DetectOuterUsageAction() {
		super();
	}


	public void run() {
		getPresenter().detectOuterUsage();
	}
}
