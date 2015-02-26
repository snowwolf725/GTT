package gttlipse.vfsmEditor.actions;

import gttlipse.scriptEditor.actions.ClipBoard;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class CopyNode extends Action {

	private IVFSMPresenter m_Presenter = null;
	private ClipBoard m_clipBoard = ClipBoard.getInstance();

	public CopyNode(IVFSMPresenter p) {
		m_Presenter = p;
	}

	public void run() {
		State node = (State)m_Presenter.getSelectionElement();

		if (node instanceof AbstractSuperState
				&& node.getName() != VFSMDef.FSM_MAIN) {
			m_clipBoard.copy(node);
		}
	}
}
