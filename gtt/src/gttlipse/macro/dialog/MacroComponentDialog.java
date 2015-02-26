package gttlipse.macro.dialog;

import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.MacroComponentNode;
import gttlipse.editor.ui.EventCoverPanel;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class MacroComponentDialog extends TitleAreaDialog {
	private MacroComponentNode m_node;
	private Text m_macroComponentNodeName;
	private EventCoverPanel m_eventCoverPanel;
	
	public MacroComponentDialog(Shell parentShell, MacroComponentNode node) {
		super(parentShell);
		m_node = node;
	}
	
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Macro Component Node");
		setMessage("Edit Macro Component Node");
		
		final Composite area = new Composite(parent, SWT.NULL);

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;
		
		area.setLayout(tabLayout);
		
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		
		Composite selectComponent = new Composite(area, SWT.NONE);
		selectComponent.setLayout(gridlayout);

		GridData textFildData = new GridData();
		textFildData.widthHint = 300;
		
		Label _labelMacroComponentNodeName = new Label(selectComponent, SWT.NULL);
		_labelMacroComponentNodeName.setText("Macro Component Name:");
		
		m_macroComponentNodeName = new Text(selectComponent, SWT.NULL);
		m_macroComponentNodeName.setLayoutData(textFildData);
		m_macroComponentNodeName.setText(m_node.getName());
		
		m_eventCoverPanel = new EventCoverPanel(area);
		setup();
		
		return parent;
	}
	
	private void setup() {
		EventCoverage c = m_node.getEventCoverage();
		m_eventCoverPanel.clear();
		Set<String> s = c.getEvents();
		Iterator<String> ite = s.iterator();
		while( ite.hasNext() ) {
			String event = (String)ite.next();
			boolean select = c.isNeedToCover(event);
			m_eventCoverPanel.add(select, event);
		}
	}
	
	protected void okPressed() {
		m_node.setName(m_macroComponentNodeName.getText());

		EventCoverage c = m_node.getEventCoverage();
		for(int i=0;i<m_eventCoverPanel.getItemCount();i++) {
			String event = m_eventCoverPanel.getEvent(i);
			boolean select = m_eventCoverPanel.getSelect(i);
			c.addNeedToCover(event, select);
		}

		super.okPressed();
	}
}