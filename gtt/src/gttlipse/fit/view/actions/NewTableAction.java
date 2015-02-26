package gttlipse.fit.view.actions;

public class NewTableAction extends FitViewAction {
	public NewTableAction() {
		super();
	}
	
	public void run() {
		m_fitViewPresenter.newFile();
	}
}
