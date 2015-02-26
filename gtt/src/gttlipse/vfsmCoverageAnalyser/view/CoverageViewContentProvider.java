package gttlipse.vfsmCoverageAnalyser.view;

import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.vfsmCoverageAnalyser.model.EventSetInvisibleRootNode;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorSite;


class CoverageViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {
	private EventSetInvisibleRootNode _invisibleRoot;
	private ProjectNode _root;
	private IEditorSite _editorSite;
	
	public CoverageViewContentProvider(IEditorSite editorSite, ProjectNode root) {
		_editorSite = editorSite;
		_invisibleRoot = null;
		_root = root;
	}
	
	public void dispose() {		
	}
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	
	}
	
	public Object[] getElements(Object inputElement) {
		if (inputElement.equals(_editorSite)) {
			if (_invisibleRoot == null)
				demo();
			return getChildren(_invisibleRoot);
		}
		return getChildren(inputElement);
	}
		
	private void demo() {
		_invisibleRoot = new EventSetInvisibleRootNode();
		_invisibleRoot.addChild(_root);
				
	}
	
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof TestMethodNode)
			return ((TestMethodNode) parentElement).getDocuments();
		else if (parentElement instanceof EventSetInvisibleRootNode)
			return ((EventSetInvisibleRootNode) parentElement).getChildren();
		else if (parentElement instanceof ProjectNode)
			return ((ProjectNode) parentElement).getMethodNodes();
		else if (parentElement instanceof TestScriptDocument)
			return ((TestScriptDocument) parentElement).getChildren();
		else if (parentElement instanceof AbstractNode)
			return ((AbstractNode) parentElement).getChildren();
		else
			return null;
	}
	
	public Object getParent(Object element) {
		if (element instanceof BaseNode)
			return ((BaseNode) element).getParent();
		else if (element instanceof TestScriptDocument)
			return ((TestScriptDocument) element).getParent();
		else if (element instanceof AbstractNode)
			return ((AbstractNode) element).getParent();
		else
			return null;
	}
	
	public boolean hasChildren(Object element) {
		if (element instanceof TestMethodNode)
			return ((TestMethodNode) element).hasDoc();
		else if (element instanceof CompositeNode)
			return ((CompositeNode) element).hasChildren();
		else if (element instanceof TestScriptDocument)
			return ((TestScriptDocument) element).hasChildren();
		else if (element instanceof AbstractNode)
			return ((AbstractNode) element).hasChildren();
		else
			return false;
	}
	
	
	
}
