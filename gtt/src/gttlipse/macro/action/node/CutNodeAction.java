package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class CutNodeAction extends MacroViewAction  {
	public CutNodeAction() {
		super();
	}

	
	public void run() {
		getPresenter().doCut();
	}
}