package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class DeleteNodeAction extends MacroViewAction  {
	public DeleteNodeAction() {
		super();
	}


	public void run() {
		getPresenter().deleteMacroNode();
	}
}