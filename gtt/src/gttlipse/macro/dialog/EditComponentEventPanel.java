package gttlipse.macro.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
import gttlipse.GTTlipseConfig;
import gttlipse.editor.ui.ArgumentPanel;
import gttlipse.editor.ui.DynamicComponentPanel;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;


public class EditComponentEventPanel {
	private Group m_group = null;
	private Combo m_eventList = null;

	private ComponentNode m_selectNode = null;
	private ComponentEventNode m_editNode = null;
	private ArgumentPanel m_argsPanel = null;
	private IEventModel m_model = EventModelFactory.getDefault();
	
	private DynamicComponentPanel m_dynPanel = null;

	private void initPanel(Composite parent) {
		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Select Event");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		m_group.setLayout(gridlayout);

		m_eventList = new Combo(m_group, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_eventList.setVisibleItemCount(10);

		GridData gd = new GridData();
		gd.widthHint = 250;
		m_eventList.setLayoutData(gd);

		m_eventList.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				updateArg();
			}

			public void widgetSelected(SelectionEvent e) {
				updateArg();
			}
		});

		m_argsPanel = new ArgumentPanel(parent, false, ArgumentPanel.ALL_BUTTON);
		
		m_dynPanel = new DynamicComponentPanel(parent, m_editNode);
	}

	public EditComponentEventPanel(Composite parent, ComponentEventNode editnode) {
		m_editNode = editnode;
		m_selectNode = (ComponentNode) m_editNode.getComponent();

		initPanel(parent);
		updateEventList();
		updateArg();
	}

	private void updateEventList() {
		if (m_editNode == null || m_selectNode == null)
			return;
		
		//Now only Web have dynamic setting. GSX 2010/4/13
		if(GTTlipseConfig.testingOnWebPlatform())
			m_dynPanel.dynamicVisible(m_selectNode);
		
		m_eventList.removeAll();

		String def = "";
		ComponentNode ref = null;

		if (m_editNode.getComponent() != null) {
			ref = (ComponentNode) m_editNode.getComponent();
			if (ref == null)
				return;

			// 要編輯的node與選到的node是同一個
			if (ref.getPath().toString().equals(
					m_selectNode.getPath().toString()))
				def = m_editNode.getEventType();
		}

		ref = m_selectNode;

		List<IEvent> events = m_model.getEvents(ref.getComponent());

		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent event = ite.next();
			if (def == "")
				def = event.getName();
			m_eventList.add(event.getName());
		}

		m_eventList.setText(def);
	}

	// 將參數列更新為預設的
	private void updateArg() {
		if (m_editNode == null || m_selectNode == null)
			return;
		m_argsPanel.clearArgument();
		ComponentNode ref = null;
		if (m_editNode.getComponent() != null) {
			ref = (ComponentNode) m_editNode.getComponent();

			// 要編輯的node與選到的node是同一個
			if (ref.getPath().toString().equals(
					m_selectNode.getPath().toString())) {
				// 未定義參數表示是一個新插入的節點，所以參數要為預設的
				if (m_editNode.getArguments().size() != 0) {
					if (m_editNode.getEventType().equals(m_eventList.getText())) {
						for (int i = 0; i < m_editNode.getArguments().size(); i++) {
							m_argsPanel.addArgument(m_editNode
									.getArguments().get(i));
						}
						return;
					}
				}
			}
		}

		ref = m_selectNode;
		if (ref == null)
			return;

		List<IEvent> events = m_model.getEvents(ref.getComponent());
		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent event = ite.next();
			if (event.getName().equals(m_eventList.getText())) {
				for (int i = 0; i < event.getArguments().size(); i++) {
					m_argsPanel.addArgument(event.getArguments().get(i));
				}
			}
		}
	}

	public String getSelectEventType() {
		return m_eventList.getText();
	}

	public int getSelectEventID() {
		ComponentNode ref = null;

		if (m_editNode.getComponent() != null) {
			ref = (ComponentNode) m_editNode.getComponent();
			// 要編輯的node與選到的node是不同個
			if (!ref.getPath().toString().equals(
					m_selectNode.getPath().toString()))
				ref = null;
		}

		if (ref == null)
			ref = m_selectNode;

		List<IEvent> events = m_model.getEvents(ref.getComponent());
		Iterator<IEvent> ite = events.iterator();
		while (ite.hasNext()) {
			IEvent event = ite.next();
			if (event.getName().equals(m_eventList.getText()))
				return event.getEventId();
		}

		return 0;
	}

	public int getArgCount() {
		return m_argsPanel.getItemCount();
	}

	public Argument getArgument(int index) {
		return Argument.create(m_argsPanel.getType(index), m_argsPanel
				.getName(index), m_argsPanel.getValue(index));
	}

	public void updateSelectNode(ComponentNode selectnode) {
		m_selectNode = selectnode;
		updateEventList();
		updateArg();
	}
	
	public String getDynamicComponentType() {
		return m_dynPanel.getDynamicComponentType();
	}
	
	public String getDynamicComponentValue() {
		return m_dynPanel.getDynamicComponentValue();
	}
	
	public int getDynamicComponentIndex() {
		return m_dynPanel.getDynamicComponentIndex();
	}
	

}
