package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Element;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class RemoveNode extends Action {

	private IVFSMPresenter m_Presenter = null;

	public RemoveNode(IVFSMPresenter viewer) {
		m_Presenter = viewer;
	}

	public void run() {
		Element node = m_Presenter.getSelectionElement();
		if (node instanceof AbstractSuperState) {
			m_Presenter.removeDeclarationChild((AbstractSuperState) node);
			m_Presenter.refreshTreeView();
		}
	}
}
