package gttlipse.tabular.actions;

import gtt.macro.macroStructure.AbstractMacroNode;
import gttlipse.GTTlipse;
import gttlipse.TestProject;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.tabular.editors.MacroTabularEditor;
import gttlipse.tabular.editors.ScriptTabularEditor;
import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.TableParser;
import gttlipse.tabular.util.TabularUtil;
import gttlipse.tabular.util.TreeStructureUtil;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;


public class Synchronize extends TabularAction {

	public Synchronize() {}
	
	@Override
	public void run() {
		TabularEditor editor = findActiveTabularEditor();
		
		// Identify editor's type
		if (editor instanceof MacroTabularEditor) {
			AbstractMacroNode root = TestProject.getMacroDocument().getMacroScript();
			TableModel model = editor.getTableModel();
			
			// Parse the macro script table
			TableParser.parseMacroTable(root, model);
			
			// Refresh and expand the tree viewer
			String nodeName = TabularUtil.getNameFromTitle(editor.getTitle());
			TreeStructureUtil.refresh(TreeStructureUtil.findNode(root, nodeName));
		}
		else if (editor instanceof ScriptTabularEditor) {
			CompositeNode root = TestProject.getProject();
			TableModel model = editor.getTableModel();
			TestCaseNode testCase = TreeStructureUtil.findTestCaseNode(root, editor.getTitle());
			
			// Remove children of the test case node(except the "getMethodName" node)
			TreeStructureUtil.removeChildren(testCase);
			
			// Parse the test script table
			TableParser.parseScriptTable(testCase, model);
			
			// Refresh the tree viewer
			GTTlipse.findScriptView().getTreeViewer().refresh();
		}
	}
	
	@Override
	public void run(IAction action) {
		run();
	}
	
	@Override
	public void dispose() {}

	@Override
	public void init(IWorkbenchWindow window) {}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}
}
