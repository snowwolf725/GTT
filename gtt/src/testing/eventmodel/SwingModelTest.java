package testing.eventmodel;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.eventmodel.swing.SwingModel;
import gttlipse.GTTlipseConfig;
import junit.framework.TestCase;

public class SwingModelTest extends TestCase {

	private SwingModel m_model;
	static {
		// gsx: have a magic problem
		// this line can not be put in setUp() -- why?
		GTTlipseConfig.setToTestingOnSwingPlatform();

	}

	protected void setUp() throws Exception {
		super.setUp();
		m_model =  (SwingModel)EventModelFactory.getDefault();
//		m_model = new SwingModel();
//		m_model.initialize(JavaCore.getClasspathVariable("ECLIPSE_HOME")
//				+ "/plugins/GTTlipse_1.0.0/" + "descriptor/swing.xml");
		
		m_model.clear();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		m_model = null;
	}

	public void testAddComponent() {
		m_model.addComponent(null);

		assertEquals(m_model.getComponentSize(), 0);
		assertEquals(m_model.getComponents().size(), 0);

		m_model.addComponent(m_model.createComponent("javax.swing.JButton"));
		assertEquals(m_model.getComponentSize(), 1);
		assertEquals(m_model.getComponents().size(), 1);

		m_model.addComponent(m_model.createComponent("javax.swing.JButton"));
		assertEquals(m_model.getComponentSize(), 1);
		assertEquals(m_model.getComponents().size(), 1);

		m_model.addComponent(m_model.getComponent("javax.swing.JButton"));
		assertEquals(m_model.getComponentSize(), 1);
		assertEquals(m_model.getComponents().size(), 1);

		m_model.addComponent(m_model.createComponent("javax.swing.JMenuItem"));
		assertEquals(m_model.getComponentSize(), 2);
		assertEquals(m_model.getComponents().size(), 2);

		m_model.addComponent(m_model.getComponent("javax.swing.JMenuItem"));
		assertEquals(m_model.getComponentSize(), 2);
		assertEquals(m_model.getComponents().size(), 2);

	}

	public void testAddEvent() {

		m_model.addEvent(null);
		assertEquals(m_model.getEventSize(), 0);
		/*
		 * 一個Event預設的scope是JComponent
		 */
		m_model.addEvent(SwingEvent.create(1, "PUSH"), "JComponent");
		assertEquals(m_model.getEventSize(), 1);

		m_model.addEvent(SwingEvent.create(1, "PUSH"));
		m_model.addEvent(SwingEvent.create(1, "PUSH"), "JComponent");
		m_model.addEvent(SwingEvent.create(1, "PUSH"));
		assertEquals(m_model.getEventSize(), 1); // 不會重覆

		m_model.addEvent(SwingEvent.create(1, "POP"));
		assertEquals(m_model.getEventSize(), 2);

		m_model.addEvent(SwingEvent.create(1, "POP"));
		m_model.addEvent(SwingEvent.create(1, "POP"), "JComponent");
		m_model.addEvent(SwingEvent.create(1, "POP"));
		assertEquals(m_model.getEventSize(), 2); // 不會重覆
	}

	public void testGetComponent() {
		assertNull(m_model.getComponent("no.such.component"));
		assertNull(m_model.getComponent(null));
	}

	public void testReadComponent() {
		assertEquals(m_model.getComponents().size(), 0);
		assertTrue(m_model.initialize("descriptor/swing.xml"));
		assertEquals(38, m_model.getComponentSize());
		assertEquals(38, m_model.getComponents().size());

		/**
		 * 共會讀到下列33個Swing元件
		 */
		assertNotNull(m_model.getComponent("javax.swing.JButton"));
		assertNotNull(m_model.getComponent("javax.swing.JCheckBox"));
		assertNotNull(m_model.getComponent("javax.swing.JMenuItem"));
		assertNotNull(m_model.getComponent("javax.swing.JMenu"));
		assertNotNull(m_model.getComponent("javax.swing.JRadioButton"));
		assertNotNull(m_model.getComponent("javax.swing.JToggleButton"));
		assertNotNull(m_model.getComponent("javax.swing.JColorChooser"));
		assertNotNull(m_model.getComponent("javax.swing.JComboBox"));
		assertNotNull(m_model.getComponent("javax.swing.JFileChooser"));
		assertNotNull(m_model.getComponent("javax.swing.JInternalFrame"));
		assertNotNull(m_model.getComponent("javax.swing.JLabel"));
		assertNotNull(m_model.getComponent("javax.swing.JList"));
		assertNotNull(m_model.getComponent("javax.swing.JMenuBar"));
		assertNotNull(m_model.getComponent("javax.swing.JPopupMenu"));
		assertNotNull(m_model.getComponent("javax.swing.JProgressBar"));
		assertNotNull(m_model.getComponent("javax.swing.JScrollBar"));
		assertNotNull(m_model.getComponent("javax.swing.JScrollPane"));
		assertNotNull(m_model.getComponent("javax.swing.JSlider"));
		assertNotNull(m_model.getComponent("javax.swing.JSpinner"));
		assertNotNull(m_model.getComponent("javax.swing.JSplitPane"));
		assertNotNull(m_model.getComponent("javax.swing.JTabbedPane"));
		assertNotNull(m_model.getComponent("javax.swing.JTable"));
		assertNotNull(m_model.getComponent("javax.swing.table.JTableHeader"));
		assertNotNull(m_model.getComponent("javax.swing.JPasswordField"));
		assertNotNull(m_model.getComponent("javax.swing.JTextField"));
		assertNotNull(m_model.getComponent("javax.swing.JEditorPane"));
		assertNotNull(m_model.getComponent("javax.swing.JTextArea"));
		assertNotNull(m_model.getComponent("javax.swing.JTree"));
		assertNotNull(m_model.getComponent("java.awt.Window"));
		assertNotNull(m_model.getComponent("java.awt.Frame"));
		assertNotNull(m_model.getComponent("java.awt.Dialog"));
		assertNotNull(m_model.getComponent("javax.swing.JDialog"));
		assertNotNull(m_model.getComponent("javax.swing.JFrame"));
	}

	public void testReadEvent() {
		assertTrue(m_model.initialize("descriptor/swing.xml"));

		// 146 種事件，而不是106種
		assertEquals(m_model.getEventSize(), 14);
	}

	public void testReadPackage() {
		assertTrue(m_model.initialize("descriptor/swing.xml"));

		// 跟comp個數一樣
		assertEquals(36, m_model.getScopeSize() );

		IComponent c;

		c = m_model.getComponent("javax.swing.JButton");
		assertNotNull(c);
		assertEquals(m_model.getScope(c), "JComponent.AbstractButton");

		c = m_model.getComponent("javax.swing.JList");
		assertNotNull(c);
		assertEquals(m_model.getScope(c), "JComponent.JList");

		c = m_model.getComponent("javax.swing.table.JTableHeader");
		assertNotNull(c);
		assertEquals(m_model.getScope(c), "JComponent.JTableHeader");

		c = m_model.getComponent("javax.swing.JMenu");
		assertNotNull(c);
		assertEquals(m_model.getScope(c), "JComponent.AbstractButton.JMenu");

		c = m_model.getComponent("javax.swing.JToolBar");
		assertNotNull(c);
		assertEquals(m_model.getScope(c), "JComponent");
	}

	public void testGetEvents() {
		assertTrue(m_model.initialize("descriptor/swing.xml"));

		assertEquals(m_model.getEvents("").size(), 0);

		IComponent c;
		
		int general_event_counts = 14;

		c = m_model.createComponent("javax.swing.JButton");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JCheckBox");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JMenuItem");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JMenu");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+7);

		c = m_model.createComponent("javax.swing.JRadioButton");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JToggleButton");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JColorChooser");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+1);

		c = m_model.createComponent("javax.swing.JComboBox");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+5);

		c = m_model.createComponent("javax.swing.JFileChooser");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+9);

		c = m_model.createComponent("javax.swing.JInternalFrame");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+8);

		c = m_model.createComponent("javax.swing.JLabel");
		assertEquals(m_model.getEvents(c).size(), general_event_counts);

		c = m_model.createComponent("javax.swing.JList");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+6);

		c = m_model.createComponent("javax.swing.JMenuBar");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JPopupMenu");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JProgressBar");
		assertEquals(m_model.getEvents(c).size(), general_event_counts);

		c = m_model.createComponent("javax.swing.JScrollBar");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JScrollPane");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+8);

		c = m_model.createComponent("javax.swing.JSlider");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JSpinner");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+3);

		c = m_model.createComponent("javax.swing.JSplitPane");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+5);

		c = m_model.createComponent("javax.swing.JTabbedPane");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+2);

		c = m_model.createComponent("javax.swing.JTable");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+5);

		c = m_model.createComponent("javax.swing.table.JTableHeader");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+2);

		c = m_model.createComponent("javax.swing.JPasswordField");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+19);

		c = m_model.createComponent("javax.swing.JEditorPane");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+19);

		c = m_model.createComponent("javax.swing.JTextArea");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+23);

		c = m_model.createComponent("javax.swing.JTree");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+11);

		c = m_model.createComponent("java.awt.Window");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("java.awt.Frame");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("java.awt.Dialog");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JDialog");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+4);

		c = m_model.createComponent("javax.swing.JFrame");
		assertEquals(m_model.getEvents(c).size(), general_event_counts+6);
	}

	public void testGetEvent() {
		assertTrue(m_model.initialize("descriptor/swing.xml"));

		assertNull(m_model.getEvent(null, "PUSH"));

		IComponent c;
		IEvent e;
		
		
		c = m_model.getComponent("javax.swing.JButton");
		e = m_model.getEvent(c, "PUSH");
		assertNotNull(e);
		e = m_model.getEvent(c, "NO_SUCH_EVENT_NAME");
		assertNull(e);

		c = m_model.getComponent("javax.swing.JRadioButton");
		e = m_model.getEvent(c, "PUSH");
		assertNotNull(e);
		e = m_model.getEvent(c, "NO_SUCH_EVENT_NAME");
		assertNull(e);

	}

	public void testGetSize() {
		assertEquals(m_model.getComponentSize(), 0);
	}

	public void testGetEventByID() {
		assertNull(m_model.getEvent(null, -1));
	}

	public void testGetEventByID_JLabel() {
		// 等同於測試 JComponent/JLabel/JProgressBar
		assertTrue(m_model.initialize("descriptor/swing.xml"));

		IComponent c;

		c = m_model.createComponent("javax.swing.JLabel");
		assertEquals(m_model.getEvent(c, 0), null); // not-found
		assertEquals(m_model.getEvent(c, 1000).getName(), "PRESS_KEY");
		assertEquals(m_model.getEvent(c, 1001).getName(), "PRESS_MOUSE");
		assertEquals(m_model.getEvent(c, 1002).getName(), "RELEASE_KEY");
		assertEquals(m_model.getEvent(c, 1003).getName(), "RELEASE_MOUSE");
		assertEquals(m_model.getEvent(c, 1004).getName(), "TYPE_KEY");
		assertEquals(m_model.getEvent(c, 1005).getName(), "CLICK_NO_BLOCK");
		assertEquals(m_model.getEvent(c, 1006).getName(), "CLICK_MOUSE");
		assertEquals(m_model.getEvent(c, 1007).getName(), "DRAGNDROP");
		assertEquals(m_model.getEvent(c, 1008).getName(), "DRAG_MOUSE");
		assertEquals(m_model.getEvent(c, 1009).getName(), "ENTER_MOUSE");
		assertEquals(m_model.getEvent(c, 1010).getName(), "EXIT_MOUSE");
	}

	public void testGetScope() {
		assertNull(m_model.getScope(null), null);
	}

	public void testAddEventScope() {
		IComponent c = m_model.createComponent("javax.swing.JLabel");
		m_model.addScope(c, ""); // default scope: JComponent
		assertEquals(m_model.getScope(c), "JComponent");
	}
}
