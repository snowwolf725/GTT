package testing.gttlipse.testcasegen;

import gtt.eventmodel.IComponent;
import gtt.eventmodel.IEvent;
import gtt.eventmodel.swing.SwingComponent;
import gtt.eventmodel.swing.SwingEvent;
import gtt.testscript.EventNode;
import gtt.testscript.NodeFactory;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.ConsecutiveSet;
import junit.framework.TestCase;

public class ConsecutiveSetTest extends TestCase 
{
	private NodeFactory m_factory = null;
	
	public ConsecutiveSetTest()
    {
		m_factory = new NodeFactory();
    }
    
    public void testIsVisited1()
    {
    	ConsecutiveSet conSet = new ConsecutiveSet();
    	Edge edge1 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge2 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp2", "event2" ) );
    	Edge edge3 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge4 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp2", "event2" ) );
    	Edge edge5 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp4", "event4" ) );
    	Edge edge6 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp5", "event5" ) );
    	Edge edge7 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp6", "event6" ) );
    	Edge edge8 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp7", "event7" ) );
    	Edge edge9 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp8", "event8" ) );
    	Edge edge10 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp9", "event9" ) );
    	conSet.add( new Edge[]{ edge1, edge2 } );
    	conSet.add( new Edge[]{ edge3, edge4 } );
    	assertTrue( conSet.size() == 2 );
    	
    	conSet.add( new Edge[]{ edge5, edge6 } );
    	conSet.add( new Edge[]{ edge7, edge8 } );
    	conSet.add( new Edge[]{ edge9, edge10 } );
    	assertTrue( conSet.isVisited( new Edge[]{ edge1, edge2 } ) == false );
    	assertTrue( conSet.isVisited( new Edge[]{ edge3, edge4 } ) == false );
    	assertTrue( conSet.isVisited( new Edge[]{ edge5, edge6 } ) == false );
    	assertTrue( conSet.isVisited( new Edge[]{ edge7, edge8 } ) == false );
    	assertTrue( conSet.isVisited( new Edge[]{ edge9, edge10 } ) == false );
    	conSet.setVisited( new Edge[]{ edge1, edge2 }, true );
    	conSet.setVisited( new Edge[]{ edge5, edge6 }, true );
    	conSet.setVisited( new Edge[]{ edge9, edge10 }, true );
    	assertTrue( conSet.isVisited( new Edge[]{ edge1, edge2 } ) == true );
    	assertTrue( conSet.isVisited( new Edge[]{ edge5, edge6 } ) == true );
    	assertTrue( conSet.isVisited( new Edge[]{ edge9, edge10 } ) == true );
    }
    
    public void testIsVisited2()
    {
    	ConsecutiveSet conSet = new ConsecutiveSet();
    	Edge edge1 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge2 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp2", "event2" ) );
    	conSet.add( new Edge[]{ edge1, edge2 } );
    	assertTrue( conSet.isVisited( 0 ) == false ); 	
    	conSet.setVisited( 0, true );
    	assertTrue( conSet.isVisited( 0 ) == true );
    }
    
    public void testSetVisited2()
    {
    	ConsecutiveSet conSet = new ConsecutiveSet();
    	Edge edge1 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge2 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp2", "event2" ) );
    	conSet.add( new Edge[]{ edge1, edge2 } );
    	assertTrue( conSet.setVisited( new Edge[]{ edge1, edge2 }, true ) );
    }
    
    public void testSetVisited1()
    {
    	ConsecutiveSet conSet = new ConsecutiveSet();
    	Edge edge1 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge2 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp2", "event2" ) );
    	conSet.add( new Edge[]{ edge1, edge2 } );
    	assertTrue( conSet.setVisited( 0, true ) );
    }
    
    public void testFindEdgeSetIndex1()
    {
    	ConsecutiveSet conSet = new ConsecutiveSet();
    	Edge edge1 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge2 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp2", "event2" ) );
    	Edge edge3 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge4 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp2", "event2" ) );
    	Edge edge5 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp4", "event4" ) );
    	Edge edge6 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp5", "event5" ) );
    	Edge edge7 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp6", "event6" ) );
    	Edge edge8 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp7", "event7" ) );
    	Edge edge9 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp8", "event8" ) );
    	Edge edge10 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp9", "event9" ) );
    	conSet.add( new Edge[]{ edge1, edge2 } );
    	conSet.add( new Edge[]{ edge3, edge4 } );
    	conSet.add( new Edge[]{ edge5, edge6 } );
    	conSet.add( new Edge[]{ edge7, edge8 } );
    	conSet.add( new Edge[]{ edge9, edge10 } );
    	assertEquals( 0, conSet.findEdgeSetIndex( new Edge[]{ edge1, edge2 } ) );
    	assertEquals( 0, conSet.findEdgeSetIndex( new Edge[]{ edge3, edge4 } ) );
    	assertEquals( 2, conSet.findEdgeSetIndex( new Edge[]{ edge5, edge6 } ) );
    	assertEquals( 3, conSet.findEdgeSetIndex( new Edge[]{ edge7, edge8 } ) );
    	assertEquals( 4, conSet.findEdgeSetIndex( new Edge[]{ edge9, edge10 } ) );
    }
    
    public void testFindEdgeSetIndex2()
    {
    	ConsecutiveSet conSet = new ConsecutiveSet();
    	Edge edge1 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge2 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge3 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge4 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge5 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge6 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge7 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge8 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge9 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	Edge edge10 = new Edge( new Vertex(null), new Vertex(null), createEventNode( "comp1", "event1" ) );
    	conSet.add( new Edge[]{ edge1, edge2 } );
    	conSet.add( new Edge[]{ edge3, edge4 } );
    	conSet.add( new Edge[]{ edge5, edge6 } );
    	conSet.add( new Edge[]{ edge7, edge8 } );
    	conSet.add( new Edge[]{ edge9, edge10 } );
    	assertTrue( conSet.findEdgeSetIndex( new Edge[]{ edge1, edge2 } ) != 5 );
    	assertTrue( conSet.findEdgeSetIndex( new Edge[]{ edge3, edge4 } ) != 4 );
    	assertTrue( conSet.findEdgeSetIndex( new Edge[]{ edge5, edge6 } ) != 3 );
    	assertTrue( conSet.findEdgeSetIndex( new Edge[]{ edge7, edge8 } ) != 2 );
    	assertTrue( conSet.findEdgeSetIndex( new Edge[]{ edge9, edge10 } ) != 1 );
    }
    
    private EventNode createEventNode( String compName, String eventName )
    {
    	IComponent iComp  = SwingComponent.createComponent( "", "", "", compName, "", 0, 0 );
    	IEvent     iEvent = SwingEvent.create( 0 , eventName );
    	return (EventNode)m_factory.createEventNode( iComp, iEvent );	
    }
}
