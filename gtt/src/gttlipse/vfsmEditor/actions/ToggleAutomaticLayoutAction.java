package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.view.IVFSMPresenter;

import org.eclipse.jface.action.Action;


public class ToggleAutomaticLayoutAction extends Action {
	IVFSMPresenter m_Presenter;
	public ToggleAutomaticLayoutAction(IVFSMPresenter p) {
		super("Automatic Layout", Action.AS_CHECK_BOX);
		setText("Automatic Layout");
		setId(VFSMDef.ACTION_AUTOMATIC_LAYOUT);

		m_Presenter = p;
	}

	public void run() {
		setChecked(!isChecked());
		System.out.println("ToggleAutomaticLayoutAction");
		
		m_Presenter.diplayMainDiagram();
	}
}
