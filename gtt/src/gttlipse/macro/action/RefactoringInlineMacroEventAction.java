package gttlipse.macro.action;

import org.eclipse.jface.resource.ImageDescriptor;


public class RefactoringInlineMacroEventAction extends MacroViewAction {

	public RefactoringInlineMacroEventAction() {
		// TODO Auto-generated constructor stub
	}

	public RefactoringInlineMacroEventAction(String arg) {
		super(arg);
		// TODO Auto-generated constructor stub
	}

	public RefactoringInlineMacroEventAction(String arg1, ImageDescriptor arg2) {
		super(arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public RefactoringInlineMacroEventAction(String arg1, int arg2) {
		super(arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		this.getPresenter().inlineMacroEvent();
	}
}
