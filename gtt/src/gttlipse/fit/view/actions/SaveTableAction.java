package gttlipse.fit.view.actions;

public class SaveTableAction extends FitViewAction {
	public SaveTableAction() {
		super();
	}
	
	public void run() {
		m_fitViewPresenter.updateModel();
		m_fitViewPresenter.saveFile();
	}
}
