/**
 * 
 */
package gttlipse.scriptEditor.actions.node;

import gtt.testscript.AbstractNode;
import gtt.testscript.FolderNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipse;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.PackageNode;
import gttlipse.scriptEditor.testScript.ProjectNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.scriptEditor.util.ScrapBook;

import org.eclipse.jface.viewers.IStructuredSelection;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.actions
 * 
 */
public class PasteNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public PasteNodeAction() {
		// TODO Auto-generated constructor stub
	}

	public void run() {
		if (ScrapBook.getData() == null)
			return;
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		if (selection.getFirstElement() instanceof ProjectNode) {
			ProjectNode projectnode = (ProjectNode) selection.getFirstElement();
			Object[] objs = ScrapBook.getData();
			for (Object obj : objs) {
				if (obj instanceof PackageNode)
					projectnode.addChild(((PackageNode) obj).clone());
				else if (obj instanceof TestCaseNode)
					projectnode.addChild(((TestCaseNode) obj).clone());
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof PackageNode) {
			PackageNode packagenode = (PackageNode) selection.getFirstElement();
			Object[] objs = ScrapBook.getData();
			for (Object obj : objs) {
				if (obj instanceof PackageNode)
					packagenode.addChild(((PackageNode) obj).clone());
				else if (obj instanceof TestCaseNode)
					packagenode.addChild(((TestCaseNode) obj).clone());
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof TestCaseNode) {
			TestCaseNode classnode = (TestCaseNode) selection.getFirstElement();
			Object[] objs = ScrapBook.getData();
			for (Object obj : objs) {
				if (obj instanceof TestMethodNode)
					classnode.addChild(((TestMethodNode) obj).clone());
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof TestMethodNode) {
			TestMethodNode methodnode = (TestMethodNode) selection
					.getFirstElement();
			Object[] objs = ScrapBook.getData();
			for (Object obj : objs) {
				if (obj instanceof TestScriptDocument) {
					TestScriptDocument doc = ((TestScriptDocument) obj).clone();
					methodnode.addDocument(doc);
					TestFileManager manager = new TestFileManager();
					manager.addScriptDocument(doc, true);
				}
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof TestScriptDocument) {
			TestScriptDocument doc = (TestScriptDocument) selection
					.getFirstElement();
			Object[] objs = ScrapBook.getData();
			for (Object obj : objs) {
				if (obj instanceof AbstractNode)
					doc.getScript().add(((AbstractNode) obj).clone());
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof FolderNode) {
			FolderNode folder = (FolderNode) selection.getFirstElement();
			Object[] objs = ScrapBook.getData();
			for (Object obj : objs) {
				if (obj instanceof AbstractNode)
					folder.add(((AbstractNode) obj).clone());
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (selection.getFirstElement() instanceof AbstractNode) {
		}
		m_TreeViewer.refresh();
	}
}
