package gttlipse.fit.Action;

import gttlipse.macro.action.MacroViewAction;

public class InsertGenerateOrderNameNodeAction extends MacroViewAction {
	public InsertGenerateOrderNameNodeAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().insertGenerateOrderNameNode();
	}
}
