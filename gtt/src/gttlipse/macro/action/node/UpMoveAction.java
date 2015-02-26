package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class UpMoveAction extends MacroViewAction  {
	public UpMoveAction() {
		super();
	}


	public void run() {
		getPresenter().upMove();
	}
}
