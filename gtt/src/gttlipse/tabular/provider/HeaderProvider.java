package gttlipse.tabular.provider;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.TestProject;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.HeaderAnalyzer;
import gttlipse.tabular.util.TabularUtil;
import gttlipse.tabular.util.TreeStructureUtil;

import java.util.List;


public class HeaderProvider {

	private TableModel _model = null;
	private AbstractMacroNode _root = null;
	
	public HeaderProvider(TableModel model) {
		_model = model;
		_root = TestProject.getMacroDocument().getMacroScript();
	}
	
	private void fill(String[] header) {
		// Insert the customized header into table
		if (_model.getRowCount() == 1) {
			// The beginning state
			_model.addRowItem(TableConstants.FIRST_HEADER_ROW, header);
		}
		else {
			_model.addRow(header);
		}
	}

	private void fill(MacroEventNode node, int index) {
		String header[] = HeaderAnalyzer.analyze((MacroEventNode)node, _model);
		
		// Refresh header
		_model.addRowItem(index, header);
	}
	
	public void fillHeader(MacroEventNode node) {
		// Create customized header by specific strategy
		String header[] = HeaderAnalyzer.analyze(node, _model);
		fill(header);
	}
	
	public void fillHeader(TestMethodNode node) {
		// Create customized header by specific strategy
		String header[] = HeaderAnalyzer.analyze(node, _model);
		fill(header);
	}
	
	public void fillHeader(String name, int selectedRow) {
		AbstractMacroNode node = TreeStructureUtil.findNode(_root, name);
		
		if (node instanceof MacroComponentNode) {
			// Get the name of MacroEventNode which we are interested
			int index = TabularUtil.findHeaderIndex(_model, selectedRow);
			String meName = _model.getContentAt(TableConstants.MACRO_EVENT_NAME, index + 1).toString();
			
			// Get all macro events from MacroComponent
			List<MacroEventNode> children = ((MacroComponentNode)node).getMacroEvents();
			for(MacroEventNode child : children) {
				// Just analyze the interested node
				if (child.getName().equals(meName)) {
					fill((MacroEventNode)child, index);
					break;
				}
			}
		}
		else if (node instanceof MacroEventNode) {
			fill((MacroEventNode)node, TableConstants.FIRST_HEADER_ROW);
		}
	}
}
