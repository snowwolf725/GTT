package gttlipse.fit.node.processor;

import gtt.tester.swing.ITester;
import gtt.testscript.EventNode;
import gttlipse.fit.node.EventTriggerNode;

import java.util.Iterator;
import java.util.List;


public class EventTriggerNodeProcessor extends EventTrigger {
	EventTriggerNode m_eventTriggerNode;
	String[] m_componentNameList;
	String[] m_eventDataList;
	ITester m_tester;
	ComponentNamePool m_componentNamePool;
	
	public EventTriggerNodeProcessor(ITester tester, ComponentNamePool pool) {
		m_tester = tester;
		m_componentNamePool = pool;
	}
	
	public boolean process(EventTriggerNode eventTriggerNode) {
		if(eventTriggerNode == null)
			return false;
		m_eventTriggerNode = eventTriggerNode;
		if(m_eventTriggerNode.getArguments().getValue(m_eventTriggerNode.getData()) != null)
			m_eventDataList = m_eventTriggerNode.getArguments().getValue(m_eventTriggerNode.getData()).split(ProcessorDefinition.DATASPLIT);
		String key = getNodeNameByPath(eventTriggerNode.getGenerationKey());
		m_componentNameList = m_componentNamePool.getComponentNameList(key);
		return trigger();
	}

	public boolean trigger() {
		boolean result = true;
		try {
			m_tester.setSleepTime(ProcessorDefinition.SLEEPTIME);
			for(int i = 0; i < m_componentNameList.length; i++) {
				List<EventNode> eventNodeList = createEventNode(m_componentNameList[i], m_eventTriggerNode.getIComponent(), m_eventTriggerNode.getEventList());
				Iterator<?> ite = eventNodeList.iterator();
				while(ite.hasNext()) {
					EventNode node = (EventNode)ite.next();
					checkArgumentList(m_eventTriggerNode.getArguments(), node.getEvent().getArguments(), i);
					result &= m_tester.fire(node);
				}
			}
			return result;
		}
		catch(NullPointerException e) {
			System.err.println("Didn't get componentList");
			return false;
		}
	}
	
	private String getNodeNameByPath(String path) {
		int begin = path.lastIndexOf("::");
		if(begin == -1)
			return path;
		String temp = path.substring(begin + 2);
		return temp;
	}
}
