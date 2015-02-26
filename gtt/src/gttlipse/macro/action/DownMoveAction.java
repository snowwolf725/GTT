package gttlipse.macro.action;



public class DownMoveAction extends MacroViewAction  {
	public DownMoveAction() {
		super();
	}

	public void run() {
		getPresenter().downMove();
	}
}
