package gttlipse.tabular.util;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.Assertion;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.SplitDataNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.testscript.EventNode;
import gtt.testscript.ReferenceMacroEventNode;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.tabular.def.TableTag;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class NodeParser {

	public static int EVENT = 0;
	public static int METHOD = 1;
	
	// Macro nodes begin
	public static void parse(ArrayList<String> items, MacroEventNode node) {
		items.add(node.getName());
		items.add(TableTag.ARG_COMPONENT);
		items.add(TableTag.ARG_ACTION);
		
		// Process event arguments
		parseArguments(items, node.getArguments(), EVENT);
	}
	
	public static void parse(ArrayList<String> items, ComponentEventNode node) {
		items.add(node.getName());
		items.add(node.getEventType());
		
		// Process event arguments
		parseArguments(items, node.getArguments(), EVENT);
	}
	
	public static void parse(ArrayList<String> items, DynamicComponentEventNode node) {
		items.add(node.getName());
		items.add(node.getEventType());
		
		// Process event arguments
		parseArguments(items, node.getArguments(), EVENT);
	}
	
	public static void parse(ArrayList<String> items, MacroEventCallerNode node) {
		items.add(node.getComponentName());
		items.add(node.getEventName());
		
		// Process macro event arguments
		parseArguments(items, node.getArguments(), EVENT);
	}
	
	public static void parse(ArrayList<String> items, ViewAssertNode node) {
		Assertion assertion = node.getAssertion();
		Method method = assertion.getMethod();
		String methodName = "";
		
		// Check the Method is null or not
		if (method != null) {
			methodName = ReflectionUtil.getFullStringOfMethod(method);
		}
		
		items.add(node.getComponent().getName());
		items.add(TableTag.VIEW_ASSERT_NODE);
		items.add(assertion.getValue());
		items.add(methodName);
		
		// Process method arguments
		parseArguments(items, assertion.getArguments(), METHOD);
	}
	
	private static void parseArguments(ArrayList<String> items, Arguments args, int type) {
		for(int i = 0; i < args.size(); i++) {
			Argument arg = args.get(i);
			String value = arg.getValue();
			
			// When the argument value is not existed or default value("value"),
			// insert the argument name or type
			if (value.equals("") || value.equals(NodeBuilder.DEFAULT_ARG_VALUE)) {
				if (type == EVENT) {
					items.add(arg.getName());
				}
				else if (type == METHOD) {
					items.add(arg.getType());
				}
				else {
					items.add("");
				}
			}
			else {
				items.add(value);
			}
		}
	}
	
	public static void parse(ArrayList<String> items, LaunchNode node) {
		items.add(TableTag.LAUNCH_NODE);
	}
	
	public static void parse(ArrayList<String> items, SplitDataNode node) {
		items.add(node.getName());
		items.add(TableTag.SPLIT_DATA_NODE);
		items.add(node.getData());
		items.add(node.getSeparator());
		items.add(node.getTarget());
	}
	// Macro nodes end
	
	// Test script nodes begin
	public static void parse(ArrayList<String> items, gtt.testscript.LaunchNode node) {
		items.add(node.getClassName());
		items.add(TableTag.AUT_INFO_NODE);
		items.add(node.getClassPath());
	}
	
	public static void parse(ArrayList<String> items, ReferenceMacroEventNode node) {
		items.add(node.getRefPath());
		items.add(TableTag.REF_MACRO_EVENT_NODE);
	}
	
	public static void parse(ArrayList<String> items, gtt.testscript.ViewAssertNode node) {
		Assertion assertion = node.getAssertion();
		Method method = assertion.getMethod();
		String methodName = "";
		
		// Check the Method is null or not
		if (method != null) {
			methodName = ReflectionUtil.getFullStringOfMethod(method);
		}
		
		items.add(node.getComponent().getName());
		items.add(TableTag.VIEW_ASSERT_NODE);
		items.add(node.getComponent().getTitle());
		items.add(node.getComponent().getType());
		items.add(methodName);
		items.add(assertion.getValue());
	}
	
	public static void parse(ArrayList<String> items, EventNode node) {
		items.add(node.getComponent().getName());
		items.add(TableTag.EVENT_NODE);
		items.add(node.getComponent().getTitle());
		items.add(node.getComponent().getType());
		items.add(node.getEvent().getName());
	}
	// Test script nodes end
}
