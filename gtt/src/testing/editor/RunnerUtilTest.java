package testing.editor;

import gtt.runner.RunnerUtil;
import junit.framework.TestCase;

public class RunnerUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLoadClass() {
		String name = "gtt.editor.view.MainEditor";
		String path = ".;" + System.getProperty("java.class.path");
		Class<?> c = RunnerUtil.loadClass(name, path);

		assertNotNull(c);
		assertEquals(c.getName(), name);
	}

	public void testLoadClass2() {
		String name = RunnerUtilTest.class.getName();
		String path = ".;" + System.getProperty("java.class.path");
		Class<?> c = RunnerUtil.loadClass(name, path);

		assertNotNull(c);
		assertEquals(c.getName(), name);
	}
}
