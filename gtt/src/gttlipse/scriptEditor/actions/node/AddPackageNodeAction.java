/**
 * 
 */
package gttlipse.scriptEditor.actions.node;

import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.ResourceManager;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.actions.Node
 * 
 */
public class AddPackageNodeAction extends EnhancedAction {

	public AddPackageNodeAction() {
	}

	public AddPackageNodeAction(String text) {
		super(text);
	}

	public AddPackageNodeAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public AddPackageNodeAction(String text, int style) {
		super(text, style);
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();

		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof SourceFolderNode) {
			// Test Code 那邊會 sync 過來所以不用修改 TestScript
			SourceFolderNode folder = (SourceFolderNode) firstElement;
			String packagename = folder.nextNewPackageName();
			ResourceManager manager = new ResourceManager();
			boolean result = manager.AddJavaPackageUnderSourceFolder(folder
					.getName(), packagename);
			if (result == false) {
				// 檔案已經存在
				folder.addNewPackageNode();
			}
			GTTlipse.makeScriptViewDirty();
		}

		if (firstElement instanceof PackageNode) {
			PackageNode node = (PackageNode) firstElement;
			String name = node.nextNewPackageName();
			ResourceManager manager = new ResourceManager();
			if (manager.AddJavaPackageUnderPackage(node, name) == false) {
				node.addNewPackage();
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
