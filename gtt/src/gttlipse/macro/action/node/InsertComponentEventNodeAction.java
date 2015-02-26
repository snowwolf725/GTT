package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertComponentEventNodeAction extends MacroViewAction  {
	public InsertComponentEventNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertComponentEventNode();
	}
}