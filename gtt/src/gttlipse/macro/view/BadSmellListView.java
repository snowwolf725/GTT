package gttlipse.macro.view;

import gttlipse.macro.action.MacroAction;
import gttlipse.macro.action.MacroActionManager;
import gttlipse.macro.report.CoverageCount;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;


public class BadSmellListView extends ViewPart {
	private MacroActionManager m_actionMgr;
	private MacroPresenter m_presenter;
	private TreeViewer m_viewer;
	private BadSmellListViewContentProvider m_vcp;
	private final int PATH_WIDTH = 500;
	private final int IMPORTANCE_WIDTH = 50;
	private final int TYPE_WIDTH = 300;
	
	
	public BadSmellListView() {
		m_viewer = null;
	}
	
	public void addBadSmell(BadSmellItem item) {
		m_vcp.getRoot().add(item);
		m_viewer.refresh();
	}
	
	public void clearBadSmell() {
		m_vcp.getRoot().clear();
		m_viewer.refresh();
	}
	
	public MacroPresenter getPresenter() {
		return m_presenter;
	}
	
	CoverageCount m_coverageCount = new CoverageCount();

	public TreeViewer getTreeViewer() {
		return m_viewer;
	}
	
	public void createPartControl(Composite parent) {
		m_presenter = new MacroPresenter(m_viewer);
		
		m_viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		m_vcp = new BadSmellListViewContentProvider(getViewSite());

		m_viewer.setContentProvider(m_vcp);

		BadSmellListViewLabelProvider vtlp = new BadSmellListViewLabelProvider();
		m_viewer.setLabelProvider(vtlp);
		m_viewer.setInput(getViewSite());

		m_viewer.refresh();

		m_viewer.getTree().setHeaderVisible(true);
		m_viewer.getTree().setLinesVisible(true);

		final TreeColumn column_i = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column_i.setText("Importance");
		column_i.setWidth(IMPORTANCE_WIDTH);
		column_i.addSelectionListener(new SelectionAdapter() {  
			   
			     @Override  
			     public void widgetSelected(SelectionEvent e) {  
			         int dir = m_viewer.getTree().getSortDirection();  
			         if (m_viewer.getTree().getSortColumn() == column_i) {  
			             dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;  
			         } else {  
			             dir = SWT.DOWN;  
			         }  
			         m_viewer.getTree().setSortDirection(dir);  
			         m_viewer.getTree().setSortColumn(column_i);  
			         m_viewer.refresh();  
			     }  
			   
			 });  
		
		final TreeColumn column_t = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column_t.setText("Type");
		column_t.setWidth(TYPE_WIDTH);
		column_t.addSelectionListener(new SelectionAdapter() {  
			   
		     @Override  
		     public void widgetSelected(SelectionEvent e) {  
		         int dir = m_viewer.getTree().getSortDirection();  
		         if (m_viewer.getTree().getSortColumn() == column_t) {  
		             dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;  
		         } else {  
		             dir = SWT.DOWN;  
		         }  
		         m_viewer.getTree().setSortDirection(dir);  
		         m_viewer.getTree().setSortColumn(column_t);  
		         m_viewer.refresh();  
		     }  
		   
		 });  

		final TreeColumn column_p = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column_p.setText("Path");
		column_p.setWidth(PATH_WIDTH);
		column_p.addSelectionListener(new SelectionAdapter() {  
			   
		     @Override  
		     public void widgetSelected(SelectionEvent e) {  
		         int dir = m_viewer.getTree().getSortDirection();  
		         if (m_viewer.getTree().getSortColumn() == column_p) {  
		             dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;  
		         } else {  
		             dir = SWT.DOWN;  
		         }  
		         m_viewer.getTree().setSortDirection(dir);  
		         m_viewer.getTree().setSortColumn(column_p);  
		         m_viewer.refresh();  
		     }  
		   
		 });  
				
		// init action manager
		makeActions();
		contributeToActionBars();
		hookDoubleClickAction();
		
		//sorter  
		
		m_viewer.setSorter(new ViewerSorter() {  
		     @Override  
		     public int compare(Viewer viewer, Object e1, Object e2) {  
		    	 TreeViewer treeViewer = (TreeViewer) viewer;  
		         TreeColumn sortColumn = treeViewer.getTree().getSortColumn();  
		         if (sortColumn == null) {  
		             return 0;  
		         }  
		   
		         ITableLabelProvider labelProvider = (ITableLabelProvider) treeViewer  
		                 .getLabelProvider();  
		   
		         int columnIndex = treeViewer.getTree().indexOf(sortColumn);  
		         if (columnIndex != -1) {  
		             String text1 = labelProvider.getColumnText(e1, columnIndex);  
		             String text2 = labelProvider.getColumnText(e2, columnIndex);  
		   
		             int rc = text1.compareTo(text2);  
		             if (m_viewer.getTree().getSortDirection() == SWT.DOWN) {  
		                 rc = rc * -1;
		             }
		             return rc;  
		         }  
		         return 0;  
		     }  
		 });  
	}

	private void makeActions() {
		m_actionMgr = new MacroActionManager(m_viewer, m_presenter);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(m_actionMgr.getAction(MacroAction.CLEAR_BADSMELL));
	}

	@Override
	public void setFocus() {
	}
	
	private void hookDoubleClickAction() {
		m_viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				m_actionMgr.getAction(MacroAction.LOCATE_BADSMELL).run();
			}
		});
	}
}
