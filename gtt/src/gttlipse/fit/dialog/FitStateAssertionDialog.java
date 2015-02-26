package gttlipse.fit.dialog;

import gttlipse.fit.FixtureDefinition;
import gttlipse.fit.node.FitStateAssertionNode;
import gttlipse.fit.view.GTTFitView;
import gttlipse.fit.view.GTTFitViewDefinition;
import gttlipse.fit.view.SWTResourceManager;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;


public class FitStateAssertionDialog extends TitleAreaDialog implements SelectionListener {
	Combo m_windowTypeCombo;
	Combo m_windowTitleCombo;
	String m_projectPath;
	Text m_fitStateAssertinoNameText;
	Combo m_tableCombo;
	FitStateAssertionNode m_fitStateAssertionNode;
	
	public FitStateAssertionDialog(Shell parentShell, FitStateAssertionNode node) {
		super(parentShell);
		m_fitStateAssertionNode = node;
		m_projectPath = node.getProjectPath();
	}
	
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Fit State Assertion Node");
		setTitle("Edit Fit State Assertion Node Dialog");
		setMessage("You can change all infomation of Fit State Assertion node here!");
		
		final GridLayout parentGridLayout = new GridLayout();
		parentGridLayout.verticalSpacing = 7;
		parent.setLayout(parentGridLayout);
		
		Composite settingNodeComposite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		settingNodeComposite.setLayout(gridLayout);
		settingNodeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Label fitNamelabel = new Label(settingNodeComposite, SWT.NONE);
		fitNamelabel.setAlignment(SWT.CENTER);
		final GridData gd_fitNameLabel = new GridData(SWT.FILL, SWT.FILL, false, true);
		fitNamelabel.setLayoutData(gd_fitNameLabel);
		fitNamelabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		fitNamelabel.setText("Name:");

		m_fitStateAssertinoNameText = new Text(settingNodeComposite, SWT.BORDER);
		m_fitStateAssertinoNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		m_fitStateAssertinoNameText.setText("TestFit");

		final Label windowTitleLabel = new Label(settingNodeComposite, SWT.CENTER);
		final GridData gd_windowTitleLabel = new GridData(SWT.FILL, SWT.FILL, false, true);
		windowTitleLabel.setLayoutData(gd_windowTitleLabel);
		windowTitleLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		windowTitleLabel.setText("Window Title:");

		m_windowTitleCombo = new Combo(settingNodeComposite, SWT.BORDER);
		m_windowTitleCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		DialogDefinition.initWindowTitleCombo(m_windowTitleCombo);

		final Label windowTypeLabel = new Label(settingNodeComposite, SWT.CENTER);
		final GridData gd_windowTypeLabel = new GridData(SWT.FILL, SWT.FILL, false, true);
		windowTypeLabel.setLayoutData(gd_windowTypeLabel);
		windowTypeLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		windowTypeLabel.setText("Window Type:");

		m_windowTypeCombo = new Combo(settingNodeComposite, SWT.BORDER);
		m_windowTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		DialogDefinition.initWindowTypeCombo(m_windowTypeCombo);
		
		final Label tableLabel = new Label(settingNodeComposite, SWT.NONE);
		tableLabel.setAlignment(SWT.CENTER);
		tableLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		final GridData gd_tableLabel = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableLabel.setLayoutData(gd_tableLabel);
		tableLabel.setText("Table:");

		m_tableCombo = new Combo(settingNodeComposite, SWT.READ_ONLY);
		final GridData gd_tableCombo = new GridData(SWT.FILL, SWT.FILL, true, false);
		m_tableCombo.setLayoutData(gd_tableCombo);
		initTableCombo(m_tableCombo);
		initInfo();
		return parent;
	}
	
	private void initTableCombo(Combo combo) {
		TreeViewer treeViewer = GTTFitView.getFitView().getTreeViewer();

		combo.removeAll();
		for(int i = 0; i < treeViewer.getTree().getItemCount(); i++) {
			if(treeViewer.getTree().getItem(i).getText().contains(GTTFitViewDefinition.SelectFolder.get(FixtureDefinition.RowFixture))) {
				TreeItem item = treeViewer.getTree().getItem(i);
				for(int j = 0; j <item.getItemCount(); j++)
					combo.add(item.getItem(j).getText());
				
			}
		}
	}

	private IResource getFitTableResource(String name) {
		TreeViewer treeViewer = GTTFitView.getFitView().getTreeViewer();
		for(int i = 0; i < treeViewer.getTree().getItemCount(); i++) {
			if(treeViewer.getTree().getItem(i).getText().contains(GTTFitViewDefinition.SelectFolder.get(GTTFitViewDefinition.RowFixtureSelection))) {
				TreeItem item = treeViewer.getTree().getItem(i);
				for(int j = 0; j <item.getItemCount(); j++)
					if(item.getItem(j).getText().compareTo(name) == 0)
						return (IResource)item.getItem(j).getData();
			}
		}
		return null;
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
				m_fitStateAssertionNode.setProjectPath(m_projectPath);
				m_fitStateAssertionNode.setName(m_fitStateAssertinoNameText.getText());
				m_fitStateAssertionNode.setWindowType(m_windowTypeCombo.getText());
				m_fitStateAssertionNode.setWindowTitle(m_windowTitleCombo.getText());
				m_fitStateAssertionNode.setFitTableSource((getFitTableResource(m_tableCombo.getText()).getFullPath().toFile()));
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

	private void initInfo() {
		if(m_fitStateAssertionNode != null) {
			m_windowTypeCombo.setText(m_fitStateAssertionNode.getWindowType());
			m_windowTitleCombo.setText(m_fitStateAssertionNode.getWindowTitle());
			m_fitStateAssertinoNameText.setText(m_fitStateAssertionNode.getName());
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}

}
