package android4gtt.eventmodel;

import gtt.eventmodel.AbstractEvent;
import gtt.eventmodel.Arguments;
import gtt.eventmodel.IEvent;

public class AndroidEvent extends AbstractEvent {

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

	private AndroidEvent(int id, String name) {
		super(id, name);
		eventID = id;
		evetName = name;
	}

	public static AndroidEvent create(int id, String name) {
		return new AndroidEvent(id, name);
	}

	private Arguments arguments = new Arguments();

	public IEvent clone() {
		AndroidEvent e = create(getEventId(), getName());
		e.setArguments(arguments.clone());

		return e;
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof AndroidEvent))
			return false;

		AndroidEvent e = (AndroidEvent) o;

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
