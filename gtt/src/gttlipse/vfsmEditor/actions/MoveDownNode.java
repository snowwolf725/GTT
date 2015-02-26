package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Element;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class MoveDownNode extends Action {

	private IVFSMPresenter m_viewer = null;

	public MoveDownNode(IVFSMPresenter viewer) {
		m_viewer = viewer;
	}

	public void run() {
		Element ele = m_viewer.getSelectionElement();

		if (ele instanceof AbstractSuperState) {
			AbstractSuperState node = (AbstractSuperState) ele;
			((AbstractSuperState) node.getParent()).moveChildrenRear(node);
		}
		m_viewer.refreshTreeView();
	}
}
