/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.testscript.ViewAssertNode;
import gttlipse.GTTlipseConfig;
import gttlipse.editor.ui.ComponentInfoPanel;
import gttlipse.editor.ui.IComponentInfoPanel;
import gttlipse.editor.ui.WebComponentInfoPanel;

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
public class EditAssertNodeDialog extends TitleAreaDialog {
	private ViewAssertNode m_assertnode = null;
	
	private AssertPanel m_assertPanel = null;
	
	private IComponentInfoPanel m_componentInfoPanel;
	
	private CompInfoSelectionAdapter_AUT m_compselectadapter_aut;
	
	private CompInfoSelectionAdapter_Source m_compselectadapter_source;
	
	/**
	 * @param parentShell
	 */
	public EditAssertNodeDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param parentShell
	 */
	public EditAssertNodeDialog(Shell parentShell,ViewAssertNode obj) {
		super(parentShell);
		// TODO Auto-generated constructor stub
		m_assertnode = obj;
		this.create();
		this.getShell().setText( "Edit AssertNode Dialog" );
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
		final Composite comInfoComp = new Composite(tabFolder1, SWT.NULL);
		comInfoComp.setLayout(gridlayout);
		final Composite compInfoPanel = new Composite(comInfoComp, SWT.NULL);
		compInfoPanel.setLayout(gridlayout);
		final Composite autSelectPanel = new Composite(comInfoComp, SWT.NULL);
		autSelectPanel.setLayout(gridlayout2);
		Composite assertInfoComp = new Composite(tabFolder1,SWT.NONE);
		assertInfoComp.setLayout(gridlayout);
		
		TabItem item1 = new TabItem(tabFolder1,SWT.NONE);
		item1.setText("Assert Info");
		item1.setControl(assertInfoComp);
		TabItem item2 = new TabItem(tabFolder1,SWT.NONE);
		item2.setText("Component Info");
		item2.setControl(comInfoComp);
		
		/* Setup Component Panel */
		if(GTTlipseConfig.testingOnWebPlatform()) {
			m_componentInfoPanel = new WebComponentInfoPanel(compInfoPanel, m_assertnode.getComponent());
		}
		else
			m_componentInfoPanel = new ComponentInfoPanel(compInfoPanel, m_assertnode.getComponent());
		
		
		/* create button for selecting AUT component info form AUT */
		final Button selecomp = new Button( autSelectPanel, SWT.PUSH );
		selecomp.setText( "Select Component by capturing" );
		m_compselectadapter_aut = new CompInfoSelectionAdapter_AUT(m_componentInfoPanel,m_assertnode,selecomp);
		selecomp.addSelectionListener( m_compselectadapter_aut );
		
		/* create button for selecting AUT component info from source code*/
		final Button seleCompBySourceCode = new Button( autSelectPanel, SWT.PUSH );
		seleCompBySourceCode.setText( "Select Component by source code" );
		m_compselectadapter_source = new CompInfoSelectionAdapter_Source(m_componentInfoPanel,getShell());
		seleCompBySourceCode.addSelectionListener( m_compselectadapter_source );
		
		/* Setup Assert Panel */
		m_assertPanel = new AssertPanel(assertInfoComp,m_assertnode.getAssertion(),
				                        m_componentInfoPanel.getComponentClassType());
		m_componentInfoPanel.setListener(m_assertPanel);
		return parent;
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
				// TODO Auto-generated method stub
				m_componentInfoPanel.update();
				m_assertPanel.update();
				
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
	
	public ViewAssertNode getValue() {
		return m_assertnode;
	}
}
