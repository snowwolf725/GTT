package gttlipse.macro.view;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IHaveArgument;
import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.TestProject;
import gttlipse.editor.ui.ArgumentPanel;
import gttlipse.refactoring.dialog.EditParameterDialog;
import gttlipse.refactoring.dialog.InputNameDialog;
import gttlipse.refactoring.dialog.SelectArgumentDialog;
import gttlipse.refactoring.dialog.SelectMacroComponentDialog;
import gttlipse.refactoring.dialog.WindowTitleDialog;
import gttlipse.refactoring.macro.EditMacroEventParameter;
import gttlipse.refactoring.macro.ExtractMacroComponent;
import gttlipse.refactoring.macro.ExtractMacroEvent;
import gttlipse.refactoring.macro.ExtractParameter;
import gttlipse.refactoring.macro.HideDelegate;
import gttlipse.refactoring.macro.InlineMacroComponent;
import gttlipse.refactoring.macro.InlineMacroEvent;
import gttlipse.refactoring.macro.InlineParameter;
import gttlipse.refactoring.macro.MoveComponent;
import gttlipse.refactoring.macro.MoveMacroComponent;
import gttlipse.refactoring.macro.MoveMacroEvent;
import gttlipse.refactoring.macro.RemoveMiddleMan;
import gttlipse.refactoring.macro.RenameMacroWindowTitle;
import gttlipse.refactoring.macro.RenameParameter;
import gttlipse.refactoring.script.ReplaceMacroReferenceVisitor;
import gttlipse.refactoring.util.MacroWindowDataCollector;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

public class RefactoringHandler {
	
	private TreeViewer m_treeViewer;
	private MacroDocument m_Document;
	
	RefactoringHandler(TreeViewer viewer) {
		m_treeViewer = viewer;
		m_Document = TestProject.getMacroDocument();
	}
	
	public AbstractMacroNode getSelectedNode() {
		IStructuredSelection selection = (IStructuredSelection) m_treeViewer
				.getSelection();
		if (selection.size() == 0)
			return null;
		return (AbstractMacroNode) selection.getFirstElement();
	}

	public List<AbstractMacroNode> getSelectedNodes() {
		IStructuredSelection selection = (IStructuredSelection) m_treeViewer
				.getSelection();

		List<AbstractMacroNode> nodes = new Vector<AbstractMacroNode>();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			AbstractMacroNode node = (AbstractMacroNode) iter.next();
			nodes.add(node);
		}

		return nodes;
	}
	
	public void addMacroEventParameter() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		// Dialog
		EditParameterDialog dialog = new EditParameterDialog(m_treeViewer
				.getControl().getShell(), (MacroEventNode) selectNode,
				ArgumentPanel.ADD_BUTTON, true);
		dialog.open();
		// press cancel
		if (dialog.getReturnCode() == 1) {
			return;
		}
		// Create editParameter then set arglist, node
		EditMacroEventParameter refactor = new EditMacroEventParameter(
				m_Document.getMacroScript());
		Arguments args = dialog.getArgumentList();
		if (refactor.isValid(((IHaveArgument) selectNode).getArguments(), args)) {
			refactor.editParameter(selectNode, args);
		} else {
			MessageDialog.openInformation(m_treeViewer.getControl().getShell(),
					"Warning", "Cannot add parameter because:\n"
							+ "1. Parameter name conflict.");
		}

		m_treeViewer.refresh();
	}

	public void removeMacroEventParameter() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		// Dialog
		EditParameterDialog dialog = new EditParameterDialog(m_treeViewer
				.getControl().getShell(), (MacroEventNode) selectNode,
				ArgumentPanel.DEL_BUTTON, true);
		dialog.open();
		// press cancel
		if (dialog.getReturnCode() == 1) {
			return;
		}
		// Create editParameter then set arglist, node
		EditMacroEventParameter refactor = new EditMacroEventParameter(
				m_Document.getMacroScript());
		Arguments args = dialog.getArgumentList();
		if (refactor.isValid(((IHaveArgument) selectNode).getArguments(), args)) {
			refactor.editParameter(selectNode, args);
		} else {
			MessageDialog.openInformation(m_treeViewer.getControl().getShell(),
					"Warning", "Cannot remove parameter because:\n"
							+ "1. Parameter name conflict.");
		}

		m_treeViewer.refresh();
	}

	public void renameMacroEventParameter() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		// Dialog
		EditParameterDialog dialog = new EditParameterDialog(m_treeViewer
				.getControl().getShell(), (MacroEventNode) selectNode,
				ArgumentPanel.DEL_BUTTON, false);
		dialog.open();
		// press cancel
		if (dialog.getReturnCode() == 1) {
			return;
		}
		// Create renameParameter then set arglist, node
		RenameParameter refactor = new RenameParameter(m_Document
				.getMacroScript());
		refactor.renameParameter(selectNode, dialog.getArgumentList());

		m_treeViewer.refresh();
	}

	public void extractMacroEvent() {
		List<AbstractMacroNode> selects = getSelectedNodes();

		ExtractMacroEvent refactor = new ExtractMacroEvent();

		// Dialog
		InputNameDialog dialog = new InputNameDialog(m_treeViewer.getControl()
				.getShell());
		dialog.open();
		// Press cancel
		if (dialog.getReturnCode() == 1) {
			return;
		} else if (dialog.getReturnCode() == SWT.OK) {
			if (refactor.isValid(selects, selects.get(0).getParent()
					.getParent(), dialog.getNewName())) {
				refactor.extractMacroEvent(selects, dialog.getNewName());
			} else {
				MessageDialog
						.openInformation(
								m_treeViewer.getControl().getShell(),
								"Warning",
								"Can not extract event beacuse:\n"
										+ "1. there exists anotnher node with the same name.");
			}
		}

		m_treeViewer.refresh();
	}

	public void inlineMacroEvent() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventCallerNode))
			return;

		InlineMacroEvent refactor = new InlineMacroEvent();

		if (refactor.isValid()) {
			refactor.inlineMacroEvent(selectNode);
		} else {
			MessageDialog.openInformation(m_treeViewer.getControl().getShell(),
					"Warning.", "Macro Event isn't same parent.");
		}

		m_treeViewer.refresh();
	}

	public void moveMacroEvent() {
		List<AbstractMacroNode> selects = getSelectedNodes();
		if (selects.size() == 0) {
			return;
		}

		// dialog
		SelectMacroComponentDialog dialog = new SelectMacroComponentDialog(
				m_treeViewer.getControl().getShell(), selects.get(0));
		dialog.setRoot(m_Document.getMacroScript());
		dialog.open();
		if (dialog.getReturnCode() == SWT.OK) {
			AbstractMacroNode parentNode = dialog.getSelectNode();
			if (parentNode != null) {
				MoveMacroEvent refactor = new MoveMacroEvent(m_Document
						.getMacroScript());
				if (refactor.isValid(selects, parentNode)) {
					// macro part move and modify reference
					refactor.move(selects, parentNode);
					// test script part modify reference
					Vector<String> oldPath = refactor.getOldPath();
					Vector<String> newPath = refactor.getNewPath();
					ReplaceMacroReferenceVisitor visitor = new ReplaceMacroReferenceVisitor();
					for (int i = 0; i < oldPath.size(); i++) {
						visitor.setOldPath(oldPath.get(i));
						visitor.setNewPath(newPath.get(i));
						visitor.visit(TestProject.getProject());
					}
				} else {
					MessageDialog
							.openInformation(
									m_treeViewer.getControl().getShell(),
									"Warning",
									"Cannot move because:\n"
											+ "1. There exists anotnher event with the same name.\n"
											+ "2. Select nodes arenot MacroEventNode.");
				}
			}
		}
		m_treeViewer.refresh();
	}

	public void extractMacroComponent() {
		List<AbstractMacroNode> selects = getSelectedNodes();

		// dialog
		InputNameDialog dialog = new InputNameDialog(m_treeViewer.getControl()
				.getShell());
		dialog.open();
		if (dialog.getReturnCode() == SWT.OK) {
			if (selects.size() != 0) {
				ExtractMacroComponent refactor = new ExtractMacroComponent(
						m_Document.getMacroScript());

				if (refactor.isValid(selects, selects.get(0).getParent(),
						dialog.getNewName())) {
					refactor.extractMacroComponent(selects, selects.get(0)
							.getParent(), dialog.getNewName());
					// replace test script reference
					ReplaceMacroReferenceVisitor visitor = new ReplaceMacroReferenceVisitor();
					Vector<String> oldPath = refactor.getOldPath();
					Vector<String> newPath = refactor.getNewPath();
					for (int i = 0; i < oldPath.size(); i++) {
						visitor.setOldPath(oldPath.get(i));
						visitor.setNewPath(newPath.get(i));
						TestProject.getProject().accept(visitor);
					}
				} else {
					final String msg = "Cannot extract because:\n"
							+ "1. Selected nodes are not in the same scope.\n"
							+ "2. There exists another event with the same name.";
					showErrorMsg(msg);
				}
			}
		}
		m_treeViewer.refresh();
	}
	
	private void showErrorMsg(String msg) {

		MessageDialog.openInformation(m_treeViewer.getControl().getShell(),
				"Warning", msg);
	}
	
	public void renameWindowTitle() {
		AbstractMacroNode selectNode = getSelectedNode();
		if (selectNode == null || !(selectNode instanceof MacroComponentNode))
			return;
		// collect title
		MacroWindowDataCollector collector = new MacroWindowDataCollector();
		collector.setRoot(selectNode);
		collector.collect();

		// dialog
		WindowTitleDialog dialog = new WindowTitleDialog(m_treeViewer
				.getControl().getShell());
		dialog.setWindow(collector.getComponentWindow());
		dialog.open();

		if (dialog.getReturnCode() == SWT.OK) {
			// test same name
			Vector<String> test = collector.getComponentWindow().getType(
					dialog.getName());
			if (test == null) {
				// rename window title
				RenameMacroWindowTitle refactor = new RenameMacroWindowTitle(
						selectNode);
				refactor.renameWindowTitle(dialog.getTitle(), dialog.getType(),
						dialog.getName());
			} else {
				final String msg = "Can not rename: there exists anotnher component with the same title.";
				showErrorMsg(msg);
			}
		}
		m_treeViewer.refresh();
	}

	public void moveMacroComponent() {
		AbstractMacroNode selectNode = getSelectedNode();
		if (selectNode == null || !(selectNode instanceof MacroComponentNode))
			return;
		// dialog
		SelectMacroComponentDialog dialog = new SelectMacroComponentDialog(
				m_treeViewer.getControl().getShell(), selectNode);
		dialog.setRoot(m_Document.getMacroScript());
		dialog.open();
		if (dialog.getReturnCode() == SWT.OK) {
			AbstractMacroNode parentNode = dialog.getSelectNode();
			if (parentNode != null && parentNode != selectNode.getParent()) {
				MoveMacroComponent refactor = new MoveMacroComponent(m_Document
						.getMacroScript());
				if (refactor.isValid(selectNode, parentNode)) {
					// macro part move and modify reference
					refactor.moveMacroComponent(selectNode, parentNode);
					// test script part modify reference
					ReplaceMacroReferenceVisitor visitor = new ReplaceMacroReferenceVisitor(
							refactor.getOldPath(), refactor.getNewPath());
					visitor.visit(TestProject.getProject());
				} else {

					final String msg = "Cannot move because:\n"
							+ "1. There exists anotnher component with the same name.\n"
							+ "2. Attempt to move to sub component or same component";
					showErrorMsg(msg);
				}
			}
		}
		m_treeViewer.refresh();
	}
	
	public void moveComponent() {
		List<AbstractMacroNode> selects = getSelectedNodes();
		if (selects.size() == 0) {
			return;
		}
		// dialog
		SelectMacroComponentDialog dialog = new SelectMacroComponentDialog(
				m_treeViewer.getControl().getShell(), selects.get(0));
		dialog.setRoot(m_Document.getMacroScript());
		dialog.open();
		if (dialog.getReturnCode() == SWT.OK) {
			AbstractMacroNode parentNode = dialog.getSelectNode();
			if (parentNode != null) {
				MoveComponent refactor = new MoveComponent(m_Document
						.getMacroScript());
				if (refactor.isValid(selects, parentNode)) {
					// macro part move and modify reference
					refactor.moveComponent(selects, parentNode);
				} else {
					final String msg = "Cannot move because:\n"
							+ "1. There exists anotnher component with the same name.\n"
							+ "2. Select nodes arenot componentNode.";
					showErrorMsg(msg);
				}
			}
		}
		m_treeViewer.refresh();
	}

	public void removeMiddleMan() {
		AbstractMacroNode selectNode = getSelectedNode();
		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		RemoveMiddleMan refactor = new RemoveMiddleMan(m_Document
				.getMacroScript());
		if (refactor.isValid(selectNode)) {
			refactor.removeMiddleMan(selectNode);
			// replace test script reference
			ReplaceMacroReferenceVisitor visitor = new ReplaceMacroReferenceVisitor(
					refactor.getOldPath(), refactor.getNewPath());

			visitor.visit(TestProject.getProject());
		} else {
			final String msg = "Cannot remove because:\n"
					+ "1. Macro Event have more than one child.\n"
					+ "2. The child is not a caller";
			showErrorMsg(msg);
		}
		m_treeViewer.refresh();
	}
	
	public void inlineMacroComponent() {
		AbstractMacroNode selectNode = getSelectedNode();
		if (selectNode == null || !(selectNode instanceof MacroComponentNode))
			return;
		// initial
		InlineMacroComponent refactor = new InlineMacroComponent(m_Document
				.getMacroScript());

		if (refactor.isValid(selectNode, selectNode.getParent())) {
			refactor.inlineMacroComponent(selectNode, selectNode.getParent());
			// replace test script reference
			ReplaceMacroReferenceVisitor visitor = new ReplaceMacroReferenceVisitor();
			Vector<String> oldPath = refactor.getOldPath();
			Vector<String> newPath = refactor.getNewPath();
			for (int i = 0; i < oldPath.size(); i++) {
				visitor.setOldPath(oldPath.get(i));
				visitor.setNewPath(newPath.get(i));
				TestProject.getProject().accept(visitor);
			}
		} else {
			final String msg = "Cannot extract because:\n"
					+ "1. There exists duplicate name with nodes.\n"
					+ "2. SelectNode is ancestry.\n" + "3. SelectNode is Root.";
			showErrorMsg(msg);
		}
		m_treeViewer.refresh();
	}
	
	public void hideDelegate() {
		AbstractMacroNode selectNode = getSelectedNode();
		if (selectNode == null || !(selectNode instanceof MacroEventCallerNode))
			return;

		// Refactor
		HideDelegate refactor = new HideDelegate(m_Document.getMacroScript());

		// Input name dialog
		InputNameDialog dialog = new InputNameDialog(m_treeViewer.getControl()
				.getShell());
		dialog.setOldName("");
		dialog.open();
		// press cancel
		if (dialog.getReturnCode() == 1)
			return;

		if ((refactor.isValid(selectNode, dialog.getNewName()) == false)) {
			final String msg = "Cannot hide delegate because:\n"
					+ "1. Input name empty.\n"
					+ "2. SelectNode are not a caller.\n"
					+ "3. Input name conflict.";
			showErrorMsg(msg);
		} else {
			refactor.hideDelegate(selectNode, dialog.getNewName());
		}

		/* 更新 Viewer */
		m_treeViewer.refresh();
	}

	public void extractMacroEventParameter() {
		AbstractMacroNode selectNode = getSelectedNode();
		// check != null and have argument
		if (selectNode == null
				|| (selectNode instanceof IHaveArgument) == false)
			return;

		ExtractParameter refactor = new ExtractParameter(m_Document
				.getMacroScript());
		Arguments args = refactor.getArguments(selectNode);

		// dialog
		SelectArgumentDialog dialog = new SelectArgumentDialog(m_treeViewer
				.getControl().getShell(), args);
		dialog.open();

		// press cancel
		if (dialog.getReturnCode() == 1) {
			return;
		}

		args = dialog.getArgumentList();
		// empty then do nothing
		if (args.size() == 0)
			return;

		if (refactor.isValid(args, selectNode) == false) {
			final String msg = "Cannot extract Parameter because :\n"
					+ "1. Argument value is empty.\n"
					+ "2. Argument name conflict.";
			showErrorMsg(msg);
		} else {
			refactor.extractParameter(selectNode, args);
		}

		/* 更新 Viewer */
		m_treeViewer.refresh();
	}

	public void inlineMacroEventParameter() {
		AbstractMacroNode selectNode = getSelectedNode();
		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		// dialog
		SelectArgumentDialog dialog = new SelectArgumentDialog(m_treeViewer
				.getControl().getShell(), ((MacroEventNode) selectNode)
				.getArguments());
		dialog.open();

		// press cancel
		if (dialog.getReturnCode() == 1) {
			return;
		}

		Arguments args = dialog.getArgumentList();
		// empty then do nothing
		if (args.size() == 0)
			return;

		InlineParameter refactor = new InlineParameter(m_Document
				.getMacroScript());
		if (refactor.isValid(args) == false) {
			final String msg = "Cannot inline Parameter because :\n"
					+ "1. Argument value is empty.";
			showErrorMsg(msg);
		} else {
			refactor.inlineParameter(selectNode, args);
		}

		/* 更新 Viewer */
		m_treeViewer.refresh();
	}
}
