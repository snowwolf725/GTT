package gttlipse.fit.Action;

import gttlipse.macro.action.MacroViewAction;

public class InsertFixNameNodeAction extends MacroViewAction {
	public InsertFixNameNodeAction() {
		super();
	}

	public void run() {
		getPresenter().insertFixNameNode();
	}
}
