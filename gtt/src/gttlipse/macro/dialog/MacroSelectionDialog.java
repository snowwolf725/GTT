package gttlipse.macro.dialog;

import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.MacroComponentNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gttlipse.macro.view.MacroViewPart;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;


public class MacroSelectionDialog extends TrayDialog {
	private Tree m_tree = null;
	private List<Object> m_selection = new Vector<Object>();
	private int m_selectionType = 0;

	// selection type = 0 (mutiple selection mode)
	// selection type = 1 (single selection mode)

	// constructor
	public MacroSelectionDialog(Shell parentShell, int selectionType) {
		super(parentShell);
		m_selectionType = selectionType;
		this.create();
	}

	// constroctor - initial a title string of dialog
	public MacroSelectionDialog(Shell parentShell, String title,
			int selectionType) {
		super(parentShell);
		m_selectionType = selectionType;
		this.create();
		this.getShell().setText(title);
	}

	protected Control createDialogArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NULL);
		area.setLayout(createGridLayout());
		area.setLayoutData(createGridData());
		createMacroEventPane(area);

		return area;
	}

	private GridData createGridData() {
		GridData areaLayoutdata = new GridData();
		areaLayoutdata.horizontalAlignment = GridData.CENTER;
		areaLayoutdata.verticalAlignment = GridData.CENTER;
		return areaLayoutdata;
	}

	private GridLayout createGridLayout() {
		GridLayout areaLayout = new GridLayout();
		areaLayout.numColumns = 1;
		return areaLayout;
	}

	// handle event of the button on the buttonbar
	protected void createButtonsForButtonBar(Composite parent) {
		// set layout of buttonbar
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);

		// create button and handel event
		Button okBtn = createButton(parent, SWT.OK, "OK", true);
		okBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TreeItem selection[] = m_tree.getSelection();
				for (int i = 0; i < selection.length; i++)
					m_selection.add(selection[i].getData());

				setReturnCode(SWT.OK);
				close();
			}
		});

		Button cancelBtn = createButton(parent, CANCEL, "Cancel", true);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	public Object[] selectedNodes() {
		List<AbstractNode> result_nodes = new LinkedList<AbstractNode>();
		NodeFactory mf = new NodeFactory();

		for (int i = 0; i < m_selection.size(); i++) {
			if (!(m_selection.get(i) instanceof MacroEventNode))
				continue;

			MacroEventNode me = (MacroEventNode) m_selection.get(i);

			result_nodes.add(mf.createReferenceMacroEventNode(me.getPath()
					.toString()));
		}

		return result_nodes.toArray();
	}

	public List<AbstractNode> getNodes() {
		List<AbstractNode> result_nodes = new LinkedList<AbstractNode>();
		NodeFactory mf = new NodeFactory();

		for (int i = 0; i < m_selection.size(); i++) {
			if (!(m_selection.get(i) instanceof MacroEventNode))
				continue;

			AbstractMacroNode me = (AbstractMacroNode) m_selection.get(i);
			String path = me.getPath().toString().substring(2);
			result_nodes.add(mf.createReferenceMacroEventNode(path));
		}

		return result_nodes;
	}

	private void createMacroEventPane(Composite parent) {
		// create state list group
		Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setText("MacroEvent List");
		group.setBounds(5, 5, 450, 450);

		m_tree = MacroPanelCreator.create(group, m_selectionType);
		m_tree.setBounds(10, 15, 400, 340);
	}
}

class MacroPanelCreator {
	public static Tree create(Composite parent, int selType) {
		return new MacroPanelCreator()._create(parent, selType);
	}

	public Tree _create(Composite parent, int selection_type) {
		int style = getTreeStyle(selection_type);
		Tree tree = new Tree(parent, style);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setText("Type");
		column1.setWidth(150);

		TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
		column2.setText("Name");
		column2.setWidth(210);

		// initial macro list data
		initMacroTreeLayout(tree);

		return tree;
	}

	private final static int SINGLE_STYLE = SWT.FULL_SELECTION | SWT.BORDER
			| SWT.H_SCROLL | SWT.V_SCROLL;
	private final static int MULTI_STYLE = SWT.MULTI | SINGLE_STYLE;

	private int getTreeStyle(int selType) {
		return (selType == 1) ? SINGLE_STYLE : MULTI_STYLE;
	}

	private void initMacroTreeLayout(Tree tree) {
		// queue for macro
		AbstractMacroNode macroRoot = getMacroRoot();
		LinkedList<AbstractMacroNode> macro_Q = new LinkedList<AbstractMacroNode>();
		macro_Q.add(macroRoot);

		// queue for TreeItem
		TreeItem rootItem = createRootItem(tree, macroRoot);
		LinkedList<TreeItem> item_Q = new LinkedList<TreeItem>();
		item_Q.add(rootItem);

		while (macro_Q.isEmpty() == false) {
			AbstractMacroNode macro_pop = macro_Q.getFirst();
			macro_Q.removeFirst();

			TreeItem item_pop = item_Q.getFirst();
			item_Q.removeFirst();

			for (int i = 0; i < macro_pop.size(); i++) {
				AbstractMacroNode child = (AbstractMacroNode) macro_pop.get(i);
				if (child instanceof MacroComponentNode) {
					macro_Q.add(child);
					TreeItem item = createTreeItem(item_pop, child,
							"Macro Component");
					item_Q.add(item);
				} else if (child instanceof MacroEventNode) {
					macro_Q.add(child);
					TreeItem item = createTreeItem(item_pop, child,
							"Macro Event");
					item_Q.add(item);
				} else if (child instanceof MacroEventNode) {
					macro_Q.add(child);
					TreeItem item = createTreeItem(item_pop, child,
							"Macro Event");
					item_Q.add(item);
				}
				// else if (child instanceof ComponentEventNode) {
				// TreeItem item = new TreeItem(item_pop, SWT.NONE);
				// item.setText(new String[] { "Single Event",
				// ((ComponentEventNode) child).toString() });
				// item.setData(child);
				// setTreeItemImage(item, child);
				// } else if (child instanceof MacroEventCallerNode) {
				// }
			}
		}
	}

	private TreeItem createTreeItem(TreeItem item_pop, AbstractMacroNode child,
			String type) {
		TreeItem item = new TreeItem(item_pop, SWT.NONE);
		item.setText(new String[] { type, child.getName() });
		item.setData(child);
		setTreeItemImage(item, child);
		return item;
	}

	private AbstractMacroNode getMacroRoot() {
		return MacroViewPart.getMacroPresenter().getDocument().getMacroScript();
	}

	private TreeItem createRootItem(Tree tree, AbstractMacroNode macroRoot) {
		TreeItem rootItem = new TreeItem(tree, SWT.NONE);
		rootItem
				.setText(new String[] { "Macro Component", macroRoot.getName() });
		rootItem.setData(macroRoot);

		setTreeItemImage(rootItem, macroRoot);
		return rootItem;
	}

	private void setTreeItemImage(TreeItem item, AbstractMacroNode node) {
		Image image = MacroViewPart.getLabelProvider().getImage(node);
		item.setImage(image);
	}
}
