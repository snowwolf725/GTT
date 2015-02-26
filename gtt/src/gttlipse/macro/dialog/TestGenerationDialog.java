package gttlipse.macro.dialog;

import gtt.testscript.LaunchNode;
import gtt.testscript.NodeFactory;
import gttlipse.GTTlipseConfig;
import gttlipse.scriptEditor.dialog.EditAUTInfoNodeDialog;
import gttlipse.scriptEditor.dialog.WebEditAUTInfoNodeDialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class TestGenerationDialog extends TrayDialog {
	private LaunchNode m_LaunchNode = null;
	private Text m_stepText = null;
	private int m_selectionNum = -1;
	private int m_step = -1;

	// constructor
	public TestGenerationDialog(Shell parentShell) {
		super(parentShell);
		this.create();
		this.getShell().setText("Test-Case Generation");
	}

	public LaunchNode getLaunchNode() {
		return m_LaunchNode;
	}

	public int getSelectionNum() {
		return m_selectionNum;
	}

	public int getStepNum() {
		return m_step;
	}

	protected Control createDialogArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout areaLayout = new GridLayout();
		areaLayout.numColumns = 1;
		area.setLayout(areaLayout);

		final GridData layoutdata = new GridData();
		layoutdata.horizontalAlignment = GridData.CENTER;
		layoutdata.verticalAlignment = GridData.CENTER;
		area.setLayoutData(layoutdata);

		createSelectPane(area);
		createAUTInfoPane(area);

		return area;
	}

	// handle event of the button on the buttonbar
	protected void createButtonsForButtonBar(Composite parent) {
		// set layout of buttonbar
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);

		// create button and handel event
		Button btnAdd = createButton(parent, SWT.OK, "OK", true);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				m_step = Integer.parseInt(m_stepText.getText());
				setReturnCode(SWT.OK);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(SWT.CANCEL);
				close();
			}
		});
	}

	private void createSelectPane(Composite parent) {
		final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setText("Select Mode");

		Button vertexBtn = new Button(group, SWT.RADIO);
		vertexBtn.setText("State Coverage");

		Button edgeBtn = new Button(group, SWT.RADIO);
		edgeBtn.setText("Transition Coverage");

		Button consecutiveBtn = new Button(group, SWT.RADIO);
		consecutiveBtn.setText("Consecutive event");

		Button randomBtn = new Button(group, SWT.RADIO);
		randomBtn.setText("Monkey Test");

		final Text stepText = new Text(group, SWT.BORDER);
		stepText.setEnabled(false);
		stepText.setText("20");
		m_stepText = stepText;

		vertexBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				m_selectionNum = 0;
				stepText.setEnabled(false);
			}
		});

		edgeBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				m_selectionNum = 1;
				stepText.setEnabled(false);
			}
		});

		consecutiveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				m_selectionNum = 2;
				stepText.setEnabled(false);
			}
		});

		randomBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				m_selectionNum = 3;
				stepText.setEnabled(true);
			}
		});

		// my layout
		group.setBounds(5, 5, 350, 250);
		vertexBtn.setBounds(10, 10, 330, 20);
		edgeBtn.setBounds(10, 30, 330, 20);
		consecutiveBtn.setBounds(10, 50, 330, 20);
		randomBtn.setBounds(10, 70, 130, 20);
		stepText.setBounds(150, 70, 100, 20);
	}

	private void createAUTInfoPane(Composite parent) {
		final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setText("AUT Setting");

		Button btn = new Button(group, SWT.PUSH);
		btn.setText("AUT Info");

		final Text pathText = new Text(group, SWT.BORDER | SWT.SINGLE);
		final Shell shell = this.getShell();

		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				NodeFactory factory = new NodeFactory();
				m_LaunchNode = factory.createLaunchNode("AUTInfo", ".");

				TitleAreaDialog autDialog = null;
				if (GTTlipseConfig.testingOnWebPlatform()) {
					autDialog = new WebEditAUTInfoNodeDialog(shell,
							m_LaunchNode);
				} else {
					autDialog = new EditAUTInfoNodeDialog(shell, m_LaunchNode);
				}

				autDialog.create();
				autDialog.open();
				pathText.setText(m_LaunchNode.getClassName());
			}
		});

		// my layout
		group.setBounds(5, 5, 350, 30);
		pathText.setBounds(10, 15, 250, 20);
		btn.setBounds(260, 15, 80, 20);
	}

}
