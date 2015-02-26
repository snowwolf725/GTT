package gttlipse.macro.dialog;

import gtt.macro.DefaultMacroFinder;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.ComponentNode;
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

// implements SelectionListener 主要是
// 在 select component panel 中，有選取某個node時，可以立即知道，以方便做相關的更新動作
public class ComponentEventDialog extends TitleAreaDialog implements
		SelectionListener {
	private ComponentPanel compPanel = null;
	private EditComponentEventPanel editPanel = null;
	private ComponentEventNode eventnode = null;

	public ComponentEventDialog(Shell parentShell, ComponentEventNode node) {
		super(parentShell);
		eventnode = node;
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText("Componentn Event Node");
		setMessage("Edit Component Event Node");

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

		MacroComponentNode localParent = DefaultMacroFinder.findRoot(eventnode);
		compPanel = new ComponentPanel(selectComponent, localParent, eventnode);

		compPanel.addSelectionListener(this);
		compPanel.setSelectNode(eventnode.getComponent());
		compPanel.setupTree();
		if (eventnode.getComponentPath().equals("")) {
			compPanel.setExpanded(eventnode.getParent());
		} else {
			compPanel.setExpanded(eventnode.getComponent());
		}

		// 編輯Event的頁籤
		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText("Event");
		Composite editEvent = new Composite(tabFolder, SWT.NONE);
		item2.setControl(editEvent);
		editEvent.setLayout(tabLayout);

		editPanel = new EditComponentEventPanel(editEvent, eventnode);
		return parent;
	}

	private void doSelect() {
		Tree t = compPanel.getTree();
		if (t.getSelectionCount() == 0)
			return;
		TreeItem item = t.getSelection()[0];
		AbstractMacroNode node = (AbstractMacroNode) item.getData();
		compPanel.setSelectNode(node);
		editPanel.updateSelectNode((ComponentNode) node);
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
		AbstractMacroNode node = compPanel.getSelectNode();

		if (node == null)
			return;
		if (!(node instanceof ComponentNode))
			return;

		updateEventNode(node);
		updateDynamicComponentPart();

		super.okPressed();
	}

	private void updateDynamicComponentPart() {
		// 設定dynamicComponent
		if (editPanel.getDynamicComponentValue().isEmpty())
			return;
		eventnode.setDyType(editPanel.getDynamicComponentType());
		eventnode.setDyValue(editPanel.getDynamicComponentValue());
		eventnode.setDyIndex(editPanel.getDynamicComponentIndex());
	}

	private void updateEventNode(AbstractMacroNode node) {
		eventnode.setComponentPath(node.getPath().toString());
		eventnode.setEvent(editPanel.getSelectEventType(), editPanel
				.getSelectEventID());
		// 設定 Arguments
		eventnode.getArguments().clear();
		for (int i = 0; i < editPanel.getArgCount(); i++) {
			eventnode.getArguments().add(editPanel.getArgument(i));
		}
	}
}
