package gttlipse.scriptEditor.views;

import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.TestProject;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;


/*
 * The content provider class is responsible for providing objects to the
 * view. It can wrap existing objects in adapters or simply return objects
 * as-is. These objects may be sensitive to the current input of the view,
 * or ignore it and always show the same content (like Task List, for
 * example).
 */
public class ScriptViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {

	private IViewSite m_viewsite = null;

	ScriptViewContentProvider(IViewSite viewsite) {
		m_viewsite = viewsite;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(m_viewsite))
			return getChildren(TestProject.getInstance());
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof BaseNode)
			return ((BaseNode) child).getParent();
		if (child instanceof TestScriptDocument)
			return ((TestScriptDocument) child).getParent();
		if (child instanceof AbstractNode)
			return ((AbstractNode) child).getParent();

		return null;
	}

	public Object[] getChildren(Object node) {
		if (node instanceof TestMethodNode)
			return ((TestMethodNode) node).getDocuments();
		if (node instanceof CompositeNode)
			return ((CompositeNode) node).getChildren();
		if (node instanceof TestScriptDocument)
			return ((TestScriptDocument) node).getChildren();
		if (node instanceof AbstractNode)
			return ((AbstractNode) node).getChildren();

		return null;
	}

	public boolean hasChildren(Object node) {
		if (node instanceof TestMethodNode)
			return ((TestMethodNode) node).hasDoc();
		if (node instanceof CompositeNode)
			return ((CompositeNode) node).hasChildren();
		if (node instanceof TestScriptDocument)
			return ((TestScriptDocument) node).hasChildren();
		if (node instanceof AbstractNode)
			return ((AbstractNode) node).hasChildren();

		return false;
	}

	// /*
	// * We will set up a dummy model to initialize tree heararchy. In a real
	// * code, you will connect to a real model and expose its hierarchy.
	// */
	// private void initialize() {
	// invisibleRoot = InvisibleRootNode.getInstance();
	// // 檢查是否已 new 過
	// if (invisibleRoot.getChildren().length == 1)
	// return;
	//
	// // init node
	// ProjectNode root = new ProjectNode("(not set)");
	// invisibleRoot.addChild(root);
	// }
}