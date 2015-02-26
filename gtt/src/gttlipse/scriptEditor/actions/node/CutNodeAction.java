/**
 * 
 */
package gttlipse.scriptEditor.actions.node;


import gtt.testscript.AbstractNode;
import gtt.testscript.FolderNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.ResourceManager;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.ScrapBook;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class CutNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public CutNodeAction() {
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();
		Object [] objs = new Object[selection.size()];
		Object [] source = selection.toArray();
		for(int i=0;i<objs.length;i++){
			if(source[i] instanceof BaseNode){
				BaseNode node = (BaseNode)source[i];
				objs[i] = node.clone();
			}else if(source[i] instanceof TestScriptDocument){
				TestScriptDocument doc = (TestScriptDocument)source[i];
				objs[i] = doc.clone();
			}else if(source[i] instanceof AbstractNode){
				AbstractNode node = (AbstractNode)source[i];
				objs[i] = node.clone();
			}
			delNode(source[i]);
		}
		ScrapBook.copy(objs);
		// set dirty
		GTTlipse.makeScriptViewDirty();
		m_TreeViewer.refresh();
	}
	
	public void delNode(Object anode) {
		if (anode instanceof ProjectNode)
			MessageDialog.openInformation(m_TreeViewer.getControl().getShell(),
					"GTT View", "You can't delete TestRoot Node.");
		else if (anode instanceof PackageNode) {
			PackageNode packagenode = (PackageNode) anode;
			if(packagenode.getParent() instanceof SourceFolderNode){
				SourceFolderNode parent = (SourceFolderNode) packagenode.getParent();
				ResourceManager manager = new ResourceManager();
				boolean result = manager.DelJavaPackage(packagenode);
				if(result == false) {
					//檔案已經不存在
					parent.removeChild((CompositeNode)anode);
				}
			} else if(packagenode.getParent() instanceof PackageNode) {
				PackageNode parent = (PackageNode) packagenode.getParent();
				ResourceManager manager = new ResourceManager();
				boolean result = manager.DelJavaPackage(packagenode);
				if(result == false) {
					//檔案已經不存在
					parent.removeChild((CompositeNode)anode);
				}
			}
		} else if (anode instanceof TestCaseNode) {
			TestCaseNode classnode = (TestCaseNode) anode;
			if(classnode.getParent() instanceof SourceFolderNode){
				SourceFolderNode parent = (SourceFolderNode) classnode.getParent();
				//del test file
				ResourceManager manager = new ResourceManager();
				manager.DelJavaClass(classnode);
				parent.removeChild((CompositeNode)anode);
			}
			else if(classnode.getParent() instanceof PackageNode) {
				PackageNode parent = (PackageNode) classnode.getParent();
				//del test file
				ResourceManager manager = new ResourceManager();
				manager.DelJavaClass(classnode);
				parent.removeChild((CompositeNode)anode);
			}
		} else if (anode instanceof TestMethodNode) {
			TestMethodNode testmethod = (TestMethodNode) anode;
			TestCaseNode classnode = (TestCaseNode) testmethod.getParent();
			classnode.removeChild(testmethod);
			String methodname = testmethod.getName();
			//	del Test Method
			TestFileManager manager = new TestFileManager();
			manager.delTestMethod(classnode, methodname);
		} else if (anode instanceof TestScriptDocument) {
			TestScriptDocument doc = (TestScriptDocument) anode;
			
			TestFileManager manager = new TestFileManager();
			manager.deleteScriptDocument(doc);
			
			FolderNode folder=(FolderNode)doc.getScript();
			folder.removeAll();
			TestMethodNode node = (TestMethodNode)doc.getParent();
			node.removeDoc(doc);
		} else if (anode instanceof AbstractNode) {
			if(anode instanceof FolderNode) {
				FolderNode folder=(FolderNode)anode;
				FolderNode parentFolder=(FolderNode)folder.getParent();
				folder.removeAll();
				parentFolder.remove(folder);
			}
			else {
				AbstractNode node=(AbstractNode)anode;
				FolderNode folder=(FolderNode)node.getParent();
				folder.remove(node);
			}
		}
	}
}
