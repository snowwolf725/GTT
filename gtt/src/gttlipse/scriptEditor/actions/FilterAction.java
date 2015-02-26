/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gttlipse.TestProject;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.def.FilterType;

/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.actions
 * 
 */
public class FilterAction extends EnhancedAction {
	public void run() {
		// ToDo
		if (this.getText().equals("Display Test Script")) {
			NodeFilter filter = new NodeFilter();
			filter.setDisplayNode(FilterType.DISPLAYTESTSCRIPT);
			this.setText("Display All File");
			this.setToolTipText("Display All File");
		} else if (this.getText().equals("Display All File")) {

			TestProject.updateScriptSync();

			this.setText("Display Test Script");
			this.setToolTipText("Display Test Script");
		}
		m_TreeViewer.refresh();
	}
}
