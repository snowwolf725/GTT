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
public class AddClassNodeAction extends EnhancedAction {
	// private IResourceFinder m_finder;

	/**
	 * 
	 */
	public AddClassNodeAction() {

	}

	/**
	 * @param text
	 */
	public AddClassNodeAction(String text) {
		super(text);
	}

	/**
	 * @param text
	 * @param image
	 */
	public AddClassNodeAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	/**
	 * @param text
	 * @param style
	 */
	public AddClassNodeAction(String text, int style) {
		super(text, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		if (selection.getFirstElement() instanceof SourceFolderNode) {
			SourceFolderNode srcfolder = (SourceFolderNode) selection
					.getFirstElement();
			String filename = srcfolder.nextNewTestCaseName();
			// add test file
			ResourceManager manager = new ResourceManager();
			boolean result = manager.AddJavaClass(srcfolder, filename);
			if (result == false) {
				// 檔案已經存在
				srcfolder.addNewTestCase();
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof PackageNode) {
			PackageNode packagenode = (PackageNode) selection.getFirstElement();
			String filename = packagenode.nextNewTestCaseName();
			// add test file
			ResourceManager manager = new ResourceManager();
			boolean result = manager.AddJavaClass(packagenode, filename);
			if (result == false) {
				// 檔案已經存在
				packagenode.addNewTestCase();
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
