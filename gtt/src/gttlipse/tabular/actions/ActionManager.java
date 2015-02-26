package gttlipse.tabular.actions;

import gttlipse.tabular.def.ActionType;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.Separator;


public class ActionManager {

	private Map<Integer, Action> _actions = null;
	
	public ActionManager() {
		_actions = new HashMap<Integer, Action>();
	}
	
	public void makeActions(int[] types) {
		ActionFactory factory = new ActionFactory();
		
		for (int i = 0; i < types.length; i++) {
			_actions.put(types[i], factory.createAction(types[i]));
		}
	}
	
	public void addToContributionManager(IContributionManager manager, int[] types) {
		if ((manager != null) && (_actions.size() != 0)) {
			for (int i = 0; i < types.length; i++) {
				if (types[i] == ActionType.SEPARATOR) {
					// Add a separator
					manager.add(new Separator());
				}
				else {
					Action action = _actions.get(types[i]);
					// When the action is not in the map
					if (action != null) {
						manager.add(action);
					}
				}
			}
		}
	}
	
	public Action getAction(int type) {
		Action action = _actions.get(type);
		
		if (action != null) {
			return action;
		}
		return null;
	}
}
