package gttlipse.fit.dialog;

import gttlipse.fit.node.SplitDataAsNameNode;
import gttlipse.fit.view.SWTResourceManager;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;


public class SplitDataAsNameDialog extends TitleAreaDialog implements SelectionListener {
	Text m_textOfNodeName;
	Text m_variableNameOfData;
	SplitDataAsNameNode m_splitDataAsNameNode;
	
	public SplitDataAsNameDialog(Shell parentShell, SplitDataAsNameNode node) {
		super(parentShell);
		m_splitDataAsNameNode = node;
	}
	
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Split Data As Name Node");
		setTitle("Edit Split Data As Name Node Dialog");
		setMessage("You can change all infomation of Split Data As Name node here!");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 2;
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		final GridData gd_tabFolder = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem nodeInfoItem = new TabItem(tabFolder, SWT.NONE);
		nodeInfoItem.setText("Node Infomation");
		
		Composite nodeInfoComp = new Composite(tabFolder, SWT.NONE);
		nodeInfoItem.setControl(nodeInfoComp);
		nodeInfoComp.setLayout(tabLayout);

		final Label nodeNamelabel = new Label(nodeInfoComp, SWT.CENTER);
		nodeNamelabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		nodeNamelabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		nodeNamelabel.setText("Name:");

		m_textOfNodeName = new Text(nodeInfoComp, SWT.BORDER);
		m_textOfNodeName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label dataLabel = new Label(nodeInfoComp, SWT.CENTER);
		dataLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		dataLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		dataLabel.setText("Name of Variable :");
		dataLabel.setBounds(0, 0, 26, 12);

		m_variableNameOfData = new Text(nodeInfoComp, SWT.BORDER);
		m_variableNameOfData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		initInfo();
		
		return parent;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_modify = createButton(parent, SWT.Modify, "Modify", true);
		btn_modify.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				m_splitDataAsNameNode.setName(m_textOfNodeName.getText());
				m_splitDataAsNameNode.setVariable(m_variableNameOfData.getText());
				setReturnCode(SWT.Modify);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		System.out.println("Selected");
	}

	private void initInfo() {
		if(m_splitDataAsNameNode != null) {
			m_textOfNodeName.setText(m_splitDataAsNameNode.getName());
			m_variableNameOfData.setText(m_splitDataAsNameNode.getVariable());
		}
	}
}
