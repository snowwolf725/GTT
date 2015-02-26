package gttlipse.fit.Action;

import gttlipse.macro.action.MacroViewAction;

public class InsertNoEventTriggerdeAction extends MacroViewAction {
	public InsertNoEventTriggerdeAction() {
		super();
	}
	
	public void run() {
		this.getPresenter().insertEventTriggerNode();
	}
}
