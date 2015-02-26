package gttlipse.macro.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gttlipse.macro.view.MacroPresenter;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class MacroEventCallerDialog extends TitleAreaDialog implements SelectionListener {
	private MacroEventCallerNode m_node = null;
	
	private ComponentPanel m_selectComponentPanel = null;
	private MacroEventCallerPanel m_editSingleMacroEventPanel = null;
	
	public MacroEventCallerDialog(Shell parentShell, MacroEventCallerNode node) {
		super(parentShell);	
		m_node = node;
	}
	
	private MacroComponentNode getLocalParent() {
		if( m_node.getParent() == null ||
			m_node.getParent().getParent() == null ) return null;

		if( !(m_node.getParent() instanceof MacroEventNode) )
			return null;
		
		if( !(m_node.getParent().getParent() instanceof MacroComponentNode) )
			return null;
		
		return (MacroComponentNode)m_node.getParent().getParent();
	}	

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Single Macro Event Node");
		setMessage("Edit Single Macro Event Node");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;

		// ­¶ÅÒ
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
		
		// ¿ï¾ÜComponent­¶ÅÒ
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Component");
		Composite selectComponent = new Composite(tabFolder, SWT.NONE);
		item1.setControl(selectComponent);
		selectComponent.setLayout(tabLayout);

		m_selectComponentPanel = new ComponentPanel(selectComponent, getLocalParent(), m_node, ComponentPanel.PARSE_MACRO_COMPONENT);
		m_selectComponentPanel.addSelectionListener(this);
		m_selectComponentPanel.setSelectNode(m_node.getReference().getParent());
		m_selectComponentPanel.setupTree();
		m_selectComponentPanel.setExpanded(m_node.getReference().getParent());

		// ½s¿èEventªº­¶ÅÒ
		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText("Event");
		Composite editEvent = new Composite(tabFolder, SWT.NONE);
		item2.setControl(editEvent);
		editEvent.setLayout(tabLayout);
		
		m_editSingleMacroEventPanel = new MacroEventCallerPanel(editEvent, m_node);
		return parent;
	}
	
	private void doSelect() {
		Tree t = m_selectComponentPanel.getTree();
		
		if( t.getSelectionCount() == 0 ) return;
		TreeItem _selectTreeItem = t.getSelection()[0];
		AbstractMacroNode _select = (AbstractMacroNode)_selectTreeItem.getData();
		
		m_selectComponentPanel.setSelectNode(_select);

		if( _select == null ) return;
		if( !(_select instanceof MacroComponentNode) ) return;

		m_editSingleMacroEventPanel.updateSelectNode((MacroComponentNode)_select);		
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		doSelect();
	}

	public void widgetSelected(SelectionEvent e) {
		doSelect();
	}
	
	protected void okPressed() {
		AbstractMacroNode node = m_selectComponentPanel.getSelectNode();
		
		if( node == null ) return;
		if( !(node instanceof MacroComponentNode) ) return;
		
		String path = MacroPresenter.getNodePath(node);
		if( path.length() < 1 ) return;
		
		path = path + "::" + m_editSingleMacroEventPanel.getSelectEventType();
		m_node.setReferencePath(path);
		
		// ³]©w Arguments
		m_node.getArguments().clear();
		for(int i=0;i<m_editSingleMacroEventPanel.getArgCount();i++) {
			m_node.getArguments().add(m_editSingleMacroEventPanel.getArgument(i));			
		}

		super.okPressed();
	}
}