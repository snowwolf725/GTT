package gttlipse.macro.action;



public class PasteMacroNodeAction extends MacroViewAction  {
	public PasteMacroNodeAction() {
		super();
	}


	public void run() {
		getPresenter().doPaste();
	}
}
