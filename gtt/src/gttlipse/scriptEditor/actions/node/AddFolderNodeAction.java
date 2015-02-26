/**
 * 
 */
package gttlipse.scriptEditor.actions.node;


import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions.Node
 * 
 */
public class AddFolderNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public AddFolderNodeAction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public AddFolderNodeAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param image
	 */
	public AddFolderNodeAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param style
	 */
	public AddFolderNodeAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		if (selection.getFirstElement() instanceof FolderNode) {
			FolderNode folderNode = (FolderNode) selection.getFirstElement();
			NodeFactory factory = new NodeFactory();
			folderNode.add(factory.createFolderNode("FolderNode"));
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof TestScriptDocument) {
			TestScriptDocument doc = (TestScriptDocument) selection.getFirstElement();
			NodeFactory factory = new NodeFactory();
			doc.getScript().add(factory.createFolderNode("FolderNode"));
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
