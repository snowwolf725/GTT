package testing.runner;

import gtt.runner.LaunchData;
import junit.framework.TestCase;

public class LaunchInformationTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreate() {
		
		LaunchData info = LaunchData.create("path","name","argument");
		assertEquals(info.getClasspath(), "path");
		assertEquals(info.getClassName(), "name");
		assertEquals(info.getArgument(), "argument");
	}

}
