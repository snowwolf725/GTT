package testing.gttlipse.tabular;

import gttlipse.tabular.table.TableItem;
import gttlipse.tabular.table.TableRow;

import junit.framework.TestCase;

public class TableRowTest extends TestCase {

	TableRow _row = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		_row = new TableRow();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddItem() {
		// Identify the initial size
		assertEquals(0, _row.getItemCount());
		
		// Add item to the row
		_row.addItem(new TableItem("First element"));
		_row.addItem(new TableItem("Second element"));
		assertEquals(2, _row.getItemCount());
		
		// Add item to the row at the specified position
		assertTrue(_row.addItem(2, new TableItem("Third element")));
		assertEquals(3, _row.getItemCount());
	}
	
	public void testAddItemWithOutOfRange() {
		// Identify the initial size
		assertEquals(0, _row.getItemCount());
		
		// Add item to the row
		_row.addItem(new TableItem("First element"));
		_row.addItem(new TableItem("Second element"));
		assertEquals(2, _row.getItemCount());
		
		// Add item to the row at the specified position
		assertFalse(_row.addItem(3, new TableItem("Third element")));
		assertEquals(2, _row.getItemCount());
	}
	
	public void testRemoveItem() {
		// Identify the initial size
		assertEquals(0, _row.getItemCount());
		
		// Add item to the row
		_row.addItem(new TableItem("First element"));
		_row.addItem(new TableItem("Second element"));
		assertEquals(2, _row.getItemCount());
		
		// Remove item from the row at the specified position
		assertTrue(_row.removeItem(0));
		assertEquals(1, _row.getItemCount());
	}
	
	public void testRemoveItemWithOutOfRange() {
		// Identify the initial size
		assertEquals(0, _row.getItemCount());
		
		// Add item to the row
		_row.addItem(new TableItem("First element"));
		_row.addItem(new TableItem("Second element"));
		assertEquals(2, _row.getItemCount());
		
		// Remove item from the row at the specified position
		assertFalse(_row.removeItem(2));
		assertEquals(2, _row.getItemCount());
	}
	
	public void testGetItem() {
		// Add item to the row
		_row.addItem(new TableItem("First element"));
		_row.addItem(new TableItem("Second element"));
		
		// Identify the content of second row
		assertEquals("Second element", ((TableItem)_row.getItem(1)).toString());
		
		// Add item to the row at the specified position
		_row.addItem(1, new TableItem("A new item"));
		assertEquals("A new item", ((TableItem)_row.getItem(1)).toString());
	}
}
