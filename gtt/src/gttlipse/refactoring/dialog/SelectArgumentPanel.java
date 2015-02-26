package gttlipse.refactoring.dialog;

import gtt.eventmodel.Argument;
import gtt.eventmodel.Arguments;
import gttlipse.widget.table.TableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellSelectionAdapter;
import de.kupzog.ktable.SWTX;

public class SelectArgumentPanel {
	private Group m_group = null;
	private KTable m_table = null;
	private TableModel m_tableModel = null;
	
	public SelectArgumentPanel(Composite parent) {
		initialSelectArgumentPanel(parent);
	}
	
	private void initialSelectArgumentPanel(Composite parent) {
		// init group
		m_group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		m_group.setText("Argument List");		
		
		// init table
		m_table = new KTable(m_group, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWTX.EDIT_ON_KEY);
		m_tableModel = new TableModel(m_table, 4);
		m_table.setModel(m_tableModel);
		m_tableModel
				.setColumnHeaderText(new String[] { "", "Type", "Argument", "Default Value" });
		m_tableModel.setAllColumnWidth(new int[] { 20, 100, 100, 100 });
		m_tableModel.setColumnEditable(0, TableModel.EDITMODE_CHECKED);
		m_tableModel.setColumnEditable(1, TableModel.EDITMODE_NONE);
		m_tableModel.setColumnEditable(2, TableModel.EDITMODE_NONE);
		m_tableModel.setColumnEditable(3, TableModel.EDITMODE_EDIT);
		m_tableModel.setTableLinsterer(m_table);
		m_tableModel.initialize();
        m_table.addCellSelectionListener( new KTableCellSelectionAdapter()
	    {
	        public void cellSelected( int col, int row, int statemask ) 
	        {
				if( m_tableModel.getColumnEditMode( col ) == TableModel.EDITMODE_COMBO )
		            hookCellComboText( col, row - 1, m_tableModel );
				
//				if( m_tableModel.getColumnEditMode( col ) == TableModel.EDITMODE_CHECKED ) {
//					Object result = m_tableModel.getItemObject(col, row -1 );
//				}
	        }
	    } );
		
		// layout
		m_group.setBounds(5, 5, 450, 165);
		m_table.setBounds(10, 15, 400, 165);
	}
	
	private void hookCellComboText( int col, int row, TableModel tableModel ) {
		if( col == 0 ) {
			String [] type = { "ฃพ", "กั" };
			tableModel.setComboList(type);
		}
	}
	
	public void addArgument(Argument arg) {
		m_tableModel.addItemObject(new Object[] {false, arg.getType() ,arg.getName(), arg.getValue()});
		m_table.redraw();
	}
	
	public void clearArgument() {
		m_tableModel.clear();
		m_table.redraw();
	}
	
	public int getItemCount() {
		return m_tableModel.getItemCount();
	}
	
	public Arguments getArguments() {
		Arguments args = new Arguments();
		for(int i = 0; i < getItemCount(); i++) {
			if(getSelect(i) == true) {
				Argument arg = Argument.create(getArgType(i), getArgName(i), getArgValue(i));
				args.add(arg);
			}
		}
		return args;
	}
	
	private String getArgType(int row) {
		return m_tableModel.getItemText(1, row);
	}
	
	private String getArgName(int row) {
		return m_tableModel.getItemText(2, row);
	}
	
	private String getArgValue(int row) {
		return m_tableModel.getItemText(3, row);
	}
	
	private boolean getSelect(int row) {
		if( row > m_tableModel.getItemCount() ) 
			return false;
		String result = m_tableModel.getItemObject(0, row).toString();
		if( result.equals("true") )
			return true;
		else 
			return false;
	}
}
