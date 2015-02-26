package gttlipse.refactoring.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;

import java.util.Iterator;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SelectArgumentDialog extends TitleAreaDialog {
	private Arguments _args = null;
	private SelectArgumentPanel _argumentPanel = null;
	
	public SelectArgumentDialog(Shell parentShell, Arguments args) {
		super(parentShell);
		_args = args;
	}
	
	private void initDialog(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		area.setLayout(gridlayout);

		GridData gd = new GridData();
		gd.heightHint = 300;
		gd.widthHint = 450;
		area.setLayoutData(gd);
		
		_argumentPanel = new SelectArgumentPanel(area);
		
		// init args
		Iterator<Argument> ite = _args.iterator();
		while (ite.hasNext()) {
			addArgument(ite.next());
		}
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Select Arguments");
		setMessage("Select Arguments");

		initDialog(parent);

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
				_args = getArguments();

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
	
	private void addArgument(Argument arg) {
		_argumentPanel.addArgument(arg.clone());
	}
	
	private Arguments getArguments() {
		return _argumentPanel.getArguments();
	}
	
	public Arguments getArgumentList() {
		return _args;
	}
}
