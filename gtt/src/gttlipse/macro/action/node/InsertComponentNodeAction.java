package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertComponentNodeAction extends MacroViewAction  {
	public InsertComponentNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertComponentNode(null);
	}
}
