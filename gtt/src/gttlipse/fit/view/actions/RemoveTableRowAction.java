package gttlipse.fit.view.actions;

public class RemoveTableRowAction extends FitViewAction {
	public RemoveTableRowAction() {
		super();
	}
	
	public void run() {
		m_fitViewPresenter.subRow();
	}
}
