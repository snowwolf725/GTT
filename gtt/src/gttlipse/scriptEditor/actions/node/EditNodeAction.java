/**
 * 
 */
package gttlipse.scriptEditor.actions.node;


import gtt.eventmodel.AbstractEvent;
import gtt.eventmodel.Argument;
import gtt.testscript.EventNode;
import gtt.testscript.FolderNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.OracleNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.GTTlipseConfig;
import gttlipse.actions.EnhancedAction;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.dialog.EditAUTInfoNodeDialog;
import gttlipse.scriptEditor.dialog.EditAssertNodeDialog;
import gttlipse.scriptEditor.dialog.EditEventNodeDialog;
import gttlipse.scriptEditor.dialog.EditGenericNodeDialog;
import gttlipse.scriptEditor.dialog.EditOracleNodeDialog;
import gttlipse.scriptEditor.dialog.WebEditAUTInfoNodeDialog;

import java.util.Iterator;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.actions
 * 
 */
public class EditNodeAction extends EnhancedAction {

	/**
	 * 
	 */
	public EditNodeAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		if (obj instanceof TestScriptDocument) {
			EditGenericNodeDialog editdialog = new EditGenericNodeDialog(m_TreeViewer.getControl().getShell(), obj);
			editdialog.open();
			TestScriptDocument doc = (TestScriptDocument) obj;
			doc = (TestScriptDocument)editdialog.getValue();
			
			TestFileManager manager = new TestFileManager();
			manager.renameTestScriptDocument(doc);
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (obj instanceof OracleNode) {
			EditOracleNodeDialog dialog = new EditOracleNodeDialog(m_TreeViewer.getControl().getShell(),(OracleNode)obj);
			dialog.open();
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (obj instanceof FolderNode) {
			EditGenericNodeDialog editdialog = new EditGenericNodeDialog(m_TreeViewer
					.getControl().getShell(), obj);
			editdialog.open();
			FolderNode node = (FolderNode) obj;
			String newname = ((FolderNode)editdialog.getValue()).getName();
			node.setName(newname);
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (obj instanceof EventNode) {
			EditEventNodeDialog editdialog = new EditEventNodeDialog(m_TreeViewer.getControl().getShell(), obj);
			editdialog.open();
			
			if(editdialog.getReturnCode() == SWT.Modify)
			{
				EventNode eventnode = (EventNode) obj;
				
				/* 要做 deep copy
				 * 先清掉之前的 arg 在加入新的 arg
				 * */
				AbstractEvent event=(AbstractEvent)eventnode.getEvent();
				event.setEventId(editdialog.getEventNode().getEvent().getEventId());
				Iterator<Argument> eite=event.getArguments().iterator();
				while (eite.hasNext())
					event.getArguments().remove(0);
				
				eite=editdialog.getEventNode().getEvent().getArguments().iterator();
				while (eite.hasNext()) {
					Argument arg=(Argument)eite.next();
					event.getArguments().add(arg);
				}
			}
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (obj instanceof ViewAssertNode) {
			EditAssertNodeDialog editdialog = new EditAssertNodeDialog(m_TreeViewer
					.getControl().getShell(), (ViewAssertNode)obj);
			editdialog.open();
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} else if (obj instanceof LaunchNode) {
			
			TitleAreaDialog dialog = null;
			if(GTTlipseConfig.testingOnWebPlatform()) {
				dialog = new WebEditAUTInfoNodeDialog(m_TreeViewer.getControl().getShell(),(LaunchNode)obj);
			}
			else {
				dialog = new EditAUTInfoNodeDialog(m_TreeViewer.getControl().getShell(),(LaunchNode)obj);
			}
			
			dialog.open();
			// set dirty
			GTTlipse.makeScriptViewDirty();
		} 
		m_TreeViewer.refresh();
	}

}
