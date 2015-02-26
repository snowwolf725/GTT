package gttlipse.macro.dialog;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import gttlipse.macro.view.MacroPresenter;


public class SetSimulationNumberDialog extends TitleAreaDialog{
	private Text m_text = null;
	private MacroPresenter m_macroPresenter = null;
	
	public SetSimulationNumberDialog(Shell parentShell, MacroPresenter macroPresenter) {
		super(parentShell);
		m_macroPresenter = macroPresenter;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText( "Set simulation number of Multi-user testing" );
		setTitle("Enter the number");
		setMessage("How many users do you want to simulate?");
		
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 3;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;
		final Label lbl_projectname = new Label(area, SWT.NULL);
		lbl_projectname.setText("Set simulation number");
		m_text = new Text(area, SWT.NULL);
		m_text.setLayoutData(data);		
		return area;
	}
	
	@Override
	protected void okPressed() {
		m_macroPresenter.setSimNumber(Integer.parseInt(m_text.getText()));
		super.okPressed();		
	}
	
	
}
