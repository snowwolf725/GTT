package gttlipse.refactoring.dialog;

import gttlipse.refactoring.util.ComponentWindow;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
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


/**
 * @author Soriel
 *
 */
public class WindowTitleDialog extends TitleAreaDialog {	
	private Text m_txt_rename;
	private Combo _titleCombo = null;
	private Combo _typeCombo = null;
	private ComponentWindow _window = null;
	private String _name = "";
	private String _title = "";
	private String _type = "";

	public WindowTitleDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public String getName() {
		return _name;
	}
	
	public String getTitle() {
		return _title;
	}
	
	public String getType() {
		return _type;
	}
	
	public void setName(String name) {
		_name = name;
	}

	public void setWindow(ComponentWindow window) {
		_window = window;
	}

	protected Control createDialogArea(Composite parent) {
		/* setup layout */
		this.getShell().setText("Rename Window Title");
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		
		// title
		Label titleLabel = new Label(area, SWT.NULL);
		titleLabel.setText("Title: ");
		_titleCombo = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		_titleCombo.setVisibleItemCount(10);
		_titleCombo.setLayoutData(data);
		_titleCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				updateType();
			}

			public void widgetSelected(SelectionEvent e) {
				updateType();
			}
		});
		updateTitle();
		_titleCombo.setText("");
		
		// window type
		Label typeLabel = new Label(area, SWT.NULL);
		typeLabel.setText("Type: ");
		_typeCombo = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		_typeCombo.setVisibleItemCount(10);
		_typeCombo.setLayoutData(data);
		_typeCombo.setText("");
		
		/* rename */
		Label lbl_newName = new Label(area, SWT.NULL);
		lbl_newName.setText("New title: ");
		m_txt_rename = new Text(area, SWT.BORDER);
		m_txt_rename.setLayoutData(data);
		m_txt_rename.setText(_name);
		m_txt_rename.selectAll();
		
		return parent;
	}
	
	private void updateTitle() {
		_titleCombo.removeAll();
		Iterator<String> ite = _window.iterator();
		
		while(ite.hasNext()) {
			_titleCombo.add(ite.next());
		}
	}
	
	private void updateType() {
		_typeCombo.removeAll();
		
		String title = _titleCombo.getText();
		Vector<String> type = _window.getType(title);
		if(type != null) {
			Iterator<String> ite = type.iterator();
			while(ite.hasNext()) {
				_typeCombo.add(ite.next());
			}
		}
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
				_title = _titleCombo.getText();
				_type = _typeCombo.getText();
				if(!_name.equals("") && !_title.equals("") && !_type.equals("")) {
					setReturnCode(SWT.OK);
					close();
				}
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
