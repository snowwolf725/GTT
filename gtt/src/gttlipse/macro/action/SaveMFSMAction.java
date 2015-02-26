package gttlipse.macro.action;



public class SaveMFSMAction  extends MacroViewAction  {
	public SaveMFSMAction() {
		super();
	}

	public void run() {
		getPresenter().saveMFSM();
	}
}