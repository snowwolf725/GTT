package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertMacroEventCallAction extends MacroViewAction  {
	public InsertMacroEventCallAction() {
		super();
	}


	public void run() {
		getPresenter().insertMacroEventCallNode();
	}
}