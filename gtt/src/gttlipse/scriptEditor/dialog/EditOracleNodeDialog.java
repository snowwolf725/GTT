/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.testscript.OracleNode;

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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class EditOracleNodeDialog extends TitleAreaDialog {
	
	private OracleInfoPanel m_oracleinfopanel;
	
	private Shell m_shell;
	
	private OracleNode m_node;
	
//	private AssertEventPanel m_assertevent;
	
	private OracleReportPanel m_report;

	/**
	 * @param parentShell
	 */
	public EditOracleNodeDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_shell = parentShell;
	}

	public EditOracleNodeDialog(Shell parentShell,OracleNode node) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_node = node;
		m_shell = parentShell;
	}
	
	protected Control createDialogArea(Composite parent) {
		/* Setup Layout */
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 1;
		final GridLayout gridlayout2 = new GridLayout();
		gridlayout2.numColumns = 2;
		GridData data = new GridData();
		data.widthHint = 300;
		data.heightHint = 20;

		/* Setup TabFolder */
		TabFolder tabFolder1 = new TabFolder(parent,SWT.NONE);
		final Composite oracleInfoComp = new Composite(tabFolder1, SWT.NULL);
		oracleInfoComp.setLayout(gridlayout);
		final Composite oracleInfoPanel = new Composite(oracleInfoComp, SWT.NULL);
		oracleInfoPanel.setLayout(gridlayout);
		Composite eventInfoComp = new Composite(tabFolder1,SWT.NONE);
		eventInfoComp.setLayout(gridlayout);
		
		TabItem item1 = new TabItem(tabFolder1,SWT.NONE);
		item1.setText("Oracle Info");
		item1.setControl(oracleInfoComp);
		TabItem item2 = new TabItem(tabFolder1,SWT.NONE);
		item2.setText("Oracle Report");
		item2.setControl(eventInfoComp);

		/* Setup Component Panel */
		m_oracleinfopanel = new OracleInfoPanel(oracleInfoComp, m_node, m_shell);
		
		/* Setup Assert Panel */
//		m_assertevent = new AssertEventPanel(eventInfoComp, m_node);
		
		/* Setup Oracle Report */
		m_report = new OracleReportPanel(eventInfoComp, m_node);
		
		return parent;
	}
		
	protected void createButtonsForButtonBar(Composite parent) {
		Button btn_modify = createButton(parent, SWT.Modify, "Modify", true);
		btn_modify.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				m_oracleinfopanel.update();
				
//				m_assertevent.update();
				
				m_report.update();
				
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
