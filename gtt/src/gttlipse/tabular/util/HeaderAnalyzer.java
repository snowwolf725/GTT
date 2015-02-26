package gttlipse.tabular.util;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.macroStructure.ViewAssertNode;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.tabular.def.TableConstants;
import gttlipse.tabular.table.TableModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import gttlipse.TestProject;

public class HeaderAnalyzer {

	public static String KEY_SEPARATOR = "::";
	public static String ARGUMENT = "Arg";
	public static String METHOD_INDICATOR = "(";
	public static String MACRO_EVENT_CALLER = "MacroEventCaller";
	
	public static int CLASS_TYPE = 0;
	public static int METHOD_NAME = 1;
	
	private static IEventModel _eventModel = EventModelFactory.getDefault();
	private static HashMap<String, Integer> _map = new HashMap<String, Integer>();
	private static AbstractMacroNode root = TestProject.getMacroDocument().getMacroScript();
	
	public static String[] createSpecialHeader(String[] defaultArgs, String[] eventArgs, int colCount) {
		String header[] = null;
		
		// According real size to initialize header array
		int size = defaultArgs.length + eventArgs.length;
		if (size > colCount) {
			header = TabularUtil.createStringArray(size);
		}
		else {
			header = TabularUtil.createStringArray(colCount);
		}
		
		// Copy default header
		for(int i = 0; i < defaultArgs.length; i++) {
			header[i] = defaultArgs[i];
		}
		
		// Fill with event arguments
		int index = defaultArgs.length;
		for(int i = 0; i < eventArgs.length; i++) {
			header[index++] = eventArgs[i];
		}
		
		// Fill with "Arg1", "Arg2" and so on
		int num = 1;
		for(int i = size; i < header.length; i++) {
			header[index++] = ARGUMENT + num++;
		}
		
		return header;
	}
	
	public static String[] createDefaultHeader(String[] defaultArgs, int colCount) {
		String[] header = TabularUtil.createStringArray(colCount);

		// Copy default header
		for(int i = 0; i < defaultArgs.length; i++) {
			header[i] = defaultArgs[i];
		}
		
		// Fill with "Arg1", "Arg2" and so on
		int num = 1;
		for(int i = defaultArgs.length; i < colCount; i++) {
			header[i] = ARGUMENT + num++;
		}
		
		return header;
	}
	
	public static String[] strategy(MacroEventNode node) {
		// Insert event name and its amount into hash table 
		for(int i = 0; i < node.size(); i++) {
			AbstractMacroNode child = node.get(i);
			Object key = "";
			
			// Only analyze 'ComponentEventNode', 'ViewAsertNode' and 'MacroEventCallerNode'
			if (child instanceof ComponentEventNode) {
				ComponentEventNode ceNode = (ComponentEventNode)child;
				key = produceKey(ceNode.getComponent().getType(), ceNode.getEventType());
			}
			else if (child instanceof ViewAssertNode) {
				ViewAssertNode vaNode = (ViewAssertNode)child;
				key = produceKey(vaNode.getComponent().getType(), vaNode.getAssertion().getFullMethodName());
			}
			else if (child instanceof MacroEventCallerNode) {
				MacroEventCallerNode mecNode = (MacroEventCallerNode)child;
				key = produceKey(MACRO_EVENT_CALLER, mecNode.getName());
			}
			else {
				// Another type, ignore it
				continue;
			}
			
			// When the key is an empty string, ignore it
			if (key.equals("")) {
				continue;
			}
			
			// The event name is existed in hash table
			int count = 1;
			if (_map.containsKey(key)) {
				count = _map.get(key) + 1;
			}
			_map.put(key.toString(), new Integer(count));
		}
		
		// Choose the highest priority type-name pair
		String[] typeNamePair = chooseBestFit().split(KEY_SEPARATOR);
		
		// Use specific component and event name to get arguments
		return getArgumentList(typeNamePair);
	}
	
	private static String produceKey(String classType, String name) {
		if (classType.equals("") || name.equals("")) {
			return "";
		}
		
		return classType + KEY_SEPARATOR + name;
	}
	
	private static String chooseBestFit() {
		int max = 0;
		Object target = null;
		
		// Choose a highest priority event from hash table
		for(Iterator<String> point = _map.keySet().iterator(); point.hasNext();) {
			Object key = point.next();
			Integer value = _map.get(key);
			
			if (value > max) {
				max = value;
				target = key;
			}
		}
		
		return target.toString();
	}
	
	private static String[] getArgumentList(String[] pair) {
		String classType = pair[CLASS_TYPE];
		String methodName = pair[METHOD_NAME];
		
		if (methodName.contains(METHOD_INDICATOR)) {
			// Assertion method
			Method method = ReflectionUtil.getMethodFromFullString(classType, methodName);
			Class<?>[] types = method.getParameterTypes();
			ArrayList<String> typeList = new ArrayList<String>();
			
			// Insert additional assertion information into list 
			typeList.add(TableConstants.ASSERTION_VALUE);
			typeList.add(TableConstants.ASSERTION_METHOD);
			
			// Copy argument type to typeList
			for(int i = 0; i < types.length; i++) {
				typeList.add(types[i].getSimpleName());
			}
			
			return (String[])typeList.toArray(new String[typeList.size()]);
		}
		else if (classType.equals(MACRO_EVENT_CALLER)) {
			// Macro event caller
			MacroEventNode node = (MacroEventNode)TreeStructureUtil.findNode(root, methodName, MacroEventNode.class);
			Arguments args = node.getArguments();
			
			return (String[])args.names().toArray(new String[args.names().size()]);
		}
		else {
			// Component event
			SwingComponent component = SwingComponent.create(classType);
			IEvent event = _eventModel.getEvent(component, methodName);
			Arguments args = event.getArguments();
			
			return (String[])args.names().toArray(new String[args.names().size()]);
		}
	}
	
	public static String[] analyze(MacroEventNode node, TableModel model) {
		int colCount = model.getColumnCount();
		String[] defaultArgs = TableConstants.DEFAULT_MACRO_HEADER;
		String[] header = createDefaultHeader(defaultArgs, colCount);
		
		// Identify the auto state
		if (!node.isAutoParsing() || !node.hasChildren()) {
			return header;
		}
	
		// Reset hash table
		_map.clear();
		
		// According specific strategy to get arguments
		String[] eventArgs = strategy(node);
		if (eventArgs != null) {
			header = createSpecialHeader(defaultArgs, eventArgs, colCount);
		}
		
		return header;
	}
	
	public static String[] analyze(TestMethodNode node, TableModel model) {
		int colCount = model.getColumnCount();
		String[] defaultArgs = TableConstants.DEFAULT_SCRIPT_HEADER;
		String[] header = createDefaultHeader(defaultArgs, colCount);
		
		return header;
	}
}
