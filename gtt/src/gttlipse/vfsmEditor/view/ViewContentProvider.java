package gttlipse.vfsmEditor.view;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.model.State;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class ViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {

	IVFSMDagram m_diagram = null;

	public ViewContentProvider(IVFSMDagram diagram) {
		m_diagram = diagram;
	}

	public Object[] getElements(Object parent) {
		if (parent instanceof AbstractSuperState) {
			return getChildren(parent);
		}
		if (parent instanceof Node) {
			return getChildren(parent);
		}
		return null;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof AbstractSuperState) {
			return ((AbstractSuperState) parent).getAll().toArray();
		}
		if (parent instanceof State) {
			return ((State) parent).getOutgoingConnections().toArray();
		}
		return null;
	}

	public Object getParent(Object child) {
		if (child instanceof AbstractSuperState)
			return ((AbstractSuperState) child).getParent();
		return null;
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof AbstractSuperState)
			return true;
		if (parent instanceof Node)
			return true;
		return false;
	}

	// // add by JasonWang 08/05/09 for control diagram
	// public void setDiagram(Diagram diagram) {
	// m_diagram = diagram;
	// }

	public IVFSMDagram getDiagram() {
		return m_diagram;
	}
}
