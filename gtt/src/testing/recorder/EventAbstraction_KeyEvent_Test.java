package testing.recorder;

import gtt.eventmodel.Arguments;
import gtt.recorder.CaptureData;
import gtt.recorder.EventAbstracter;
import gtt.recorder.atje.AbstractEvent;
import gtt.recorder.atje.KeyEventData;
import gtt.tester.swing.SwingTesterTag;
import gtt.testscript.AbstractNode;
import gtt.testscript.EventNode;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextField;

import junit.framework.TestCase;

public class EventAbstraction_KeyEvent_Test extends TestCase {

	EventAbstracter m_abstraction;

	final long KEY_MASK = KeyEvent.KEY_EVENT_MASK;
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

	// 建立 AWT Key Event
	private KeyEvent createKeyEvent_a(Component c, int event_id) {
		return new KeyEvent(c, event_id, 0, 0, KeyEvent.VK_A,
				'a');
	}

	// 建立 AWT Key Event
	private KeyEvent createKeyEvent_z(Component c, int event_id) {
		return new KeyEvent(c, event_id, 0, 0, KeyEvent.VK_Z,
				'z');
	}
	private List<CaptureData> creaeKeyEventList() {
		List<CaptureData> result = new LinkedList<CaptureData>();

		JFrame frame = new JFrame();
		frame.setName("jframe");
		JTextField btn1, btn2;

		btn1 = new JTextField();
		btn1.setName("txt1");
		frame.getContentPane().add(btn1);

		btn2 = new JTextField();
		btn2.setName("txt2");
		frame.getContentPane().add(btn2);

		frame.pack();

		KeyEvent me;
		// type 1
		me = createKeyEvent_a(btn1, KeyEvent.KEY_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createKeyEvent_a(btn1, KeyEvent.KEY_RELEASED);
		result.add(new CaptureData(me, nextRecordTime()));

		// type 1
		me = createKeyEvent_a(btn1, KeyEvent.KEY_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createKeyEvent_a(btn1, KeyEvent.KEY_RELEASED);
		result.add(new CaptureData(me, nextRecordTime()));

		// type 2
		me = createKeyEvent_z(btn2, KeyEvent.KEY_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createKeyEvent_z(btn2, KeyEvent.KEY_RELEASED);
		result.add(new CaptureData(me, nextRecordTime()));
		
		// type 2
		me = createKeyEvent_z(btn2, KeyEvent.KEY_PRESSED);
		result.add(new CaptureData(me, nextRecordTime()));
		me = createKeyEvent_z(btn2, KeyEvent.KEY_RELEASED);
		result.add(new CaptureData(me, nextRecordTime()));

		return result;
	}

	public void test_abstraction() {
		List<CaptureData> list = creaeKeyEventList();
		List<AbstractNode> nodes = m_abstraction.abstraction(list);

		assertNotNull(nodes);
		assertEquals(nodes.size(), 4); // 4 個 event nodes

		assertTrue(nodes.get(0) instanceof EventNode);
		assertTrue(nodes.get(1) instanceof EventNode);
		assertTrue(nodes.get(2) instanceof EventNode);
		assertTrue(nodes.get(3) instanceof EventNode);
		
		EventNode en;
		en = (EventNode) nodes.get(0);
		assertEquals(en.getComponent().getName(), "txt1");
		assertEquals(en.getComponent().getType(), "javax.swing.JTextField");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.TYPE_KEY);
		Arguments args = en.getEvent().getArguments();
		assertEquals(args.size(), 2);
		assertEquals(args.get(0).getName(), "Modifier" );
		assertEquals(args.get(0).getValue(), ""+KeyEvent.VK_A );
		assertEquals(args.get(1).getName(), "Char" );
		assertEquals(args.get(1).getValue(), "a" );

		en = (EventNode) nodes.get(1);
		assertEquals(en.getComponent().getName(), "txt1");
		assertEquals(en.getComponent().getType(), "javax.swing.JTextField");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.TYPE_KEY);
		args = en.getEvent().getArguments();
		assertEquals(args.size(), 2);
		assertEquals(args.get(0).getName(), "Modifier" );
		assertEquals(args.get(0).getValue(), ""+KeyEvent.VK_A );
		assertEquals(args.get(1).getName(), "Char" );
		assertEquals(args.get(1).getValue(), "a" );

		
		en = (EventNode) nodes.get(2);
		assertEquals(en.getComponent().getName(), "txt2");
		assertEquals(en.getComponent().getType(), "javax.swing.JTextField");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.TYPE_KEY);
		args = en.getEvent().getArguments();
		assertEquals(args.size(), 2);
		assertEquals(args.get(0).getName(), "Modifier" );
		assertEquals(args.get(0).getValue(), ""+KeyEvent.VK_Z );
		assertEquals(args.get(1).getName(), "Char" );
		assertEquals(args.get(1).getValue(), "z" );

		en = (EventNode) nodes.get(3);
		assertEquals(en.getComponent().getName(), "txt2");
		assertEquals(en.getComponent().getType(), "javax.swing.JTextField");
		assertEquals(en.getEvent().getEventId(), SwingTesterTag.TYPE_KEY);
		args = en.getEvent().getArguments();
		assertEquals(args.size(), 2);
		assertEquals(args.get(0).getName(), "Modifier" );
		assertEquals(args.get(0).getValue(), ""+KeyEvent.VK_Z );
		assertEquals(args.get(1).getName(), "Char" );
		assertEquals(args.get(1).getValue(), "z" );
	}
	

	public void test_highLevel() {
		List<CaptureData> list = creaeKeyEventList();
		List<AbstractEvent> ae = m_abstraction.lowLevel(list);
		ae = m_abstraction.mediumLevel(ae);
		assertNotNull(ae);
		assertEquals(ae.size(), 4); // 8 個事件減成4個type事件

		// 測試高階抽象化： AbstractEvent 變成 EventNode
		List<AbstractNode> nodes = m_abstraction.highLevel(ae);
		assertNotNull(nodes);
		assertEquals(nodes.size(), 4); // 4 個 event nodes
	}

	public void test_mediumLevel() {
		List<CaptureData> list = creaeKeyEventList();
		List<AbstractEvent> ae = m_abstraction.lowLevel(list);

		// 測試中階抽象化： press + release 變成 typed
		ae = m_abstraction.mediumLevel(ae);

		assertNotNull(ae);
		assertEquals(ae.size(), 4); // 8 個事件減成4個click事件

		assertNotNull(ae.get(0).getComponentData());
		assertEquals(ae.get(0).getComponentData().getName(), "txt1");
		
		assertEquals(ae.get(0).getEventID(), KeyEvent.KEY_TYPED); // button1
		assertTrue(ae.get(0) instanceof KeyEventData); // button 1
		
		KeyEventData kd;
		kd = (KeyEventData)ae.get(0);
		assertEquals(kd.getKeyCode(), KeyEvent.VK_A);
		assertEquals(kd.getKeyChar(), 'a');

		assertNotNull(ae.get(1).getComponentData());
		assertEquals(ae.get(1).getComponentData().getName(), "txt1");
		assertEquals(ae.get(1).getEventID(), KeyEvent.KEY_TYPED); // button1
		assertTrue(ae.get(1) instanceof KeyEventData); // button 1
		kd = (KeyEventData)ae.get(1);
		assertEquals(kd.getKeyCode(), KeyEvent.VK_A);
		assertEquals(kd.getKeyChar(), 'a');

		assertNotNull(ae.get(2).getComponentData());
		assertEquals(ae.get(2).getComponentData().getName(), "txt2");
		assertEquals(ae.get(2).getEventID(), KeyEvent.KEY_TYPED); // button1
		assertTrue(ae.get(2) instanceof KeyEventData); // button 1
		kd = (KeyEventData)ae.get(2);
		assertEquals(kd.getKeyCode(), KeyEvent.VK_Z);
		assertEquals(kd.getKeyChar(), 'z');

		assertNotNull(ae.get(3).getComponentData());
		assertEquals(ae.get(3).getComponentData().getName(), "txt2");
		assertEquals(ae.get(3).getEventID(), KeyEvent.KEY_TYPED); // button1
		assertTrue(ae.get(3) instanceof KeyEventData); // button 1
		kd = (KeyEventData)ae.get(3);
		assertEquals(kd.getKeyCode(), KeyEvent.VK_Z);
		assertEquals(kd.getKeyChar(), 'z');

	}

	public void test_lowLevel() {
		// 測試低階抽像化

		List<CaptureData> list = creaeKeyEventList();
		List<AbstractEvent> ae = m_abstraction.lowLevel(list);

		assertNotNull(ae);
		assertEquals(ae.size(), 8);

		assertNotNull(ae.get(0).getComponentData());
		assertNotNull(ae.get(0).getComponentData().getName(), "btn1");
		assertEquals(ae.get(0).getEventID(), KeyEvent.KEY_PRESSED); // button

		assertNotNull(ae.get(1).getComponentData());
		assertNotNull(ae.get(1).getComponentData().getName(), "btn1");
		assertEquals(ae.get(1).getEventID(), KeyEvent.KEY_RELEASED); // button

		assertNotNull(ae.get(2).getComponentData());
		assertNotNull(ae.get(2).getComponentData().getName(), "btn1");
		assertEquals(ae.get(2).getEventID(), KeyEvent.KEY_PRESSED); // button

		assertNotNull(ae.get(3).getComponentData());
		assertNotNull(ae.get(3).getComponentData().getName(), "btn1");
		assertEquals(ae.get(3).getEventID(), KeyEvent.KEY_RELEASED); // button

		assertNotNull(ae.get(4).getComponentData());
		assertNotNull(ae.get(4).getComponentData().getName(), "btn2");
		assertEquals(ae.get(4).getEventID(), KeyEvent.KEY_PRESSED); // button

		assertNotNull(ae.get(5).getComponentData());
		assertNotNull(ae.get(5).getComponentData().getName(), "btn2");
		assertEquals(ae.get(5).getEventID(), KeyEvent.KEY_RELEASED); // button

		assertNotNull(ae.get(6).getComponentData());
		assertNotNull(ae.get(6).getComponentData().getName(), "btn2");
		assertEquals(ae.get(6).getEventID(), KeyEvent.KEY_PRESSED); // button

		assertNotNull(ae.get(7).getComponentData());
		assertNotNull(ae.get(7).getComponentData().getName(), "btn2");
		assertEquals(ae.get(7).getEventID(), KeyEvent.KEY_RELEASED); // button
	}

}
