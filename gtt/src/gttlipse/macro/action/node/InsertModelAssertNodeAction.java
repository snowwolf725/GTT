package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertModelAssertNodeAction extends MacroViewAction  {
	public InsertModelAssertNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertModelAssertNode();
	}
}