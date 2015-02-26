package gttlipse.fit.Action;

import gtt.testscript.AbstractNode;
import gtt.testscript.FolderNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.actions.EnhancedAction;

import org.eclipse.jface.viewers.IStructuredSelection;


public class AddReferenceFitNodeAction extends EnhancedAction {
	public void addNode(String path) {
		AbstractNode added = null;
		IStructuredSelection sel = (IStructuredSelection) m_TreeViewer.getSelection();
		if (sel.getFirstElement() instanceof TestScriptDocument) {
			added = ((TestScriptDocument) sel.getFirstElement()).getScript();
		} else if (sel.getFirstElement() instanceof FolderNode) {
			added = (FolderNode) sel.getFirstElement();
		}

		// «Ø¥ß ReferenceMacroEventNode
		added.add(new NodeFactory().createReferenceFitNode(path));

		m_TreeViewer.setExpandedState(sel.getFirstElement(), true);
		m_TreeViewer.refresh();
	}
}
