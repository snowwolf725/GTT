package gttlipse.vfsmEditor.actions;

import gttlipse.scriptEditor.actions.ClipBoard;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.view.IVFSMDagram;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class PasteNode extends Action {

	private IVFSMPresenter m_Presenter = null;
	private ClipBoard m_clipBoard = ClipBoard.getInstance();

	public PasteNode(IVFSMPresenter p) {
		m_Presenter = p;
	}

	public void run() {

		State node = (State) m_Presenter.getSelectionElement();
		State pasteNode = m_clipBoard.cloneCopyNode();

		if (node instanceof AbstractSuperState) {
			if (node.getName() == VFSMDef.FSM_MAIN)
				pasteGraph((AbstractSuperState) node, pasteNode);
		}

		// for refresh diagram
		IVFSMDagram diagram = m_Presenter.getDiagram();
		diagram.getFSMMain().refreshDiagram();

		m_Presenter.refreshTreeView();
		m_clipBoard.clearClipBoard();
	}

	private void pasteGraph(AbstractSuperState selectNode, State pasteNode) {
		String rewName = reNameProcess(selectNode, pasteNode);
		pasteNode.setName(rewName);
		pasteNode.setDimension(m_clipBoard.getBestDimension());
		selectNode.addState(pasteNode);
	}

	private String reNameProcess(AbstractSuperState selectNode, State pasteNode) {
		AbstractSuperState chkNode = (AbstractSuperState) selectNode
				.getChildrenByName(pasteNode.getName());
		String newName = pasteNode.getName();
		if (chkNode != null) {
			AbstractSuperState chkCopyNode = (AbstractSuperState) selectNode
					.getChildrenByName(pasteNode.getName() + "(Copy)");
			if (chkCopyNode == null)
				newName += "(Copy)";
			else {
				int max = 1;
				String pasteName = pasteNode.getName();
				newName = pasteNode.getName() + "(Copy)";
				for (int i = 0; i < selectNode.size(); i++) {
					String graphName = selectNode.getAll().get(i).getName();
					if (graphName.length() <= newName.length())
						continue;

					String splitHead = graphName.substring(0, pasteName
							.length());
					if (splitHead.equals(pasteName) == false)
						continue;

					String splitTail = graphName.substring(pasteName.length(),
							pasteName.length() + 5);
					if (splitTail.equals("(Copy") == false)
						continue;

					String num = graphName.substring(pasteName.length() + 5,
							graphName.length() - 1);
					max = Math.max(max, Integer.parseInt(num));
				}
				max++;
				newName = pasteNode.getName() + "(Copy" + max + ")";
			}
		}
		return newName;
	}
}
