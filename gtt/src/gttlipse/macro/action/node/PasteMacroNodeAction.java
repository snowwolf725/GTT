package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class PasteMacroNodeAction extends MacroViewAction  {
	public PasteMacroNodeAction() {
		super();
	}


	public void run() {
		getPresenter().doPaste();
	}
}
