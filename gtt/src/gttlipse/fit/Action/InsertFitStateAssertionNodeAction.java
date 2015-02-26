package gttlipse.fit.Action;

import gttlipse.macro.action.MacroViewAction;

public class InsertFitStateAssertionNodeAction extends MacroViewAction {
	public InsertFitStateAssertionNodeAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().insertFitStateAssertionNode();
	}
}
