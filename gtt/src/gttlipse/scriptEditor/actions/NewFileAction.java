/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.TestProject;
import gttlipse.actions.EnhancedAction;

import org.eclipse.jface.resource.ImageDescriptor;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class NewFileAction extends EnhancedAction {

	public NewFileAction() {
		super();
	}

	public NewFileAction(String arg0) {
		super(arg0);
	}

	public NewFileAction(String arg0, ImageDescriptor arg1) {
		super(arg0, arg1);
	}

	public NewFileAction(String arg0, int arg1) {
		super(arg0, arg1);
	}

	public void run() {
		TestProject.initProject();
		m_TreeViewer.refresh();
	}

}
