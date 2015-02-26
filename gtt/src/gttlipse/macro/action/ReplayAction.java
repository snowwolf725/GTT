package gttlipse.macro.action;

public class ReplayAction extends MacroViewAction {
	public ReplayAction() {
		super();
	}
	
	public void run() {
		getPresenter().replayMacroScript();
	}
}
