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
public class InputNameDialog extends TitleAreaDialog {	
	private Text m_txt_name;
	private String _name = "";

	public InputNameDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public String getNewName() {
		return _name;
	}
	
	public void setOldName(String name) {
		_name = name;
	}

	protected Control createDialogArea(Composite parent) {
		/* setup layout */
		this.getShell().setText("Input Name");
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		
		/* rename */
		Label lbl_newName = new Label(area, SWT.NULL);
		lbl_newName.setText("Name: ");
		m_txt_name = new Text(area, SWT.BORDER);
		m_txt_name.setLayoutData(data);
		m_txt_name.setText(_name);
		m_txt_name.selectAll();
		
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
				_name = m_txt_name.getText();
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
