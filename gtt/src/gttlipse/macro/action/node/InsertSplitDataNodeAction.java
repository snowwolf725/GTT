package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;

public class InsertSplitDataNodeAction extends MacroViewAction {

	public InsertSplitDataNodeAction() {
		super();
	}
	
	@Override
	public void run() {
		getPresenter().insertSplitDataNode();
	}
}
