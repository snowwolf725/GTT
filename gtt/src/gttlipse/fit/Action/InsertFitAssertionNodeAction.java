package gttlipse.fit.Action;

import gttlipse.macro.action.MacroViewAction;

public class InsertFitAssertionNodeAction extends MacroViewAction {
	public InsertFitAssertionNodeAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().insertFitAssertionNode();
	}
}
