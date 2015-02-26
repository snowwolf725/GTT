/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.AbstractEvent;
import gtt.eventmodel.IComponent;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
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
 * created first in project GTTlipse.dialog
 * 
 */
public class EditEventNodeDialog extends TitleAreaDialog {
	
	private IComponentInfoPanel m_componentInfoPanel;
	
	private EventPanel m_eventPanel;

	private EventNode m_eventNode;
	
	private CompInfoSelectionAdapter_AUT m_compselectadapter_aut;
	
	private CompInfoSelectionAdapter_Source m_compselectadapter_source;

	/**
	 * 
	 */
	public EditEventNodeDialog(Shell shell, Object obj) {
		super(shell);
		// TODO Auto-generated constructor stub
		this.m_eventNode = (EventNode) obj;
		this.m_componentInfoPanel = null;
		this.create();
		this.getShell().setText( "Edit EventNode Dialog" );
	}

	// add by David wu on 2007/02/06
	public EditEventNodeDialog(Shell shell, Object obj, String title) {
		super(shell);
		// TODO Auto-generated constructor stub
		this.m_eventNode = (EventNode) obj;
		this.m_componentInfoPanel = null;
		this.create();
		this.getShell().setText( title );
	}
	
	protected Control createDialogArea(Composite parent) {
		this.setTitle("Edit TestEvent Node");
		this.setMessage("Modify the property of TestEvent Node.");
		
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
		Composite eventInfoComp = new Composite(tabFolder1,SWT.NONE);
		eventInfoComp.setLayout(gridlayout);
		
		TabItem item1 = new TabItem(tabFolder1,SWT.NONE);
		item1.setText("Event Info");
		item1.setControl(eventInfoComp);
		TabItem item2 = new TabItem(tabFolder1,SWT.NONE);
		item2.setText("Component Info");
		item2.setControl(comInfoComp);

		/* Setup Component Panel */
		if(GTTlipseConfig.testingOnWebPlatform()) 
			m_componentInfoPanel = new WebComponentInfoPanel(compInfoPanel, m_eventNode.getComponent());
		else
			m_componentInfoPanel = new ComponentInfoPanel(compInfoPanel, m_eventNode.getComponent());
		
		/* create button for selecting AUT component info form AUT */
		final Button selecomp = new Button( autSelectPanel, SWT.PUSH );
		selecomp.setText( "Select Component by capturing" );
		m_compselectadapter_aut = new CompInfoSelectionAdapter_AUT(m_componentInfoPanel,m_eventNode,selecomp);
		selecomp.addSelectionListener( m_compselectadapter_aut );
		
		/* create button for selecting AUT component info from source code*/
		final Button seleCompBySourceCode = new Button( autSelectPanel, SWT.PUSH );
		seleCompBySourceCode.setText( "Select Component by source code" );
		m_compselectadapter_source = new CompInfoSelectionAdapter_Source(m_componentInfoPanel,getShell());
		seleCompBySourceCode.addSelectionListener( m_compselectadapter_source );
		
		/* Setup Event Panel */
		m_eventPanel = new EventPanel(eventInfoComp,m_eventNode.getEvent(),m_componentInfoPanel);
		m_componentInfoPanel.setListener(m_eventPanel);
		
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
				m_componentInfoPanel.update();
				AbstractEvent event=(AbstractEvent)m_eventNode.getEvent();
				m_eventPanel.update();
				IComponent _com=m_eventNode.getComponent();
				m_eventNode = null;
				NodeFactory factory = new NodeFactory();
				m_eventNode = (EventNode)factory.createEventNode(_com,event);
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

	public EventNode getEventNode() {
		return m_eventNode;
	}
}
