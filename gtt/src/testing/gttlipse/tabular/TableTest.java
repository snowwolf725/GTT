package testing.gttlipse.tabular;

import gttlipse.tabular.table.Table;
import gttlipse.tabular.table.TableRow;

import junit.framework.TestCase;

public class TableTest extends TestCase {

	private Table _table = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		_table = new Table();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddRow() {
		// Identify the initial size
		assertEquals(0, _table.getRowCount());
		
		// Add row to the table
		_table.addRow(new TableRow());
		_table.addRow(new TableRow());
		assertEquals(2, _table.getRowCount());
		
		// Add row to the table at the specified position
		assertTrue(_table.addRow(2, new TableRow()));
		assertEquals(3, _table.getRowCount());
	}
	
	public void testRemoveRow() {
		// Identify the initial size
		assertEquals(0, _table.getRowCount());
		
		// Add row to the table
		_table.addRow(new TableRow());
		_table.addRow(new TableRow());
		assertEquals(2, _table.getRowCount());
		
		// Remove row from the table at the specified position
		assertTrue(_table.removeRow(0));
		assertEquals(1, _table.getRowCount());
	}
}
