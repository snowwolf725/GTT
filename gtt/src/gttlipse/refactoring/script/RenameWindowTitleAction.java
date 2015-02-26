package gttlipse.refactoring.script;

import gttlipse.actions.EnhancedAction;
import gttlipse.refactoring.dialog.WindowTitleDialog;
import gttlipse.scriptEditor.testScript.BaseNode;

import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;


public class RenameWindowTitleAction extends EnhancedAction {
	public RenameWindowTitleAction() {
		// TODO Auto-generated constructor stub
	}

	public RenameWindowTitleAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RenameWindowTitleAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RenameWindowTitleAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer.getSelection();		
		BaseNode node = (BaseNode) selection.getFirstElement();

		// collect window title
		ScriptWindowDataCollector collector = new ScriptWindowDataCollector();
		node.accept(collector);
		
		//input new name in dialog
		WindowTitleDialog dialog = new WindowTitleDialog(m_TreeViewer.getControl().getShell());
		dialog.setWindow(collector.getComponentWindow());
		dialog.open();
		if(dialog.getReturnCode() == SWT.OK) {
			Vector<String> test = collector.getComponentWindow().getType(dialog.getName());
			if(test == null) {
				// initial rename window title refactoring object
				RenameScriptWindowTitle refactor = new RenameScriptWindowTitle();
				refactor.setName(dialog.getName());
				refactor.setTitle(dialog.getTitle());
				refactor.setWindowType(dialog.getType());
				refactor.setRoot(node);
				
				refactor.renameWindowTitle();
				MessageDialog.openInformation(m_TreeViewer.getControl().getShell(),
						"Complete", "Rename Complete");	
			}
			else {
				MessageDialog.openInformation(m_TreeViewer.getControl().getShell(),
						"Warning", "Can not rename: there exists anotnher component with the same name.");
			}
		}		
		m_TreeViewer.refresh();
	}
}
