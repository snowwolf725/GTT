package gttlipse.macro.action;



public class SaveFileAction extends MacroViewAction  {
	public SaveFileAction() {
		super();
	}

	public void run() {
		getPresenter().saveFile();
	}
}