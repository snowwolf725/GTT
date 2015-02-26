package gttlipse.fit.view.actions;

public class AddTableRowAction extends FitViewAction {
	public AddTableRowAction() {
		super();
	}
	
	public void run() {
		m_fitViewPresenter.addRow();
	}
}
