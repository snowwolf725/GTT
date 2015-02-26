package gttlipse.tabular.view;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.editors.MacroTabularEditor;
import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.NodeParser;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;


public class MacroTabularPresenter extends TabularPresenter {

	public MacroTabularPresenter() {}

	@Override
	protected void modifyTableModel(Object node) {
		if (node instanceof MacroComponentNode) {
			processMacroComponentNode((MacroComponentNode)node);
		}
		else if (node instanceof MacroEventNode) {
			processMacroEventNode((MacroEventNode)node);
		}
	}
	
	@Override
	protected TabularEditor findEditor() {
		MacroTabularEditor editor = null;
		
		try {
			// Get editors' references
			IEditorReference editors[] = GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow()
										    	 .getActivePage().getEditorReferences();
			
			// Find the Macro Table Editor
			for(int i = 0; i < editors.length; i++) {
				if(editors[i].getEditor(true) instanceof MacroTabularEditor) {
					editor = (MacroTabularEditor)editors[i].getEditor(true);
					
					// The editor title equals the specified title
					if (editor.getTitle().equals(_title)) {
						GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow()
					    					 .getActivePage().activate(editor);
						return editor;
					}
				}
			}
			
			// Not found the specified title of editor
			editor = (MacroTabularEditor)GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow()
									             .getActivePage().openEditor(new TableModel(), TableConstants.MACRO_EDITOR_ID);
		}
		catch (PartInitException e) {
			e.printStackTrace();
		}
		return editor;
	}
	
	private void processMacroComponentNode(MacroComponentNode node) {
		for(int i = 0; i < node.size(); i++) {
			AbstractMacroNode child = node.get(i);
			
			if (child instanceof MacroEventNode) {
				processMacroEventNode((MacroEventNode)child);
			}
		}
	}
	
	private void processMacroEventNode(MacroEventNode node) {
		// Insert the customized header into table
		_headerProvider.fillHeader(node);
		
		// Insert the name and arguments of macro event node
		NodeParser.parse(_row, (MacroEventNode)node);
		fillTableModel(_row.toArray());
		
		// Reset the list
		_row.clear();
		
		// Process the child nodes
		for(int i = 0; i < node.size(); i++) {
			AbstractMacroNode child = node.get(i);
			
			// Insert an empty cell
			_row.add("");
			
			if (child instanceof ComponentEventNode) {
				NodeParser.parse(_row, (ComponentEventNode)child);
			}
			else if (child instanceof DynamicComponentEventNode) {
				NodeParser.parse(_row, (DynamicComponentEventNode)child);
			}
			else if (child instanceof MacroEventCallerNode) {
				NodeParser.parse(_row, (MacroEventCallerNode)child);
			}
			else if (child instanceof ViewAssertNode) {
				NodeParser.parse(_row, (ViewAssertNode)child);
			}
			else if (child instanceof LaunchNode) {
				NodeParser.parse(_row, (LaunchNode)child);
			}
			else if (child instanceof SplitDataNode) {
				NodeParser.parse(_row, (SplitDataNode)child);
			}
			
			// Insert items as a row into table
			fillTableModel(_row.toArray());
			
			// Reset the list
			_row.clear();
		}
	}
}
