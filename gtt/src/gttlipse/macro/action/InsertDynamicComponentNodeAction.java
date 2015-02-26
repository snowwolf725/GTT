package gttlipse.macro.action;

public class InsertDynamicComponentNodeAction extends MacroViewAction {

	public InsertDynamicComponentNodeAction() {
		super();
	}
	
	@Override
	public void run() {
		getPresenter().insertDynamicComponentNode();
	}
}
