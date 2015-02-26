package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertViewAssertNodeAction extends MacroViewAction  {
	public InsertViewAssertNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertViewAssertNode();
	}
}
