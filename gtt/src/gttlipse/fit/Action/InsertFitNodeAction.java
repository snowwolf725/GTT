package gttlipse.fit.Action;

import gttlipse.macro.action.MacroViewAction;

public class InsertFitNodeAction extends MacroViewAction {
	public InsertFitNodeAction() {
		super();
	}

	public void run() {
		getPresenter().insertFitNode();
	}
}