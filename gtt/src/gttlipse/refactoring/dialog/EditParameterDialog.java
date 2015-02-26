package gttlipse.refactoring.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.editor.ui.ArgumentPanel;

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


public class EditParameterDialog extends TitleAreaDialog {
//	private MacroEventNode m_node = null;
	private ArgumentPanel m_argumentPanel = null;
	private Arguments m_argList = null;
	private int _visableBtn = 0;
	private boolean _hasBtn = true;

	public EditParameterDialog(Shell parentShell, MacroEventNode node, int visableBtn, boolean hasBtn) {
		super(parentShell);
		m_argList = node.getArguments();
		_visableBtn = visableBtn;
		_hasBtn = hasBtn;
	}
	
	public EditParameterDialog(Shell parentShell, Arguments args, int visableBtn, boolean hasBtn) {
		super(parentShell);
		m_argList = args;
		_visableBtn = visableBtn;
		_hasBtn = hasBtn;
	}

	private void initMacroEventInfo(Composite parent) {		
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		area.setLayout(gridlayout);

		GridData gd = new GridData();
		gd.heightHint = 300;
		gd.widthHint = 450;
		area.setLayoutData(gd);
		
		m_argumentPanel = new ArgumentPanel(area,_hasBtn, _visableBtn);
		
		// init args
		Iterator<Argument> ite = m_argList.iterator();
		while (ite.hasNext()) {
			addArgument(ite.next());
		}
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit Parameter");
		setMessage("Edit Parameter");

		initMacroEventInfo(parent);

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
				m_argList = new Arguments();
				for (int i = 0; i < getArgCount(); i++) {
					m_argList.add(getArgument(i));
				}
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
		m_argumentPanel.addArgument(arg.clone());
	}
	
	private int getArgCount() {
		return m_argumentPanel.getItemCount();
	}
	
	private Argument getArgument(int index){
		return Argument.create(
				m_argumentPanel.getType(index),
				m_argumentPanel.getName(index),
				m_argumentPanel.getValue(index)
				);
	}
	
	public Arguments getArgumentList() {
		return m_argList;
	}
}
