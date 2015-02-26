package gttlipse.macro.dialog;

import gtt.macro.macroStructure.SplitDataNode;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
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

public class SplitDataDialog extends TitleAreaDialog {

	private SplitDataNode _node = null;
	
	private TabFolder _tabFolder = null;
	
	private Composite _parent = null;
	
	private GridLayout _tabLayout = null;
	private GridLayout _groupLayout = null;
	
	private GridData _labelData = null;
	private GridData _textData = null;
	
	private Text _nodeName = null;
	private Text _data = null;
	private Text _separator = null;
	private Text _target = null;
	
	public SplitDataDialog(Shell parentShell, SplitDataNode node) {
		super(parentShell);
		_node = node;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Split Data Node");
		setMessage("Edit Split Data Node");
	
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
		
		return parent;
	}
	
	@Override
	protected void okPressed() {
		// Update informations of the node
		_node.setName(_nodeName.getText());
		_node.setData(_data.getText());
		updateSeparator();
		updateTarget();
		
		super.okPressed();
	}
	
	private void updateSeparator() {
		String separator = _separator.getText();
		
		if (separator.equals("")) {
			_node.setSeparator(SplitDataNode.DEFAULT_SEPARATOR);
		}
		else {
			_node.setSeparator(separator);
		}
	}
	
	private void updateTarget() {
		String target = _target.getText();
		
		if (target.equals("")) {
			_node.setTarget(SplitDataNode.DEFAULT_TARGET);
		}
		else {
			_node.setTarget(target);
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
		nameGroup.setText("SplitDataNode Information");
		
		// Group content
		initName(nameGroup);
	}
	
	private void initDataGroup() {
		// Group
		Group dataGroup = new Group(_parent, SWT.SHADOW_ETCHED_IN);
		dataGroup.setLayout(_groupLayout);
		dataGroup.setText("SplitData Information");
		
		// Group content
		initData(dataGroup);
		initSeparator(dataGroup);
		initTarget(dataGroup);
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
	
	private void initData(Group group) {
		// Label
		Label label = new Label(group, SWT.NULL);
		label.setText("Data: ");
		label.setLayoutData(_labelData);
		
		// Text
		_data = new Text(group, SWT.BORDER);
		_data.setLayoutData(_textData);
		_data.setText(_node.getData());
	}
	
	private void initSeparator(Group group) {
		// Label
		Label label = new Label(group, SWT.NULL);
		label.setText("Separator: ");
		label.setLayoutData(_labelData);
		
		// Text
		_separator = new Text(group, SWT.BORDER);
		_separator.setLayoutData(_textData);
		_separator.setText(_node.getSeparator());
	}
	
	private void initTarget(Group group) {
		// Label
		Label label = new Label(group, SWT.NULL);
		label.setText("Target: ");
		label.setLayoutData(_labelData);
		
		// Text
		_target = new Text(group, SWT.BORDER);
		_target.setLayoutData(_textData);
		_target.setText(_node.getTarget());
	}
}
