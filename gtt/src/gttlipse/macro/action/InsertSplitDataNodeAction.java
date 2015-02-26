package gttlipse.macro.action;

public class InsertSplitDataNodeAction extends MacroViewAction {

	public InsertSplitDataNodeAction() {
		super();
	}
	
	@Override
	public void run() {
		getPresenter().insertSplitDataNode();
	}
}
