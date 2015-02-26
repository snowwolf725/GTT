/**
 * 
 */
package gttlipse.scriptEditor.actions.node;

import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.TestCaseNode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions.Node
 * 
 */
public class AddMethodNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public AddMethodNodeAction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public AddMethodNodeAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param image
	 */
	public AddMethodNodeAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param style
	 */
	public AddMethodNodeAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		TestCaseNode classnode = (TestCaseNode) selection.getFirstElement();
		String methodname = classnode.addNewTestMethod();
		TestFileManager manager = new TestFileManager();
		manager.addTestMethod(classnode,methodname);
		// set dirty
		GTTlipse.makeScriptViewDirty();
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
