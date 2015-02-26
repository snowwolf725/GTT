package gttlipse.vfsmEditor.view;

import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Diagram;
import gttlipse.vfsmEditor.model.Element;
import gttlipse.vfsmEditor.model.State;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;


public class VFSMPresenter implements IVFSMPresenter {

	TreeViewer m_Viewer;
	IVFSMEditor m_Editor;
	GraphicalViewer m_GViewer;

	public VFSMPresenter(IVFSMEditor editor, TreeViewer viewer,
			GraphicalViewer gv) {
		m_Viewer = viewer;
		m_Editor = editor;
		m_GViewer = gv;

		diplayMainDiagram();
	}

	@Override
	public Shell getShell() {
		return m_Viewer.getControl().getShell();
	}

	@Override
	public Element getSelectionElement() {
		ISelection selection = m_Viewer.getSelection();
		return (Element) ((IStructuredSelection) selection).getFirstElement();
	}

	@Override
	public void refreshTreeView() {
		m_Viewer.refresh();
	}

	@Override
	public void addDeclarationChild(String name) {
		String newName = getDiagram().addDeclarationChild(name);
		diplayDiagram(newName);
	}

	@Override
	public void diplayDiagram(String name) {
		refreshGrapicalView();
		m_GViewer.setContents(getDiagram().createDiagram(name));
		m_Editor.setPartName(name);
	}

	@Override
	public void diplayMainDiagram() {
		refreshGrapicalView();
		m_GViewer.setContents(getDiagram().createDiagram());
		m_Editor.setPartName("Main");
	}

	@Override
	public IVFSMDagram getDiagram() {
		return getContentProvider().getDiagram();
	}

	@Override
	public void inheritanceFrom(AbstractSuperState ss) {
		ss.setDeclaration(true);
		getDiagram().addDeclarationChild(ss);
		diplayDiagram(ss.getName());
	}

	@Override
	public void removeDeclarationChild(AbstractSuperState ss) {
		getDiagram().removeDeclarationChild(ss);
		diplayMainDiagram();
	}

	private void removeAllListener(Element e) {
		e.removeAllPropertyChangeListener();

		if (e instanceof Diagram) {
			Diagram diagram = (Diagram) e;
			Iterator<State> iter = diagram.getMainState().getAll().iterator();
			while (iter.hasNext()) {
				removeAllListener((Element) iter.next());
			}
			return;
		}

		if (e instanceof AbstractSuperState) {
			AbstractSuperState superstate = (AbstractSuperState) e;
			Iterator<State> iter = superstate.getAll().iterator();
			while (iter.hasNext()) {
				removeAllListener((Element) iter.next());
			}
			return;
		}
	}

	private void refreshGrapicalView() {
		EditPart oldpart = m_GViewer.getContents();
		if (oldpart == null)
			return;
		Element element = (Element) oldpart.getModel();
		removeAllListener(element);
	}

	private ViewContentProvider getContentProvider() {
		return (ViewContentProvider) m_Viewer.getContentProvider();
	}


}
