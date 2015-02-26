package gttlipse.fit.node.processor;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingModel;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class EventTrigger {

	protected IComponent createComponent(String componentType, String componentName, String title, String windowType) {
		IComponent ic;
		ic = new SwingModel().createComponent(componentType);
		ic.setName(componentName);
		ic.setTitle(title);
		ic.setWinType(windowType);
		return ic;
	}
	
	protected EventNode createEventNode(String componentName, String componentType, String eventName, String title, String windowType) {
		NodeFactory nodeFactory = new NodeFactory();
		IComponent ic = createComponent(componentType, componentName, title, windowType);
		IEvent ie = EventModelFactory.getDefault().getEvent(ic, eventName);
	
		EventNode eventNode = nodeFactory.createEventNode(ic, ie);

		return eventNode;
	}

	protected List<EventNode> createEventNode(String componentName, IComponent ic, List<IEvent> eventList) {
		List<EventNode> eventNodeList = new ArrayList<EventNode>();
		NodeFactory nodeFactory = new NodeFactory();
		ic.setName(componentName);
		Iterator<?> ite = eventList.iterator();
		while(ite.hasNext()) {
			IEvent ie = (IEvent)ite.next();
			eventNodeList.add(nodeFactory.createEventNode(ic, ie));
		}

		return eventNodeList;
	}

	protected void checkArgumentList(Arguments source, Arguments target, int index) {
		StringPreprocessor processor = new StringPreprocessor();
		processor.setArguments(source);
		Iterator<?> ite = target.iterator();
		while(ite.hasNext()) {
			Argument arg = (Argument)ite.next();
			String value = arg.getValue();
			String[] valueList = processor.process(value).split(",");
			if(valueList.length == 1) {
				arg.setValue(valueList[0]);
				continue;
			}
			try {
				arg.setValue(valueList[index]);
			} catch(Exception e) {
				System.err.println(arg.getName() + ":" + arg.getValue() + " ERROR!");
			}
		}
	}

	//每種node都要提供tirgger的方法
	abstract protected boolean trigger();
}
