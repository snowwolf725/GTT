package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertIncludeNodeAction extends MacroViewAction  {
	public InsertIncludeNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertIncludeNode();
	}
}