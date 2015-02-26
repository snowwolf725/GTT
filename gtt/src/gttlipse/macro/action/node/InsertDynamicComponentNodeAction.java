package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;

public class InsertDynamicComponentNodeAction extends MacroViewAction {

	public InsertDynamicComponentNodeAction() {
		super();
	}
	
	@Override
	public void run() {
		getPresenter().insertDynamicComponentNode();
	}
}
