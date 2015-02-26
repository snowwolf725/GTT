/**
 * 
 */
package gttlipse.scriptEditor.actions;

import gtt.runner.Controller;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.TestScriptDocument;
import gttlipse.GTTlipseConfig;
import gttlipse.actions.EnhancedAction;
import gttlipse.scriptEditor.dialog.EditAUTInfoNodeDialog;
import gttlipse.scriptEditor.dialog.WebEditAUTInfoNodeDialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;



/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class RecordAction extends EnhancedAction {
	private Controller _controller; // 2003art.

	/**
	 * 
	 */
	public RecordAction() {
		super();
	}

	public void run() {
		// 使用者要選取 Interaction Sequence or FolderNode 才能錄影
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		if (!(selection.getFirstElement() instanceof TestScriptDocument)&&
			!(selection.getFirstElement() instanceof FolderNode)) {
			MessageBox box = new MessageBox(m_TreeViewer.getControl().getShell(),
					SWT.OK);
			box.setText("Warring");
			box.setMessage("You need to select Interaction Sequence or FolderNode for recording.");
			box.open();
			return;
		}
		// 設定 AUT
		NodeFactory factory = new NodeFactory();
		LaunchNode node = (LaunchNode)factory.createLaunchNode("AUTInfo", "");
		
		TitleAreaDialog dialog = null;
		if(GTTlipseConfig.testingOnWebPlatform()) {
			dialog = new WebEditAUTInfoNodeDialog(m_TreeViewer.getControl().getShell(),node);
		}
		else {
			dialog = new EditAUTInfoNodeDialog(m_TreeViewer.getControl().getShell(),node);
		}
		
		dialog.open();

		if (node.getClassName() == null) {
			MessageBox box = new MessageBox(m_TreeViewer.getControl().getShell(),	SWT.OK);
			box.setText("Warring");
			box.setMessage("AUT not found");
			box.open();
			return;
		}
		
		// add AUT info Node
		if (selection.getFirstElement() instanceof TestScriptDocument) {
			TestScriptDocument doc = (TestScriptDocument) selection.getFirstElement();
			doc.getScript().add(node);
		} else if (selection.getFirstElement() instanceof FolderNode) {
			FolderNode folderNode = (FolderNode) selection.getFirstElement();
			folderNode.add(node);
		}
		// stop old AUT
		_controller.reset();
		// load AUT
		_controller.loadAUTbyMain(node.getLaunchData());
		// 設定開始錄影
		_controller.startRecord();
		_controller.showAppWindow();
	}

	public void setController(Controller _controller) {
		this._controller = _controller;
	}
}
