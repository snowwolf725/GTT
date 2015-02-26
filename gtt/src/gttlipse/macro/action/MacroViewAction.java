package gttlipse.macro.action;

import gttlipse.macro.view.MacroPresenter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;



public class MacroViewAction extends Action {
	private MacroPresenter m_presenter = null;

	public MacroViewAction() {
		super();
	}

	public MacroViewAction(String arg) {
		super(arg);
	}

	public MacroViewAction(String arg1, ImageDescriptor arg2) {
		super(arg1, arg2);
	}

	public MacroViewAction(String arg1, int arg2) {
		super(arg1, arg2);
	}

	public void setPresenter(MacroPresenter p) {
		m_presenter = p;
	}

	public MacroPresenter getPresenter() {
		return m_presenter;
	}
}
