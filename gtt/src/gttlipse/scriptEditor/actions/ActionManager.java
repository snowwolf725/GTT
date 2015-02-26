/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.scriptEditor.def.ActionType;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;


/**
 * @author SnowWolf725
 * 
 * created first in project GTTlipse.scriptEditor.actions
 * 
 */
public class ActionManager {
	Map<Integer,Action> actions = new HashMap<Integer,Action>();
	
	protected TreeViewer m_viewer;
	
	public ActionManager(TreeViewer viewer) {
		m_viewer = viewer;
	}

	public void initActions(int [] list) {
		IActionFactory action = new ActionFactory();
		for(int i=0;i<list.length;i++) {
			actions.put(list[i],action.getAction(m_viewer, list[i]));
		}
	}
	
	public void addToContributionManager(IContributionManager manager,int [] list) {
		if(manager == null) {
			System.out.println("ContributionManager is NULL");
			return;
		} else if(actions == null) {
			System.out.println("actions is NULL");
			return;
		}
		for(int i=0;i<list.length;i++) {
			if(list[i] == ActionType.SEPARATOR)
				manager.add(new Separator());
			else{
				if(actions.get(list[i]) == null) {
					System.out.println("action is null,actiontype:"+list[i]);
					continue;
				}
				else 
				manager.add(actions.get(list[i]));
			}
		}
	}
	
	public Action getAction(int type) {
		return actions.get(type);
	}
}
