package gttlipse.tabular.provider;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.TestProject;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.def.TableTag;
import gttlipse.tabular.table.TableModel;
import gttlipse.tabular.util.TreeStructureUtil;

public class ArgumentProvider {

	private TableModel _model = null;
	private AbstractMacroNode _root = null;
	private IEventModel _eventModel = null;
	
	public static int EVENT = 0;
	public static int METHOD = 1;
	
	public ArgumentProvider(TableModel model) {
		_model = model;
		_root = TestProject.getMacroDocument().getMacroScript();
		_eventModel = EventModelFactory.getDefault();
	}
	
	private String getComponentName(int row) {
		return _model.getContentAt(TableConstants.COMPONENT_NAME, row).toString();
	}
	
	private String getActionName(int row) {
		return _model.getContentAt(TableConstants.ACTION_NAME, row).toString();
	}
	
	private String getMethodName(int row) {
		return _model.getContentAt(TableConstants.COMPONENT_METHOD, row).toString();
	}
	
	private void increaseColumn(int expect) {
		int real = _model.getColumnCount();
		
		while(real < expect) {
			_model.addColumn();
			
			// Get column count after adding a column
			real = _model.getColumnCount();
		}
		_model.initialize();
	}
	
	private void fill(Object[] args, int row, int start) {
		// Copy the tip which is name or type of argument
		for(int i = 0; i < args.length; i++) {
			_model.setContentAt(start + i, row, args[i]);
		}
		
		// Clear the additional table cells
		for(int i = start + args.length; i < _model.getColumnCount(); i++) {
			_model.setContentAt(i, row, "");
		}
	}
	
	private boolean isLegalActionName(int type, String actionName) {
		if (type == EVENT) {
			return (!actionName.equals(TableTag.VIEW_ASSERT_NODE))
					&& (!actionName.equals(TableTag.SPLIT_DATA_NODE))
					&& (!actionName.equals(""));
		}
		else if (type == METHOD) {
			return (actionName.equals(TableTag.VIEW_ASSERT_NODE));
		}
		
		return false;
	}
	
	public void fillArgumentsOfMethod(int row) {
    	String compName = getComponentName(row);
    	String actionName = getActionName(row);
    	String methodName = getMethodName(row);
    	
    	if (isLegalActionName(METHOD, actionName)) {
    		// Find node by component name
    		ComponentNode node = (ComponentNode)TreeStructureUtil.findNode(_root, compName, ComponentNode.class);
    		
    		if (node != null && !methodName.equals("")) {
    			Object[] args = ReflectionUtil.getArgumentTypes(node.getType(), methodName);
    			
    			// Add columns
    			int size = TableConstants.METHOD_ARGUMENT_START + args.length;
    			increaseColumn(size);
    			
    			// Fill with arguments
    			fill(args, row, TableConstants.METHOD_ARGUMENT_START);
    		}
    	}
	}
	
	public void fillArgumentsOfEvent(int row) {
    	String compName = getComponentName(row);
    	String actionName = getActionName(row);
    	Object[] args = null;
    	
    	if (isLegalActionName(EVENT, actionName)) {
    		// Find node by component name
    		AbstractMacroNode node = TreeStructureUtil.findNode(_root, compName);
    		
    		if (node instanceof ComponentNode) {
    			IEvent event = _eventModel.getEvent(((ComponentNode)node).getComponent(), actionName);
    			args = event.getArguments().names().toArray();
    		}
    		else if (node instanceof DynamicComponentNode) {
    			IEvent event = _eventModel.getEvent(((DynamicComponentNode)node).getComponent(), actionName);
    			args = event.getArguments().names().toArray();
    		}
    		else if (node instanceof MacroComponentNode) {
    			// Find node by action name(name of MacroEvent)
    			MacroEventNode meNode = (MacroEventNode)TreeStructureUtil.findNode(_root, actionName, MacroEventNode.class);
    			args = meNode.getArguments().names().toArray();
    		}
    		
    		// Add columns
			int size = TableConstants.EVENT_ARGUMENT_START + args.length;
			increaseColumn(size);
			
			// Fill with arguments
			fill(args, row, TableConstants.EVENT_ARGUMENT_START);
    	}
	}
}
