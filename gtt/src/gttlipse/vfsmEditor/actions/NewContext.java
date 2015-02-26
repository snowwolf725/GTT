package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.view.IVFSMDagram;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class NewContext extends Action {

	private IVFSMPresenter m_presenter = null;


	public NewContext(IVFSMPresenter presenter) {
		m_presenter = presenter;
	}

	public void run() {
		IVFSMDagram diagram = m_presenter.getDiagram();
		diagram.getFSMMain().getMainState().clear();
		diagram.getFSMMain().refreshDiagram();
		m_presenter.refreshTreeView();
	}
}
