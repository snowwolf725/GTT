package gttlipse.macro.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.LaunchNode;
import gtt.macro.macroStructure.MacroComponentNode;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class MacroComponentSelectionDialog extends TitleAreaDialog implements
		SelectionListener {
	private ComponentPanel _panel = null;
	private LaunchNode _node = null;
	private MacroComponentNode _selectNode = null;

	public MacroComponentSelectionDialog(Shell parentShell, LaunchNode node, MacroComponentNode selectNode) {
		super(parentShell);
		_node = node;
		_selectNode = selectNode;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Select Macro Component");
		setMessage("Select A Macro Component to Parse and Store results");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;

		MacroComponentNode localParent = _selectNode;
		_panel = new ComponentPanel(parent, localParent, _node, ComponentPanel.PARSE_MACRO_COMPONENT);
		_panel.addSelectionListener(this);
		_panel.setSelectNode(_selectNode);
		_panel.setupTree();

		if (_selectNode.getPath().equals("")) {
			_panel.setExpanded(_selectNode.getParent());
		} else {
			_panel.setExpanded(_selectNode);
		}

		return parent;
	}

	private void doSelect() {
		Tree t = _panel.getTree();
		if (t.getSelectionCount() == 0)
			return;
		TreeItem item = t.getSelection()[0];
		AbstractMacroNode node = (AbstractMacroNode) item.getData();
		_panel.setSelectNode(node);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		doSelect();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		doSelect();
	}

	protected void okPressed() {
		AbstractMacroNode node = _panel.getSelectNode();

		if (node == null)
			return;
		if (node.getParent() == null)
			return;		
		if (!(node instanceof MacroComponentNode))
			return;
		
		_node.setClassPath(node.getPath().toString());
		super.okPressed();
	}

}
