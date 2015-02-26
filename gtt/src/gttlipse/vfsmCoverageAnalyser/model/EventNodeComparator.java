package gttlipse.vfsmCoverageAnalyser.model;

import gtt.macro.macroStructure.ComponentEventNode;
import gtt.testscript.EventNode;

public class EventNodeComparator {
	private EventNodeComparator() {
		
	}
	
	public static boolean compare(Object event1, Object event2) {
		if(event1 instanceof EventNode && event2 instanceof EventNode) {
			return compare((EventNode)event1, (EventNode)event2);
		}
		else if(event1 instanceof EventNode && event2 instanceof ComponentEventNode) {
			return compare((EventNode)event1, (ComponentEventNode)event2);
		}
		else if(event1 instanceof ComponentEventNode && event2 instanceof ComponentEventNode) {
			return compare((ComponentEventNode)event1, (ComponentEventNode)event2);
		}
		else if(event1 instanceof ComponentEventNode && event2 instanceof EventNode) {
			return compare((ComponentEventNode)event1, (EventNode)event2);
		}
		
		return false;
	}
	
	private static boolean compare(EventNode event1, EventNode event2) {
		if(event1.getEvent().getEventId() == event2.getEvent().getEventId()
			&& event1.getComponent().getName().equals(event2.getComponent().getName()))
			return true;
		else return false;
	}
	
	private static boolean compare(EventNode event1, ComponentEventNode event2) {
		if(event1.getEvent().getEventId() == event2.getEventID()
			&& event1.getComponent().getName().equals(event2.getComponent().getName()))
			return true;
		else return false;
	}
	
	private static boolean compare(ComponentEventNode event1, ComponentEventNode event2) {
		if(event1.getEventID() == event2.getEventID()
			&& event1.getComponent().getName().equals(event2.getComponent().getName()))
			return true;
		else return false;
	}
	
	private static boolean compare(ComponentEventNode event1, EventNode event2) {
		if(event1.getEventID() == event2.getEvent().getEventId()
			&& event1.getComponent().getName().equals(event2.getComponent().getName()))
			return true;
		else return false;
	}
	
}
