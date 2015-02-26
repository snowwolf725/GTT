package testing.oracle;

import gtt.oracle.IOracleHandler;
import gtt.oracle.OracleData;

import java.util.UUID;

import junit.framework.TestCase;

public class OracleDataTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testUUID() {
		OracleData d1 = new OracleData();
		OracleData d2 = new OracleData();

		assertFalse(d1.getUUID().equals(d2.getUUID()));
		
		String s1 = d1.getUUID().toString();
		d2.setUUID(UUID.fromString(s1));
		assertEquals(d1.getUUID(),d2.getUUID());
	}
	

	public void testOracleType() {
		OracleData d = new OracleData();
		d.setLevel(IOracleHandler.Level.APPLICATION_LEVEL);
		assertEquals(IOracleHandler.Level.APPLICATION_LEVEL, d.getLevel());
		d.setLevel(IOracleHandler.Level.WINDOW_LEVEL);
		assertEquals(IOracleHandler.Level.WINDOW_LEVEL, d.getLevel());
		d.setLevel(IOracleHandler.Level.COMPONENT_LEVEL);
		assertEquals(IOracleHandler.Level.COMPONENT_LEVEL, d.getLevel());
	}

	public void testEventLevel() {
		OracleData d = new OracleData();
		d.setType(IOracleHandler.EventType.DEFAULT);
		assertEquals(IOracleHandler.EventType.DEFAULT, d.getType());
		d.setType(IOracleHandler.EventType.USER_SELECTED);
		assertEquals(IOracleHandler.EventType.USER_SELECTED, d.getType());
		d.setType(IOracleHandler.EventType.ALL);
		assertEquals(IOracleHandler.EventType.ALL, d.getType());
	}

	public void testFileName() {
		OracleData d1 = new OracleData();
		assertEquals("/" + d1.getUUID().toString() +".oracle" ,d1.filename());
		
		d1.setSubDir("");
		assertEquals("", d1.getSubDir());
		assertEquals("/" + d1.getUUID().toString() +".oracle" ,d1.filename());

		d1.setSubDir(null);
		assertEquals("", d1.getSubDir());
		assertEquals("/" + d1.getUUID().toString() +".oracle" ,d1.filename());

		
		d1.setSubDir("subdir");
		assertEquals("subdir", d1.getSubDir());
		assertEquals("subdir/" + d1.getUUID().toString() +".oracle" ,d1.filename());
	}
	
	public void testSetType() {
		OracleData d1 = new OracleData();
		assertEquals(IOracleHandler.EventType.DEFAULT, d1.getType());
		
		d1.setType(0);
		assertEquals(IOracleHandler.EventType.DEFAULT, d1.getType());
		
		d1.setType(1);
		assertEquals(IOracleHandler.EventType.USER_SELECTED, d1.getType());
		
		d1.setType(2);
		assertEquals(IOracleHandler.EventType.ALL, d1.getType());
	}
	
	public void testSetLevel() {
		OracleData d1 = new OracleData();
		assertEquals(IOracleHandler.Level.WINDOW_LEVEL, d1.getLevel());
		
		d1.setLevel(0);
		assertEquals(IOracleHandler.Level.APPLICATION_LEVEL, d1.getLevel());
		d1.setLevel(1);
		assertEquals(IOracleHandler.Level.WINDOW_LEVEL, d1.getLevel());
		d1.setLevel(2);
		assertEquals(IOracleHandler.Level.COMPONENT_LEVEL, d1.getLevel());
	}
	
	public void testClone() {
		OracleData d1 = new OracleData();
		
		OracleData d2 = d1.clone();
		
		assertEquals(d1.getType(), d2.getType());
		assertEquals(d1.getLevel(), d2.getLevel());
		assertEquals(d1.getUUID(), d2.getUUID());
		assertNotSame(d1, d2);

	}

}
