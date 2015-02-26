/**
 * 
 */
package gttlipse.scriptEditor.dialog;

import gtt.eventmodel.IComponent;
import gttlipse.widget.table.TableModel;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
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
import de.kupzog.ktable.KTableCellSelectionAdapter;
import de.kupzog.ktable.SWTX;

/**
 * @author SnowWolf
 * 
 * created first in project GTTlipse.scriptEditor.dialog
 * 
 */
public class SelectComponentDialog extends TitleAreaDialog {

	private List<IComponent> m_coms;

	private KTable m_table;

	private int m_selrow = -1;

	/**
	 * 
	 */
	public SelectComponentDialog(Shell parentShell, List<IComponent> coms) {
		// TODO Auto-generated constructor stub
		super(parentShell);
		m_coms = coms;
	}

	protected Control createDialogArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		area.setLayout(gridlayout);
		GridData data = new GridData();
		data.widthHint = 700;
		data.heightHint = 20;

		// create assert args list group
		Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setText("Args List");
		// create assert args list table
		m_table = new KTable(group, SWT.FULL_SELECTION | SWT.MULTI
				| SWT.V_SCROLL | SWTX.EDIT_ON_KEY);
		final TableModel m_tableModel = new TableModel(m_table, 3);
		m_table.setModel(m_tableModel);
		m_tableModel
				.setColumnHeaderText(new String[] { "Type", "Name", "Value" });
		m_tableModel.setAllColumnWidth(new int[] { 100, 100, 100 });
		m_tableModel.setColumnEditable(0, TableModel.EDITMODE_EDIT);
		m_tableModel.setColumnEditable(1, TableModel.EDITMODE_EDIT);
		m_tableModel.setColumnEditable(2, TableModel.EDITMODE_EDIT);
		m_tableModel.setColumnResizable(0, true);
		m_tableModel.setColumnResizable(1, true);
		m_tableModel.setColumnResizable(2, true);
		m_tableModel.setTableLinsterer(m_table);
		m_tableModel.initialize();

		// init args
		Iterator<IComponent> ite = m_coms.iterator();
		while (ite.hasNext()) {
			IComponent com = (IComponent) ite.next();
			m_tableModel.addItemText(new String[] { com.getType(),
					com.getName(), com.getText() });
		}

		m_table.addCellSelectionListener(new KTableCellSelectionAdapter() {
			public void cellSelected(int col, int row, int statemask) {
				m_selrow = row;
			}
		});

		// my layout
		group.setBounds(5, 5, 700, 250);
		m_table.setBounds(10, 15, 650, 139);

		return parent;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		GridData griddata = new GridData();
		griddata.grabExcessVerticalSpace = true;
		griddata.horizontalAlignment = GridData.CENTER;
		griddata.verticalAlignment = GridData.END;
		parent.setLayoutData(griddata);
		Button btn_modify = createButton(parent, SWT.Modify, "Modify", true);
		btn_modify.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

				setReturnCode(SWT.Modify);
				close();
			}
		});

		Button btn_cancel = createButton(parent, CANCEL, "Cancel", true);
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				m_selrow = -1;
				// TODO Auto-generated method stub
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	public IComponent getComponent() {
		if (m_selrow != -1)
			return m_coms.get(m_selrow - 1);
		else
			return null;
	}
}
