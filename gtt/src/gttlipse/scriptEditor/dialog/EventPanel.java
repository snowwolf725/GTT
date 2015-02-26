/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.AbstractEvent;
import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gttlipse.GTTlipseConfig;
import gttlipse.editor.ui.ArgumentPanel;
import gttlipse.editor.ui.IComponentInfoPanel;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class EventPanel implements ModifyListener {

	private Combo m_com_eventname;

	private IComponentInfoPanel m_componentInfoPanel;

	private IEvent m_Event;

	private ArgumentPanel m_argsPanel = null;

	final IEventModel model = EventModelFactory.getDefault();

	public EventPanel(Composite eventInfoComp, IEvent _Event, IComponentInfoPanel _componentInfoPanel) {
		/* store reference */
		m_Event = _Event;
		m_componentInfoPanel = _componentInfoPanel;
		/* Setup Layout */
		Composite eventInfoComp_combo = new Composite(eventInfoComp, SWT.NULL);
		Composite eventInfoComp_arg = new Composite(eventInfoComp, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		final GridLayout gridlayout2 = new GridLayout();
		gridlayout2.numColumns = 2;
		GridData data = new GridData();
		data.widthHint = 300;
		data.heightHint = 20;
		eventInfoComp_combo.setLayout(gridlayout2);
		eventInfoComp_arg.setLayout(gridlayout);
		// 設定 EventName Combo
		createEventNameCombo(eventInfoComp_combo, data);

		/* Method Args */
		m_argsPanel = new ArgumentPanel(eventInfoComp_arg, false, ArgumentPanel.ALL_BUTTON);
		Iterator<Argument> ite = m_Event.getArguments().iterator();
		while (ite.hasNext()) {
			Argument a = (Argument) ite.next();
			m_argsPanel.addArgument(a);
		}

		m_com_eventname.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if (m_com_eventname.getText() == null
						|| m_com_eventname.getText() == "")
					return;
				AbstractEvent event = (AbstractEvent) m_Event;

				if (!event.getName().matches(m_com_eventname.getText())) {
					List<IEvent> events = model.getEvents(SwingComponent
							.create(m_componentInfoPanel
									.getComponentClassType().getText()));
					if (events.size() == 0)
						events = model.getEvents(SwingComponent
								.create("javax.swing.JLabel"));
					Iterator<IEvent> eite = events.iterator();
					while (eite.hasNext()) {
						AbstractEvent _event = (AbstractEvent) eite.next();
						if (_event.getName().matches(m_com_eventname.getText())) {
							event = (AbstractEvent) _event.clone();
						}
					}
				}

				m_argsPanel.clearArgument();
				/* fix for default event */
				if (m_com_eventname.getText().endsWith("_MOUSE")) {
					m_argsPanel.addArgument(Argument.create("int", "X", "0"));
					m_argsPanel.addArgument(Argument.create("int", "y", "0"));
				}
				Iterator<Argument> ite = event.getArguments().iterator();
				while (ite.hasNext()) {
					Argument a = (Argument) ite.next();
					m_argsPanel.addArgument(a);
				}
			}

		});

	}

	/**
	 * @param area
	 * @param data
	 */
	private void createEventNameCombo(final Composite area, GridData data) {
		final Label lbl_eventname = new Label(area, SWT.NULL);
		lbl_eventname.setText("Event name:");
		m_com_eventname = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_eventname.setLayoutData(data);
		m_com_eventname.setVisibleItemCount(10);
		// 動態加入 Event
		boolean firstEvent = true;
		m_com_eventname.removeAll();

		List<IEvent> events = model.getEvents(SwingComponent.create(m_componentInfoPanel.getComponentClassType().getText()));
//		System.out.println(m_componentInfoPanel.getComponentClassType().getText());
		if (events.size() == 0)
			events = model.getEvents(SwingComponent.create("javax.swing.JLabel"));
		TreeSet<String> sset_events = new TreeSet<String>();
		Iterator<IEvent> eite = events.iterator();

		// sort events
		if(GTTlipseConfig.testingOnSwingPlatform()) {
		/* default event */
			sset_events.add("CLICK_NO_BLOCK");
			sset_events.add("CLICK_MOUSE");
			sset_events.add("DRAG_MOUSE");
			sset_events.add("DRAGNDROP");
			sset_events.add("ENTER_MOUSE");
			sset_events.add("EXIT_MOUSE");
			sset_events.add("MOVE_MOUSE");
			sset_events.add("PRESS_KEY");
			sset_events.add("PRESS_MOUSE");
			sset_events.add("RELEASE_KEY");
			sset_events.add("RELEASE_MOUSE");
			sset_events.add("SET_TIMEOUT");
			sset_events.add("TYPE_KEY");
		}		
		while (eite.hasNext()) {
			AbstractEvent _event = (AbstractEvent) eite.next();
			sset_events.add(_event.getName());
		}

		// add event to combobox
		Iterator<String> eites = sset_events.iterator();
		while (eites.hasNext()) {
			String _eventname = (String) eites.next();
			m_com_eventname.add(_eventname);
			if (firstEvent == true) {
				m_com_eventname.setText(_eventname);
				firstEvent = false;
			}
			if (m_Event.getName().matches(_eventname))
				m_com_eventname.setText(_eventname);
		}
	}

	public void modifyText(ModifyEvent e) {
		// TODO Auto-generated method stub
		m_com_eventname.removeAll();
		boolean firstEvent = true;
		Object obj = e.getSource();
		if (obj instanceof Combo) {
			Combo com_comp = (Combo) obj;
//			System.out.println(com_comp.getText());
			List<IEvent> events = model.getEvents(SwingComponent.create(com_comp.getText()));
			TreeSet<String> sset_events = new TreeSet<String>();
			Iterator<IEvent> eite = events.iterator();
			// sort event
			if(GTTlipseConfig.testingOnSwingPlatform()) {
			/* default event */
				sset_events.add("CLICK_NO_BLOCK");
				sset_events.add("CLICK_MOUSE");
				sset_events.add("DRAG_MOUSE");
				sset_events.add("DRAGNDROP");
				sset_events.add("ENTER_MOUSE");
				sset_events.add("EXIT_MOUSE");
				sset_events.add("MOVE_MOUSE");
				sset_events.add("PRESS_KEY");
				sset_events.add("PRESS_MOUSE");
				sset_events.add("RELEASE_KEY");
				sset_events.add("RELEASE_MOUSE");
				sset_events.add("SET_TIMEOUT");
				sset_events.add("TYPE_KEY");
			}
			while (eite.hasNext()) {
				AbstractEvent _event = (AbstractEvent) eite.next();
				sset_events.add(_event.getName());
			}
			// add event to combobox
			Iterator<String> eites = sset_events.iterator();
			while (eites.hasNext()) {
				String _eventname = (String) eites.next();
				m_com_eventname.add(_eventname);
				if (firstEvent == true) {
					m_com_eventname.setText(_eventname);
					firstEvent = false;
				}
			}
		}
	}

	public void update() {
		AbstractEvent event = (AbstractEvent) m_Event;
		/* update event */
		if (!event.getName().matches(m_com_eventname.getText())) {
			event.setName(m_com_eventname.getText());

			List<IEvent> events = model.getEvents(SwingComponent
					.create(m_componentInfoPanel
							.getComponentClassType().getText()));
			if (events.size() == 0)
				events = model.getEvents(SwingComponent
						.create("javax.swing.JLabel"));
			Iterator<IEvent> eite = events.iterator();
			while (eite.hasNext()) {
				AbstractEvent _event = (AbstractEvent) eite.next();
				if (_event.getName().matches(m_com_eventname.getText())) {
					event.setEventId(_event.getEventId());
					event.setName(_event.getName());
				}
			}
		}

		/* update event args */
		Iterator<Argument> eite = event.getArguments().iterator();
		while (eite.hasNext())
			event.getArguments().remove(0);

		for (int i = 0; i < m_argsPanel.getItemCount(); i++) {
			Argument a = Argument.create(m_argsPanel.getType(i), m_argsPanel
					.getName(i), m_argsPanel.getValue(i));
			event.getArguments().add(a);
		}
	}
}
