package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;


public class InsertExistenceAssertNodeAction extends MacroViewAction  {
	public InsertExistenceAssertNodeAction() {
		super();
	}

	public void run() {
		getPresenter().insertExistenceAssertNode();
	}
}