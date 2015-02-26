package gttlipse.macro.dialog;

import gtt.macro.macroStructure.DynamicComponentNode;
import gttlipse.editor.ui.ComponentInfoPanel;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class DynamicComponentDialog extends TitleAreaDialog implements ModifyListener {

	private DynamicComponentNode _node = null;
	
	private TabFolder _tabFolder = null;
	
	private Composite _parent = null;
	
	private GridLayout _tabLayout = null;
	private GridLayout _groupLayout = null;
	
	private GridData _labelData = null;
	private GridData _textData = null;
	
	private Text _nodeName = null;
	private Text _source = null;
	
	private ComponentInfoPanel _panel = null;
	
	public DynamicComponentDialog(Shell parentShell, DynamicComponentNode node) {
		super(parentShell);
		_node = node;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Dynamic Component Node");
		setMessage("Edit Dynamic Component Node");
		
		// Layout
		initLayout();

		// Component Tab
		initTabFolder(parent);

		// GridData
		initGridData();
		
		// Group of name
		initNameGroup();
		
		// Group of data
		initDataGroup();
		
		// Initialize the component information panel
		initComponentPanel();
		
		return parent;
	}
	
	@Override
	protected void okPressed() {
		updateNodeName();
		updateSource();
		_panel.update();
		
		super.okPressed();
	}
	
	@Override
	public void modifyText(ModifyEvent e) {
		_panel.update();
	}
	
	private void updateNodeName() {
		String nodeName = _nodeName.getText();
		
		if (nodeName.equals("")) {
			_node.setName(DynamicComponentNode.DEFAULT_NAME);
		}
		else {
			_node.setName(nodeName);
		}
	}
	
	private void updateSource() {
		String source = _source.getText();
		
		if (source.equals("")) {
			_node.setSource(DynamicComponentNode.DEFAULT_SOURCE);
		}
		else {
			_node.setSource(source);
		}
	}
	
	private void initLayout() {
		_tabLayout = new GridLayout();
		_tabLayout.numColumns = 1;
		
		_groupLayout = new GridLayout();
		_groupLayout.numColumns = 2;
	}
	
	private void initTabFolder(Composite parent) {
		_tabFolder = new TabFolder(parent, SWT.NONE);
		
		_parent = new Composite(_tabFolder, SWT.NONE);
		_parent.setLayout(_tabLayout);
		
		TabItem item = new TabItem(_tabFolder, SWT.NONE);
		item.setText("Component");
		item.setControl(_parent);
	}
	
	private void initGridData() {
		_labelData = new GridData();
		_labelData.widthHint = 115;
		
		_textData = new GridData();
		_textData.widthHint = 300;
	}
	
	private void initNameGroup() {
		// Group
		Group nameGroup = new Group(_parent, SWT.SHADOW_ETCHED_IN);
		nameGroup.setLayout(_groupLayout);
		nameGroup.setText("Node Information");
		
		// Group content
		initName(nameGroup);
	}
	
	private void initDataGroup() {
		// Group
		Group dataGroup = new Group(_parent, SWT.SHADOW_ETCHED_IN);
		dataGroup.setLayout(_groupLayout);
		dataGroup.setText("DataPool Information");
		
		// Group content
		initSource(dataGroup);
	}
	
	private void initName(Group group) {
		// Label
		Label label = new Label(group, SWT.NULL);
		label.setText("Node Name: ");
		label.setLayoutData(_labelData);
		
		// Text
		_nodeName = new Text(group, SWT.BORDER);
		_nodeName.setLayoutData(_textData);
		_nodeName.setText(_node.getName());
	}
	
	private void initSource(Group group) {
		// Label
		Label label = new Label(group, SWT.NULL);
		label.setText("Source: ");
		label.setLayoutData(_labelData);
		
		// Text
		_source = new Text(group, SWT.BORDER);
		_source.setLayoutData(_textData);
		_source.setText(_node.getSource());
	}
	
	private void initComponentPanel() {
		_panel = new ComponentInfoPanel(_parent, _node.getComponent());
		_panel.setListener(this);
	}
}
