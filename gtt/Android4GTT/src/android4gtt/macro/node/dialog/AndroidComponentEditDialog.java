package android4gtt.macro.node.dialog;

import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.ComponentNode;
import gttlipse.editor.ui.EventCoverPanel;

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


public class AndroidComponentEditDialog extends TitleAreaDialog implements ModifyListener {
	private ComponentNode m_node;
	private AndroidComponentInfoPanel m_componentInfoPanel;
	private EventCoverPanel m_eventCoverPanel;
	
	private Label m_coverage;
	private Text _nodeName;
	
	public AndroidComponentEditDialog(Shell parentShell, ComponentNode node) {
		super(parentShell);
		m_node = (ComponentNode) node;
	}
	
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Android Component Node");
		setMessage("Edit Android Component Node");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;

		// ≠∂≈“
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);

		// Component≠∂≈“
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Component");
		Composite selectComponent = new Composite(tabFolder, SWT.NONE);
		item1.setControl(selectComponent);
		selectComponent.setLayout(tabLayout);

		// layout
		final GridLayout componentLayout = new GridLayout();
		componentLayout.numColumns = 2;
		GridData textFildData = new GridData();
		textFildData.widthHint = 320;
		GridData labelData = new GridData();
		labelData.widthHint = 115;
		
		// ComponentNode Information
		// group
		Group group1 = new Group(selectComponent, SWT.SHADOW_ETCHED_IN);
		group1.setLayout(componentLayout);
		group1.setText("ComponentNode Information");
		// Node Name
		Label lblName = new Label(group1, SWT.NULL);
		lblName.setText("Node Name: ");
		lblName.setLayoutData(labelData);
		_nodeName = new Text(group1, SWT.BORDER);
		_nodeName.setText(m_node.getName());
		_nodeName.setLayoutData(textFildData);	
		
		// Component Information
		m_componentInfoPanel = new AndroidComponentInfoPanel(selectComponent, m_node
				.getComponent());
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
		m_eventCoverPanel.clear();

		EventCoverage coverage = m_node.getEventCoverage();
		Iterator<String> ite = coverage.getEvents().iterator();

		while (ite.hasNext()) {
			String event = ite.next();
			boolean select = coverage.isNeedToCover(event);
			m_eventCoverPanel.add(select, event);
		}
		m_coverage.setText(coverage.getCoverageString());
	}

	protected void okPressed() {
		m_componentInfoPanel.update();
		updateNeedToCoverEvent();
		updateNodeName();
		super.okPressed();
	}

	private void updateNodeName() {
		// set ComponentNode name
		if (_nodeName.getText().equals("")) {
			m_node.setName(m_componentInfoPanel.getComponent().getName());
		} else {
			m_node.setName(_nodeName.getText());
		}
	}

	private void updateNeedToCoverEvent() {
		EventCoverage c = m_node.getEventCoverage();
		for (int i = 0; i < m_eventCoverPanel.getItemCount(); i++) {
			String event = m_eventCoverPanel.getEvent(i);
			boolean select = m_eventCoverPanel.getSelect(i);
			c.addNeedToCover(event, select);
		}
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
