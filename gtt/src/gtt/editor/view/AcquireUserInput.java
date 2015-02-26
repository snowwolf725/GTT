package gtt.editor.view;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;

import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class AcquireUserInput {
	public static IComponent acquireComponent(List<IComponent> cs) {
		return (IComponent) JOptionPane.showInputDialog(null,
				"Component selection", "Component", JOptionPane.PLAIN_MESSAGE,
				null, cs.toArray(), null);
	}

	public static IEvent acquireEvent(List<IEvent> es) {
		return (IEvent) JOptionPane.showInputDialog(null, "Event selection",
				"Event", JOptionPane.PLAIN_MESSAGE, null, es.toArray(), null);
	}
	
	static String m_LastAbsolutePath = ".";

	public static String acquireFilePath() {
		JFileChooser chooser = new JFileChooser(m_LastAbsolutePath);
		int option = chooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			m_LastAbsolutePath = chooser.getSelectedFile().getAbsolutePath();
			return chooser.getSelectedFile().getAbsoluteFile().toString();
		}
		return null;
	}

	public static String acquireInput(String msg) {
		return JOptionPane.showInputDialog(msg);
	}
}
