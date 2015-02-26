package gttlipse.editor.ui;

import gttlipse.widget.table.TableModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellSelectionAdapter;
import de.kupzog.ktable.SWTX;

public class EventCoverPanel {
	private Group m_group = null;
	private KTable m_table = null;
	private TableModel m_tableModel = null;
	
	public EventCoverPanel(Composite parent) {
		initArgumentPanel(parent);
	}
	
	private void initArgumentPanel(Composite parent) {
		// init group
		m_group = new Group( parent, SWT.SHADOW_ETCHED_IN );
		m_group.setText( "Event Cover" );

		// init table
		m_table = new KTable( m_group, SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWTX.EDIT_ON_KEY );
		m_tableModel = new TableModel( m_table, 2 );
		m_table.setModel( m_tableModel );
        m_tableModel.setColumnHeaderText( new String[] { "", "Event" } );
        m_tableModel.setAllColumnWidth( new int[]{ 10, 200 } );
		m_tableModel.setColumnEditable( 0, TableModel.EDITMODE_CHECKED );
		m_tableModel.setColumnEditable( 1, TableModel.EDITMODE_EDIT );
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
		m_group.setBounds( 5, 5 , 450, 165 );
		m_table.setBounds( 10, 15, 400, 210 );
	}

	private void hookCellComboText( int col, int row, TableModel tableModel ) {
		if( col == 0 ) {
			String [] type = { "ฃพ", "กั" };
			tableModel.setComboList(type);
		}
	}
	
	public Group getGroup() {
		return m_group;
	}
	
	public TableModel getTableModel() {
		return m_tableModel;
	}

	public void add(String select, String event) {
		m_tableModel.addItemText(new String[] {select, event});
		m_table.redraw();
	}
	
	public void add(boolean select, String event) {
//		m_tableModel.addItemText(new String[] {select, event});
		m_tableModel.addItemObject(new Object[] {select, event});
		m_table.redraw();
	}

	public void clear() {
		m_tableModel.clear();
		m_table.redraw();
	}
	
	public int getItemCount() {
		return m_tableModel.getItemCount();
	}
	
	public String getEvent(int row) {
		if( row > m_tableModel.getItemCount() ) return "";
		return m_tableModel.getItemText(1, row);
	}

	public boolean getSelect(int row) {
		if( row > m_tableModel.getItemCount() ) return false;
		String result = m_tableModel.getItemObject(0, row).toString();
		if( result.equals("true") )
			return true;
		else return false;
	}
}
