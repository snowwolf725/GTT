package gttlipse.macro.action.refactoring;

import gttlipse.macro.action.MacroViewAction;

import org.eclipse.jface.resource.ImageDescriptor;


public class RefactoringExtractMacroComponentAction extends MacroViewAction {

	public RefactoringExtractMacroComponentAction() {
		// TODO Auto-generated constructor stub
	}

	public RefactoringExtractMacroComponentAction(String arg) {
		super(arg);
		// TODO Auto-generated constructor stub
	}

	public RefactoringExtractMacroComponentAction(String arg1, ImageDescriptor arg2) {
		super(arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public RefactoringExtractMacroComponentAction(String arg1, int arg2) {
		super(arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		this.getPresenter().extractMacroComponent();
	}
}
