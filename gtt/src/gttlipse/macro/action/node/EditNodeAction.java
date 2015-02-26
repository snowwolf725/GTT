package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class EditNodeAction extends MacroViewAction  {
	public EditNodeAction() {
		super();
	}


	public void run() {
		getPresenter().editSelectedNode();
	}
}
