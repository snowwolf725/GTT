package gttlipse.fit.dialog;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.macro.macroStructure.ComponentNode;
import gttlipse.fit.node.FitAssertionNode;
import gttlipse.fit.view.SWTResourceManager;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;


public class FitAssertionDialog extends TitleAreaDialog implements SelectionListener {
	Text m_textOfDataVariable;
	Combo m_comboOfWindowTitle;
	Combo m_comboOfWindowType;
	Combo m_comboOfGenerationalKey;
	Combo m_comboOfComponentType;
	FitAssertionNode m_fitAssertionNode;
	AssertionPanel m_assertionPanel;
	List<String> m_generationList;

	public FitAssertionDialog(Shell parentShell, FitAssertionNode node, List<String> generationList) {
		super(parentShell);
		m_fitAssertionNode = node;
		m_generationList = generationList;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Fit Assertion Node");
		setTitle("Edit Fit Assertion Node Dialog");
		setMessage("You can change all infomation of Fit Assertion node here!");

		final GridLayout componentLayout = new GridLayout();
		componentLayout.numColumns = 2;
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		final GridData gd_tabFolder = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem componentItem = new TabItem(tabFolder, SWT.NONE);
		componentItem.setText("Component Information");

		final Composite componentComp = new Composite(tabFolder, SWT.NONE);
		componentItem.setControl(componentComp);
		componentComp.setLayout(componentLayout);

		TabItem assertionItem = new TabItem(tabFolder, SWT.NONE);
		assertionItem.setText("Assertion Information");

		final Composite assertionComp = new Composite(tabFolder, SWT.NONE);
		assertionComp.setLayout(new GridLayout());
		assertionItem.setControl(assertionComp);
		m_assertionPanel = new AssertionPanel(assertionComp, new FitAssertionNode());

		final Label windowTypeLabel = new Label(componentComp, SWT.CENTER);
		windowTypeLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		windowTypeLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		windowTypeLabel.setText("Window Type:");

		m_comboOfWindowType = new Combo(componentComp, SWT.BORDER);
		m_comboOfWindowType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		m_comboOfWindowType.setText(DialogDefinition.DEFAULTWINDOWTYPE);
		DialogDefinition.initWindowTypeCombo(m_comboOfWindowType);

		final Label windowTitleLabel = new Label(componentComp, SWT.CENTER);
		windowTitleLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		windowTitleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		windowTitleLabel.setText("Window Title:");

		m_comboOfWindowTitle = new Combo(componentComp, SWT.BORDER);
		m_comboOfWindowTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		DialogDefinition.initWindowTitleCombo(m_comboOfWindowTitle);

		final Label componentTypeLabel = new Label(componentComp, SWT.CENTER);
		componentTypeLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_componentTypeLabel = new GridData(SWT.FILL, SWT.CENTER, true, true);
		componentTypeLabel.setLayoutData(gd_componentTypeLabel);
		componentTypeLabel.setText("Component Type:");

		m_comboOfComponentType = new Combo(componentComp, SWT.READ_ONLY);
		m_comboOfComponentType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		m_comboOfComponentType.add("");
		List<IComponent> coms = EventModelFactory.getDefault().getComponents();
		Iterator<?> ite = coms.iterator();
		while (ite.hasNext()) {
			IComponent c = (IComponent) ite.next();
			m_comboOfComponentType.add(c.getType());
		}
		m_comboOfComponentType.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if(m_comboOfComponentType.getText() != "") {
					ComponentNode comp = ComponentNode.create();
					comp.setType(m_comboOfComponentType.getText());
					m_assertionPanel.setSelectNode(comp);
				}
			}
		});

		final Label dataVariablelabel = new Label(componentComp, SWT.CENTER);
		dataVariablelabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		final GridData gd_dataVariableLabel = new GridData(SWT.FILL, SWT.CENTER, true, true);
		dataVariablelabel.setLayoutData(gd_dataVariableLabel);
		dataVariablelabel.setText("Assertion Data Variable:");

		m_textOfDataVariable = new Text(componentComp, SWT.BORDER);
		m_textOfDataVariable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label typeLabel = new Label(componentComp, SWT.CENTER);
		typeLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		typeLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		typeLabel.setText("Generation Node:");

		m_comboOfGenerationalKey = new Combo(componentComp, SWT.READ_ONLY);
		m_comboOfGenerationalKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		m_comboOfGenerationalKey.add("");
		Iterator<String> iterator = m_generationList.iterator();
		while(iterator.hasNext()) {
			String item = (String)iterator.next();
			m_comboOfGenerationalKey.add(item);
		}

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
				m_fitAssertionNode.setComponentType(m_comboOfComponentType.getText());
				m_fitAssertionNode.setGenerationKey(m_comboOfGenerationalKey.getText());
				m_fitAssertionNode.setWindowType(m_comboOfWindowType.getText());
				m_fitAssertionNode.setWindowTitle(m_comboOfWindowTitle.getText());
				m_fitAssertionNode.setAssertionDataVariable(m_textOfDataVariable.getText());
				m_fitAssertionNode.getAssertion().setMethod(m_assertionPanel.getMethod());
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
		if(m_fitAssertionNode!= null) {
			m_comboOfGenerationalKey.setText(m_fitAssertionNode.getGenerationKey());
			m_comboOfWindowType.setText(m_fitAssertionNode.getWindowType());
			m_comboOfWindowTitle.setText(m_fitAssertionNode.getWindowTitle());
			m_textOfDataVariable.setText(m_fitAssertionNode.getAssertionDataVariable());
//			m_comboOfComponentType.setText(m_fitAssertionNode.getComponentType());
		}
	}

}
