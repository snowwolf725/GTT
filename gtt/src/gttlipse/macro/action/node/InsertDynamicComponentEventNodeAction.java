package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;

public class InsertDynamicComponentEventNodeAction extends MacroViewAction {
	
	public InsertDynamicComponentEventNodeAction() {
		super();
	}
	
	@Override
	public void run() {
		getPresenter().insertDynamicComponentEventNode();
	}
}
