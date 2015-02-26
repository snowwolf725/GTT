package testing.eventmodel;

import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingEvent;
import junit.framework.TestCase;

public class SwingEventTest extends TestCase {

	public void testEventInfo() {
		IEvent e = SwingEvent.create(1, "name");
		assertEquals(e.getEventId(), 1);
		assertEquals(e.getName(), "name");
	}


	public void testEquals() {
		SwingEvent e1 = SwingEvent.create(1, "name");
		// e1.setScope("a");
		SwingEvent e2 = SwingEvent.create(1, "name");
		// e2.setScope("a");
		SwingEvent e3 = SwingEvent.create(1, "name");
		// e3.setScope("b");

		assertEquals(e1, e1);
		assertEquals(e2, e2);
		assertEquals(e3, e3);

		assertEquals(e1, e2);
		assertEquals(e2, e1);

		assertFalse(e1.equals(new String("name")));

	}

	public void testSetEventId() {
		SwingEvent e1 = SwingEvent.create(1, "name");
		assertEquals(e1.getEventId(), 1);
		assertEquals(e1.getName(), "name");

		e1.setEventId(2);
		assertEquals(e1.getEventId(), 2);
		assertEquals(e1.getName(), "name");
	}

	public void testSetName() {
		SwingEvent e1 = SwingEvent.create(1, "name");
		assertEquals(e1.getEventId(), 1);
		assertEquals(e1.getName(), "name");

		e1.setName("other");
		assertEquals(e1.getEventId(), 1);
		assertEquals(e1.getName(), "other");
	}

	public void testClone() {
		IEvent e1 = SwingEvent.create(1, "name");
		IEvent e2 = e1.clone();

		assertEquals(e1.getEventId(), e2.getEventId());
		assertEquals(e1.getName(), e2.getName());
		assertEquals(e1.getArguments(), e2.getArguments());
	}

}
