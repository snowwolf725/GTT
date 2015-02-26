package gttlipse.vfsmEditor.actions;

import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.Element;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.view.IVFSMPresenter;
import gttlipse.vfsmEditor.view.dialog.DeclarationDialog;
import gttlipse.vfsmEditor.view.dialog.MacroEventNodeSettingDialog;

import org.eclipse.jface.action.Action;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class DoubleClickHandle extends Action {

	private IVFSMPresenter m_presenter = null;

	public DoubleClickHandle(IVFSMPresenter presenter) {
		m_presenter = presenter;
	}

	private Shell getShell() {
		return m_presenter.getShell();
	}

	public void run() {
		Element ele = m_presenter.getSelectionElement();

		if (ele instanceof Connection) {
			dbClickOnConnection((Connection) ele);
		} else {
			dbClickOnState((State) ele);
		}
	}

	// double-click on connection
	private void dbClickOnConnection(Connection conn) {
		MacroEventNodeSettingDialog editDialog = new MacroEventNodeSettingDialog(
				getShell(), conn.getEventList());
		editDialog.open();
		if (editDialog.getReturnCode() != SWT.OK)
			return;

		conn.setEventList(editDialog.getSelectEventList());
	}

	// double-click on Node-related object
	private void dbClickOnState(State state) {
		if (state.getName() == VFSMDef.FSM_FSM) {
			insertDeclarationChild();
		} else if (state.getName() == VFSMDef.FSM_MAIN) {
			m_presenter.diplayMainDiagram();
		} else if (state.isDeclaration()) {
			m_presenter.diplayDiagram(state.getName());
		} else {
			// double-click 預設行為是 setting name
			setStateName(state);
		}
	}

	private void insertDeclarationChild() {
		final String default_name = "Superstate";
		DeclarationDialog editDialog = new DeclarationDialog(getShell(),
				"Create a new Declaration", default_name);
		if (editDialog != null)
			editDialog.open();
		if (editDialog.getReturnCode() == SWT.OK)
			m_presenter.addDeclarationChild(editDialog.getDeclarationName());

		m_presenter.refreshTreeView();
	}

	// double-click 預設行為是 setting name
	private void setStateName(State state) {
		NameSettingDialog dialog = new NameSettingDialog(getShell(), "Rename",
				state.getName());
		dialog.open();
		if (dialog.getReturnCode() == SWT.OK)
			state.setName(dialog.getSettingName());
	}
}

/**
 * @author by David Wu 
 * note : The function of this dialog is to edit the name of some node.
 * date : 2007/01/01
 */

class NameSettingDialog extends TrayDialog {
	private Text m_nameText = null;
	private String m_settingName = "";

	// constructor
	public NameSettingDialog(Shell parentShell, String initName) {
		this(parentShell, "", initName);
	}

	// constroctor - initial a title string of dialog
	public NameSettingDialog(Shell parentShell, String title, String initName) {
		super(parentShell);
		m_settingName = initName;
		create();
		getShell().setText(title);
	}

	public String getSettingName() {
		return m_settingName;
	}

	protected Control createDialogArea(Composite parent) {
		final GridLayout areaLayout = new GridLayout();
		areaLayout.numColumns = 1;

		final Composite area = new Composite(parent, SWT.NULL);
		area.setLayout(areaLayout);

		final GridData layoutdata = new GridData();
		layoutdata.horizontalAlignment = GridData.CENTER;
		layoutdata.verticalAlignment = GridData.CENTER;
		area.setLayoutData(layoutdata);

		final Group group = new Group(area, SWT.SHADOW_ETCHED_IN);

		group.setText("Rename");
		group.setBounds(5, 5, 200, 100);

		// create label and textbox
		Label nameLabel = new Label(group, SWT.NULL);
		nameLabel.setText("Name:");
		nameLabel.setBounds(10, 25, 30, 20);

		m_nameText = new Text(group, SWT.BORDER | SWT.SINGLE);
		m_nameText.setText(m_settingName);
		m_nameText.setBounds(50, 25, 250, 20);

		if (m_settingName.matches(VFSMDef.FSM_MAIN) == true
				|| m_settingName.matches(VFSMDef.FSM_FSM) == true)
			m_nameText.setEditable(false);
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
				modifyNodeName();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	private void modifyNodeName() {
		if (m_nameText.getText().matches("") == true) {
			MessageBox messageBox = new MessageBox(getShell(), SWT.CANCEL
					| SWT.ICON_ERROR);
			messageBox.setText("Waring");
			messageBox.setMessage("Can not input null name");
			messageBox.open();
		} else {
			// refactor ?
			m_settingName = m_nameText.getText();
			setReturnCode(SWT.OK);
			close();
		}
	}
}

