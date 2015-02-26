/**
 * 
 */
package gttlipse.scriptEditor.actions;


import gtt.testscript.TestScriptDocument;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class GotoCodeAction extends EnhancedAction {
	
//	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		if(selection.getFirstElement() instanceof PackageNode) {
			
		}
		else if(selection.getFirstElement() instanceof TestCaseNode) {
			TestCaseNode classnode = (TestCaseNode)selection.getFirstElement();
			GotoCode gotocode = new GotoCode();
			gotocode.GotoTestFile(classnode);
		}
		else if(selection.getFirstElement() instanceof TestMethodNode) {
			TestMethodNode methodnode = (TestMethodNode) selection.getFirstElement();
			GotoCode gotocode = new GotoCode();
			gotocode.GotoTestMethod(methodnode);
		}
		else if(selection.getFirstElement() instanceof TestScriptDocument) {
			TestScriptDocument doc = null;
			doc = (TestScriptDocument)selection.getFirstElement();
			GotoCode gotocode = new GotoCode();
			gotocode.GotoTestScriptDoc(doc);
		}
	}
}
