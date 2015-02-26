package gttlipse.fit.view.actions;

import gttlipse.fit.view.GTTFitView;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;


public class FitViewActionManager {
	Map<Integer, Action> actionMap = new HashMap<Integer, Action>();
	GTTFitView m_view;

	public FitViewActionManager(GTTFitView view) {
		m_view = view;
	}

	public void initAction() {
		FitViewActionFactory fitViewActionFactory = new FitViewActionFactory();
		for (int i = 0; i < FitViewActionType.actionList.length; i++) {
			FitViewAction action = (FitViewAction) fitViewActionFactory.getAction(FitViewActionType.actionList[i]);
			action.setFitViewPresenter(m_view.getFitPresenter());
			actionMap.put(FitViewActionType.actionList[i], action);
		}
	}

	public Action getAction(int type) {
		return actionMap.get(type);
	}
}
