package gttlipse.refactoring.script;

import gtt.eventmodel.IComponent;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.TestProject;
import gttlipse.actions.EnhancedAction;
import gttlipse.refactoring.dialog.InputNameDialog;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;


public class RenameAction extends EnhancedAction {
	private RefactoringRenameVisitor _renameVisitor = null;
	private CheckSameNameVisitor _checkVisitor = null;

	public RenameAction() {
		// TODO Auto-generated constructor stub
	}

	public RenameAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RenameAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RenameAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		// Get rename component
		IStructuredSelection selection = (IStructuredSelection) m_TreeViewer
				.getSelection();
		IComponent component = null;
		AbstractNode node = (AbstractNode) selection.getFirstElement();
		if (node instanceof EventNode) {
			EventNode event = (EventNode) node;
			component = event.getComponent();
		} else if (node instanceof ViewAssertNode) {
			ViewAssertNode assertNode = (ViewAssertNode) node;
			component = assertNode.getComponent();
		}

		// input new name in dialog
		InputNameDialog dialog = new InputNameDialog(m_TreeViewer.getControl()
				.getShell());
		dialog.setOldName(component.getName());
		dialog.open();
		if (dialog.getReturnCode() == SWT.OK) {
			// initial rename object
			IComponent renameComponent = component.clone();
			renameComponent.setName(dialog.getNewName());
			// Check have change value
			if (renameComponent.match(component) == false) {
				_renameVisitor = new RefactoringRenameVisitor();
				_renameVisitor.setComponent(component);
				_renameVisitor.setRenameComponent(renameComponent);
				_checkVisitor = new CheckSameNameVisitor();
				_checkVisitor.setComponet(renameComponent);
				_checkVisitor.setOldName(component.getName());

				// get class node
				TestScriptDocument doc = getDocument(node);
				BaseNode baseNode = doc.getParent();
				TestCaseNode classNode = getClassNode(baseNode);

				// check same name
				_checkVisitor.visit(classNode);
				if (_checkVisitor.isSameName()) {
					MessageDialog
							.openInformation(m_TreeViewer.getControl()
									.getShell(), "Warning",
									"Can not rename: there exists anotnher component with the same name.");
				} else {
					// Rename Step
					_renameVisitor.visit(TestProject.getProject());
					MessageDialog.openInformation(m_TreeViewer.getControl()
							.getShell(), "Complete", "Rename Complete");
				}
			}
		}
		m_TreeViewer.refresh();
	}

	private TestScriptDocument getDocument(AbstractNode node) {
		if (node == null)
			return null;
		if (node.getParent() == null)
			return node.getDocument();
		return getDocument(node.getParent());
	}

	private TestCaseNode getClassNode(BaseNode node) {
		if (node == null)
			return null;
		if (node instanceof TestCaseNode)
			return (TestCaseNode) node;
		return getClassNode(node.getParent());
	}
}
