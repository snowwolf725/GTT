package gttlipse.vfsmEditor.view.dialog;

import gtt.testscript.AbstractNode;
import gttlipse.macro.dialog.MacroSelectionDialog;
import gttlipse.widget.table.TableModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;

/**
 * @author Jason Wang note : the purpose of this dialog is to setting event node
 *         and save it in the event node container (history). when connection is
 *         created, show the list to be selection. date : 2008/06/28
 */
public class MacroEventNodeSettingDialog extends TrayDialog {

	private static List<AbstractNode> m_EventList = new ArrayList<AbstractNode>();

	public MacroEventNodeSettingDialog(Shell shell) {
		super(shell);
	}

	public MacroEventNodeSettingDialog(Shell shell, String title) {
		super(shell);
		this.getShell().setText(title);
	}

	public MacroEventNodeSettingDialog(Shell shell, List<AbstractNode> eventlist) {
		super(shell);
		m_EventList = eventlist;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout areaLayout = new GridLayout();
		areaLayout.numColumns = 1;
		composite.setLayout(areaLayout);

		GridData areaLayoutdata = new GridData();
		areaLayoutdata.horizontalAlignment = GridData.CENTER;
		areaLayoutdata.verticalAlignment = GridData.CENTER;
		composite.setLayoutData(areaLayoutdata);
		createEventNodeConatinerPane(composite);

		return composite;
	}

	private void updateTable() {
		m_TableModel.clear();
		for (int i = 0; i < m_EventList.size(); i++) {
			String name = m_EventList.get(i).toString();
			m_TableModel.addItemText(new String[] { Integer.toString(i + 1),
					name });
		}
		m_Table.redraw();
	}

	private KTable m_Table;
	private TableModel m_TableModel;

	private void createEventNodeConatinerPane(Composite composite) {
		// create state list group
		// my layout
		Group group = new Group(composite, SWT.SHADOW_ETCHED_IN);
		group.setText("Macro Event Container (History List) ");
		group.setBounds(5, 5, 465, 250);

		initTable(group);
		initAddButton(group);
		initRemoveButton(group);
	}

	private void initTable(Group group) {
		m_Table = new KTable(group, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWTX.EDIT_ON_KEY);
		m_TableModel = new TableModel(m_Table, 2);

		m_TableModel.setColumnHeaderText(new String[] { "index", "event" });
		m_TableModel.setAllColumnWidth(new int[] { 50, 350 });
		m_TableModel.initialize();
		m_Table.setModel(m_TableModel);
		// 更新 table model 資料
		updateTable();
		// 設定 table bounds
		m_Table.setBounds(5, 20, 400, 145);
	}

	private void initRemoveButton(Group group) {
		// remove button
		Button removeBtn = new Button(group, SWT.PUSH);
		removeBtn.setText("remove");
		removeBtn.setBounds(70, 170, 50, 25);
		removeBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				int idx = m_Table.getRowSelection()[0];
				int itemIndex = idx - m_TableModel.getFixedRowCount();
				// remove table item and handle
				m_TableModel.removeItem(itemIndex);
				m_EventList.remove(itemIndex);
				m_Table.redraw();

				updateTableSelection(idx, itemIndex);

			}

			private void updateTableSelection(int idx, int itemIndex) {
				if (m_TableModel.getItemCount() == 0)
					m_Table.clearSelection();
				else if (itemIndex < m_TableModel.getItemCount())
					m_Table.setSelection(0, idx, true);
				else
					m_Table.setSelection(0, idx - 1, true);
			}
		});
	}

	private void initAddButton(Group group) {
		// add button
		Button addBtn = new Button(group, SWT.PUSH);
		addBtn.setText("add");
		addBtn.setBounds(10, 170, 50, 25);
		addBtn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				MacroSelectionDialog dialog = new MacroSelectionDialog(null, 0);
				dialog.open();
				if (dialog.getReturnCode() != SWT.OK)
					return;
				// 加入使用者選取的 MacroEventNodes
				m_EventList.addAll(dialog.getNodes());
				// 更新 Table UI
				updateTable();
			}
		});
	}

	protected void createButtonsForButtonBar(Composite parent) {
		// set layout of buttonbar
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);

		// create button and handle event
		Button okBtn = createButton(parent, SWT.OK, "OK", true);
		okBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
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

	public List<AbstractNode> getSelectEventList() {
		return m_EventList;
	}
}
