package gttlipse.fit.view.actions;

public class RemoveTableColumnAction extends FitViewAction {
	public RemoveTableColumnAction() {
		super();
	}
	
	public void run() {
		m_fitViewPresenter.subColumn();
	}
}
