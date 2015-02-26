package gttlipse.macro.action;



public class OpenFileAction extends MacroViewAction  {
	public OpenFileAction() {
		super();
	}

	public void run() {
		getPresenter().openFile();
	}
}