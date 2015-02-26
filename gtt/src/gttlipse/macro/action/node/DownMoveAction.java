package gttlipse.macro.action.node;

import gttlipse.macro.action.MacroViewAction;



public class DownMoveAction extends MacroViewAction  {
	public DownMoveAction() {
		super();
	}

	public void run() {
		getPresenter().downMove();
	}
}
