package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertMacroComponentNodeAction extends MacroViewAction  {
	public InsertMacroComponentNodeAction() {
		super();
	}


	public void run() {
		getPresenter().insertMacroComponentNode();
	}
}