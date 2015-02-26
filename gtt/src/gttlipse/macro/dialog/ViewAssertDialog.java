package gttlipse.macro.dialog;

import gtt.eventmodel.Argument;
import gtt.macro.DefaultMacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentNode;
import gtt.macro.macroStructure.ViewAssertNode;

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

public class ViewAssertDialog extends TitleAreaDialog implements
		SelectionListener {
	private ViewAssertNode m_node = null;
	private AbstractMacroNode m_selectNode = null;

	private ComponentPanel m_selectComponentPanel = null;
	private ViewAssertPanel m_viewAssertPanel = null;

	public ViewAssertDialog(Shell parentShell, ViewAssertNode node) {
		super(parentShell);
		m_node = node;

		if (m_node.getComponent() == null)
			m_selectNode = DefaultMacroFinder.findLocalRoot(m_node);
		else
			m_selectNode = m_node.getComponent();
	}

	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("View Assert Node");
		setMessage("Edit View Assert Node");

		final GridLayout tabLayout = new GridLayout();
		tabLayout.numColumns = 1;

		// 頁籤
		TabFolder tabFolder = new TabFolder(parent, SWT.NONE);

		// 選擇Component頁籤
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText("Component");
		Composite selectComponent = new Composite(tabFolder, SWT.NONE);
		item1.setControl(selectComponent);
		selectComponent.setLayout(tabLayout);

		m_selectComponentPanel = new ComponentPanel(selectComponent,
				DefaultMacroFinder.findLocalRoot(m_node), m_node);
		m_selectComponentPanel.addSelectionListener(this);
		m_selectComponentPanel.setupTree();

		// 編輯View Assert的頁籤
		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText("View Assert");
		Composite viewAssert = new Composite(tabFolder, SWT.NONE);
		item2.setControl(viewAssert);
		viewAssert.setLayout(tabLayout);

		m_viewAssertPanel = new ViewAssertPanel(viewAssert, m_node);
		
		setSelectNode(m_selectNode);
		
		m_selectComponentPanel.setExpanded(m_selectNode);
		return parent;
	}

	private void setSelectNode(AbstractMacroNode node) {
		if (node == null)
			return;
		if (!(node instanceof ComponentNode))
			return;

		m_selectNode = node;
		m_viewAssertPanel.setSelectNode((ComponentNode) node);
		m_selectComponentPanel.setSelectNodeName(node.getName());
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		doSelect();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		doSelect();
	}

	@Override
	protected void okPressed() {
		if (!(m_selectNode instanceof ComponentNode))
			return;

		// 設定reference
		m_node.setComponentPath(m_selectNode.getPath().toString());

		m_node.getAssertion().setMethod(m_viewAssertPanel.getMethod());

		// 設定 args
		m_node.getArguments().clear();
		for (int i = 0; i < m_viewAssertPanel.getArgCount(); i++) {
			Argument arg = Argument.create(m_viewAssertPanel.getArgType(i), "",
					m_viewAssertPanel.getArgValue(i));
			m_node.getArguments().add(arg);
		}

		// 設定 assert value
		m_node.getAssertion().setValue(m_viewAssertPanel.getAssertValue());

		// 設定 message
		m_node.getAssertion().setMessage(m_viewAssertPanel.getMessage());

		// 設定correct Amount
		m_node.getAssertion().setExpectedSizeOfCheck(
				Integer.parseInt(m_viewAssertPanel.getCorrectAmount()));

		// 設定dynamicComponent
		if (!m_viewAssertPanel.getDynamicComponentValue().isEmpty()) {
			m_node.setDyType(m_viewAssertPanel.getDynamicComponentType());
			m_node.setDyValue(m_viewAssertPanel.getDynamicComponentValue());
			m_node.setDyIndex(m_viewAssertPanel.getDynamicComponentIndex());
		}

		super.okPressed();
	}

	private void doSelect() {
		Tree t = m_selectComponentPanel.getTree();

		if (t.getSelectionCount() == 0)
			return;
		TreeItem _selectTreeItem = t.getSelection()[0];
		AbstractMacroNode selectNode = (AbstractMacroNode) _selectTreeItem
				.getData();

		m_selectComponentPanel.setSelectNode(selectNode);

		if (!(m_selectComponentPanel.getSelectNode() instanceof ComponentNode)) {
			m_selectComponentPanel
					.setMesssage("You should select a component node.");
			return;
		}

		m_selectComponentPanel.setMesssage("View Assert");
		setSelectNode((ComponentNode) m_selectComponentPanel.getSelectNode());
	}

}
