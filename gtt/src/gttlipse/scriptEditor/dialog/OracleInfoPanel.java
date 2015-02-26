/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.Assertion;
import gtt.eventmodel.swing.SwingComponent;
import gtt.oracle.IOracleHandler;
import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gtt.testscript.OracleNode;
import gtt.testscript.ViewAssertNode;
import gttlipse.GTTlipse;
import gttlipse.scriptEditor.def.TestScriptTage;

import java.util.Iterator;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * @author SnowWolf
 * 
 *         created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class OracleInfoPanel {

	private Tree m_tree;

	private Text m_text_nodename;

	private OracleNode m_node;

	private Combo m_com_type;

	private Combo m_com_eventtype;

	private Shell m_shell;

	public OracleInfoPanel(Composite parent, OracleNode _oracle, Shell _shell) {
		m_shell = _shell;
		m_node = _oracle;

		/* setup layout */
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 300;

		/* node name */
		final Label m_lbl_nodename = new Label(area, SWT.NULL);
		m_lbl_nodename.setText("Node:     ");
		m_text_nodename = new Text(area, SWT.NULL);
		m_text_nodename.setLayoutData(data);
		m_text_nodename.setText(m_node.getName());

		/* Oracle Type */
		final Label m_lbl_type = new Label(area, SWT.NULL);
		m_lbl_type.setText("Oracle Type:     ");
		m_com_type = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_type.setLayoutData(data);
		m_com_type.setVisibleItemCount(10);
		m_com_type.add("Application");
		m_com_type.add("Window");
		m_com_type.add("Component");

		/* Event Type */
		final Label m_lbl_eventtype = new Label(area, SWT.NULL);
		m_lbl_eventtype.setText("Event Type:     ");
		m_com_eventtype = new Combo(area, SWT.READ_ONLY | SWT.DROP_DOWN);
		m_com_eventtype.setLayoutData(data);
		m_com_eventtype.setVisibleItemCount(10);

		// refactoring by zwshen 2008/05/12
		// using enum instead of integer constant

		// m_com_eventtype.add("Default event");
		// m_com_eventtype.add("User selected event");
		// m_com_eventtype.add("All event");
		IOracleHandler.EventType.DEFAULT.toString();
		m_com_eventtype.add(IOracleHandler.EventType.DEFAULT.toString());
		m_com_eventtype.add(IOracleHandler.EventType.USER_SELECTED.toString());
		m_com_eventtype.add(IOracleHandler.EventType.ALL.toString());

		/* AssertNode Tree */
		final Composite area2 = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout2 = new GridLayout();
		gridlayout2.numColumns = 1;
		area2.setLayout(gridlayout2);
		m_tree = new Tree(area2, SWT.NULL);
		GridData data2 = new GridData();
		data2.widthHint = 400;
		m_tree.setLayoutData(data2);
		m_tree.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				TreeItem[] selitems = m_tree.getSelection();
				if (selitems == null)
					return;
				TreeItem item = selitems[0];
				ViewAssertNode assertnode = (ViewAssertNode) item.getData();
				EditAssertNodeDialog editdialog = new EditAssertNodeDialog(
						m_shell, assertnode);
				editdialog.open();
				item.setText(assertnode.toSimpleString());
			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		// add button/ del button
		final Composite area3 = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout3 = new GridLayout();
		gridlayout3.numColumns = 2;
		area3.setLayout(gridlayout3);
		final Button btn_add = new Button(area3, SWT.Modify);
		btn_add.setText("Add");
		// final Button btn_add = createButton(area3, SWT.Modify, "Add", true);
		btn_add.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				NodeFactory factory = new NodeFactory();
				SwingComponent com = SwingComponent.createDefault();
				com.setName("Assert Object");
				Assertion a = new Assertion();
				ViewAssertNode assertnode = (ViewAssertNode) factory
						.createViewAssertNode(com, a);

				final TreeItem treeitem = new TreeItem(m_tree, SWT.NULL);
				treeitem.setText(assertnode.toSimpleString());
				treeitem.setData(assertnode);
				ImageRegistry image_registry = GTTlipse.getDefault()
						.getImageRegistry();
				treeitem.setImage(image_registry
						.get(TestScriptTage.ASSERT_NODE));
			}

		});

		final Button btn_del = new Button(area3, SWT.Modify);
		btn_del.setText("Del");
		// final Button btn_del = createButton(area3, SWT.Modify, "Del", true);
		btn_del.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				TreeItem[] selitems = m_tree.getSelection();
				if (selitems == null)
					return;
				TreeItem item = selitems[0];
				item.dispose();
			}

		});

		m_com_type.select(m_node.getOracleData().getLevel().ordinal());
		m_com_eventtype.select(m_node.getOracleData().getType().ordinal());

		/* init Tree */
		ImageRegistry image_registry = GTTlipse.getDefault().getImageRegistry();
		Iterator<AbstractNode> ite = m_node.iterator();
		while (ite.hasNext()) {
			ViewAssertNode assertnode = (ViewAssertNode) ite.next();
			final TreeItem treeitem = new TreeItem(m_tree, SWT.NULL);
			treeitem.setText(assertnode.toString());
			treeitem.setData(assertnode);
			treeitem.setImage(image_registry.get(TestScriptTage.ASSERT_NODE));
		}
	}

	public int getEventType() {
		return m_com_eventtype.getSelectionIndex();
	}

	public void update() {
		m_node.getOracleData().setLevel(m_com_type.getSelectionIndex());
		m_node.getOracleData()
				.setType(m_com_eventtype.getSelectionIndex());
		m_node.removeAll();
		for (TreeItem item : m_tree.getItems()) {
			m_node.add((ViewAssertNode) item.getData());
		}
	}
}
