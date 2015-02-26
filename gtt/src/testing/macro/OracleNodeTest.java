package testing.macro;

import gtt.macro.macroStructure.OracleNode;
import gtt.oracle.IOracleHandler;
import junit.framework.TestCase;

public class OracleNodeTest extends TestCase {

	OracleNode node;

	protected void setUp() throws Exception {
		super.setUp();
		node = new OracleNode();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testGetName() {
		assertEquals("TestOracle", node.getName());
	}
	public void testClone() {
		OracleNode n = node.clone();
		assertNotSame(n, node);
		assertEquals(n.getOracleData().getLevel(), node.getOracleData()
				.getLevel());
	}

	public void testOracleLevel() {
		assertEquals(0, IOracleHandler.Level.APPLICATION_LEVEL.ordinal());
		assertEquals(1, IOracleHandler.Level.WINDOW_LEVEL.ordinal());
		assertEquals(2, IOracleHandler.Level.COMPONENT_LEVEL.ordinal());
	}

	public void testEventType() {
		assertEquals(0, IOracleHandler.EventType.DEFAULT.ordinal());
		assertEquals(1, IOracleHandler.EventType.USER_SELECTED.ordinal());
		assertEquals(2, IOracleHandler.EventType.ALL.ordinal());
	}

}
