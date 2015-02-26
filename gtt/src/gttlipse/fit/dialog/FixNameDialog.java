package gttlipse.fit.dialog;

import gttlipse.fit.node.FixNameNode;
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


public class FixNameDialog extends TitleAreaDialog implements SelectionListener {
	private Text m_textOfName;
	Text m_textOfComponentName;
	FixNameNode m_generationNode;
	
	public FixNameDialog(Shell parentShell, FixNameNode node) {
		super(parentShell);
		m_generationNode = node;
	}
	
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Fix Name Node");
		setTitle("Edit Fix Name Node Dialog");
		setMessage("You can change all infomation of Fix Name node here!");

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

		final Label nameLabel = new Label(nodeInfoComp, SWT.CENTER);
		nameLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_nameLabel = new GridData(SWT.FILL, SWT.CENTER, true, true);
		nameLabel.setLayoutData(gd_nameLabel);
		nameLabel.setText("Name:");

		m_textOfName = new Text(nodeInfoComp, SWT.BORDER);
		m_textOfName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label componentNameLabel = new Label(nodeInfoComp, SWT.CENTER);
		componentNameLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_componentNameLabel = new GridData(SWT.FILL, SWT.CENTER, true, true);
		componentNameLabel.setLayoutData(gd_componentNameLabel);
		componentNameLabel.setText("Component Name:");
		componentNameLabel.setBounds(0, 0, 26, 12);

		m_textOfComponentName = new Text(nodeInfoComp, SWT.BORDER);
		m_textOfComponentName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

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
				m_generationNode.setName(m_textOfName.getText());
				m_generationNode.setComponentName(m_textOfComponentName.getText());
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
		if(m_generationNode != null) {
			m_textOfComponentName.setText(m_generationNode.getComponentName());
			m_textOfName.setText(m_generationNode.getName());
		}
	}

}
