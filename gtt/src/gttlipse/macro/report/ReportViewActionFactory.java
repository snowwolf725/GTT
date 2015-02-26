package gttlipse.macro.report;

import gttlipse.GTTlipse;
import gttlipse.scriptEditor.actions.IActionFactory;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;


public class ReportViewActionFactory implements IActionFactory {
	public ReportViewActionFactory() {
	}

	public Action getAction(TreeViewer viewer, int type) {
		if (type == ReportActionType.REFRESH) {
			Action action = new RefreshAction();
			setupAction(action, "Refresh", "Refresh", "refresh.gif");
			return action;
		}
		return null;
	}

	private void setupAction(Action item, String text, String tiptext,
			String imgname) {
		item.setText(text);
		item.setToolTipText(tiptext);

		URL baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");

		try {
			URL imgurl = new URL(baseurl, imgname);
			item.setImageDescriptor(ImageDescriptor.createFromURL(imgurl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
