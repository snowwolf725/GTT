package gttlipse.refactoring.dialog;

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
import org.eclipse.swt.widgets.Text;

/**
 * @author Soriel
 *
 */
public class RenameSwingComponentDialog extends TitleAreaDialog {
	
	private Text m_txt_rename;
	private Text m_txt_winType;
	private Text m_txt_type;
	private String _name;
	private String _winType;
	private String _type;

	public RenameSwingComponentDialog(Shell parentShell) {
		super(parentShell);
		_name = "";
	}
	
	public String getNewName() {
		return _name;
	}
	
	public void setOldName(String name) {
		_name = name;
	}
	
	public String getNewType() {
		return _type;
	}
	
	public void setOldType(String type) {
		_type = type;
	}
	
	public String getNewWinType() {
		return _winType;
	}
	
	public void setOldWinType(String winType) {
		_winType = winType;
	}

	protected Control createDialogArea(Composite parent) {
		/* setup layout */
		this.getShell().setText("Rename");
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		
		/* rename */
		Label lbl_newName = new Label(area, SWT.NULL);
		lbl_newName.setText("New name: ");
		m_txt_rename = new Text(area, SWT.BORDER);
		m_txt_rename.setLayoutData(data);
		m_txt_rename.setText(_name);
		m_txt_rename.selectAll();
		
		/* rename */
		Label lbl_newType = new Label(area, SWT.NULL);
		lbl_newType.setText("New Type: ");
		m_txt_type = new Text(area, SWT.BORDER);
		m_txt_type.setLayoutData(data);
		m_txt_type.setText(_type);
		m_txt_type.selectAll();
		
		/* rename */
		Label lbl_newWinType = new Label(area, SWT.NULL);
		lbl_newWinType.setText("New WinType: ");
		m_txt_winType = new Text(area, SWT.BORDER);
		m_txt_winType.setLayoutData(data);
		m_txt_winType.setText(_winType);
		m_txt_winType.selectAll();
		
		return parent;
	}
	
	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_ok = createButton(parent, SWT.OK, "OK", true);
		btn_ok.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				_name = m_txt_rename.getText();
				_type = m_txt_type.getText();
				_winType = m_txt_winType.getText();
				setReturnCode(SWT.OK);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", false);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}
}
