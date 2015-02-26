package gttlipse.tabular.view;

import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;
import gtt.testscript.LaunchNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.testscript.TestScriptDocument;
import gtt.testscript.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.testScript.BaseNode;
import gttlipse.scriptEditor.testScript.TestCaseNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.def.TableTag;
import gttlipse.tabular.editors.ScriptTabularEditor;
import gttlipse.tabular.editors.TabularEditor;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.NodeParser;

import org.eclipse.ui.PartInitException;


public class ScriptTabularPresenter extends TabularPresenter {

	private TableModel _input = null;
	
	public ScriptTabularPresenter() {}
	
	@Override
	protected void modifyTableModel(Object node) {
		if (node instanceof TestCaseNode) {
			processTestCaseNode((TestCaseNode)node);
		}
	}
	
	@Override
	protected TabularEditor findEditor() {
		ScriptTabularEditor editor = null;
		
		// Initialize the input of the editor
		if (_input == null) {
			_input = new TableModel();
		}
		
		try {
			editor = (ScriptTabularEditor)GTTlipse.getDefault().getWorkbench().getActiveWorkbenchWindow()
			            				          .getActivePage().openEditor(_input, TableConstants.SCRIPT_EDITOR_ID);
			return editor;
		} 
		catch (PartInitException e) {
			e.printStackTrace();
		}
		return editor;
	}
	
	private void processTestCaseNode(TestCaseNode node) {
		BaseNode children[] = node.getChildren();
		
		for(int i = 0; i < children.length; i++) {
			if (children[i] instanceof TestMethodNode) {
				processTestMethodNode((TestMethodNode)children[i]);
			}
		}
	}
	
	private void processTestMethodNode(TestMethodNode node) {
		// Ignore the test method: getMethodName
		if (node.getName().equals(TableConstants.GET_METHOD_NAME)) {
			return;
		}
		
		// Insert the customized header into table
		_headerProvider.fillHeader(node);
		
		// Insert the name of test method node into the first cell
		String name = specifyMethodName(node.getName());
		_row.clear();
		_row.add(name);
		
		// Always get the first test script document
		TestScriptDocument doc = node.getDocAt(0);
		if (doc == null) {
			fillTableModel(_row.toArray());
			return;
		}
		
		// Insert a row which is only test method name into table
		if (!doc.hasChildren()) {
			fillTableModel(_row.toArray());
			return;
		}
		
		// Get children of test script document and process it
		AbstractNode children[] = doc.getChildren();
		for(int i = 0; i < children.length; i++) {
			if (children[i] instanceof LaunchNode) {
				NodeParser.parse(_row, (LaunchNode)children[i]);
			}
			else if (children[i] instanceof ReferenceMacroEventNode) {
				NodeParser.parse(_row, (ReferenceMacroEventNode)children[i]);
			}
			else if (children[i] instanceof ViewAssertNode) {
				NodeParser.parse(_row, (ViewAssertNode)children[i]);
			}
			else if (children[i] instanceof EventNode) {
				NodeParser.parse(_row, (EventNode)children[i]);
			}
			
			// Insert items as a row into table
			fillTableModel(_row.toArray());
			
			// Reset the array
			_row.clear();
			_row.add("");
		}
	}
	
	private String specifyMethodName(String name) {
		if (name.equals(TableConstants.SETUP)) {
			return TableTag.SETUP;
		}
		else if (name.equals(TableConstants.TEARDOWN)) {
			return TableTag.TEARDOWN;
		}
		return name;
	}
}
