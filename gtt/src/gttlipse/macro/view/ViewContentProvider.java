package gttlipse.macro.view;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.InvisibleRootNode;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;

public class ViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {
	private InvisibleRootNode invisibleRoot = new InvisibleRootNode();
	private IViewSite m_viewSite = null;

	public ViewContentProvider(IViewSite viewsite) {
		m_viewSite = viewsite;
	}

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
		if (parent.equals(m_viewSite))
			return getChildren(invisibleRoot);
		return getChildren(parent);
	}

	@Override
	public Object getParent(Object child) {
		if (child instanceof InvisibleRootNode)
			return null;

		return ((AbstractMacroNode) child).getParent();
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof AbstractMacroNode)
			return ((AbstractMacroNode) parent).getChildren();

		return new Object[0];
	}

	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof AbstractMacroNode)
			return ((AbstractMacroNode) parent).hasChildren();
		return false;
	}


	public void initialize(AbstractMacroNode root) {
		invisibleRoot.removeAll();	
		invisibleRoot.add(root);
	}
}
