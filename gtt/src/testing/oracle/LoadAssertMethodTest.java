package testing.oracle;

import gtt.oracle.AssertableMethodLoader;
import junit.framework.TestCase;

public class LoadAssertMethodTest extends TestCase {
	AssertableMethodLoader loader = null;

	protected void setUp() {
		loader = new AssertableMethodLoader();
	}

	protected void tearDown() {
		loader = null;
	}

	public void testLoadAssertMethod() {
		assertNull(loader.getMethods(""));
		
		assertNotNull(loader.getMethods("java.awt.Dialog"));
		assertEquals(58,loader.getMethods("java.awt.Dialog").size());
		
		assertNotNull(loader.getMethods("javax.swing.JTextField"));
		assertEquals(79,loader.getMethods("javax.swing.JTextField").size());
		
		
	}

}
