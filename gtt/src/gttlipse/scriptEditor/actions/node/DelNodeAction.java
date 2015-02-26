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
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.SourceFolderNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;



/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class DelNodeAction extends EnhancedAction implements
		IWorkbenchWindowActionDelegate {
//	private IResourceFinder m_finder;

	/**
	 * 
	 */
	public DelNodeAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void run() {
//		m_finder=new IResourceFinder();
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		Object nodes[] = selection.toArray();
		for (int i = 0; i < nodes.length; i++) {
			delNode(nodes[i]);
		}
		// set dirty
		GTTlipse.makeScriptViewDirty();
		m_TreeViewer.refresh();
	}

	public void delNode(Object anode) {
		if (anode instanceof ProjectNode)
			MessageDialog.openInformation(m_TreeViewer.getControl().getShell(),
					"GTT View", "You can't delete TestProject Node.");
		else if (anode instanceof SourceFolderNode) {
			SourceFolderNode folder = (SourceFolderNode) anode;
			ResourceManager manager = new ResourceManager();
			manager.DelJavaSourceFolder(folder);
		} else if (anode instanceof PackageNode) {
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
			String methodname = testmethod.getName();
			classnode.removeChild(testmethod);
			//del Test Method
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

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		// TODO Auto-generated method stub
		// action.setAccelerator(SWT.DEL);
		// action.setActionDefinitionId("GTTlipse.actions.DelNodeAction");
		run();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}
}
