package gttlipse.componentEditor;

import gttlipse.GTTlipse;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;


public class ComponentView extends ViewPart {
	private URL baseurl;
	private TreeViewer m_viewer;
	private InsertComponentAction m_insertComponent;

	public ComponentView() {
	}

	public void createPartControl(Composite parent) {
		baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");

		// init tree viewer
		m_viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		ViewContentProvider vcp = new ViewContentProvider(getViewSite());

		m_viewer.setContentProvider(vcp);
//		m_viewer.setLabelProvider(new ViewLabelProvider());
		m_viewer.setInput(getViewSite());
		m_viewer.refresh();

		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(m_insertComponent);
	}

	private void setupAction(Action item, String text, String tiptext, String imgname) {
		item.setText(text);
		item.setToolTipText(tiptext);
		try {
			URL imgurl = new URL(baseurl, imgname);
			item.setImageDescriptor(ImageDescriptor.createFromURL(imgurl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void makeActions() {
		m_insertComponent = new InsertComponentAction();
		m_insertComponent.setViewer(m_viewer);
		setupAction(m_insertComponent, "Insert a primitive component to Macro", "Insert a primitive component to Macro", "defToMacro.gif");
	}

	private void hookDoubleClickAction() {
	}

	public void setFocus() {
		m_viewer.getControl().setFocus();
	}
}
