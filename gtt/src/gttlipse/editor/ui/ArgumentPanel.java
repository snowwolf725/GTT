package gttlipse.editor.ui;

import gtt.eventmodel.Argument;
import gttlipse.widget.table.TableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellSelectionAdapter;
import de.kupzog.ktable.SWTX;

public class ArgumentPanel {
	private Group m_group = null;
	private KTable m_table = null;
	private TableModel m_tableModel = null;
	private boolean m_hasBtn;
	private int _visableBtn = 0;
	
	public static final int ALL_BUTTON = 0;
	public static final int ADD_BUTTON = 1;
	public static final int DEL_BUTTON = 2;

	public ArgumentPanel(Composite parent, boolean hasBtn, int visableBtn) {
		_visableBtn = visableBtn;
		m_hasBtn = hasBtn;
		initArgumentPanel(parent);
	}

	private void initArgumentPanel(Composite parent) {
		// init group
		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Args List");

		// init table
		m_table = new KTable(m_group, SWT.FULL_SELECTION | SWT.MULTI
				| SWT.V_SCROLL | SWTX.EDIT_ON_KEY);
		m_tableModel = new TableModel(m_table, 3);
		m_table.setModel(m_tableModel);
		m_tableModel
				.setColumnHeaderText(new String[] { "Type", "Name", "Value" });
		m_tableModel.setAllColumnWidth(new int[] { 100, 100, 100 });
		m_tableModel.setColumnEditable(0, TableModel.EDITMODE_COMBO);
		m_tableModel.setColumnEditable(1, TableModel.EDITMODE_EDIT);
		m_tableModel.setColumnEditable(2, TableModel.EDITMODE_EDIT);
		m_tableModel.setTableLinsterer(m_table);
		m_tableModel.initialize();

		m_table.addCellSelectionListener(new KTableCellSelectionAdapter() {
			public void cellSelected(int col, int row, int statemask) {
				if (m_tableModel.getColumnEditMode(col) == TableModel.EDITMODE_COMBO)
					hookCellComboText(col, row - 1, m_tableModel);
			}
		});

		if (m_hasBtn) {
			switch(_visableBtn) {
			case ALL_BUTTON: 
				// craete add button for adding item
				final Button addBtn = new Button(m_group, SWT.PUSH);
				addBtn.setText("Add");
	
				addBtn.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						m_tableModel.addItemText(new String[] { "String", "name",
								"value" });
						m_table.redraw();
					}
				});
	
				// create del button for deling item
				final Button delBtn = new Button(m_group, SWT.PUSH);
				delBtn.setText("Del");
				delBtn.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						int[] selrows = m_table.getRowSelection();
						for (int i = 0; i < selrows.length; i++) {
							m_tableModel.removeItem(selrows[i] - 1);
						}
						m_table.redraw();
					}
				});
	
				// Btn layout
				addBtn.setBounds(10, 90, 50, 25);
				delBtn.setBounds(65, 90, 50, 25);
				break;
			case ADD_BUTTON:
				// craete add button for adding item
				final Button addButton = new Button(m_group, SWT.PUSH);
				addButton.setText("Add");
	
				addButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						m_tableModel.addItemText(new String[] { "String", "name",
								"value" });
						m_table.redraw();
					}
				});
				addButton.setBounds(10, 90, 50, 25);
				break;
			case DEL_BUTTON:
				// create del button for deling item
				final Button delButton = new Button(m_group, SWT.PUSH);
				delButton.setText("Del");
				delButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						int[] selrows = m_table.getRowSelection();
						for (int i = 0; i < selrows.length; i++) {
							m_tableModel.removeItem(selrows[i] - 1);
						}
						m_table.redraw();
					}
				});
				delButton.setBounds(10, 90, 50, 25);
				break;
			}
		}

		// layout
		m_group.setBounds(5, 5, 450, 165);
		m_table.setBounds(10, 15, 400, 62);
	}

	private void hookCellComboText(int col, int row, TableModel tableModel) {
		if (col == 0) {
			String[] type = { "String", "char", "double", "float", "int",
					"Long", "Object" };
			tableModel.setComboList(type);
		}
	}

	public Group getGroup() {
		return m_group;
	}

	public TableModel getTableModel() {
		return m_tableModel;
	}

	public void addArgument(Argument arg) {
		m_tableModel.addItemText(new String[] { arg.getType(), arg.getName(),
				arg.getValue() });
		m_table.redraw();
	}

	public void clearArgument() {
		m_tableModel.clear();
		m_table.redraw();
	}

	public int getItemCount() {
		return m_tableModel.getItemCount();
	}

	public String getType(int row) {
		if (row > m_tableModel.getItemCount())
			return "";
		return m_tableModel.getItemText(0, row);
	}

	public String getName(int row) {
		if (row > m_tableModel.getItemCount())
			return "";
		return m_tableModel.getItemText(1, row);
	}

	public String getValue(int row) {
		if (row > m_tableModel.getItemCount())
			return "";
		return m_tableModel.getItemText(2, row);
	}
}
