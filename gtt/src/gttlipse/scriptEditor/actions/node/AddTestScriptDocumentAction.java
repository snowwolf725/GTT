/**
 * 
 */
package gttlipse.scriptEditor.actions.node;


import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions.Node
 * 
 */
public class AddTestScriptDocumentAction extends EnhancedAction {

	/**
	 * 
	 */
	public AddTestScriptDocumentAction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public AddTestScriptDocumentAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param image
	 */
	public AddTestScriptDocumentAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param style
	 */
	public AddTestScriptDocumentAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		TestMethodNode testmethod = (TestMethodNode) selection.getFirstElement();
		TestScriptDocument doc = testmethod.addInteractionSequence();
		TestFileManager manager = new TestFileManager();
		manager.addScriptDocument(doc,true);
		// set dirty
		GTTlipse.makeScriptViewDirty();
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
