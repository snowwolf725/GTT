package gttlipse.componentEditor;

import gttlipse.macro.action.MacroViewAction;
import gttlipse.macro.view.MacroPresenter;
import gttlipse.macro.view.MacroViewPart;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;


// 插入 Component 至 GTTMacroScripView 的 Action
public class InsertComponentAction extends MacroViewAction {
	private TreeViewer m_viewer;

	public InsertComponentAction() {
		super();
	}

	public InsertComponentAction(String arg) {
		super(arg);
	}

	public InsertComponentAction(String arg1, ImageDescriptor arg2) {
		super(arg1, arg2);
	}

	public InsertComponentAction(String arg1, int arg2) {
		super(arg1, arg2);
	}

	public void setViewer(TreeViewer viewer) {
		m_viewer = viewer;
	}

	public void run() {
		// 取得選取的 node
		IStructuredSelection selection = (IStructuredSelection) m_viewer
				.getSelection();
		if (!(selection.getFirstElement() instanceof ComponentTreeNode))
			return;
		ComponentTreeNode selectNode = (ComponentTreeNode) selection
				.getFirstElement();

		MacroPresenter presenter = MacroViewPart.getMacroPresenter();
		presenter.insertComponentNode(selectNode.getName());
	}
}