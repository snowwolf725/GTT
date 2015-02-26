package gttlipse.macro.dialog;

import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.ComponentNode;
import gttlipse.editor.ui.EventCoverPanel;
import gttlipse.editor.ui.WebComponentInfoPanel;

import java.util.Iterator;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;


public class WebComponentDialog extends TitleAreaDialog implements
		ModifyListener {
	private ComponentNode m_node;
	private WebComponentInfoPanel m_componentInfoPanel;
	private EventCoverPanel m_eventCoverPanel;
	private Label m_coverage;
	private Text _nodeName;

	public WebComponentDialog(Shell parentShell, ComponentNode node) {
		super(parentShell);
		m_node = node;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Web Component Node");
		setMessage("Edit Web Component Node");

		getShell().setText("Edit Web Component Event");
		setMessage("Edit Web Component Event");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;

		// ≠∂≈“
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);

		// Component≠∂≈“
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Web Component");
		Composite selectComponent = new Composite(tabFolder, SWT.NONE);
		item1.setControl(selectComponent);
		selectComponent.setLayout(tabLayout);

		// ComponentNode name
		// layout
		final GridLayout nameLayout = new GridLayout();
		nameLayout.numColumns = 2;
		GridData textFildData = new GridData();
		textFildData.widthHint = 320;
		GridData nameLabelData = new GridData();
		nameLabelData.widthHint = 115;
		// group
		Group group = new Group(selectComponent, SWT.SHADOW_ETCHED_IN);
		group.setLayout(nameLayout);
		group.setText("ComponentNode Information");
		// name label
		Label lblName = new Label(group, SWT.NULL);
		lblName.setText("Node Name: ");
		lblName.setLayoutData(nameLabelData);
		// name text
		_nodeName = new Text(group, SWT.BORDER);
		_nodeName.setLayoutData(textFildData);
		_nodeName.setText(m_node.getName());

		m_componentInfoPanel = new WebComponentInfoPanel(selectComponent,
				m_node.getComponent());
		m_componentInfoPanel.setListener(this);

		// Event≠∂≈“
		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText("Event Cover");
		Composite editEvent = new Composite(tabFolder, SWT.NONE);
		item2.setControl(editEvent);
		editEvent.setLayout(tabLayout);

		m_eventCoverPanel = new EventCoverPanel(editEvent);
		m_eventCoverPanel.getTableModel().setBGHook(this);

		m_coverage = new Label(editEvent, SWT.NULL);

		setup();

		return parent;
	}

	private void setup() {
		EventCoverage c = m_node.getEventCoverage();
		m_eventCoverPanel.clear();

		Iterator<String> ite = c.getEvents().iterator();

		while (ite.hasNext()) {
			String e = ite.next();
			boolean select = c.isNeedToCover(e);
			m_eventCoverPanel.add(select, e);
		}
		m_coverage.setText(c.getCoverageString());
	}

	protected void okPressed() {
		m_componentInfoPanel.update();

		EventCoverage c = m_node.getEventCoverage();
		for (int i = 0; i < m_eventCoverPanel.getItemCount(); i++) {
			String event = m_eventCoverPanel.getEvent(i);
			boolean select = m_eventCoverPanel.getSelect(i);
			c.addNeedToCover(event, select);
		}

		// set ComponentNode name
		m_node.setName(_nodeName.getText());

		// debug();
		super.okPressed();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		m_componentInfoPanel.update();
		m_node.getEventCoverage().setup();
		setup();
	}

	public Color getRenderColor(String event) {
		return m_node.getEventCoverage().getRenderColor(event);
	}
}
