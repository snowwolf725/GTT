package gttlipse.macro.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gtt.macro.DefaultMacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.editor.ui.ArgumentPanel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;


public class ComponentEventPanel {
	private Group m_group = null;
	private Combo m_comboEventList = null;

	private ArgumentPanel m_argsPanel = null;
	private MacroEventCallerNode m_node = null;
	private AbstractMacroNode m_selectNode = null;

	private Map<String, IEvent> m_table = new HashMap<String, IEvent>();

	public ComponentEventPanel(Composite parent) {
		this(parent, null);
	}

	public ComponentEventPanel(Composite parent, MacroEventCallerNode node) {
		m_node = node;
		initPanel(parent);
	}

	public void update(AbstractMacroNode select) {
		m_selectNode = select;
		updateComboAndArguPanelBySelectNode();
	}

	public void setSelectEvent(String arg) {
		int index = m_comboEventList.indexOf(arg);
		m_comboEventList.select(index);
	}

	public String getSelectEventType() {
		return m_comboEventList.getText();
	}

	public int getArgCount() {
		return m_argsPanel.getItemCount();
	}

	public String getArgType(int row) {
		return m_argsPanel.getType(row);
	}

	public String getArgName(int row) {
		return m_argsPanel.getName(row);
	}

	public String getArgValue(int row) {
		return m_argsPanel.getValue(row);
	}

	private void doWidgetSelected() {
		if (m_selectNode == null) {
			if (m_node.getEventName().equals(m_comboEventList.getText()))
				initComboAndArguPanel();
			else
				updateArgsBySelectEvent();
			return;
		}

		if (m_selectNode.getPath().equals(m_node.getPath())) {
			if (m_node.getEventName().equals(m_comboEventList.getText()))
				initComboAndArguPanel();
			else
				updateArgsBySelectEvent();
		} else {
			updateArgPanelBySelectNode();
		}
	}

	private void initPanel(Composite parent) {
		// init group
		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Select Event");

		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		m_group.setLayout(gridlayout);

		m_comboEventList = new Combo(m_group, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_comboEventList.setVisibleItemCount(10);

		GridData gd = new GridData();
		gd.widthHint = 250;
		m_comboEventList.setLayoutData(gd);

		m_comboEventList.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				doWidgetSelected();
			}

			public void widgetSelected(SelectionEvent e) {
				doWidgetSelected();
			}
		});

		m_argsPanel = new ArgumentPanel(parent, false, ArgumentPanel.ALL_BUTTON);

		initComboAndArguPanel();
	}

	private void updateArguByEditNode() {
		// init argument panel
		Iterator<Argument> ite = m_node.getArguments().iterator();
		m_argsPanel.clearArgument();
		while (ite.hasNext()) {
			Argument arg = ite.next();
			m_argsPanel.addArgument(arg);
		}
	}

	IEventModel m_EventModel = EventModelFactory.getDefault();

	private void initComboAndArguPanel() {
		if (m_node == null)
			return;

		// new node (如果node沒有設定event type 表示是一個新的要插入的node
		// 並在UI上預設其 event和argument
		if (m_node.getEventName() == null || m_node.getEventName() == "") {
			AbstractMacroNode ref = m_node.getReference();
			if (ref instanceof MacroComponentNode) {
				forMacroComponentNode((MacroComponentNode) ref);
				return;
			}
			if (ref instanceof ComponentNode) {
				forComponentNode((ComponentNode) ref);
				return;
			}
			return;
			// 舊node (如果node有設定event type 表示這個已經存在在script中的node)
		}

		AbstractMacroNode ref = m_node.getReference();
		if (ref instanceof MacroComponentNode) {
			MacroComponentNode reference = (MacroComponentNode) ref;

			// init combo
			m_comboEventList.removeAll();
			for (int i = 0; i < reference.size(); i++) {
				if (reference.get(i) instanceof MacroEventNode) {
					MacroEventNode child = (MacroEventNode) reference.get(i);
					m_comboEventList.add(child.getName());
				}
			}
			m_comboEventList.setText(m_node.getEventName());
			updateArguByEditNode();

			return;
		}

		if (ref instanceof ComponentNode) {
			ComponentNode reference = (ComponentNode) ref;
			List<IEvent> elist = m_EventModel.getEvents(SwingComponent
					.create(reference.getType()));
			Iterator<IEvent> eite = elist.iterator();

			TreeSet<String> sset_events = new TreeSet<String>();
			// sort events
			while (eite.hasNext()) {
				IEvent event = eite.next();
				sset_events.add(event.getName());
				m_table.put(event.getName(), event);
			}

			if (m_comboEventList.getItemCount() > 0)
				m_comboEventList.removeAll();

			// add event to combobox
			Iterator<?> oite = sset_events.iterator();
			while (oite.hasNext()) {
				String _eventname = (String) oite.next();
				m_comboEventList.add(_eventname);
			}
			m_comboEventList.setText(m_node.getEventName());
			updateArguByEditNode();
			return;
		}
	}

	private void forComponentNode(ComponentNode reference) {
		IEventModel model = EventModelFactory.getDefault();
		List<IEvent> m_eventList = model.getEvents(SwingComponent
				.create(reference.getType()));

		Iterator<IEvent> eite = m_eventList.iterator();

		// sort events
		String def = "";
		while (eite.hasNext()) {
			IEvent event = eite.next();
			if (def == "") {
				def = event.getName();
				m_argsPanel.clearArgument();
				for (int i = 0; i < event.getArguments().size(); i++) {
					Argument arg = event.getArguments().get(i);
					m_argsPanel.addArgument(arg);
				}
			}
			m_comboEventList.add(event.getName());
		}
		m_comboEventList.setText(def);
	}

	private void forMacroComponentNode(MacroComponentNode reference) {
		String def = "";
		m_comboEventList.removeAll();
		for (int i = 0; i < reference.size(); i++) {
			if (!(reference.get(i) instanceof MacroEventNode))
				continue;
			MacroEventNode child = (MacroEventNode) reference.get(i);
			if (def == "") {
				def = child.getName();
				m_argsPanel.clearArgument();
				for (int j = 0; j < child.getArguments().size(); j++)
					m_argsPanel.addArgument(child.getArguments().get(j));
			}
			m_comboEventList.add(child.getName());
		}
		m_comboEventList.setText(def);
	}

	private void updateArgsBySelectEvent() {
		AbstractMacroNode ref = m_node.getReference();

		if (ref instanceof MacroComponentNode) {
			MacroComponentNode reference = (MacroComponentNode) ref;
			AbstractMacroNode _select = new DefaultMacroFinder(reference)
					.findByName(m_comboEventList.getText());
			if (_select != null && _select instanceof MacroEventNode) {
				m_argsPanel.clearArgument();
				MacroEventNode s = (MacroEventNode) _select;
				for (int i = 0; i < s.getArguments().size(); i++) {
					m_argsPanel.addArgument(s.getArguments().get(i));
				}
			}
			return;
		}

		if (ref instanceof ComponentNode) {
			ComponentNode reference = (ComponentNode) ref;
			List<IEvent> m_eventList = EventModelFactory.getDefault()
					.getEvents(SwingComponent.create(reference.getType()));
			Iterator<IEvent> eite = m_eventList.iterator();
			// sort events
			while (eite.hasNext()) {
				IEvent _event = eite.next();
				if (_event.getName().equals(m_comboEventList.getText())) {
					m_argsPanel.clearArgument();
					for (int i = 0; i < _event.getArguments().size(); i++) {
						m_argsPanel.addArgument(_event.getArguments().get(i));
					}
				}
			}

			return;
		}
	}

	private void updateComboAndArguPanelBySelectNode() {
		if (m_selectNode == null)
			return;

		if (m_selectNode instanceof MacroComponentNode) {
			MacroComponentNode select = (MacroComponentNode) m_selectNode;
			// update combo
			String def = "";
			m_comboEventList.removeAll();
			for (int i = 0; i < select.size(); i++) {
				if (select.get(i) instanceof MacroEventNode) {
					MacroEventNode child = (MacroEventNode) select.get(i);
					if (def == "") {
						def = child.getName();
						// update argument panel
						m_argsPanel.clearArgument();
						for (int j = 0; j < child.getArguments().size(); j++)
							m_argsPanel
									.addArgument(child.getArguments().get(j));
					}
					m_comboEventList.add(child.getName());
				}
			}
			m_comboEventList.setText(def);
			return;
		}

		if (m_selectNode instanceof ComponentNode) {
			ComponentNode select = (ComponentNode) m_selectNode;
			List<IEvent> m_eventList = m_EventModel.getEvents(SwingComponent
					.create(select.getType()));

			Iterator<IEvent> eite = m_eventList.iterator();
			// sort events
			String def = "";
			while (eite.hasNext()) {
				IEvent _event = eite.next();
				if (def == "") {
					m_argsPanel.clearArgument();
					def = _event.getName();
					for (int i = 0; i < _event.getArguments().size(); i++) {
						Argument arg = _event.getArguments().get(i);
						m_argsPanel.addArgument(arg);
					}
				}
				m_comboEventList.add(_event.getName());
			}
			m_comboEventList.setText(def);
			return;
		}
	}

	private void updateArgPanelBySelectNode() {
		if (m_selectNode == null)
			return;

		if (m_selectNode instanceof MacroComponentNode) {
			MacroComponentNode reference = (MacroComponentNode) m_selectNode;
			AbstractMacroNode _select = new DefaultMacroFinder(reference)
					.findByName(m_comboEventList.getText());

			if (_select != null && _select instanceof MacroEventNode) {
				m_argsPanel.clearArgument();
				MacroEventNode s = (MacroEventNode) _select;
				for (int i = 0; i < s.getArguments().size(); i++) {
					m_argsPanel.addArgument(s.getArguments().get(i));
				}
			}
			return;
		}

		if (m_selectNode instanceof ComponentNode) {
			ComponentNode reference = (ComponentNode) m_selectNode;
			List<IEvent> elist = m_EventModel.getEvents(SwingComponent
					.create(reference.getType()));
			Iterator<IEvent> eite = elist.iterator();
			// sort events
			while (eite.hasNext()) {
				IEvent event = eite.next();
				if (event.getName().equals(m_comboEventList.getText())) {
					m_argsPanel.clearArgument();
					for (int i = 0; i < event.getArguments().size(); i++) {
						Argument arg = event.getArguments().get(i);
						m_argsPanel.addArgument(arg);
					}
				}
			}
			return;
		}
	}
}
