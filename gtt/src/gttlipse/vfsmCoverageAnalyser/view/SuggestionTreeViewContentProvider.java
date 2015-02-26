package gttlipse.vfsmCoverageAnalyser.view;

import gtt.testscript.AbstractNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;


public class SuggestionTreeViewContentProvider implements IStructuredContentProvider, 
														  ITreeContentProvider {

	private CompositeNode _invisibleRoot;
	private IViewSite m_viewsite=null;
	
	public SuggestionTreeViewContentProvider(IViewSite viewsite, CompositeNode invisibleRoot) {
		m_viewsite = viewsite;
		_invisibleRoot = invisibleRoot;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(m_viewsite)) {
			return getChildren(_invisibleRoot);
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof BaseNode)
			return ((BaseNode) child).getParent();
		else if (child instanceof TestScriptDocument)
			return ((TestScriptDocument) child).getParent();
		else if (child instanceof AbstractNode)
			return ((AbstractNode) child).getParent();
		else
			return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof TestMethodNode)
			return ((TestMethodNode) parent).getDocuments();
		else if (parent instanceof CompositeNode)
			return ((CompositeNode) parent).getChildren();
		else if (parent instanceof TestScriptDocument)
			return ((TestScriptDocument) parent).getChildren();
		else if (parent instanceof AbstractNode)
			return ((AbstractNode) parent).getChildren();
		else
			return null;
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof TestMethodNode)
			return ((TestMethodNode) parent).hasDoc();
		else if (parent instanceof CompositeNode)
			return ((CompositeNode) parent).hasChildren();
		else if (parent instanceof TestScriptDocument)
			return ((TestScriptDocument) parent).hasChildren();
		else if (parent instanceof AbstractNode)
			return ((AbstractNode) parent).hasChildren();
		else
			return false;
	}
}
