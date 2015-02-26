/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.OracleNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.actions
 * 
 */
public class CollectCompInfoAction extends EnhancedAction {
	
	private int m_EventNodeCount = 0;
	
	private int m_AssertNodeCount = 0;
	
	private int m_AUTInfoNodeCount = 0;
	
	private int m_ReferenceMacroEventNodeCount = 0;
	
	private int m_OracleNodeCount = 0;
	
	public void run(){
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		m_EventNodeCount = 0;
		m_AssertNodeCount = 0;
		m_AUTInfoNodeCount = 0;
		m_ReferenceMacroEventNodeCount = 0;
		m_OracleNodeCount = 0;
		countAllScript(selection.getFirstElement());
		MessageDialog.openInformation(m_TreeViewer.getControl().getShell(),
				"Node Count", 
				"EventNode Count:\t\t" + m_EventNodeCount + "\n" +
				"AssertNode Count:\t\t" + m_AssertNodeCount + "\n" +
				"AUTInfoNode Count:\t" + m_AUTInfoNodeCount + "\n" +
				"MacroEventNode Count:\t" + m_ReferenceMacroEventNodeCount + "\n" +
				"OracleNode Count:\t\t" + m_OracleNodeCount
				);
	}
	
	private void countAllScript(Object node) {
		if (node instanceof ProjectNode || node instanceof SourceFolderNode
				|| node instanceof PackageNode || node instanceof TestCaseNode) {
			CompositeNode cmpnode = (CompositeNode) node;
			BaseNode[] childs = cmpnode.getChildren();
			if (childs == null)
				return;
			
			for (BaseNode child : childs) {
				countAllScript(child);
			}
		} else if (node instanceof TestMethodNode) {
			TestMethodNode method = (TestMethodNode) node;
			TestScriptDocument[] docs = method.getDocuments();
			if (docs == null)
				return;
			for (TestScriptDocument doc : docs) {
				countAllScript(doc.getScript());
			}
		} else if(node instanceof TestScriptDocument){
			TestScriptDocument doc = (TestScriptDocument)node;
			countAllScript(doc.getScript());
		} else if(node instanceof FolderNode){
			FolderNode folder = (FolderNode)node;
			for(AbstractNode anode:folder.getChildren()){
				if(anode instanceof LaunchNode){
					m_AUTInfoNodeCount++;
				} else if(anode instanceof EventNode){
					m_EventNodeCount++;
				} else if(anode instanceof ViewAssertNode){
					m_AssertNodeCount++;
				} else if(anode instanceof ReferenceMacroEventNode){
					m_ReferenceMacroEventNodeCount++;
				} else if(anode instanceof OracleNode){
					m_OracleNodeCount++;
				}
			}
		}
	}
}
