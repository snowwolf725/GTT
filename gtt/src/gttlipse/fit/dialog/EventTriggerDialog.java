package gttlipse.fit.dialog;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.swing.SwingModel;
import gttlipse.fit.node.EventTriggerNode;
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


public class EventTriggerDialog extends TitleAreaDialog implements SelectionListener {
	Combo m_comboOfWindowTitle;
	Combo m_comboOfWindowType;
	Combo m_comboOfGenerationalKey;
	Text m_textOfData;
	Combo m_comboOfComponentType;
	EventTriggerNode m_eventTriggerNode;
	MultiEventPanel m_multiEventPanel;
	List<String> m_generationList;

	public EventTriggerDialog(Shell parentShell, EventTriggerNode node, List<String> generationList) {
		super(parentShell);
		m_eventTriggerNode = node;
		m_generationList = generationList;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Event Trigger Node");
		setTitle("Edit Event Trigger Node Dialog");
		setMessage("You can change all infomation of event trigger node here!");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 2;
		
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		final GridData gd_tabFolder = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem nodeInfoItem = new TabItem(tabFolder, SWT.NONE);
		nodeInfoItem.setText("Component Infomation");

		final TabItem eventItem = new TabItem(tabFolder, SWT.NONE);
		eventItem.setText("Event Infomation");

		final Composite eventComposite = new Composite(tabFolder, SWT.NONE);
		eventItem.setControl(eventComposite);
		
		m_multiEventPanel = new MultiEventPanel(eventComposite);
		
		Composite nodeInfoComp = new Composite(tabFolder, SWT.NONE);
		nodeInfoItem.setControl(nodeInfoComp);
		nodeInfoComp.setLayout(tabLayout);

		final Label windowTypeLabel = new Label(nodeInfoComp, SWT.CENTER);
		windowTypeLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		windowTypeLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		windowTypeLabel.setText("Window Type");

		m_comboOfWindowType = new Combo(nodeInfoComp, SWT.BORDER);
		m_comboOfWindowType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		m_comboOfWindowType.setText(DialogDefinition.DEFAULTWINDOWTYPE);
		DialogDefinition.initWindowTypeCombo(m_comboOfWindowType);

		final Label windowTitleLabel = new Label(nodeInfoComp, SWT.CENTER);
		windowTitleLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		windowTitleLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		windowTitleLabel.setText("Window Title");

		m_comboOfWindowTitle = new Combo(nodeInfoComp, SWT.BORDER);
		m_comboOfWindowTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		DialogDefinition.initWindowTitleCombo(m_comboOfWindowTitle);
		
		final Label componentLabel = new Label(nodeInfoComp, SWT.CENTER);
		componentLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		componentLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		componentLabel.setText("Component Type");

		m_comboOfComponentType = new Combo(nodeInfoComp, SWT.READ_ONLY);
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
				m_multiEventPanel.update();
				if(m_comboOfComponentType.getText().compareTo("") == 0)
					return;
				m_multiEventPanel.resetIComponent(new SwingModel().createComponent(m_comboOfComponentType.getText()));
			}
		});

		final Label dataLabel = new Label(nodeInfoComp, SWT.CENTER);
		dataLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		dataLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		dataLabel.setText("Variable");
		dataLabel.setBounds(0, 0, 26, 12);

		m_textOfData = new Text(nodeInfoComp, SWT.BORDER);
		m_textOfData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		final Label typeLabel = new Label(nodeInfoComp, SWT.CENTER);
		typeLabel.setFont(SWTResourceManager.getFont("Times New Roman", 10, SWT.BOLD));
		typeLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		typeLabel.setText("Generation Node");

		m_comboOfGenerationalKey = new Combo(nodeInfoComp, SWT.READ_ONLY);
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
				m_multiEventPanel.saveEventArgument();
				m_eventTriggerNode.setData(m_textOfData.getText());
				m_eventTriggerNode.setComponentType(m_comboOfComponentType.getText());
				m_eventTriggerNode.setGenerationKey(m_comboOfGenerationalKey.getText());
				m_eventTriggerNode.setWindowType(m_comboOfWindowType.getText());
				m_eventTriggerNode.setWindowTitle(m_comboOfWindowTitle.getText());
				m_eventTriggerNode.setEventList(m_multiEventPanel.getEventList());
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
		if(m_eventTriggerNode != null) {
			m_comboOfComponentType.setText(m_eventTriggerNode.getComponentType());
			m_textOfData.setText(m_eventTriggerNode.getData());
			m_comboOfGenerationalKey.setText(m_eventTriggerNode.getGenerationKey());
			if(m_eventTriggerNode.getWindowType().compareTo("") != 0)
				m_comboOfWindowType.setText(m_eventTriggerNode.getWindowType());
			else
				m_comboOfWindowType.setText(DialogDefinition.DEFAULTWINDOWTYPE);
			m_comboOfWindowTitle.setText(m_eventTriggerNode.getWindowTitle());
			m_multiEventPanel.resetIComponent(m_eventTriggerNode.getIComponent());
			m_multiEventPanel.setEventList(m_eventTriggerNode.getEventList());
			m_multiEventPanel.updateSelectedEventList();
		}
	}
}
