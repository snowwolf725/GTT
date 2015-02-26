/**
 * 
 */
package gttlipse.scriptEditor.actions.node;

import gtt.testscript.AbstractNode;
import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;

import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SKWen
 * 
 */
public class AddReferenceMacroEventNode extends EnhancedAction {

	public void addNode(String path) {
		AbstractNode added = null;
		IStructuredSelection sel = (IStructuredSelection) m_TreeViewer.getSelection();
		if (sel.getFirstElement() instanceof TestScriptDocument) {
			added = ((TestScriptDocument) sel.getFirstElement()).getScript();
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (sel.getFirstElement() instanceof FolderNode) {
			added = (FolderNode) sel.getFirstElement();
			// set dirty
			GTTlipse.makeScriptViewDirty();
		}

		// «Ø¥ß ReferenceMacroEventNode
		added.add(new NodeFactory().createReferenceMacroEventNode(path));

		m_TreeViewer.setExpandedState(sel.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
