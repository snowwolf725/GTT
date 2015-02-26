package testing.eventmodel;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.swing.SwingComponent;
import junit.framework.TestCase;

public class SwingComponentTest extends TestCase {

	public void testSwingComponent() {
		SwingComponent a = SwingComponent.createDefault();
		assertEquals(a.getType(), SwingComponent.DEFAULT_TYPE);
		assertEquals(a.getWinType(), SwingComponent.DEFAULT_WIN_TYPE);
		assertEquals(a.getTitle(), SwingComponent.DEFAULT_TITLE);
		assertEquals(a.getName(), SwingComponent.DEFAULT_NAME);
		assertEquals(a.getText(), SwingComponent.DEFAULT_TEXT);
		assertEquals(a.getIndex(), SwingComponent.DEFAULT_INDEX);
		assertEquals(a.getIndexOfSameName(),
				SwingComponent.DEFAULT_INDEX_OF_SAME_NAME);
	}

	public void testClone() {
		SwingComponent a = SwingComponent.createDefault();
		SwingComponent b = a.clone();

		/**
		 * a, b 不會是同一個物件 但所儲存的資訊是一模一樣的
		 */
		assertNotSame(a, b);
		assertEquals(a.getType(), b.getType());
		assertEquals(a.getType(), SwingComponent.DEFAULT_TYPE);
	}

	public void testClone2() {
		SwingComponent a = SwingComponent.createDefault();
		a.setType("JButton");
		SwingComponent b = a.clone();

		/**
		 * a, b 不會是同一個物件 但所儲存的資訊是一模一樣的
		 */
		assertNotSame(a, b);
		assertEquals(a.getType(), b.getType());
		assertEquals(a.getType(), "JButton");
		assertEquals(b.getType(), "JButton");
	}

	public void testToString() {
		SwingComponent a = SwingComponent.createDefault();
		a.setType("JButton");

		// 沒有name 時，就不會出現 type
		assertEquals(a.toString(), a.getType());

		a.setName("1");
		assertEquals(a.toString(), a.getName() + ":" + a.getType());

		SwingComponent b = SwingComponent.createDefault();
		assertFalse(a.toString() == b.toString());

		b.setType("JButton");
		b.setName("1");
		assertEquals(a.toString(), b.toString());
	}
	
	public void testSetName() {
		SwingComponent a = SwingComponent.createDefault();

		assertEquals("", a.getName());
		a.setName(null);
		assertNotNull(a.getName()); // 不會有null name
		assertEquals("", a.getName());
	}

	public void testSetText() {
		SwingComponent a = SwingComponent.createDefault();

		assertEquals("", a.getText());
		a.setText(null);
		assertNotNull(a.getText()); // 不會有null text
		assertEquals("", a.getText());
	}

	public void testSetType() {
		SwingComponent a = SwingComponent.createDefault();

		assertEquals(SwingComponent.DEFAULT_TYPE, a.getType());
		a.setType(null);
		assertNotNull(a.getType()); // 不會有null type
		assertEquals(SwingComponent.DEFAULT_TYPE, a.getType());
	}
	
	public void testEqals() {
		SwingComponent a = SwingComponent.createDefault();
		
		assertFalse( a.equals(null));
		assertFalse( a.equals(this));
		
		assertEquals(a, a);
		IComponent b = a.clone();
		assertEquals(b, a);
		
		a.setName("a");
		b.setName("b");
		assertFalse(a.equals(b)); // name 不一樣
	}
	

}
