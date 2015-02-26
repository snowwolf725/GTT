package gttlipse.vfsmEditor.actions;

import gttlipse.scriptEditor.actions.ClipBoard;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;
import gttlipse.vfsmEditor.view.IVFSMPresenter;
import gttlipse.vfsmEditor.view.dialog.InheritanceDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;


public class Inheritance extends Action {
	private IVFSMPresenter m_Presenter = null;
	private ClipBoard m_clipBoard = ClipBoard.getInstance();
	private static int treeCounter = 0;

	public Inheritance(IVFSMPresenter p) {
		m_Presenter = p;
	}

	public void run() {
		State ele = (State)m_Presenter.getSelectionElement();
		State pasteNode = null;

		if (ele instanceof Node) {
			m_clipBoard.copy(ele);
		}
		pasteNode = m_clipBoard.cloneCopyNode();

		String ssName = "SS" + treeCounter++ + " : " + pasteNode.getName();
		TrayDialog editDialog = new InheritanceDialog(m_Presenter.getShell(),
				"Create a new Inheritance", ssName);
		editDialog.open();
		if (editDialog.getReturnCode() == SWT.OK) {
			pasteNode.setName(((InheritanceDialog) editDialog)
					.getDeclarationName());
			m_Presenter.inheritanceFrom((SuperState) pasteNode);
		}
		m_Presenter.refreshTreeView();
	}
}
