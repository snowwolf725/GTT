package gttlipse.tabular.util;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.testscript.TestScriptDocument;
import gttlipse.resource.TestFileManager;
import gttlipse.scriptEditor.testScript.CompositeNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.def.TableTag;
import gttlipse.tabular.table.TableModel;

public class TableParser {
	
	private static TestFileManager _manager = new TestFileManager();
	
	public static void parseMacroTable(AbstractMacroNode root, TableModel model) {
		MacroEventNode subRoot = null;
		
		for(int i = 1; i < model.getRowCount(); i++) {
			String[] row = model.getRowItem(i);
			
			// The macro event name cell
			String meName = row[TableConstants.MACRO_EVENT_NAME];
			if (meName.equals(TableConstants.MACRO_HEADER_INDICATOR)) {
				// This is a header row, ignore it
				continue;
			}
			else if (!meName.equals("")) {
				subRoot = (MacroEventNode)TreeStructureUtil.findNode(root, meName, MacroEventNode.class);
				
				if (subRoot != null) {
					// Macro event node
					NodeBuilder.createMacroEventNode(subRoot, row);
					
					// This is an argument row
					continue;
				}
				else {
					// Avoid another errors when the macro event is not existed
					break;
				}
			}
			
			// Identify the name of the node
			String comName = row[TableConstants.COMPONENT_NAME];
			if (comName.equals("")) {
				// Ignore this row
				continue;
			}
			
			// Identify the type of the node
			AbstractMacroNode node = TreeStructureUtil.findNode(root, comName);
			String action = row[TableConstants.ACTION_NAME];
			if (node instanceof MacroComponentNode) {
				if (!action.equals("")) {
					MacroEventNode meNode = (MacroEventNode)TreeStructureUtil.findNode(root, action, MacroEventNode.class);
					
					// The macro event node is not existed
					if (meNode != null) {
						// Macro event caller node
						subRoot.add(NodeBuilder.createMacroEventCallerNode(meNode, row));
					}
				}
			}
			else if (node instanceof ComponentNode) {
				ComponentNode comNode = (ComponentNode)node;
				
				if (action.equals(TableTag.VIEW_ASSERT_NODE)) {
					String method = row[TableConstants.COMPONENT_METHOD];
					
					if (!method.equals("")) {
						// View assert node
						subRoot.add(NodeBuilder.createViewAssertNode(comNode, row));
					}
				}
				else if (!action.equals("")) {
					// Component event node
					subRoot.add(NodeBuilder.createComponentEventNode(comNode, row));
				}
			}
			else if (node instanceof DynamicComponentNode) {
				DynamicComponentNode comNode = (DynamicComponentNode)node;
				
				if (!action.equals("")) {
					// Dynamic component event node
					subRoot.add(NodeBuilder.createDynamicComponentEventNode(comNode, row));
				}
			}
			else if (action.equals(TableTag.SPLIT_DATA_NODE)) {
				// Split data node
				subRoot.add(NodeBuilder.createSplitDataNode(row));
			}
			else {
				// The node is null or its type is unknown
				continue;
			}
		}
	}
	
	public static void parseScriptTable(CompositeNode root, TableModel model) {
		TestMethodNode subRoot = null;
		TestScriptDocument doc = null;
		
		for(int i = 1; i < model.getRowCount(); i++) {
			String[] row = model.getRowItem(i);
			
			// The method name cell
			String methodName = row[TableConstants.TEST_METHOD_NAME];
			if (methodName.equals(TableConstants.SCRIPT_HEADER_INDICATOR)) {
				// This is a header row, ignore it
				continue;
			}
			else if (!methodName.equals("")) {
				// Identify the specific method name
				if (methodName.equals(TableTag.SETUP)) {
					methodName = TableConstants.SETUP;
				}
				else if (methodName.equals(TableTag.TEARDOWN)) {
					methodName = TableConstants.TEARDOWN;
				}
				
				// Find the test method node by its name
				subRoot = (TestMethodNode)root.getChildrenByName(methodName);
				if (subRoot == null) {
					// Avoid another errors when the test method is not existed
					break;
				}
			}
			
			// Identify the name of the node
			String comName = row[TableConstants.COMPONENT_NAME];
			if (comName.equals("")) {
				// Ignore this row
				continue;
			}

			// Find the first test script documentation
			doc = subRoot.getDocAt(0);
			if (doc == null) {
				// Insert a test script document into test method node
				doc = subRoot.addInteractionSequence();
				_manager.addScriptDocument(doc, true);
			}
			
			// Identify the type of the node
			String action = row[TableConstants.ACTION_NAME];
			String type = row[TableConstants.COMPONENT_TYPE];
			String method = row[TableConstants.METHOD_NAME];
			if (action.equals(TableTag.AUT_INFO_NODE)) {
				doc.getScript().add(NodeBuilder.createAUTInfoNode(row));
			}
			else if (action.equals(TableTag.EVENT_NODE)) {
				// Check the class type and method name are empty or not
				if (type.equals("") || method.equals("")) {
					continue;
				}
				
				doc.getScript().add(NodeBuilder.createEventNode(row));
			}
			else if (action.equals(TableTag.VIEW_ASSERT_NODE)) {
				// Check the class type and method name are empty or not
				if (type.equals("") || method.equals("")) {
					continue;
				}
				
				doc.getScript().add(NodeBuilder.createViewAssertNode(row));
			}
			else if (action.equals(TableTag.REF_MACRO_EVENT_NODE)) {
				doc.getScript().add(NodeBuilder.createRefMacroEventNode(row));
			}
			else {
				// Undefined action type
				continue;
			}
		}
	}
}
