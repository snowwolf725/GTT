/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.EclipseProject;
import gttlipse.actions.EnhancedAction;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class SaveFileAction extends EnhancedAction {
	public SaveFileAction() {
		super();
	}

	public void run() {
		EclipseProject.saveProject();
		m_TreeViewer.refresh();
	}

}
