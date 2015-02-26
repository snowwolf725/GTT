/**
 * 
 */
package gttlipse.scriptEditor.actions.node;

import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;

import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.actions.node
 * 
 */
public class AddOracleNodeAction extends EnhancedAction {

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		if (selection.getFirstElement() instanceof FolderNode) {
			FolderNode folderNode = (FolderNode) selection.getFirstElement();
			NodeFactory factory = new NodeFactory();
			folderNode.add(factory.createOracleNode());
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof TestScriptDocument) {
			TestScriptDocument doc = (TestScriptDocument) selection
					.getFirstElement();
			NodeFactory factory = new NodeFactory();
			doc.getScript().add(factory.createOracleNode());
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}
		m_TreeViewer.setExpandedState(selection.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
