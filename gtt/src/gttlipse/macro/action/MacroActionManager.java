package gttlipse.macro.action;

import gttlipse.macro.view.MacroPresenter;
import gttlipse.scriptEditor.actions.IActionFactory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;


public class MacroActionManager {
	Map<Integer, Action> m_actionMap;

	public MacroActionManager(TreeViewer treeViewer, MacroPresenter p) {
		m_actionMap = new HashMap<Integer, Action>();

		IActionFactory factory = new MacroViewActionFactory();
		for (int i = 0; i < MacroAction.actionList.length; i++) {
			Action action = factory.getAction(treeViewer,
					MacroAction.actionList[i]);
			if (action == null)
				continue;
			((MacroViewAction) action).setPresenter(p);
			m_actionMap.put(MacroAction.actionList[i], action);
		}
	}

	public Action getAction(int type) {
		return m_actionMap.get(type);
	}
}
