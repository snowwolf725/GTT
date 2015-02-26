package gttlipse.fit.Action;

import gttlipse.macro.action.MacroViewAction;

public class InsertSplitDataAsNameNodeAction extends MacroViewAction {
	public  InsertSplitDataAsNameNodeAction() {
		super();
	}
	
	public void run() {
		this.getPresenter(). insertSplitDataAsNameNode();
	}
}
