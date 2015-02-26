package gttlipse.fit.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gttlipse.editor.ui.ArgumentPanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class MultiEventPanel {
	boolean m_resetEventList;
	IComponent m_iComponent;
	IEvent m_argumentSettingEvent;
	List<IEvent> m_eventList;
	org.eclipse.swt.widgets.List m_selectedEventList;
	Combo m_eventCombo;
	ArgumentPanel m_argumentPanel;

	public MultiEventPanel(Composite parent) {
		m_eventList = new ArrayList<IEvent>();
//		Composite m_parent = new Composite(null, SWT.NONE);
		initPanel(parent);
	}

	private void initPanel(Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		parent.setLayout(gridLayout);
		
		final Label eventLabel_1 = new Label(parent, SWT.NONE);
		eventLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		eventLabel_1.setText("Event:");
		
		m_eventCombo = new Combo(parent, SWT.READ_ONLY);
		m_eventCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		m_eventCombo.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				if(m_iComponent != null) {
					List<IEvent> events = EventModelFactory.getDefault().getEvents(m_iComponent);
					Iterator<?> ite = events.iterator();
					while (ite.hasNext()) {
						IEvent ie = (IEvent) ite.next();
						m_eventCombo.add(ie.getName());
					}
				}
			}
		});

		final Button addEventButton = new Button(parent, SWT.NONE);
		addEventButton.setLayoutData(new GridData());
		addEventButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				IEvent ie = EventModelFactory.getDefault().getEvent(m_iComponent, m_eventCombo.getText());
				m_eventList.add(ie);
				updateSelectedEventList();
			}
		});
		addEventButton.setText("Add Event");
		
		m_selectedEventList = new org.eclipse.swt.widgets.List(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		m_selectedEventList.setRedraw(true);
		m_selectedEventList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		m_selectedEventList.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				String name = m_selectedEventList.getItem(m_selectedEventList.getSelectionIndex());
				saveEventArgument();
				updateArgumentListPanel(name);
			}
		});
		

		final Composite eventArgumentComb = new Composite(parent, SWT.NONE);
		eventArgumentComb.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 3, 1));
		eventArgumentComb.setLayout(new GridLayout());
		
		m_argumentPanel = new ArgumentPanel(eventArgumentComb, false, ArgumentPanel.ALL_BUTTON);
	}

	public void saveEventArgument() {
		if(m_argumentSettingEvent == null)
			return;
		m_argumentSettingEvent.getArguments().clear();
		for (int i = 0; i < m_argumentPanel.getItemCount(); i++) {
			m_argumentSettingEvent.getArguments().add(getArgument(i));
		}
	}

	private Argument getArgument(int index) {
		return Argument.create(m_argumentPanel.getType(index), m_argumentPanel
				.getName(index), m_argumentPanel.getValue(index));
	}

	public void updateSelectedEventList() {
		m_selectedEventList.removeAll();
		Iterator<?> ite = m_eventList.iterator();
		while (ite.hasNext()) {
			IEvent ie = (IEvent) ite.next();
			m_selectedEventList.add(ie.getName());
		}
	}

	private void updateArgumentListPanel(String name) {
		m_argumentPanel.clearArgument();
		Iterator<?> ite = m_eventList.iterator();
		while (ite.hasNext()) {
			IEvent ie = (IEvent) ite.next();
			if(name.compareTo(ie.getName()) == 0) {
				m_argumentSettingEvent = ie;
				for(int i = 0; i < ie.getArguments().size(); i++)
					m_argumentPanel.addArgument(ie.getArguments().get(i));
			}
				
		}
	}
	
	public void update() {
		m_eventCombo.removeAll();
		m_selectedEventList.removeAll();
		m_eventList.clear();
		m_argumentPanel.clearArgument();
		m_iComponent = null;
	}

	public void resetIComponent(IComponent ic) {
		m_iComponent = ic;
	}

	public void setEventList(List<IEvent> list) {
		m_eventList = list;
	}

	public List<IEvent> getEventList() {
		return m_eventList;
	}
}