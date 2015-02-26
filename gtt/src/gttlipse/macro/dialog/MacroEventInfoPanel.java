package gttlipse.macro.dialog;

import gtt.eventmodel.Argument;
import gttlipse.editor.ui.ArgumentPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;


public class MacroEventInfoPanel {
	private Text m_nodeName = null;
	private ArgumentPanel m_argumentPanel = null;
	
	// uhsing 2010/12/27 (For customized table header)
	private Button _btnAuto = null;
	
	public MacroEventInfoPanel(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		area.setLayout(gridlayout);

		GridData gd = new GridData();
		gd.heightHint = 300;
		gd.widthHint = 450;
		area.setLayoutData(gd);

		GridData textFildData = new GridData();
		textFildData.widthHint = 300;

		Group nodeInfo = new Group(area, SWT.NULL);
		nodeInfo.setLayout(gridlayout);
		nodeInfo.setText("Macro Event Name");

		m_nodeName = new Text(nodeInfo, SWT.NULL);
		m_nodeName.setLayoutData(textFildData);

		m_argumentPanel = new ArgumentPanel(area, true,
				ArgumentPanel.ALL_BUTTON);
		
		// uhsing 2010/12/27 (For customized table header) begin
		Group groupAuto = new Group(area, SWT.NULL);
		groupAuto.setLayout(gridlayout);
		groupAuto.setText("Auto Parsing");
		
		_btnAuto = new Button(groupAuto, SWT.CHECK);
		_btnAuto.setText("Auto Fill With Table Header By Event Arguments");
		_btnAuto.setSelection(false);
		// end
	}

	public void addArgument(Argument arg) {
		m_argumentPanel.addArgument(arg.clone());
	}

	public int getArgCount() {
		return m_argumentPanel.getItemCount();
	}

	public Argument getArgument(int index) {
		return Argument.create(m_argumentPanel.getType(index), m_argumentPanel
				.getName(index), m_argumentPanel.getValue(index));
	}

	public void setNodeName(String name) {
		m_nodeName.setText(name);
	}

	public String getName() {
		return m_nodeName.getText();
	}
	
	// uhsing 2010/12/27 (For customized table header) begin
	public void setAutoState(boolean state) {
		_btnAuto.setSelection(state);
	}
	
	public boolean isAutoParsing() {
		return _btnAuto.getSelection();
	}
	// end
}