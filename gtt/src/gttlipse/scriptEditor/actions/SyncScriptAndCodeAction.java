/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.GTTlipse;
import gttlipse.TestProject;
import gttlipse.actions.EnhancedAction;

import org.eclipse.jface.resource.ImageDescriptor;


/**
 * @author ¤ýºaÄQ
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class SyncScriptAndCodeAction extends EnhancedAction {

	public SyncScriptAndCodeAction() {
	}

	public SyncScriptAndCodeAction(String text) {
		super(text);
	}

	public SyncScriptAndCodeAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public SyncScriptAndCodeAction(String text, int style) {
		super(text, style);
	}

	public void run() {
		TestProject.updateScriptSync();

		GTTlipse.makeScriptViewDirty();

		m_TreeViewer.refresh();
	}
}
