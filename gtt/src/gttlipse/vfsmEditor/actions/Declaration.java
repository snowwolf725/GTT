package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.view.IVFSMPresenter;
import gttlipse.vfsmEditor.view.dialog.DeclarationDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;


public class Declaration extends Action {

	private IVFSMPresenter m_Presenter = null;

	public Declaration(IVFSMPresenter p) {
		m_Presenter = p;
	}

	public void run() {
		String ssName = "SuperState";
		DeclarationDialog dialog = new DeclarationDialog(
				m_Presenter.getShell(), "Create a new Declaration", ssName);
		dialog.open();
		if (dialog.getReturnCode() == SWT.OK) {
			m_Presenter.addDeclarationChild(dialog.getDeclarationName());
			m_Presenter.refreshTreeView();
		}
	}
}
