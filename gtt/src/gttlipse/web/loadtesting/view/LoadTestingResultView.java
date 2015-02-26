package gttlipse.web.loadtesting.view;

import gttlipse.macro.action.MacroAction;
import gttlipse.macro.action.MacroActionManager;
import gttlipse.macro.report.CoverageCount;
import gttlipse.macro.view.MacroPresenter;

//import org.eclipse.jface.viewers.DoubleClickEvent;
//import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;


public class LoadTestingResultView extends ViewPart {
	private MacroActionManager m_actionMgr;
	private MacroPresenter m_presenter;
	private TreeViewer m_viewer;
	private LoadTestingResultViewContentProvider m_ltrvcp;
	private final int PATH_WIDTH = 500;
	private final int TIME_WIDTH = 300;
	private final int TYPE_WIDTH = 100;
	
	
	public LoadTestingResultView() {
		m_viewer = null;
	}
	
	public void addResult(LoadTestingResultItem item) {
		m_ltrvcp.getRoot().add(item);
		m_viewer.refresh();
	}
	
	public void clearResults() {
		m_ltrvcp.getRoot().clear();
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
		m_ltrvcp = new LoadTestingResultViewContentProvider(getViewSite());

		m_viewer.setContentProvider(m_ltrvcp);

		LoadTestingResultViewLabelProvider vtlp = new LoadTestingResultViewLabelProvider();
		m_viewer.setLabelProvider(vtlp);
		m_viewer.setInput(getViewSite());

		m_viewer.refresh();

		m_viewer.getTree().setHeaderVisible(true);
		m_viewer.getTree().setLinesVisible(true);

		final TreeColumn column_i = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column_i.setText("Type");
		column_i.setWidth(TYPE_WIDTH);
		
		final TreeColumn column_p = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column_p.setText("Path");
		column_p.setWidth(PATH_WIDTH); 		
		
		final TreeColumn column_t = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column_t.setText("Time");
		column_t.setWidth(TIME_WIDTH);
				
		// init action manager
		makeActions();
		contributeToActionBars();
//		hookDoubleClickAction();
		
	}

	private void makeActions() {
		m_actionMgr = new MacroActionManager(m_viewer, m_presenter);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(m_actionMgr.getAction(MacroAction.CLEAR_LOADTESTINGRESULT));
	}

	@Override
	public void setFocus() {
	}
	
//	private void hookDoubleClickAction() {
//		m_viewer.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				m_actionMgr.getAction(MacroAction.LOCATE_BADSMELL).run();
//			}
//		});
//	}
}
