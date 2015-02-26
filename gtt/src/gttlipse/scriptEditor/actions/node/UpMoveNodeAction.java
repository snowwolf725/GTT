/**
 * 
 */
package gttlipse.scriptEditor.actions.node;


import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;



/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class UpMoveNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public UpMoveNodeAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public UpMoveNodeAction(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UpMoveNodeAction(String arg0, ImageDescriptor arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UpMoveNodeAction(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		if(selection.getFirstElement() instanceof CompositeNode) {
			CompositeNode node = (CompositeNode) selection.getFirstElement();
			CompositeNode parent = node.getParent();
			if (parent != null) {
				parent.moveChildFront(node);
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
			}else if (selection.getFirstElement() instanceof TestScriptDocument) {
			TestScriptDocument doc = (TestScriptDocument) selection.getFirstElement();
			TestMethodNode parent = (TestMethodNode)doc.getParent();
			if (parent != null) {
				int oldindex = parent.indexOf(doc);
				parent.moveChildrenFront(doc);
				int newindex = parent.indexOf(doc);
				if(newindex != oldindex) {
					TestFileManager manager = new TestFileManager();
					manager.swapTestScriptDocument(parent, oldindex, newindex);
				}
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
			}else if(selection.getFirstElement() instanceof AbstractNode){
			AbstractNode node = (AbstractNode)((IStructuredSelection) selection).getFirstElement();
			AbstractNode parent = node.getParent();
			int index = parent.indexOf(node);
			if(index <= 0)
				return;
			int newindex = index - 1;
			AbstractNode firstnode = parent.get(newindex);
			parent.remove(node);
			parent.remove(firstnode);
			parent.add(node, newindex);
			parent.add(firstnode, index);
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}
		m_TreeViewer.refresh();
	}

}
