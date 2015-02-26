/**
 * 
 */
package gttlipse.scriptEditor.actions.node;

import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.ResourceFinder;
import gttlipse.resource.ResourceManager;
import gttlipse.scriptEditor.testScript.ProjectNode;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.actions.node
 * 
 */
public class AddSourceFolderNodeAction extends EnhancedAction {

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		if (selection.getFirstElement() instanceof ProjectNode) {
			// Test Code 那邊會 sync 過來所以不用修改 TestScript
			ProjectNode project = (ProjectNode) selection.getFirstElement();
			ResourceFinder finder = new ResourceFinder();
			IProject prj = finder.findIProject(project);
			
			ResourceManager manager = new ResourceManager();
			String folder = project.addNewSourceFolder();
			manager.addJavaSourceFolder(prj, folder);
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
