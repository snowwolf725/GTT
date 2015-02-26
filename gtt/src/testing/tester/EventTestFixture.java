package testing.tester;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEventModel;
import gtt.tester.swing.SwingTester;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import junit.framework.TestCase;

public abstract class EventTestFixture extends TestCase {
	// //////////////////////////////////////////////////////////////////////
	
	static IEventModel m_EventModel =  EventModelFactory.createSwingModel();
	SwingTester m_tester = new SwingTester();

	final String m_TITLE = "testing.tester";

	public Frame showFrame(Component comp) {
		JFrame frame = new JFrame(m_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = (JPanel) frame.getContentPane();
		pane.setBorder(new EmptyBorder(10, 10, 10, 10));
		pane.add(comp);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}
}