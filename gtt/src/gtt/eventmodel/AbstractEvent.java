package gtt.eventmodel;

import gtt.eventmodel.swing.SwingEvent;

public class AbstractEvent implements IEvent {
	private int eventID;

	private String evetName;

	public int getEventId() {
		return eventID;
	}

	public void setEventId(int id) {
		eventID = id;
	}

	public String getName() {
		return evetName;
	}

	public void setName(String name) {
		evetName = name;
	}

	public AbstractEvent(int id, String name) {
		eventID = id;
		evetName = name;
	}

	public static IEvent create(int id, String name) {
		return new AbstractEvent(id, name);
	}

	private Arguments arguments = new Arguments();

	public IEvent clone() {
		IEvent e = create(getEventId(), getName());
		e.setArguments(arguments.clone());

		return e;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SwingEvent))
			return false;

		SwingEvent e = (SwingEvent) o;

		return e.getName().equals(evetName) && e.getEventId() == eventID;
	}

	public String toString() {
		StringBuilder result = new StringBuilder(evetName);
		result.append(arguments.toString());
		return result.toString();
	}

	public Arguments getArguments() {
		return arguments;
	}

	public void setArguments(Arguments list) {
		arguments = list.clone();
	}
}
