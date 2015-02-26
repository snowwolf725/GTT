package gttlipse.tabular.actions;

import gttlipse.GTTlipse;
import gttlipse.tabular.def.ActionType;

import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;


public class ActionFactory {

	private URL _baseURL = GTTlipse.getDefault().getBundle().getEntry("images/");
	
	public ActionFactory() {}
	
	public Action createAction(int type) {
		Action action = null;
		
		if (type == ActionType.SAVE) {
			action = new Synchronize();
			setupAction(action, "Save table", "The table contents will be translated into the tree", "savefile.jpg");
		}
		return action;
	}
	
	private void setupAction(Action action, String text, String toolTipText, String imgName) {
		action.setText(text);
		action.setToolTipText(toolTipText);
		try {
			URL imgURL = new URL(_baseURL, imgName);
			action.setImageDescriptor(ImageDescriptor.createFromURL(imgURL));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
