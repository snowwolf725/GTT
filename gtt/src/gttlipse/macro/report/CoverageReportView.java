package gttlipse.macro.report;

import gtt.macro.MacroDocument;
import gttlipse.EclipseProject;
import gttlipse.macro.view.ViewContentProvider;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;


public class CoverageReportView extends ViewPart {
	private MacroDocument m_doc;
	private TreeViewer m_viewer;
	private ReportActionManager m_actionManager;
	private ViewContentProvider m_vcp;

	CoverageCount m_coverageCount = new CoverageCount();

	public TreeViewer getTreeViewer() {
		return m_viewer;
	}

	public CoverageReportView() {
		m_viewer = null;
		m_doc = null;
	}

	private void initDocument() {
		IProject prj = EclipseProject.getEclipseProject();
		if (prj != null) {
			m_doc = MacroDocument.createFromFile(prj.getLocation()
					+ "\\GTTlipse.rep");
		} else {
			m_doc = MacroDocument.create();
		}
	}

	public void refreshCoverageCount() {
		m_coverageCount.doRefreshCoverage(m_doc.getMacroScript());

	}

	public void doRefresh() {
		initDocument();
		m_vcp.initialize(m_doc.getMacroScript());
		m_viewer.refresh();
		refreshCoverageCount();
	}

	public void createPartControl(Composite parent) {
		initDocument();

		m_viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		m_vcp = new ViewContentProvider(getViewSite());

		m_viewer.setContentProvider(m_vcp);

		CoverageReportLabelProvider vtlp = new CoverageReportLabelProvider();
		m_viewer.setLabelProvider(vtlp);
		m_viewer.setInput(getViewSite());

		m_vcp.initialize(m_doc.getMacroScript());
		m_viewer.refresh();

		m_viewer.getTree().setHeaderVisible(true);
		m_viewer.getTree().setLinesVisible(true);

		TreeColumn column = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column.setText("Component Name");
		column.setWidth(300);

		column = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column.setText("Primitive Event Coverage");
		column.setWidth(165);

		column = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column.setText("Primitive Component Coverage");
		column.setWidth(165);

		column = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column.setText("Macro Event Coverage");
		column.setWidth(165);

		column = new TreeColumn(m_viewer.getTree(), SWT.LEFT);
		column.setText("Macro Component Coverage");
		column.setWidth(165);

		// init action manager
		m_actionManager = new ReportActionManager(this);

		makeActions();
		contributeToActionBars();

		refreshCoverageCount();
	}

	private void makeActions() {
		m_actionManager.initAction();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();

		bars.getToolBarManager().add(
				m_actionManager.getAction(ReportActionType.REFRESH));
	}

	@Override
	public void setFocus() {
	}

}
