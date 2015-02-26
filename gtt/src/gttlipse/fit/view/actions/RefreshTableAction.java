package gttlipse.fit.view.actions;

public class RefreshTableAction extends FitViewAction {
	public RefreshTableAction() {
		super();
	}
	
	public void run() {
		m_fitViewPresenter.refreshFile();
	}
}
