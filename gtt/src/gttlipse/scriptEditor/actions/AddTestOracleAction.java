/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gtt.testscript.FolderNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.dialog.AddOracleNodeToScriptDialog;
import gttlipse.scriptEditor.testScript.BaseNode;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;


/**
 * @author ¤ýºaÄQ
 * 
 * created first in project GTTlipse.scriptEditor.actions
 * 
 */
public class AddTestOracleAction extends EnhancedAction {

	public void run(){
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj instanceof TestScriptDocument || obj instanceof FolderNode) {
			Shell shell = gttlipse.GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
			FolderNode folder = (FolderNode)((TestScriptDocument)obj).getScript();
			AddOracleNodeToScriptDialog dialog = new AddOracleNodeToScriptDialog(shell,folder);
			dialog.open();
		} else if (obj instanceof FolderNode) {
			Shell shell = gttlipse.GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
			AddOracleNodeToScriptDialog dialog = new AddOracleNodeToScriptDialog(shell,(FolderNode)obj);
			dialog.open();
		}  else if (obj instanceof BaseNode) {
			Shell shell = gttlipse.GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
			AddOracleNodeToScriptDialog dialog = new AddOracleNodeToScriptDialog(shell,(BaseNode)obj);
			dialog.open();
		}		
		
		m_TreeViewer.refresh();
	}
}
