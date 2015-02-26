package gttlipse.scriptEditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;


public interface IActionFactory {
	public abstract Action getAction(TreeViewer viewer, int type);

}