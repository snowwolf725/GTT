package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class InsertMacroEventNodeAction extends MacroViewAction  {
	public InsertMacroEventNodeAction() {
		super();
	}


	public void run() {
		this.getPresenter().insertMacroEventNode();
	}
}