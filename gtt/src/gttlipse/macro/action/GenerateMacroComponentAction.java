package gttlipse.macro.action;



public class GenerateMacroComponentAction extends MacroViewAction  {
	public GenerateMacroComponentAction() {
		super();
	}


	public void run() {
		getPresenter().generateMacroComponent();
	}
}
