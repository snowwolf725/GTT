package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class CopyNodeAction extends MacroViewAction  {
	public CopyNodeAction() {
		super();
	}

	public void run() {
		getPresenter().doCopy();
	}
}