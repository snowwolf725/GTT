package gttlipse.macro.report;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

public class ReportAction extends Action {
	private CoverageReportView m_view = null;
	
	public ReportAction() {
		super();
	}

	public ReportAction(String arg) {
		super(arg);
	}
	
	public ReportAction(String arg1, ImageDescriptor arg2) {
		super(arg1, arg2);
	}

	public ReportAction(String arg1, int arg2) {
		super(arg1, arg2);
	}

	public void setup(CoverageReportView view) {
		m_view = view;
	}
	
	public CoverageReportView getView() {
		return m_view;
	}
}
