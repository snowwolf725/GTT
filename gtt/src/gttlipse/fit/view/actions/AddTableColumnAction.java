package gttlipse.fit.view.actions;

public class AddTableColumnAction extends FitViewAction {
	public AddTableColumnAction() {
		super();
	}
	
	public void run() {
		m_fitViewPresenter.addColumn();
	}
}
