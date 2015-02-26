package gttlipse.tabular.dropDownList;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.macro.visitor.MacroFindingVisitor;
import gtt.util.refelection.ReflectionUtil;
import gttlipse.TestProject;
import gttlipse.tabular.def.TableTag;
import gttlipse.tabular.util.TreeStructureUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class DropDownList {

	private AbstractMacroNode _root = TestProject.getMacroDocument().getMacroScript();
	private IEventModel _eventModel = EventModelFactory.getDefault();
	private ArrayList<String> _list = new ArrayList<String>();
	private MacroFindingVisitor _visitor = new MacroFindingVisitor();
	private Class<?> _class = null;
	private List<Method> _methods = null;
	private List<IComponent> _components = null;
	
	public DropDownList() {}
	
	public String[] getEventListByClassName(String className) {
		// Reset list
		_list.clear();
		
		// Create a Component by class name and use it to get all events
		SwingComponent component = SwingComponent.create(className);
		List<IEvent> events = _eventModel.getEvents(component);
		
		// Insert all event names into list
		for(IEvent event : events) {
			_list.add(event.getName());
		}
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	public String[] getEventListByComponentName(String componentName) {
		// Reset list
		_list.clear();
		
		// Use component name to find the ComponentNode
		AbstractMacroNode node = TreeStructureUtil.findNode(_root, componentName);
		
		if (node instanceof ComponentNode) {
			ComponentNode comNode = (ComponentNode)node;
			return getEventListByClassName(comNode.getComponent().getType());
		}
		
		if (node instanceof DynamicComponentNode) {
			DynamicComponentNode comNode = (DynamicComponentNode)node;
			return getEventListByClassName(comNode.getComponent().getType());
		}
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	public String[] getMethodListByClassName(String className) {
		// Reset list
		_list.clear();
		
		try {
			_class = Class.forName(className);
			
			// According the specific Class and prefix to fetch Methods
			_methods = ReflectionUtil.getMethodsStartsWith(_class, "get");
			
			// Insert all method names into list
			for(Method method : _methods) {
				_list.add(ReflectionUtil.getFullStringOfMethod(method));
			}
			
			// According the specific Class and prefix to fetch Methods
			_methods = ReflectionUtil.getMethodsStartsWith(_class, "is");
			
			// Insert all method names into list
			for(Method method : _methods) {
				_list.add(ReflectionUtil.getFullStringOfMethod(method));
			}
			
			// According the specific Class and prefix to fetch Methods
			_methods = ReflectionUtil.getMethodsStartsWith(_class, "others");
			
			// Insert all method names into list
			for(Method method : _methods) {
				_list.add(ReflectionUtil.getFullStringOfMethod(method));
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	public String[] getMethodListByComponentName(String componentName) {
		// Reset list
		_list.clear();
		
		// Use component name to find the ComponentNode
		AbstractMacroNode node = TreeStructureUtil.findNode(_root, componentName);
		
		if (node instanceof ComponentNode) {
			ComponentNode comNode = (ComponentNode)node;
			return getMethodListByClassName(comNode.getType());
		}
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	public String[] getActionList() {
		// Reset list
		_list.clear();
		
		// Insert action tag into list
		_list.add(TableTag.AUT_INFO_NODE);
		_list.add(TableTag.REF_MACRO_EVENT_NODE);
		_list.add(TableTag.EVENT_NODE);
		_list.add(TableTag.VIEW_ASSERT_NODE);
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	public String[] getClassList() {
		_components = _eventModel.getComponents();
		
		// Reset list
		_list.clear();
		
		// Insert all class types into list
		for(IComponent component : _components) {
			_list.add(component.getType());
		}
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	public String[] getMacroEventList(MacroComponentNode node) {
		// Reset list
		_list.clear();
		
		// Use MacroComponentNode to get all MacroEventNode
		if (node != null) {
			List<MacroEventNode> events = node.getMacroEvents();
			
			// Insert all macro event name into list
			for(MacroEventNode macroEventNode : events) {
				_list.add(macroEventNode.getName());
			}
		}
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
	
	public String[] getComponentList() {
		// Reset list
		_list.clear();
		
		// Collect all component nodes and macro component nodes by visitor
		_root.accept(_visitor);
		_list = _visitor.getResultList();
		
		return (String[])_list.toArray(new String[_list.size()]);
	}
}
