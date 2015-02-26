package gttlipse.tabular.provider;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.TestProject;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.def.TableTag;
import gttlipse.tabular.dropDownList.DropDownList;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.TreeStructureUtil;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.editors.KTableCellEditorCombo;
import de.kupzog.ktable.editors.KTableCellEditorComboText;
import de.kupzog.ktable.editors.KTableCellEditorText;

public class DropDownListProvider {

	private TableModel _model = null;
	private DropDownList _list = null;
	private AbstractMacroNode _root = null;
	
	public DropDownListProvider(TableModel model) {
		_model = model;
		_list = new DropDownList();
		_root = TestProject.getMacroDocument().getMacroScript();
	}
	
	private String getComponentName(int row) {
		return _model.getContentAt(TableConstants.COMPONENT_NAME, row).toString();
	}
	
	private String getActionName(int row) {
		return _model.getContentAt(TableConstants.ACTION_NAME, row).toString();
	}
	
	private String getClassName(int row) {
		return _model.getContentAt(TableConstants.COMPONENT_TYPE, row).toString();
	}
	
	public KTableCellEditor getComponentList() {
		KTableCellEditorComboText combo = new KTableCellEditorComboText();
		
		// Insert component names into combo text
		combo.setItems(_list.getComponentList());
		
		return combo;
	}
	
	public KTableCellEditor getEventListOfMacro(int row) {
		KTableCellEditorCombo combo = new KTableCellEditorCombo();
        String compName = getComponentName(row);
        String[] eventList = null;
        
        // Find node by component name
        AbstractMacroNode node = TreeStructureUtil.findNode(_root, compName);
        
        // Check type of the node
        if (node instanceof MacroComponentNode) {
        	eventList = _list.getMacroEventList((MacroComponentNode)node);
        }
        else if (node instanceof ComponentNode) {
        	String[] tempList = _list.getEventListByComponentName(compName);
        	eventList = new String[tempList.length + TableTag.COMPONENT_TAG_LIST.length];
        	
            // Insert some tags on the top
            for(int i = 0; i < TableTag.COMPONENT_TAG_LIST.length; i++) {
            	eventList[i] = TableTag.COMPONENT_TAG_LIST[i];
            }
            
            // Copy events from temporary list
            for(int i = 0; i < tempList.length; i++) {
            	eventList[i + TableTag.COMPONENT_TAG_LIST.length] = tempList[i];
            }
        }
        else if (node instanceof DynamicComponentNode) {
        	eventList = _list.getEventListByComponentName(compName);
        }
        else {
        	eventList = new String[TableTag.NORMAL_TAG_LIST.length];
        	
        	// Insert some tags on the top
            for(int i = 0; i < TableTag.NORMAL_TAG_LIST.length; i++) {
            	eventList[i] = TableTag.NORMAL_TAG_LIST[i];
            }
        }
        
        // Insert event names into combo
        combo.setItems(eventList);
        
		return combo;
	}
	
    public KTableCellEditor getMethodListOfMacro(int row) {
    	String actionName = getActionName(row);
    	
    	if (actionName.equals(TableTag.VIEW_ASSERT_NODE)) {
    		KTableCellEditorCombo combo = new KTableCellEditorCombo();
    		String compName = getComponentName(row);
    		
    		// Insert method names into combo
    		combo.setItems(_list.getMethodListByComponentName(compName));
    		
    		return combo;
    	}
    	
    	return new KTableCellEditorText();
    }
    
    public KTableCellEditor getActionList() {
   	 	KTableCellEditorCombo combo = new KTableCellEditorCombo();
      	 
   	 	// Insert action tags into combo
        combo.setItems(_list.getActionList());
        
        return combo;
    }
    
    public KTableCellEditor getClassList() {
   	 	KTableCellEditorCombo combo = new KTableCellEditorCombo();
   	 
   	 	// Insert class types into combo
        combo.setItems(_list.getClassList());
        
        return combo;
    }
    
    public KTableCellEditor getEventOrMethodListOfScript(int row) {
    	String actionName = getActionName(row);
    	String className = getClassName(row);
    	KTableCellEditorCombo combo = new KTableCellEditorCombo();
    	
    	if (actionName.equals(TableTag.VIEW_ASSERT_NODE)) {
    		// Insert method names into combo
    		combo.setItems(_list.getMethodListByClassName(className));
    	}
    	else {
    		// Insert event names into combo
    		combo.setItems(_list.getEventListByClassName(className));
    	}
    	
    	return combo;
    }
}
