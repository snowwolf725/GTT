package testing.testscript;

import gtt.eventmodel.Arguments;
import gtt.eventmodel.IEvent;



/**
 * mock object for IEvent
 * 
 * @author zwshen
 * 
 */
public class MockEvent implements IEvent {

	public IEvent clone() {
		return this;
	}

	public int getEventId() {
		return 0;
	}

	public final static String NAME = "NAME";

	public String getName() {
		return NAME;
	}
	
	public static final String TO_STRING = "TO_STRING";

	public String toString() {
		return TO_STRING;
	}


	Arguments m_arglist = new Arguments();
	public Arguments getArguments() {
		return m_arglist;
	}

	public void setArguments(Arguments list) {
		m_arglist = list;
	}

}
