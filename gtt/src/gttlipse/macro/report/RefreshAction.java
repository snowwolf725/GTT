package gttlipse.macro.report;

import org.eclipse.jface.resource.ImageDescriptor;

public class RefreshAction extends ReportAction {
	public RefreshAction() {
		super();
	}

	public RefreshAction(String arg) {
		super(arg);
	}
	
	public RefreshAction(String arg1, ImageDescriptor arg2) {
		super(arg1, arg2);
	}

	public RefreshAction(String arg1, int arg2) {
		super(arg1, arg2);
	}
	
	public void run() {
		getView().doRefresh();
	}
}
