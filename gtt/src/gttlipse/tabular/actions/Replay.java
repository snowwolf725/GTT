package gttlipse.tabular.actions;

import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.TestProject;
import gttlipse.tabular.editors.MacroTabularEditor;
import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.util.TabularUtil;
import gttlipse.tabular.util.TreeStructureUtil;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;

public class Replay extends TabularAction {

	@Override
	public void run(IAction action) {
		TabularEditor editor = findActiveTabularEditor();
		
		// Just only process the macro tabular editor
		if (editor instanceof MacroTabularEditor) {
			// Get a node name from the editor's title
			String title = editor.getTitle();
			String name = TabularUtil.getNameFromTitle(title);
			
			// Identify the type of a node(Avoid the name of nodes are the same)
			Class<?> type = null;
			if (title.contains("(")) {
				type = MacroEventNode.class;
			}
			else {
				type = MacroComponentNode.class;
			}
			
			// Find the target(a node) by the name and its type
			AbstractMacroNode root = TestProject.getMacroDocument().getMacroScript();
			AbstractMacroNode node = TreeStructureUtil.findNode(root, name, type);
			
			// Replay the script
			TreeStructureUtil.replayMacroScript(node);
		}
	}
	
	@Override
	public void dispose() {}

	@Override
	public void init(IWorkbenchWindow window) {}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}
}
