package gttlipse.macro.dialog;

import gtt.eventmodel.Argument;
import gtt.macro.EventCoverage;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;

import java.util.Iterator;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class MacroEventDialog extends TitleAreaDialog {
	private MacroEventNode m_node = null;
	private MacroEventInfoPanel m_infoPanel = null;
	private ContractPanel m_ContractPanel = null;

	public MacroEventDialog(Shell parentShell, MacroEventNode node) {
		super(parentShell);
		m_node = node;
	}

	private void initMacroEventInfo(Composite parent) {
		m_infoPanel = new MacroEventInfoPanel(parent);

		// init args
		Iterator<Argument> ite = m_node.getArguments().iterator();
		while (ite.hasNext()) {
			m_infoPanel.addArgument(ite.next());
		}

		// init name
		m_infoPanel.setNodeName(m_node.getName());
		
		// uhsing 2010/12/27 (For customized table header)
		// init auto parsing
		m_infoPanel.setAutoState(m_node.isAutoParsing());
	}

	private void initContractPanel(Composite parent) {
		m_ContractPanel = new ContractPanel(parent);
		m_ContractPanel.setContract(m_node.getContract());
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Macro Event Node");
		setMessage("Macro Event Node");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;

		// 頁籤
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);

		// MacroEventNode Info 頁籤
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Infomaction");
		Composite info = new Composite(tabFolder, SWT.NONE);
		item1.setControl(info);
		info.setLayout(tabLayout);

		initMacroEventInfo(info);

		// MacroEventNode Contract 頁籤
		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText("Contract");
		Composite contract = new Composite(tabFolder, SWT.NONE);
		item2.setControl(contract);
		contract.setLayout(tabLayout);

		initContractPanel(contract);

		return parent;
	}

	protected void okPressed() {
		// 名字有變更
		if (!m_node.getName().equals(m_infoPanel.getName())) {
			if (m_node.getParent() != null) {
				if (m_node.getParent() instanceof MacroComponentNode) {
					MacroComponentNode parent = (MacroComponentNode) m_node
							.getParent();
					EventCoverage c = parent.getEventCoverage();
					c.removeNeedToCover(m_node.getName());
					c.addNeedToCover(m_infoPanel.getName(), true);
				}
			}
		}

		m_node.setName(m_infoPanel.getName());
		m_node.getArguments().clear();
		for (int i = 0; i < m_infoPanel.getArgCount(); i++) {
			m_node.getArguments().add(m_infoPanel.getArgument(i));
		}

		m_node.setContract(m_ContractPanel.getContract());
		// m_node.getContract().setAction(m_ContractPanel.getAction());
		// m_node.getContract().setPostCondition(m_ContractPanel.getPostCond());

		// uhsing 2010/12/27 (For customized table header)
		m_node.setAutoState(m_infoPanel.isAutoParsing());
		
		super.okPressed();
	}
}
