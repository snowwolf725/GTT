package gttlipse.macro.action;



public class NewFileAction extends MacroViewAction  {
	public NewFileAction() {
		super();
	}

//	public OpenNewFileAction(String arg) {
//		super(arg);
//	}
//
//	public OpenNewFileAction(String arg1, ImageDescriptor arg2) {
//		super(arg1, arg2);
//	}
//
//	public OpenNewFileAction(String arg1, int arg2) {
//		super(arg1, arg2);
//	}

	public void run() {
		getPresenter().newFile();
	}
}
