package gttlipse.testgen.testsuite;

import gttlipse.testgen.graph.Vertex;

import java.util.List;
import java.util.Vector;


public class VertexSequenceContainer 
{
	private List<Vertex[]> m_Sequences = new Vector<Vertex[]>();
	
	public VertexSequenceContainer()
    {
    }
    
	public void addSequence( List<Vertex> seq )
	{
		Vertex array[] = new Vertex[seq.size()];	
		m_Sequences.add( seq.toArray( array ) );
	}
	
	public Vertex[] getSequence( int index )
	{
		if( index >= 0 && index < m_Sequences.size() )
			return m_Sequences.get( index );
		
		return null;
	}
	
	private int findMaxTestcase()
	{
		int max      = 0;
		int maxIndex = -1;
		
		for( int i = 0; i < m_Sequences.size(); i++ )
        {
        	Vertex v[] = m_Sequences.get( i ); 
			if( v.length > max )
			{
				max = v.length;
                maxIndex = i;
		  	}
        }
		return maxIndex;
	}
	
	public List<Vertex[]> findUnionOrderList( int vertexSize )
	{
		List<Vertex[]> findList   = new Vector<Vertex[]>();
		List<Vertex>   checkList  = new Vector<Vertex>();		
		Vertex order[] = m_Sequences.get( findMaxTestcase() );
		
		if( order.length == vertexSize )
		{
			findList.add( order );
			return findList;
		}
		
		for( int i = 0; i < order.length; i++ )
			checkList.add( order[i] );
		
		for( int i = 0; i < m_Sequences.size(); i++ )
        {
        	boolean hasNewVertex = false;
			Vertex  v[]          = m_Sequences.get( i ); 
        	for( int j = 0; j < v.length; j++ )
        	{
        	    if( checkList.contains( v[j] ) == false )
        	    {
        	    	checkList.add( v[j] );
        	    	hasNewVertex = true;
        	    }
        	}
        	
        	if( hasNewVertex == true )
        		findList.add( v );
        	
        	if( checkList.size() == vertexSize )
        		break;
        }
	    return findList;	
	}
	
	public int size()
	{
		return m_Sequences.size();
	}
}
