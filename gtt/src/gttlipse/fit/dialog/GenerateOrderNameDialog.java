package gttlipse.fit.dialog;

import gtt.eventmodel.Argument;
import gttlipse.editor.ui.ArgumentPanel;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.view.SWTResourceManager;

import java.util.Iterator;

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


public class GenerateOrderNameDialog extends TitleAreaDialog implements SelectionListener {
	private Text m_textOfName;
	Text m_variableNameOfEnd;
	Text m_variableNameOfStart;
	Text m_textOfSuffix;
	Text m_textOfPrefix;
	GenerateOrderNameNode m_generationNode;
	ArgumentPanel m_argumentPanel;
	
	public GenerateOrderNameDialog(Shell parentShell, GenerateOrderNameNode node) {
		super(parentShell);
		m_generationNode = node;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Generate Order Name Node");
		setTitle("Edit Generate Order Name Node Dialog");
		setMessage("You can change all infomation of Generate Order Name node here!");

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
		final GridData gd_nameLabel = new GridData(SWT.FILL, SWT.CENTER, true, false);
		nameLabel.setLayoutData(gd_nameLabel);
		nameLabel.setText("Name:");

		m_textOfName = new Text(nodeInfoComp, SWT.BORDER);
		m_textOfName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label prefixLable = new Label(nodeInfoComp, SWT.CENTER);
		prefixLable.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_prefixLable = new GridData(SWT.FILL, SWT.CENTER, true, true);
		prefixLable.setLayoutData(gd_prefixLable);
		prefixLable.setText("Prefix :");
		prefixLable.setBounds(0, 0, 26, 12);

		m_textOfPrefix = new Text(nodeInfoComp, SWT.BORDER);
		m_textOfPrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		final Label suffixLabel = new Label(nodeInfoComp, SWT.CENTER);
		suffixLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_suffixLable = new GridData(SWT.FILL, SWT.CENTER, true, true);
		suffixLabel.setLayoutData(gd_suffixLable);
		suffixLabel.setText("Suffix:");

		m_textOfSuffix = new Text(nodeInfoComp, SWT.BORDER);
		m_textOfSuffix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label variableNameOfStartLable = new Label(nodeInfoComp, SWT.CENTER);
		variableNameOfStartLable.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_variableNameOfStartLable = new GridData(SWT.FILL, SWT.CENTER, true, true);
		variableNameOfStartLable.setLayoutData(gd_variableNameOfStartLable);
		variableNameOfStartLable.setText("Variable of Start:");

		m_variableNameOfStart = new Text(nodeInfoComp, SWT.BORDER);
		m_variableNameOfStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label variableNameOfEndLabel = new Label(nodeInfoComp, SWT.CENTER);
		variableNameOfEndLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		variableNameOfEndLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_variableNameOfEndLabel = new GridData(SWT.FILL, SWT.CENTER, true, true);
		variableNameOfStartLable.setLayoutData(gd_variableNameOfEndLabel);
		variableNameOfEndLabel.setText("Variable Of End:");

		m_variableNameOfEnd = new Text(nodeInfoComp, SWT.BORDER);
		m_variableNameOfEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final TabItem argumentTabItem = new TabItem(tabFolder, SWT.NONE);
		argumentTabItem.setText("Args Infomation");
		final Composite argumentComposite = new Composite(tabFolder, SWT.NONE);
		argumentComposite.setLayout(new GridLayout());
		argumentTabItem.setControl(argumentComposite);
		initArgumentInfo(argumentComposite);

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
				m_generationNode.setPrefix(m_textOfPrefix.getText());
				m_generationNode.setSuffix(m_textOfSuffix.getText());
				m_generationNode.setVariableNameOfStart(m_variableNameOfStart.getText());
				m_generationNode.setVariableNameOfEnd(m_variableNameOfEnd.getText());
				m_generationNode.getArguments().clear();
				for (int i = 0; i < m_argumentPanel.getItemCount(); i++)
					m_generationNode.getArguments().add(Argument.create(m_argumentPanel.getType(i), m_argumentPanel.getName(i), m_argumentPanel.getValue(i)));
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

	private void initArgumentInfo(Composite parent) {
		m_argumentPanel = new ArgumentPanel(parent, true, ArgumentPanel.ALL_BUTTON);
		m_argumentPanel.clearArgument();
		Iterator<Argument> ite = m_generationNode.getArguments().iterator();
		while (ite.hasNext()) {
			m_argumentPanel.addArgument(ite.next());
		}
	}

	private void initInfo() {
		if(m_generationNode != null) {
			m_variableNameOfEnd.setText(m_generationNode.getVariableNameOfEnd());
			m_variableNameOfStart.setText(m_generationNode.getVariableNameOfStart());
			m_textOfSuffix.setText(m_generationNode.getSuffix());
			m_textOfPrefix.setText(m_generationNode.getPrefix());
			m_textOfName.setText(m_generationNode.getName());
		}
	}
}
