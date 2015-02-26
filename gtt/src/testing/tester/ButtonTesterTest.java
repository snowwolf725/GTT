package testing.tester;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.tester.swing.SwingTesterTag;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ButtonTesterTest extends EventTestFixture {

	public void testClickButton() {
		
		final JButton b = new JButton("btn1");
		b.setName("btn1");
		b.setText("button not clicked");
		final String expected = "button clicked";
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				b.setText(expected);
			}
		});
		EventModelFactory.destroy();
		Frame frm = showFrame(b);

		// testing
		IComponent ic = m_EventModel.createDefaultComponent();
		assertNotNull(ic);
		ic.setType("javax.swing.JButton");
		ic.setTitle(m_TITLE);
		ic.setName("btn1");

		IEvent ie = m_EventModel.getEvent(ic, SwingTesterTag.PUSH);
		assertNotNull(ie);

		EventNode en = new NodeFactory().createEventNode(ic, ie);
		assertTrue(m_tester.fire(en));
		assertTrue("Button not clicked", expected.matches(b.getText()));
		
		frm.dispose();
	}

}
