package testing.macro;

import gtt.editor.view.BoxTableView;

import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import junit.framework.TestCase;

public class BoxTableViewTest extends TestCase {

	BoxTableView m_Table;
	Vector<String> titles;
	Vector<Integer> columWidths;
	int rowHight = 50;

	protected void setUp() throws Exception {
		titles = new Vector<String>();
		titles.add("type");
		titles.add("name");
		titles.add("value");

		columWidths = new Vector<Integer>();
		columWidths.add(50);
		columWidths.add(50);
		columWidths.add(50);

		m_Table = new BoxTableView(titles, columWidths, rowHight);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		m_Table = null;
		super.tearDown();
	}

	public void testAddRow() {
		assertEquals(m_Table.getRowCount(), 0);

		assertTrue(m_Table.addRow(createTableRow()));
		assertEquals(m_Table.getRowCount(), 1);

		assertTrue(m_Table.addRow(createTableRow()));
		assertEquals(m_Table.getRowCount(), 2);

		assertTrue(m_Table.addRow(createTableRow()));
		assertEquals(m_Table.getRowCount(), 3);

		assertTrue(m_Table.removeLastRow());
		assertEquals(m_Table.getRowCount(), 2);

		assertTrue(m_Table.removeLastRow());
		assertEquals(m_Table.getRowCount(), 1);

		assertTrue(m_Table.removeLastRow());
		assertEquals(m_Table.getRowCount(), 0);

		assertFalse(m_Table.removeLastRow());
	}

	final String[] ArgumentTypeListing = { "char", "double", "float", "int",
			"String", "boolean", "long", "short" };

	private Vector<JComponent> createTableRow() {
		Vector<JComponent> v = new Vector<JComponent>();
//		JComboBox box = new JComboBox(ArgumentTypeListing);
		v.add(new JComboBox(ArgumentTypeListing)); // type
		v.add(new JTextField()); // name
		v.add(new JTextField()); // value
		return v;
	}


	public void testRemoveAll() {

		assertEquals(m_Table.getRowCount(), 0);

		assertTrue(m_Table.addRow(createTableRow()));
		assertEquals(m_Table.getRowCount(), 1);

		assertTrue(m_Table.addRow(createTableRow()));
		assertEquals(m_Table.getRowCount(), 2);

		assertTrue(m_Table.addRow(createTableRow()));
		assertEquals(m_Table.getRowCount(), 3);

		m_Table.removeAll();
		assertEquals(m_Table.getRowCount(), 0);

	}

	public void testGetRowCount() {
		assertEquals(m_Table.getRowCount(), 0);
	}

	public void testGetColumnSize() {
		assertEquals(m_Table.getColumnSize(), titles.size());

		m_Table.removeAll();
		assertEquals(m_Table.getColumnSize(), titles.size());
	}

	public void testSetValue() {
		assertEquals(m_Table.getRowCount(), 0);

		assertTrue(m_Table.addRow(createTableRow()));
		assertEquals(m_Table.getRowCount(), 1);
		assertTrue(m_Table.setValue("String", 0, 0));
		assertTrue(m_Table.setValue("name1", 0, 1));
		assertTrue(m_Table.setValue("value1", 0, 2));
		assertEquals(m_Table.getValue(0, 0), "String");
		assertEquals(m_Table.getValue(0, 1), "name1");
		assertEquals(m_Table.getValue(0, 2), "value1");
	}

	public void testSetValue_multiple() {
		assertEquals(m_Table.getRowCount(), 0);

		// 加入十個參數
		for(int row=0;row<10;row++) {
			assertTrue(m_Table.addRow(createTableRow()));
			assertEquals(m_Table.getRowCount(), row+1);
			assertTrue(m_Table.setValue("String", row, 0));
			assertTrue(m_Table.setValue("name"+row, row, 1));
			assertTrue(m_Table.setValue("value"+row, row, 2));
			assertEquals(m_Table.getValue(row, 0), "String");
			assertEquals(m_Table.getValue(row, 1), "name"+row);
			assertEquals(m_Table.getValue(row, 2), "value"+row);
		}

		assertEquals(m_Table.getRowCount(), 10);
	}


	public void testSetName() {
		m_Table.setName("table");
		assertEquals(m_Table.getViewName(), "table");
		m_Table.setName("table1");
		assertEquals(m_Table.getViewName(), "table1");
		m_Table.setName("table2");
		assertEquals(m_Table.getViewName(), "table2");
	}

	public void testGetView() {
		assertNotNull(m_Table.getView());
	}


}
