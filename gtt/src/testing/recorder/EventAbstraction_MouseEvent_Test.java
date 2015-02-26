package testing.recorder;

import gtt.recorder.CaptureData;
import gtt.recorder.EventAbstracter;
import gtt.recorder.atje.AbstractEvent;
import gtt.recorder.atje.CenterMouseEventData;
import gtt.recorder.atje.MouseEventData;
import gtt.tester.swing.SwingTesterTag;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import junit.framework.TestCase;

public class EventAbstraction_MouseEvent_Test extends TestCase {

	EventAbstracter m_abstraction;

	final long MOUSE_MASK = MouseEvent.MOUSE_EVENT_MASK;
	final int click = 1;

	long record_time = 1000;

	private long nextRecordTime() {
		record_time += 1000; // 每次遞增 1000ms
		return record_time; // += 1000;
	}

	protected void setUp() throws Exception {
		m_abstraction = new EventAbstracter();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		m_abstraction = null;
		super.tearDown();
	}

	// 建立 AWT Mouse Event
	private MouseEvent createMouseEvent(Component c, int event_id) {
		return new MouseEvent(c, event_id, MOUSE_MASK, c.getX(), c.getY(),
				click, 0, false);
	}

	private List<CaptureData> creaeMouseEventList() {
		List<CaptureData> result = new LinkedList<CaptureData>();

		JFrame frame = new JFrame();
		frame.setName("jframe");
		JButton btn1, btn2;

		btn1 = new JButton("1");
		btn1.setName("btn1");
		frame.getContentPane().add(btn1);

		btn2 = new JButton("2");
		btn2.setName("btn2");
		frame.getContentPane().add(btn2);

		frame.pack();

		MouseEvent me;
		// click 1
		me = createMouseEvent(btn1, MouseEvent.MOUSE_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createMouseEvent(btn1, MouseEvent.MOUSE_RELEASED);
		// click 1
		result.add(new CaptureData(me, nextRecordTime()));
		me = createMouseEvent(btn1, MouseEvent.MOUSE_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createMouseEvent(btn1, MouseEvent.MOUSE_RELEASED);
		result.add(new CaptureData(me, nextRecordTime()));

		// click 2
		me = createMouseEvent(btn2, MouseEvent.MOUSE_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createMouseEvent(btn2, MouseEvent.MOUSE_RELEASED);
		result.add(new CaptureData(me, nextRecordTime()));
		// click 2
		me = createMouseEvent(btn2, MouseEvent.MOUSE_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createMouseEvent(btn2, MouseEvent.MOUSE_RELEASED);
		result.add(new CaptureData(me, nextRecordTime()));

		return result;
	}

	public void test_abstraction() {
		List<CaptureData> list = creaeMouseEventList();
		List<AbstractNode> nodes = m_abstraction.abstraction(list);

		assertNotNull(nodes);
		assertEquals(nodes.size(), 4); // 4 個 event nodes

		assertTrue(nodes.get(0) instanceof EventNode);
		assertTrue(nodes.get(1) instanceof EventNode);
		assertTrue(nodes.get(2) instanceof EventNode);
		assertTrue(nodes.get(3) instanceof EventNode);
		EventNode en;

		en = (EventNode) nodes.get(0);
		assertEquals(en.getComponent().getName(), "btn1");
		assertEquals(en.getComponent().getType(), "javax.swing.JButton");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.PUSH);

		en = (EventNode) nodes.get(1);
		assertEquals(en.getComponent().getName(), "btn1");
		assertEquals(en.getComponent().getType(), "javax.swing.JButton");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.PUSH);

		en = (EventNode) nodes.get(2);
		assertEquals(en.getComponent().getName(), "btn2");
		assertEquals(en.getComponent().getType(), "javax.swing.JButton");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.PUSH);

		en = (EventNode) nodes.get(3);
		assertEquals(en.getComponent().getName(), "btn2");
		assertEquals(en.getComponent().getType(), "javax.swing.JButton");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.PUSH);
	}

	public void test_highLevel() {
		List<CaptureData> list = creaeMouseEventList();
		List<AbstractEvent> ae = m_abstraction.lowLevel(list);
		ae = m_abstraction.mediumLevel(ae);
		assertNotNull(ae);
		assertEquals(ae.size(), 4); // 8 個事件減成4個click事件

		// 測試高階抽象化： AbstractEvent 變成 EventNode
		List<AbstractNode> nodes = m_abstraction.highLevel(ae);
		assertNotNull(nodes);
		assertEquals(nodes.size(), 4); // 4 個 event nodes
	}

	public void test_mediumLevel() {
		List<CaptureData> list = creaeMouseEventList();
		List<AbstractEvent> ae = m_abstraction.lowLevel(list);

		// 測試中階抽象化： press + release 變成 click
		ae = m_abstraction.mediumLevel(ae);

		assertNotNull(ae);
		assertEquals(ae.size(), 4); // 8 個事件減成4個click事件

		assertNotNull(ae.get(0).getComponentData());
		assertNotNull(ae.get(0).getComponentData().getName(), "btn1");
		assertEquals(ae.get(0).getEventID(), MouseEvent.MOUSE_CLICKED); // button1
		assertTrue(ae.get(0) instanceof MouseEventData); // button 1
		assertTrue(ae.get(0) instanceof CenterMouseEventData); // button 1

		assertNotNull(ae.get(1).getComponentData());
		assertNotNull(ae.get(1).getComponentData().getName(), "btn1");
		assertEquals(ae.get(1).getEventID(), MouseEvent.MOUSE_CLICKED); // button1
		assertTrue(ae.get(1) instanceof MouseEventData); // button 1
		assertTrue(ae.get(1) instanceof CenterMouseEventData); // button 1

		assertNotNull(ae.get(2).getComponentData());
		assertNotNull(ae.get(2).getComponentData().getName(), "btn2");
		assertEquals(ae.get(2).getEventID(), MouseEvent.MOUSE_CLICKED); // button1
		assertTrue(ae.get(2) instanceof MouseEventData); // button 1
		assertTrue(ae.get(2) instanceof CenterMouseEventData); // button 1

		assertNotNull(ae.get(3).getComponentData());
		assertNotNull(ae.get(3).getComponentData().getName(), "btn2");
		assertEquals(ae.get(3).getEventID(), MouseEvent.MOUSE_CLICKED); // button1
		assertTrue(ae.get(3) instanceof MouseEventData); // button 1
		assertTrue(ae.get(3) instanceof CenterMouseEventData); // button 1
	}

	public void test_lowLevel() {
		// 測試低階抽像化
		List<CaptureData> list = creaeMouseEventList();
		List<AbstractEvent> ae = m_abstraction.lowLevel(list);

		assertNotNull(ae);
		assertEquals(ae.size(), 8);

		assertNotNull(ae.get(0).getComponentData());
		assertNotNull(ae.get(0).getComponentData().getName(), "btn1");
		assertEquals(ae.get(0).getEventID(), MouseEvent.MOUSE_PRESSED); // button

		assertNotNull(ae.get(1).getComponentData());
		assertNotNull(ae.get(1).getComponentData().getName(), "btn1");
		assertEquals(ae.get(1).getEventID(), MouseEvent.MOUSE_RELEASED); // button

		assertNotNull(ae.get(2).getComponentData());
		assertNotNull(ae.get(2).getComponentData().getName(), "btn1");
		assertEquals(ae.get(2).getEventID(), MouseEvent.MOUSE_PRESSED); // button

		assertNotNull(ae.get(3).getComponentData());
		assertNotNull(ae.get(3).getComponentData().getName(), "btn1");
		assertEquals(ae.get(3).getEventID(), MouseEvent.MOUSE_RELEASED); // button

		assertNotNull(ae.get(4).getComponentData());
		assertNotNull(ae.get(4).getComponentData().getName(), "btn2");
		assertEquals(ae.get(4).getEventID(), MouseEvent.MOUSE_PRESSED); // button

		assertNotNull(ae.get(5).getComponentData());
		assertNotNull(ae.get(5).getComponentData().getName(), "btn2");
		assertEquals(ae.get(5).getEventID(), MouseEvent.MOUSE_RELEASED); // button

		assertNotNull(ae.get(6).getComponentData());
		assertNotNull(ae.get(6).getComponentData().getName(), "btn2");
		assertEquals(ae.get(6).getEventID(), MouseEvent.MOUSE_PRESSED); // button

		assertNotNull(ae.get(7).getComponentData());
		assertNotNull(ae.get(7).getComponentData().getName(), "btn2");
		assertEquals(ae.get(7).getEventID(), MouseEvent.MOUSE_RELEASED); // button
	}

}
