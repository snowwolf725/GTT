package gttlipse.fit.view.actions;

import gttlipse.GTTlipse;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;


public class FitViewActionFactory {
	private URL baseurl = GTTlipse.getDefault().getBundle().getEntry("images/");

	public FitViewActionFactory() {
	}
	
	public Action getAction(int type) {
		Action action = null;

		if(type == FitViewActionType.NEW_FILE) {
			action = new NewTableAction();
			setupAction(action, "New Table", "New Table", "newFile.jpg");
		} else if(type == FitViewActionType.SAVE_FILE) {
			action = new SaveTableAction();
			setupAction(action, "Save Table", "Save Table", "saveFile.jpg");
		} else if(type == FitViewActionType.TABLE_COL_ADD) {
			action = new AddTableColumnAction();
			setupAction(action, "Add Column", "Add Column", "ColAdd.ico");
		} else if(type == FitViewActionType.TABLE_COL_REMOVE) {
			action = new RemoveTableColumnAction();
			setupAction(action, "Remove Column", "Remove Column", "ColRemove.ico");
		} else if(type == FitViewActionType.TABLE_ROW_ADD) {
			action = new AddTableRowAction();
			setupAction(action, "Add Row", "Add Row", "RowAdd.ico");
		} else if(type == FitViewActionType.TABLE_ROW_REMOVE) {
			action = new RemoveTableRowAction();
			setupAction(action, "Remove Row", "Remove Row", "RowRemove.ico");
		} else if(type == FitViewActionType.REFRESH_FILE) {
			action = new RefreshTableAction();
			setupAction(action, "Refresh File", "Refresh File", "refresh.gif");
		}
		return action;
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
}
