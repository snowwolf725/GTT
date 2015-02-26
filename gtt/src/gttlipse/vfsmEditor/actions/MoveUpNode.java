package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Element;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class MoveUpNode extends Action {

	private IVFSMPresenter m_Presenter = null;

	public MoveUpNode(IVFSMPresenter p) {
		m_Presenter = p;
	}

	public void run() {
		Element ele = m_Presenter.getSelectionElement();

		if (ele instanceof AbstractSuperState) {
			AbstractSuperState node = (AbstractSuperState) ele;
			((AbstractSuperState) node.getParent()).moveChildrenFront(node);
		}
		m_Presenter.refreshTreeView();
	}
}
