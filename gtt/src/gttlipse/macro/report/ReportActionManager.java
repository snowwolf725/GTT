package gttlipse.macro.report;

import gttlipse.scriptEditor.actions.IActionFactory;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;


public class ReportActionManager {
	Map<Integer, Action> actions = new HashMap<Integer, Action>();
	private CoverageReportView m_view;

	public ReportActionManager(CoverageReportView view) {
		m_view = view;
	}

	public void initAction() {
		IActionFactory factory = new ReportViewActionFactory();

		ReportAction act = (ReportAction) factory.getAction(m_view
				.getTreeViewer(), ReportActionType.REFRESH);
		act.setup(m_view);
		
		actions.put(ReportActionType.REFRESH, act);
	}

	public Action getAction(int type) {
		return actions.get(type);
	}
}
