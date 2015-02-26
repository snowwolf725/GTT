/**
 * 
 */
package gttlipse.scriptEditor.views;

import gttlipse.EclipseProject;
import gttlipse.scriptEditor.actions.ActionManager;
import gttlipse.scriptEditor.def.ActionType;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.views
 * 
 */
public class HotKeyListener extends KeyAdapter {
	private ActionManager actionmanager = null;

	HotKeyListener(ActionManager actionmanager) {
		this.actionmanager = actionmanager;
	}

	public void keyPressed(KeyEvent e) {
		if ((e.stateMask & SWT.CTRL) != 0 && (e.stateMask & SWT.ALT) != 0) {
			if (e.keyCode == 'e')
				actionmanager.getAction(ActionType.EDIT_NODE).run();
			else if (e.keyCode == 'd')
				actionmanager.getAction(ActionType.DEL_NODE).run();
			else if (e.keyCode == 's') {
				EclipseProject.saveProject();
			} else if (e.keyCode == 'r') {
				actionmanager.getAction(ActionType.SYNC).run();
			}
		} else if ((e.stateMask & SWT.CTRL) != 0) {
			if (e.keyCode == 'c')
				actionmanager.getAction(ActionType.COPY_NODE).run();
			else if (e.keyCode == 'v')
				actionmanager.getAction(ActionType.PASTE_NODE).run();
		} else if (e.character == SWT.DEL)
			actionmanager.getAction(ActionType.DEL_NODE).run();
	}
}
