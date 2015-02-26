package testing.tester;

import gtt.eventmodel.EventModelFactory;
import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEventModel;
import gtt.eventmodel.swing.SwingComponent;
import gtt.testscript.NodeFactory;

import javax.swing.JFrame;

import junit.framework.TestCase;


public class PlaybackTest extends TestCase {

	IEventModel m_model = EventModelFactory.getDefault();
	NodeFactory m_Factory = new NodeFactory();
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testFindComponent() {
		IComponent c = SwingComponent.createDefault();
		
		assertNotNull(c);
		
		assertEquals(c.getWinType(),  "javax.swing.JFrame" );
		assertEquals("class " + c.getWinType(), JFrame.class.toString() );
		try {
			assertEquals( Class.forName( c.getWinType() ), JFrame.class );
		} catch (ClassNotFoundException e) {
			assertTrue(false);
		}
	}
	
}
