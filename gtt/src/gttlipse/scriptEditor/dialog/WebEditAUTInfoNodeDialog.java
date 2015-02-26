package gttlipse.scriptEditor.dialog;

import gtt.testscript.LaunchNode;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WebEditAUTInfoNodeDialog extends TitleAreaDialog  {
	private Text m_text_url;
	private LaunchNode m_loadautnode;
	Button[] radios;

	/**
	 * @param parentShell
	 */
	public WebEditAUTInfoNodeDialog(Shell parentShell, LaunchNode node) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_text_url = null;
		m_loadautnode = node;
		radios = null;
	}

protected Control createDialogArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NONE);
		final FormLayout layout = new FormLayout();
		area.setLayout(layout);
		
		final Label m_lbl_autname = new Label(area, SWT.CENTER);
		m_lbl_autname.setFont(new Font(parent.getFont().getDevice(),"Arial",12,SWT.BOLD));
		m_lbl_autname.setText("Control Browser:");
		area.getParent().pack();
		FormData data = new FormData();
		
		data.left = new FormAttachment(60);
		m_lbl_autname.setLayoutData(data);
		
		final Composite area2 = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout2 = new GridLayout(2, false);
		area2.setLayout(gridlayout2);
		
		
		radios = new Button[3];
		radios[0] = new Button(area2, SWT.RADIO);
		radios[0].setText("Under test URL");
		
		
		GridData textdata = new GridData();
		textdata.widthHint = 300;
		textdata.heightHint = 13;
		
		m_text_url = new Text(area2, SWT.NULL);
		m_text_url.setLayoutData(textdata);
		
		GridData radio1data = new GridData();
		radio1data.horizontalSpan = 2;
		
		radios[1] = new Button(area2, SWT.RADIO);
		radios[1].setText("Previous Page");
		radios[1].setLayoutData(radio1data);
		
		radios[2] = new Button(area2, SWT.RADIO);
		radios[2].setText("Next Page");
		
		
		if(m_loadautnode.getClassName().equals("UP"))
			radios[1].setSelection(true);
		else if(m_loadautnode.getClassName().equals("DOWN"))
			radios[2].setSelection(true);
		else {
			radios[0].setSelection(true);
			m_text_url.setText(m_loadautnode.getArgument());
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
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				
				for (int i=0; i<radios.length; i++) {
					  if(radios[0].getSelection()) {
						  m_loadautnode.setClassName(m_text_url.getText());
						  m_loadautnode.setArgument(m_text_url.getText());
					  }
					  if(radios[1].getSelection()) {
						  m_loadautnode.setClassName("UP");
					  }
					  if(radios[2].getSelection()) {
						  m_loadautnode.setClassName("DOWN");
					  }
				}
				setReturnCode(SWT.Modify);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				setReturnCode(CANCEL);
				close();
			}
		});
	}

}
