package gttlipse.fit.view.actions;

import gttlipse.fit.view.FitViewPresenter;

import org.eclipse.jface.action.Action;


public abstract class FitViewAction  extends Action {
	FitViewPresenter m_fitViewPresenter;
	
	public FitViewAction() {
		super();
	}
	
	public void setFitViewPresenter(FitViewPresenter presenter) {
		m_fitViewPresenter = presenter;
	}
	
	public FitViewPresenter getFitViewPresenter() {
		return m_fitViewPresenter;
	}
}
