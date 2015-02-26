package gttlipse.macro.view;

import gtt.macro.MacroUtil;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.ExistenceAssertNode;
import gtt.macro.macroStructure.IncludeNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroNodeFactory;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.TestProject;
import gttlipse.fit.node.EventTriggerNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.node.FixNameNode;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.SplitDataAsNameNode;
import gttlipse.macro.dialog.EditDialogFactory;
import gttlipse.macro.dialog.ModelAssertDialog;
import gttlipse.refactoring.dialog.InputNameDialog;
import gttlipse.refactoring.macro.RenameComponent;
import gttlipse.refactoring.macro.RenameFitNode;
import gttlipse.refactoring.macro.RenameMacroComponent;
import gttlipse.refactoring.macro.RenameMacroEvent;
import gttlipse.refactoring.script.RenameMacroReferenceVisitor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

public class NodeManager {
	
	private TreeViewer m_treeViewer;
	private List<String> m_generationList;
	
	public NodeManager(TreeViewer viewer) {
		m_treeViewer = viewer;
		m_generationList = new LinkedList<String>();
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
	
	public void editSelectedNode() {
		updateGenerationList();

		AbstractMacroNode select = getSelectedNode();
		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), select, m_generationList);
		if(dialog != null)
			dialog.open();
		
		m_treeViewer.refresh();
	}
	
	public void deleteMacroNode() {
		Iterator<AbstractMacroNode> ite = getSelectedNodes().iterator();
		while (ite.hasNext()) {
			MacroUtil.removeNode(ite.next());
		}
		m_treeViewer.refresh();
	}
	
	private void refreshandExpendTreeViewer(AbstractMacroNode node) {
		m_treeViewer.refresh();
		m_treeViewer.setExpandedState(node, true);
	}
	
	// 取得macro底下所有的generation node，讓event triger和assert方便取得
	private void updateGenerationList() {
		m_generationList.clear();
		AbstractMacroNode root = (AbstractMacroNode) m_treeViewer.getTree()
				.getItem(0).getData();
		AbstractMacroNode[] children = root.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof FitNode)
				addGenerationNode((FitNode) children[i]);
		}
	}

	private void addGenerationNode(FitNode fitNode) {
		Iterator<AbstractMacroNode> ite = fitNode.iterator();
		while (ite.hasNext()) {
			AbstractMacroNode node = ite.next();
			m_generationList.add(MacroPresenter.getNodePath(node));
		}
	}
	
	
	
	////////////////////////////////////////////////
	public void insertComponentNode(String type) {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null)
			return;
		if (!(selectNode instanceof MacroComponentNode))
			return;

		ComponentNode comp = new MacroNodeFactory().createComponentNode();
		if (type != null)
			comp.setType(type);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), comp, m_generationList);
		if(dialog != null)
			dialog.open();

		if (dialog.getReturnCode() == 1)
			return;

		MacroUtil.insertNode(selectNode, comp);

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertComponentEventNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		ComponentEventNode node = new MacroNodeFactory()
				.createComponentEventNode();
		MacroUtil.insertNode(selectNode, node);
		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertMacroEventCallNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		MacroNodeFactory factory = new MacroNodeFactory();

		String path = MacroPresenter.getNodePath(selectNode);

		MacroEventCallerNode node = factory.createMacroEventCallerNode(path);

		MacroUtil.insertNode(selectNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertMacroComponentNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroComponentNode))
			return;

		MacroNodeFactory factory = new MacroNodeFactory();
		AbstractMacroNode macroNode = factory
				.createMacroComponentNode("MacroComponent");

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), macroNode, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1)
			return;

		MacroUtil.insertNode(selectNode, macroNode);

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertMacroEventNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroComponentNode))
			return;

		MacroNodeFactory factory = new MacroNodeFactory();
		// AbstractMacroNode macroEventNode = factory.createMacroEventNode();
		AbstractMacroNode macroEventNode = factory.createMacroEventNode();
		macroEventNode.setName("MacroEvent");

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), macroEventNode, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1)
			return;

		MacroUtil.insertNode(selectNode, macroEventNode);

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertModelAssertNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		ModelAssertDialog dialog = new ModelAssertDialog(m_treeViewer
				.getControl().getShell(), null);
		dialog.open();

		if (dialog.getReturnCode() == 1)
			return;

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertViewAssertNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		Vector<String> path = new Vector<String>();
		path.add(selectNode.getParent().getName());

		ViewAssertNode node = new MacroNodeFactory().createViewAssertNode();

		MacroUtil.insertNode(selectNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertExistenceAssertNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		Vector<String> path = new Vector<String>();
		path.add(selectNode.getParent().getName());

		ExistenceAssertNode node = new MacroNodeFactory()
				.createExistenceAssertNode();

		MacroUtil.insertNode(selectNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectNode);
	}

	public void insertLaunchNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null || !(selectNode instanceof MacroEventNode))
			return;

		Vector<String> path = new Vector<String>();
		path.add(selectNode.getParent().getName());

		LaunchNode node = new MacroNodeFactory().createLaunchNode();

		MacroUtil.insertNode(selectNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectNode);
	}
	
	public void insertIncludeNode() {
		AbstractMacroNode selectNode = getSelectedNode();

		if (selectNode == null)
			return;

		IncludeNode node = new MacroNodeFactory().createIncludeNode();
		MacroUtil.insertNode(selectNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectNode);		
	}

	// 新增加入Event Trigger node相關動作 by Pan
	public void insertEventTriggerNode() {
		updateGenerationList();
		AbstractMacroNode selectionNode = getSelectedNode();

		if (selectionNode == null || !(selectionNode instanceof MacroEventNode))
			return;

		EventTriggerNode eventTriggerNode = new MacroNodeFactory()
				.createEventTriggerNode();

		MacroUtil.insertNode(selectionNode, eventTriggerNode);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), eventTriggerNode, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, eventTriggerNode);
			return;
		}

		refreshandExpendTreeViewer(selectionNode);
	}

	// 新增加入FitAssertion node相關動作 by Pan
	public void insertFitAssertionNode() {
		updateGenerationList();
		AbstractMacroNode selectionNode = getSelectedNode();

		if (selectionNode == null || !(selectionNode instanceof MacroEventNode))
			return;

		FitAssertionNode fitAssertionNode = new MacroNodeFactory()
				.createFitAssertionNode();

		MacroUtil.insertNode(selectionNode, fitAssertionNode);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), fitAssertionNode, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, fitAssertionNode);
			return;
		}

		refreshandExpendTreeViewer(selectionNode);
	}

	// 新增加入FitTableAssertion node相關動作 by Pan
	public void insertFitStateAssertionNode() {
		AbstractMacroNode selectionNode = getSelectedNode();

		if (selectionNode == null || !(selectionNode instanceof MacroEventNode))
			return;

		FitStateAssertionNode fitTableAssertionNode = new MacroNodeFactory()
				.createFitStateAssertionNode();

		MacroUtil.insertNode(selectionNode, fitTableAssertionNode);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), fitTableAssertionNode, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, fitTableAssertionNode);
			return;
		}

		refreshandExpendTreeViewer(selectionNode);
	}

	// 新增加入Fit node 相關動作 by Pan
	public void insertFitNode() {
		AbstractMacroNode selectionNode = getSelectedNode();

		if (selectionNode == null
				|| !(selectionNode instanceof MacroComponentNode))
			return;

		FitNode node = new MacroNodeFactory().createFitNode();

		MacroUtil.insertNode(selectionNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectionNode);
	}

	// 新增加入 Split Data As Name node 相關動作 by Pan
	public void insertSplitDataAsNameNode() {
		AbstractMacroNode selectionNode = getSelectedNode();

		if (selectionNode == null || !(selectionNode instanceof FitNode))
			return;

		SplitDataAsNameNode node = new MacroNodeFactory()
				.createSplitDataAsNameNode();

		MacroUtil.insertNode(selectionNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, node);
			return;
		}
		refreshandExpendTreeViewer(selectionNode);
	}

	// 新增加入 Generate Order Name node 相關動作 by Pan
	public void insertGenerateOrderNameNode() {
		AbstractMacroNode selectionNode = getSelectedNode();

		if (selectionNode == null || !(selectionNode instanceof FitNode))
			return;

		GenerateOrderNameNode node = new MacroNodeFactory()
				.createGenerateOrderNameNode();

		MacroUtil.insertNode(selectionNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectionNode);
	}

	// 新增加入 Fix Name node 相關動作 by Pan
	public void insertFixNameNode() {
		AbstractMacroNode selectionNode = getSelectedNode();

		if (selectionNode == null || !(selectionNode instanceof FitNode))
			return;

		FixNameNode node = new MacroNodeFactory().createFixNameNode();

		MacroUtil.insertNode(selectionNode, node);

		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();

		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, node);
			return;
		}

		refreshandExpendTreeViewer(selectionNode);
	}

	// uhsing 2011.03.16
	public void insertSplitDataNode() {
		// Identify the type of the selection node
		AbstractMacroNode selectionNode = getSelectedNode();
		if (selectionNode == null || !(selectionNode instanceof MacroEventNode)) {
			return;
		}
		
		// Create a SplitDataNode and insert it into MacroEventNode
		SplitDataNode node = new MacroNodeFactory().createSplitDataNode("SplitData", "target");
		MacroUtil.insertNode(selectionNode, node);
		
		// Open a dialog
		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();
		
		// When the user does not press the OK button
		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, node);
			return;
		}
		
		// Update the tree structure
		refreshandExpendTreeViewer(selectionNode);
	}
	
	// uhsing 2011.03.22
	public void insertDynamicComponentNode() {
		// Identify the type of the selection node
		AbstractMacroNode selectionNode = getSelectedNode();
		if (selectionNode == null || !(selectionNode instanceof MacroComponentNode)) {
			return;
		}
		
		// Create a DynamicComponentNode and insert it into MacroComponentNode
		DynamicComponentNode node = new MacroNodeFactory().createDynamicComponentNode();
		MacroUtil.insertNode(selectionNode, node);
		
		// Open a dialog
		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();
		
		// When the user does not press the OK button
		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, node);
			return;
		}
		
		// Update the tree structure
		refreshandExpendTreeViewer(selectionNode);
	}
	
	// uhsing 2011.03.28
	public void insertDynamicComponentEventNode() {
		// Identify the type of the selection node
		AbstractMacroNode selectionNode = getSelectedNode();
		if (selectionNode == null || !(selectionNode instanceof MacroEventNode)) {
			return;
		}
		
		// Create a DynamicComponentEventNode and insert it into MacroEventNode
		DynamicComponentEventNode node = new MacroNodeFactory().createDynamicComponentEventNode();
		MacroUtil.insertNode(selectionNode, node);
		
		// Open a dialog
		TitleAreaDialog dialog = EditDialogFactory.getEditingDialog(m_treeViewer.getControl().getShell(), node, m_generationList);
		dialog.open();
		
		// When the user does not press the OK button
		if (dialog.getReturnCode() == 1) {
			MacroUtil.removeNode(selectionNode, node);
			return;
		}
		
		// Update the tree structure
		refreshandExpendTreeViewer(selectionNode);
	}
	
	public void rename() {
		updateGenerationList();

		AbstractMacroNode select = getSelectedNode();
		if (select == null)
			return;

		if (select instanceof ComponentNode) {
			ComponentNode selectNode = (ComponentNode) select;
			// rename dialog
			InputNameDialog dialog = new InputNameDialog(m_treeViewer
					.getControl().getShell());
			dialog.setOldName(selectNode.getName());
			dialog.open();
			// press cancel
			if (dialog.getReturnCode() == 1)
				return;

			// Check same name and rename
			RenameComponent rename = new RenameComponent(TestProject.getMacroDocument().getMacroScript());

			if (rename.isValid(selectNode, dialog.getNewName()) == false) {
				MessageDialog
						.openInformation(m_treeViewer.getControl().getShell(),
								"Warning",
								"Can not rename: there exists anotnher component with the same name.");
			} else {
				rename.rename(selectNode, dialog.getNewName());
			}

			m_treeViewer.refresh();
			return;
		}

		if (select instanceof MacroComponentNode) {
			MacroComponentNode selectNode = (MacroComponentNode) select;
			// rename dialog
			InputNameDialog dialog = new InputNameDialog(m_treeViewer
					.getControl().getShell());
			dialog.setOldName(selectNode.getName());
			dialog.open();
			// press cancel
			if (dialog.getReturnCode() == 1) {
				return;
			}

			// Check same name and rename
			RenameMacroComponent rename = new RenameMacroComponent(TestProject.getMacroDocument().getMacroScript());
			if (rename.isValid(selectNode, dialog.getNewName()) == false) {
				MessageDialog
						.openInformation(m_treeViewer.getControl().getShell(),
								"Warning",
								"Can not rename: there exists anotnher component with the same name.");
			} else {
				// Rename Test Script part
				RenameMacroReferenceVisitor visitor = new RenameMacroReferenceVisitor(
						selectNode, dialog.getNewName());
				visitor.setIsFullPath(false);
				visitor.visit(TestProject.getProject());
				// Rename Macro part
				rename.rename(selectNode, dialog.getNewName());
			}

			m_treeViewer.refresh();
			return;
		}

		if (select instanceof MacroEventNode) {
			MacroEventNode selectNode = (MacroEventNode) select;
			// rename dialog
			InputNameDialog dialog = new InputNameDialog(m_treeViewer
					.getControl().getShell());
			dialog.setOldName(selectNode.getName());
			dialog.open();
			// press cancel
			if (dialog.getReturnCode() == 1) {
				return;
			}

			// Check same name and rename
			RenameMacroEvent rename = new RenameMacroEvent(TestProject.getMacroDocument().getMacroScript());
			if (rename.isValid(selectNode, dialog.getNewName()) == false) {
				MessageDialog
						.openInformation(m_treeViewer.getControl().getShell(),
								"Warning",
								"Can not rename: there exists anotnher event with the same name.");
			} else {
				// Rename Test Script part
				RenameMacroReferenceVisitor visitor = new RenameMacroReferenceVisitor(
						selectNode, dialog.getNewName());
				visitor.setIsFullPath(true);
				visitor.visit(TestProject.getProject());
				// Rename Macro part
				rename.rename(selectNode, dialog.getNewName());
			}

			m_treeViewer.refresh();
			return;
		}

		if (select instanceof FitNode) {
			FitNode selectNode = (FitNode) select;
			// rename dialog
			InputNameDialog dialog = new InputNameDialog(m_treeViewer
					.getControl().getShell());
			dialog.setOldName(selectNode.getName());
			dialog.open();
			// press cancel
			if (dialog.getReturnCode() == 1) {
				return;
			}

			// Check same name and rename
			RenameFitNode rename = new RenameFitNode();
			if (rename.isValid(selectNode, dialog.getNewName()) == false) {
				MessageDialog
						.openInformation(m_treeViewer.getControl().getShell(),
								"Warning",
								"Can not rename: there exists anotnher component with the same name.");
			} else {
				// Rename Test Script part
				RenameMacroReferenceVisitor visitor = new RenameMacroReferenceVisitor(
						selectNode, dialog.getNewName());
				visitor.setIsFullPath(true);
				visitor.visit(TestProject.getProject());
				// Rename Macro part
				rename.rename(selectNode, dialog.getNewName());
			}

			m_treeViewer.refresh();
		}
	}
}
