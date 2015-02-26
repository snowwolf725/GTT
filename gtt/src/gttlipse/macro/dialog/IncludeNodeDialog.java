package gttlipse.macro.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.IncludeNode;
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

public class IncludeNodeDialog extends TitleAreaDialog implements
		SelectionListener {
	private ComponentPanel _panel = null;
	private IncludeNode _node = null;

	public IncludeNodeDialog(Shell parentShell, IncludeNode node) {
		super(parentShell);
		_node = node;
	}

	protected Control createDialogArea(Composite parent) {
	
		getShell().setText("Select a Macro Component");
		setMessage("Select A Macro Component to included");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;

		MacroComponentNode localParent = (MacroComponentNode) _node.getParent();
		_panel = new ComponentPanel(parent, localParent, ComponentPanel.PARSE_MACRO_SHAREDCOMPONENT);
		_panel.addSelectionListener(this);
		_panel.setSelectNode(localParent);
		_panel.setupTree();

		if (localParent.getPath().equals("")) {
			_panel.setExpanded(localParent.getParent());
		} else {
			_panel.setExpanded(localParent);
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
		
		_node.setIncludeMacroComPath(node.getPath().toString());
		_node.setIncludeMacroComName(node.getName());
		super.okPressed();
	}

}
