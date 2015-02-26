package gttlipse.macro.action;

public class InsertDynamicComponentEventNodeAction extends MacroViewAction {
	
	public InsertDynamicComponentEventNodeAction() {
		super();
	}
	
	@Override
	public void run() {
		getPresenter().insertDynamicComponentEventNode();
	}
}
