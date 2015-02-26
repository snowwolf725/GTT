package gttlipse.fit.dialog;

import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.fit.node.FitNode;
import gttlipse.fit.view.GTTFitView;
import gttlipse.fit.view.GTTFitViewDefinition;
import gttlipse.fit.view.SWTResourceManager;
import gttlipse.macro.view.MacroPresenter;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
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


public class FitNodeDialog extends TitleAreaDialog implements SelectionListener {
	Text m_fitNameText;
	Combo m_fixtureTypeCombo;
	Combo m_referenceMacroCombo;
	Combo m_tableCombo;
	FitNode m_fitNode;
	String m_projectPath;
	
	public FitNodeDialog(Shell parentShell, FitNode node) {
		super(parentShell);
		m_fitNode = node;
		m_projectPath = node.getProjectPath();
	}
	
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Fit Node");
		setTitle("Edit Fit Node Dialog");
		setMessage("You can change all infomation of Fit node here!");
		
		final GridLayout parentGridLayout = new GridLayout();
		parentGridLayout.verticalSpacing = 7;
		parent.setLayout(parentGridLayout);
		
		Composite settingNodeComposite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		settingNodeComposite.setLayout(gridLayout);
		settingNodeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		final Label fitNamelabel = new Label(settingNodeComposite, SWT.NONE);
		fitNamelabel.setAlignment(SWT.CENTER);
		final GridData gd_fitNameLabel = new GridData(SWT.FILL, SWT.FILL, false, true);
		fitNamelabel.setLayoutData(gd_fitNameLabel);
		fitNamelabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		fitNamelabel.setText("Fit Name:");

		m_fitNameText = new Text(settingNodeComposite, SWT.BORDER);
		m_fitNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		m_fitNameText.setText("TestFit");

		final Label fixtureTypeLabel = new Label(settingNodeComposite, SWT.NONE);
		fixtureTypeLabel.setAlignment(SWT.CENTER);
		final GridData gd_fixtureTypeLabel = new GridData(SWT.FILL, SWT.FILL, false, true);
		fixtureTypeLabel.setLayoutData(gd_fixtureTypeLabel);
		fixtureTypeLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		fixtureTypeLabel.setText("Fixture Type:");

		m_fixtureTypeCombo = new Combo(settingNodeComposite, SWT.READ_ONLY);
		m_fixtureTypeCombo.setItems(new String[] {GTTFitViewDefinition.FixtureTypeDefaultSelection, GTTFitViewDefinition.ColumnFixtureSelection, GTTFitViewDefinition.RowFixtureSelection, GTTFitViewDefinition.ActionFixtureSelection});
		final GridData gd_fixtureTypeCombo = new GridData(SWT.FILL, SWT.FILL, true, true);
		m_fixtureTypeCombo.setLayoutData(gd_fixtureTypeCombo);
		m_fixtureTypeCombo.select(GTTFitViewDefinition.FixtureTypeDefaultSelectionValue);
		m_fixtureTypeCombo.addFocusListener(new FocusAdapter() {
			public void focusGained(final FocusEvent e) {
				if(m_fixtureTypeCombo.getText().compareTo(GTTFitViewDefinition.FixtureTypeDefaultSelection) == 0)
					m_fixtureTypeCombo.remove(GTTFitViewDefinition.FixtureTypeDefaultSelectionValue);
			}
			public void focusLost(final FocusEvent e) {
				if(m_fixtureTypeCombo.getText().compareTo("") == 0)
					m_fixtureTypeCombo.setText(GTTFitViewDefinition.ColumnFixtureSelection);
				initTableCombo(m_tableCombo);
			}
		});

		final Label tableLabel = new Label(settingNodeComposite, SWT.NONE);
		tableLabel.setAlignment(SWT.CENTER);
		tableLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		final GridData gd_tableLabel = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableLabel.setLayoutData(gd_tableLabel);
		tableLabel.setText("Table:");

		m_tableCombo = new Combo(settingNodeComposite, SWT.READ_ONLY);
		final GridData gd_tableCombo = new GridData(SWT.FILL, SWT.FILL, true, false);
		m_tableCombo.setLayoutData(gd_tableCombo);
		

		final Label macroLabel = new Label(settingNodeComposite, SWT.NONE);
		macroLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD | SWT.ITALIC));
		macroLabel.setAlignment(SWT.CENTER);
		final GridData gd_macroLabel = new GridData(SWT.FILL, SWT.FILL, true, false);
		macroLabel.setLayoutData(gd_macroLabel);
		macroLabel.setText("Macro:");

		m_referenceMacroCombo = new Combo(settingNodeComposite, SWT.READ_ONLY);
		final GridData gd_macroCombo = new GridData(SWT.FILL, SWT.FILL, true, false);
		m_referenceMacroCombo.setLayoutData(gd_macroCombo);
		initMacroCombo(m_referenceMacroCombo);
		
		initInfo();
		
		return parent;
	}
	
	private void initTableCombo(Combo combo) {
		TreeViewer treeViewer = GTTFitView.getFitView().getTreeViewer();

		combo.removeAll();
		for(int i = 0; i < treeViewer.getTree().getItemCount(); i++) {
			if(treeViewer.getTree().getItem(i).getText().contains(GTTFitViewDefinition.SelectFolder.get(m_fixtureTypeCombo.getText()))) {
				TreeItem item = treeViewer.getTree().getItem(i);
				for(int j = 0; j <item.getItemCount(); j++)
					combo.add(item.getItem(j).getText());
				
			}
		}
	}

	private IResource getFitTableResource(String name) {
		TreeViewer treeViewer = GTTFitView.getFitView().getTreeViewer();
		for(int i = 0; i < treeViewer.getTree().getItemCount(); i++) {
			if(treeViewer.getTree().getItem(i).getText().contains(GTTFitViewDefinition.SelectFolder.get(m_fixtureTypeCombo.getText()))) {
				TreeItem item = treeViewer.getTree().getItem(i);
				for(int j = 0; j <item.getItemCount(); j++)
					if(item.getItem(j).getText().compareTo(name) == 0)
						return (IResource)item.getItem(j).getData();
			}
		}
		return null;
	}
	
	private void initMacroCombo(Combo combo) {
//		TreeViewer treeViewer = MacroViewPart.getMacroView().getViewer();
//
//		combo.removeAll();
//
//		for(int i = 0; i < treeViewer.getTree().getItemCount(); i++) {
//			if(treeViewer.getTree().getItem(i).getData() instanceof MacroComponentNode) {
//				TreeItem item = treeViewer.getTree().getItem(i);
//				for(int j = 0; j <item.getItemCount(); j++)
//					if(item.getItem(j).getData() instanceof MacroEventNode)
//						combo.add(item.getItem(j).getText());
//			}
//		}
		
		// change by soriel 
		// find same parent macro event
		MacroComponentNode node = (MacroComponentNode) m_fitNode.getParent();
		
		combo.removeAll();
		
		for(int i = 0; i < node.size(); i++) {
			if(node.get(i) instanceof MacroEventNode) {
				combo.add(node.get(i).toString());
			}
		}
	}

	private MacroEventCallerNode getReferenceMacroEventNode(String name) {
//		TreeViewer treeViewer = MacroViewPart.getMacroView().getViewer();
//
//		for(int i = 0; i < treeViewer.getTree().getItemCount(); i++) {
//			if(treeViewer.getTree().getItem(i).getData() instanceof MacroComponentNode) {
//				TreeItem item = treeViewer.getTree().getItem(i);
//				for(int j = 0; j <item.getItemCount(); j++) {
//					if(item.getItem(j).getData() instanceof MacroEventNode && item.getItem(j).getText().compareTo(name) == 0) {
//						String path = ((MacroEventNode)item.getItem(j).getData()).getPath().toString();
//						
//						if (path.startsWith("::"))
//							path = path.substring(2);
//						if (path.endsWith("::"))
//							path = path.substring(0, path.length() - 3);
//						return new MacroEventCallerNode(path);
//					}
//				}
//			}
//		}
//		return null;
		
		// change by soriel
		MacroComponentNode node = (MacroComponentNode) m_fitNode.getParent();
		
		for(int i = 0; i < node.size(); i++) {
			if(node.get(i) instanceof MacroEventNode) {
				if(node.get(i).toString().equals(name)) {
					String path = MacroPresenter.getNodePath(node.get(i));
					return new MacroEventCallerNode(path);
				}
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
				m_fitNode.setProjectPath(m_projectPath);
				m_fitNode.setName(m_fitNameText.getText());
				m_fitNode.setFitTableSource(getFitTableResource(m_tableCombo.getText()).getFullPath().toFile());
				m_fitNode.setMacroEventCallerNode(getReferenceMacroEventNode(m_referenceMacroCombo.getText()));
				m_fitNode.setFixtureType(m_fixtureTypeCombo.getText());
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
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
	}
	
	private void initInfo() {
		if(m_fitNode.getName() != "FIT") {
			m_fitNameText.setText(m_fitNode.getName());
			m_fixtureTypeCombo.setText(m_fitNode.getFixtureType());
			initTableCombo(m_tableCombo);
		}
	}

}
