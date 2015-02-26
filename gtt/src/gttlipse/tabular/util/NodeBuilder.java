package gttlipse.tabular.util;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.Assertion;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.MacroNodeFactory;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.testscript.EventNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.tabular.def.TableConstants;

import java.lang.reflect.Method;


public class NodeBuilder {

	public static String DEFAULT_ARG_TYPE = "String";
	public static String DEFAULT_ARG_VALUE = "value";
	
	private static IEventModel _eventModel = EventModelFactory.getDefault();
	
	// Macro nodes begin
	public static MacroEventNode createMacroEventNode(MacroEventNode node, String[] row) {
		// Remove all child nodes from the macro event node
		node.removeAll();
		
		// Remove existing arguments
		Arguments args = node.getArguments();
		args.clear();
		
		// Process arguments of the macro event node
		for(int i = TableConstants.EVENT_ARGUMENT_START; i < row.length; i++) {
			if (row[i].equals("")) {
				continue;
			}
			
			// Create an argument and add it into list 
			Argument arg = Argument.create(DEFAULT_ARG_TYPE, row[i], DEFAULT_ARG_VALUE);
			args.add(arg);
		}
		
		return node;
	}
	
	public static MacroEventCallerNode createMacroEventCallerNode(MacroEventNode node, String[] row) {
		// Bind the macro event caller node with the reference of macro event node 
		MacroEventCallerNode mecNode = new MacroEventCallerNode(node.getPath());
		
		// Process macro event arguments
		Arguments meArgs = node.getArguments();
		Arguments args = mecNode.getArguments();
		for(int i = 0; i < meArgs.size(); i++) {
			Argument meArg = meArgs.get(i);
			
			// Get argument information from argument of MacroEventNode
			Argument arg = Argument.create(meArg.getType(), meArg.getName(), row[TableConstants.EVENT_ARGUMENT_START + i]);
			args.add(arg);
		}
		
		// Set macro event arguments
		mecNode.setArguments(args);
		return mecNode;
	}
	
	public static ComponentEventNode createComponentEventNode(ComponentNode node, String[] row) {
		// Create a component event node and bind the reference of component with it
		ComponentEventNode ceNode = new ComponentEventNode(node);
		
		// Use Component and event name to get Event
		IEvent event = _eventModel.getEvent(node.getComponent(), row[TableConstants.ACTION_NAME]);
		
		if (event != null) {
			// Set event type
			ceNode.setEvent(event.getName(), event.getEventId());
		
			// Process event arguments
			Arguments args = event.getArguments();
			for(int i = 0; i < args.size(); i++) {
				Argument arg = args.get(i);
				arg.setValue(row[TableConstants.EVENT_ARGUMENT_START + i]);
			}
			
			// Set event arguments
			ceNode.setArguments(args);
		}
		
		return ceNode;
	}
	
	public static DynamicComponentEventNode createDynamicComponentEventNode(DynamicComponentNode node, String[] row) {
		// Create a dynamic component event node and bind the reference of component with it
		DynamicComponentEventNode ceNode = new DynamicComponentEventNode(node);
		
		// Use Component and event name to get Event
		IEvent event = _eventModel.getEvent(node.getComponent(), row[TableConstants.ACTION_NAME]);
		
		if (event != null) {
			// Set event type
			ceNode.setEvent(event.getName(), event.getEventId());
		
			// Process event arguments
			Arguments args = event.getArguments();
			for(int i = 0; i < args.size(); i++) {
				Argument arg = args.get(i);
				arg.setValue(row[TableConstants.EVENT_ARGUMENT_START + i]);
			}
			
			// Set event arguments
			ceNode.setArguments(args);
		}
		
		return ceNode;
	}
	
	public static ViewAssertNode createViewAssertNode(ComponentNode node, String[] row) {
		// Create a view assert node and bind the reference of component with it
		ViewAssertNode vaNode = new ViewAssertNode();
		vaNode.setComponentPath(node.getPath().toString());
		
		// Use component type and method name to get Method 
		Method method = ReflectionUtil.getMethodFromFullString(node.getType(), row[TableConstants.COMPONENT_METHOD]);
		
		if (method != null) {
			// Set attributes for the assertion method and value
			Assertion assertion = vaNode.getAssertion();
			assertion.setMethod(method);
			assertion.setValue(row[TableConstants.MACRO_ASSERT_VALUE]);
			
			// Process method arguments
			Object[] argTypes = ReflectionUtil.getArgumentTypes(method);
			Arguments args = assertion.getArguments();
			for(int i = 0; i < argTypes.length; i++) {
				String type = argTypes[i].toString();
				String value = row[TableConstants.METHOD_ARGUMENT_START + i];
				
				// Create Argument by argument type and its value
				Argument arg = Argument.create(type, type, value);
				args.add(arg);
			}
			
			// Set method arguments
			assertion.setArguments(args);
		}
		
		return vaNode;
	}
	
	public static SplitDataNode createSplitDataNode(String[] row) {
		String name = row[TableConstants.COMPONENT_NAME];
		String data = row[TableConstants.SPLIT_DATA];
		String separator = row[TableConstants.SPLIT_SEPARATOR];
		String target = row[TableConstants.SPLIT_TARGET];
		
		// Create a split data node
		SplitDataNode sdNode = new MacroNodeFactory().createSplitDataNode(name, target);
		sdNode.setData(data);
		sdNode.setSeparator(separator);
		
		return sdNode;
	}
	// Macro nodes end
	
	// Test script nodes begin
	public static gtt.testscript.LaunchNode createAUTInfoNode(String[] row) {
		gtt.testscript.LaunchNode autNode = new gtt.testscript.LaunchNode();
		autNode.setClassName(row[TableConstants.CLASS_NAME]);
		autNode.setClassPath(row[TableConstants.CLASS_PATH]);
		return autNode;
	}
	
	public static EventNode createEventNode(String[] row) {
		// Set some attributes of the component
		SwingComponent component = SwingComponent.createDefault();
		component.setName(row[TableConstants.COMPONENT_NAME]);
		component.setTitle(row[TableConstants.WINDOW_TITLE]);
		component.setType(row[TableConstants.COMPONENT_TYPE]);
		
		// Create a event with its id and type
		IEvent event = _eventModel.getEvent(component, row[TableConstants.METHOD_NAME]);
		
		EventNode eventNode = new EventNode(component, event);
		return eventNode;
	}
	
	public static gtt.testscript.ViewAssertNode createViewAssertNode(String[] row) {
		// Set some attributes of the component
		SwingComponent component = SwingComponent.createDefault();
		component.setName(row[TableConstants.COMPONENT_NAME]);
		component.setTitle(row[TableConstants.WINDOW_TITLE]);
		component.setType(row[TableConstants.COMPONENT_TYPE]);
		
		// Set attribute for the assertion
		Method method = ReflectionUtil.getMethodFromFullString(component.getType(), row[TableConstants.METHOD_NAME]);
		Assertion assertion = new Assertion();
		assertion.setMethod(method);
		assertion.setValue(row[TableConstants.SCRIPT_ASSERT_VALUE]);
		
		gtt.testscript.ViewAssertNode vaNode = new gtt.testscript.ViewAssertNode(component, assertion);
		return vaNode;
	}
	
	public static ReferenceMacroEventNode createRefMacroEventNode(String[] row) {
		return new ReferenceMacroEventNode(row[TableConstants.REF_MACRO_EVENT_PATH]);
	}
	// Test script nodes end
}
