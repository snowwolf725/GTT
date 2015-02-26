package gttlipse.fit.view;

import gttlipse.GTTlipse;
import gttlipse.fit.table.TableLoader;
import gttlipse.fit.view.actions.FitViewActionManager;
import gttlipse.fit.view.actions.FitViewActionType;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;

public class GTTFitView extends ViewPart {
	KTable m_table = null;
	TreeViewer m_treeViewer = null;
	FitViewPresenter m_fitPresenter = null;
	FitViewActionManager m_fitViewActionManager;
	TreeViewContentProvider m_treeViewContentProvider = null;

	public GTTFitView() {
	}

	public void createPartControl(final Composite parent) {
		final GridLayout compGridLayout = new GridLayout();
		compGridLayout.verticalSpacing = 7;
		parent.setLayout(compGridLayout);
		parent.setBackground(SWTResourceManager.getColor(255, 255, 255));

		m_treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
		m_treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		m_treeViewContentProvider = new TreeViewContentProvider(getViewSite());
		m_treeViewer.setContentProvider(m_treeViewContentProvider);
		m_treeViewer.setInput(getViewSite());
		m_treeViewer.refresh();

		final Group tableEditorGroup = new Group(parent, SWT.NONE);
		tableEditorGroup.setBackground(SWTResourceManager.getColor(172, 213, 206));
		final GridData tableEditorGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableEditorGridData.heightHint = 154;
		tableEditorGridData.widthHint = 244;
		tableEditorGroup.setLayout(new GridLayout());
		tableEditorGroup.setLayoutData(tableEditorGridData);
		tableEditorGroup.setText("Table Edit");

		m_table = new KTable(tableEditorGroup, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWTX.EDIT_ON_KEY);
		m_table.setBackground(SWTResourceManager.getColor(255, 255, 255));
		m_table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		m_fitPresenter = new FitViewPresenter(m_treeViewer, m_table, m_treeViewContentProvider);
		initActions();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	public FitViewPresenter getFitPresenter(){
		return m_fitPresenter;
	}

	public TreeViewer getTreeViewer() {
		return m_treeViewer;
	}
	
//	public static GTTTestScriptView getScriptView() {
//		try {
//			if(GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(GTTFitViewDefinition.ScriptViewId) != null)
//				return (GTTTestScriptView) GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(GTTFitViewDefinition.ScriptViewId);
//		} catch (PartInitException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public static GTTFitView getFitView() {
		try {
			if(GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(GTTFitViewDefinition.FitViewId) != null)
				return (GTTFitView) GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(GTTFitViewDefinition.FitViewId);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void hookDoubleClickAction() {
		m_treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				m_treeViewer.refresh();
				IStructuredSelection selection = (IStructuredSelection) m_treeViewer.getSelection();
				if (selection.getFirstElement() instanceof IFile) {
					((IFile)selection.getFirstElement()).getName().lastIndexOf(".");
					String fileName = ((IFile)selection.getFirstElement()).getName();
					String subtitle = fileName.substring(fileName.lastIndexOf("."), fileName.length());
					m_fitPresenter.setFitTable(TableLoader.read(m_fitPresenter.getProjectRoot() + ((IFile)selection.getFirstElement()).getFullPath().toString(), GTTFitViewDefinition.SubtitleToType.get(subtitle)));
					m_fitPresenter.updateView();
				}
			}
		});
	}

	private void initActions() {
		// init macro action manager
		m_fitViewActionManager = new FitViewActionManager(this);
		m_fitViewActionManager.initAction();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(m_fitViewActionManager.getAction(FitViewActionType.NEW_FILE));
		manager.add(m_fitViewActionManager.getAction(FitViewActionType.SAVE_FILE));
		manager.add(m_fitViewActionManager.getAction(FitViewActionType.REFRESH_FILE));
		manager.add(new Separator());
		manager.add(m_fitViewActionManager.getAction(FitViewActionType.TABLE_COL_ADD));
		manager.add(m_fitViewActionManager.getAction(FitViewActionType.TABLE_COL_REMOVE));
		manager.add(m_fitViewActionManager.getAction(FitViewActionType.TABLE_ROW_ADD));
		manager.add(m_fitViewActionManager.getAction(FitViewActionType.TABLE_ROW_REMOVE));
	}
	
	@Override
	public void setFocus() {
	}
}
