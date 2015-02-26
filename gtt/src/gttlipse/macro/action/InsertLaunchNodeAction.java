package gttlipse.macro.action;


public class InsertLaunchNodeAction  extends MacroViewAction  {
	public InsertLaunchNodeAction() {
		super();
	}

//	public InsertViewAssertNodeAction(String arg) {
//		super(arg);
//	}
//
//	public InsertViewAssertNodeAction(String arg1, ImageDescriptor arg2) {
//		super(arg1, arg2);
//	}
//
//	public InsertViewAssertNodeAction(String arg1, int arg2) {
//		super(arg1, arg2);
//	}

	public void run() {
		this.getPresenter().insertLaunchNode();
	}
}
