package gttlipse.macro.dialog;

import gtt.eventmodel.Argument;
import gtt.macro.DefaultMacroFinder;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.editor.ui.ArgumentPanel;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;


public class MacroEventCallerPanel {
	private Group m_group = null;
	private Combo m_eventList = null;
	//Argument's Panel
	private ArgumentPanel m_argsPanel = null;

	private MacroEventCallerNode m_editNode;
	private MacroComponentNode m_selectNode;

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
	}

	public MacroEventCallerPanel(Composite parent,
			MacroEventCallerNode editnode) {
		m_editNode = editnode;
		m_selectNode = getLocalParent();

		initPanel(parent);
		updateEventList();
		updateArg();
	}

	private MacroComponentNode getLocalParent() {
		MacroEventNode ref = (MacroEventNode) m_editNode.getReference();

		if (ref == null)
			return null;
		if (ref.getParent() == null)
			return null;
		if (!(ref.getParent() instanceof MacroComponentNode))
			return null;

		return (MacroComponentNode) ref.getParent();
	}

	private void updateEventList() {
		if (m_editNode == null || m_selectNode == null)
			return;

		MacroComponentNode parent = getLocalParent();
		if (parent == null)
			return;

		m_eventList.removeAll();

		// 選到的node與正在編輯的node是同一個
		if (parent.getPath().toString().equals(
				m_selectNode.getPath().toString())) {
			List<MacroEventNode> events = parent.getMacroEvents();

			for (int i = 0; i < events.size(); i++) {
				MacroEventNode child = events.get(i);
				m_eventList.add(child.getName());
			}

			m_eventList.setText(m_editNode.getReference().getName());

		} else {
			List<MacroEventNode> events = m_selectNode.getMacroEvents();

			String def = "";
			for (int i = 0; i < events.size(); i++) {
				MacroEventNode child = events.get(i);

				if (def == "")
					def = child.getName();

				m_eventList.add(child.getName());
			}

			m_eventList.setText(def);
		}
	}

	private void updateArg() {
		if (m_editNode == null || m_selectNode == null)
			return;

		MacroComponentNode parent = getLocalParent();
		if (parent == null)
			return;

		m_argsPanel.clearArgument();

		// 選到的node與正在編輯的node是同一個
		if (parent.getPath().equals(m_selectNode.getPath())) {
			if (m_editNode.getReference().getName().equals(
					m_eventList.getText())) {
				if (m_editNode.getArguments().size() == 0) {
					MacroEventNode ref = (MacroEventNode) m_editNode
							.getReference();
					for (int i = 0; i < ref.getArguments().size(); i++) {
						Argument arg = ref.getArguments().get(i);
						m_argsPanel.addArgument(arg);
					}
				} else {
					for (int i = 0; i < m_editNode.getArguments().size(); i++) {
						Argument arg = m_editNode.getArguments().get(i);
						m_argsPanel.addArgument(arg);
					}
				}
			} else {
				MacroEventNode node = (MacroEventNode) new DefaultMacroFinder(
						parent).findByName(m_eventList.getText());
				if (node != null) {
					for (int i = 0; i < node.getArguments().size(); i++) {
						Argument arg = node.getArguments().get(i);
						m_argsPanel.addArgument(arg);
					}
				}
			}
		} else {
			MacroEventNode select = (MacroEventNode) new DefaultMacroFinder(
					m_selectNode).findByName(m_eventList.getText());
			for (int i = 0; i < select.getArguments().size(); i++) {
				Argument arg = select.getArguments().get(i);
				m_argsPanel.addArgument(arg);
			}
		}
	}

	public String getSelectEventType() {
		return m_eventList.getText();
	}

	public int getArgCount() {
		return m_argsPanel.getItemCount();
	}

	public Argument getArgument(int index) {
		return Argument.create(m_argsPanel.getType(index), m_argsPanel
				.getName(index), m_argsPanel.getValue(index));
	}

	public void updateSelectNode(MacroComponentNode selectnode) {
		m_selectNode = selectnode;
		updateEventList();
		updateArg();
	}
}
