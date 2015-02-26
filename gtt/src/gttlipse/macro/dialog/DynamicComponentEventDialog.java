package gttlipse.macro.dialog;

import gtt.macro.DefaultMacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.DynamicComponentEventNode;
import gtt.macro.macroStructure.DynamicComponentNode;
import gtt.macro.macroStructure.MacroComponentNode;

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

public class DynamicComponentEventDialog extends TitleAreaDialog implements SelectionListener {

	private DynamicComponentEventNode _node = null;
	
	private TabFolder _tabFolder = null;
	
	private Composite _compParent = null;
	private Composite _eventParent = null;
	
	private GridLayout _tabLayout = null;
	
	private ComponentPanel _compPanel = null;
	private DynamicComponentEventPanel _eventPanel = null;
	
	public DynamicComponentEventDialog(Shell parentShell, DynamicComponentEventNode node) {
		super(parentShell);
		_node = node;
	}

	@Override
	public Control createDialogArea(Composite parent) {
		getShell().setText("Dynamic Component Event Node");
		setMessage("Edit Dynamic Component Event Node");
		
		// Layout
		initLayout();

		// Component Tab
		initTabFolder(parent);
		
		// Initialize the component panel
		initComponentPanel();
		
		// Initialize the event panel
		initEventPanel();
		
		return parent;
	}
	
	@Override
	public void okPressed() {
		AbstractMacroNode selection = _compPanel.getSelectNode();
		
		if (selection == null) {
			return;
		}
		
		if (!(selection instanceof DynamicComponentNode)) {
			return;
		}
		
		updateEventNode(selection);
		
		super.okPressed();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		doSelect();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		doSelect();
	}
	
	private void doSelect() {
		Tree t = _compPanel.getTree();
		
		if (t.getSelectionCount() == 0) {
			return;
		}
		
		TreeItem item = t.getSelection()[0];
		AbstractMacroNode node = (AbstractMacroNode)item.getData();
		
		_compPanel.setSelectNode(node);
		
		if (node instanceof DynamicComponentNode) {
			_eventPanel.updateSelectNode((DynamicComponentNode)node);
		}
	}
	
	private void updateEventNode(AbstractMacroNode node) {
		_node.setComponentPath(node.getPath().toString());
		_node.setEvent(_eventPanel.getSelectEventType(), _eventPanel.getSelectEventID());
		_node.getArguments().clear();
		
		for (int i = 0; i < _eventPanel.getArgCount(); i++) {
			_node.getArguments().add(_eventPanel.getArgument(i));
		}
	}
	
	private void initLayout() {
		_tabLayout = new GridLayout();
		_tabLayout.numColumns = 1;
	}
	
	private void initTabFolder(Composite parent) {
		_tabFolder = new TabFolder(parent, SWT.NONE);
		
		_compParent = new Composite(_tabFolder, SWT.NONE);
		_compParent.setLayout(_tabLayout);
		
		_eventParent = new Composite(_tabFolder, SWT.NONE);
		_eventParent.setLayout(_tabLayout);
		
		TabItem compTab = new TabItem(_tabFolder, SWT.NONE);
		compTab.setText("Component");
		compTab.setControl(_compParent);
		
		TabItem eventTab = new TabItem(_tabFolder, SWT.NONE);
		eventTab.setText("Event");
		eventTab.setControl(_eventParent);
	}
	
	private void initComponentPanel() {
		MacroComponentNode localParent = DefaultMacroFinder.findRoot(_node);
		
		_compPanel = new ComponentPanel(_compParent, localParent, ComponentPanel.PARSE_DYNAMIC_COMPONENT);
		_compPanel.addSelectionListener(this);
		_compPanel.setSelectNode(_node.getComponent());
		_compPanel.setupTree();
		
		// Expand the tree structure
		if (_node.getComponentPath().equals("")) {
			_compPanel.setExpanded(_node.getParent());
		}
		else {
			_compPanel.setExpanded(_node.getComponent());
		}
	}
	
	private void initEventPanel() {
		_eventPanel = new DynamicComponentEventPanel(_eventParent, _node);
	}
}
