/**
 * 
 */
package gttlipse.testgen.algorithm;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;
import gttlipse.testgen.testsuite.VertexSequenceContainer;

import java.util.List;
import java.util.Stack;
import java.util.Vector;


public class CompleteStateCoverage implements ITestGenerationAlgorithm
{
	private  IGraph m_graph     = null;
	private  VertexSequenceContainer m_orderList = new VertexSequenceContainer(); 
	
	public CompleteStateCoverage( IGraph graph )
	{
		m_graph = graph;
	}
	
	public void startTrace()
	{
        // order�O�����@����쪺path��,vertex�Qtrace������
        // orderBack�O�������[�I��order������,�]�N�O�Ytrace�Qreturn��,path�nreturn������
        // nodeStack�O�����j�M�`�I���j�����|
		// returnStack�O����trace�쪺�`�I�O�_���l�`�I�i�����̫�@��,�@��orderBack�O�_����pop��flag
		List<Vertex>   order       = new Vector<Vertex>();
		Stack<Integer> orderBack   = new Stack<Integer>();
		Stack<Vertex>  nodeStack   = new Stack<Vertex>();	
		Stack<Integer> returnStack = new Stack<Integer>();
		Vertex         start       = m_graph.getStart();
		nodeStack.push( start );
		
		while( nodeStack.isEmpty() != true )
		{			
			Vertex     currentNode = nodeStack.pop();		
			List<Edge> outList     = currentNode.getOutEdgeList();
			int        returnFlag  = -1;
						
			if( returnStack.size() > 0 )
			{
				returnFlag = returnStack.pop();
                // ���X��l�`�I���̫�@��,�]��back���`�I�٭n�b��W�@�h,stack��back��Tpop	
				if( returnFlag == 0 /*&& orderBack.size() > 0*/ )
			    	orderBack.pop();
			}
			
			// �border�s�񪺶��Ǧ�C��,�d�ߥثe���X�쪺�`�I�O�_�w�g���X�L,�Y�S�����X�L�h��Jorder����C
			if( order.contains( currentNode ) == false )
				order.add( currentNode );
			 
            // �`�I�Y�O���X�L
			else
			{   
                // return back
				pathReturnBack( order, orderBack );
				continue;
			}	
			
			// �Y�ثe���X��`�I�֦��l�`�I,�h�N��l�`�I�̱����J���|����
			if( outList.isEmpty() == false )
			{			   				 
			    // �Noutput����Tpush��stack����
				List<Vertex> temp  = new Vector<Vertex>();
				int          count = 0;
				int          startIndex = outList.size() - 1;

				for( int i = startIndex; i >= 0; i-- )
				{			    
					Edge   e    = outList.get( i );
				    Vertex dest = e.destination();
				 	    
				    // �Y�O�l�`�I�M�ثetrace�쪺�`�I���ۦP��,�h����J���|
				    // �l�`�I���w�g��J�L���|��,�h����J�ĤG��
				    if( currentNode != dest && temp.contains( dest ) == false )
			        {
			        	nodeStack.push( dest );	
			        	temp.add( dest );
			        	
			        	if( count == 0 )
				        	returnStack.push( 0 );
			        	else
			        		returnStack.push( 1 );		        	
			        	count++;
			        }
			    }		    			
				
				if( count == 1 )
					returnStack.set( returnStack.size() - 1, 1 );
				
				// ����back������,��K�o��return��back����m
				if( count > 1 )
			        orderBack.push( order.size() );	
			}
			// �Y�ثe���X�쪺�`�I�S������l���I,��ܨ�����I,�h���@�ӵ��G
			else
			{   
                // printout and record
				m_orderList.addSequence( order );
				//printFind( order );
				
				// return back
				pathReturnBack( order, orderBack );
			}
		}
	}
		
	public TestSuite getTestSuite()
	{
        TestSuite  caseContainer = new TestSuite();
		List<Object>       eventSeq      = null;
		List<Integer[]>    groupList     = null;
		List<Vertex[]>     getList       = m_orderList.findUnionOrderList( m_graph.vertices().size() );
		
		if( m_orderList.size() == 1 && getList.size() == 0 )
			getList.add( m_orderList.getSequence( 0 ) );

		for( int i = 0; i < getList.size(); i++ )
		{
			Vertex vertexSeq[] = getList.get( i );
		    eventSeq  = new Vector<Object>();
			groupList = buildEdgeGroup( vertexSeq );
		
		    for( int j = 0; j < groupList.size(); j++ )
		    {
			    Vertex  v       = vertexSeq[j];
			    Integer group[] = groupList.get( j );
			   
		        int index = (int)( Math.random() * group.length );
		        Edge outEdge = v.getOutEdgeList().get( group[index] );
		        eventSeq.add( outEdge.getUserObject() );
		    }
			
		    caseContainer.addTestCase( eventSeq );
		}
		return caseContainer;
	}
	
	private List<Integer[]> buildEdgeGroup( Vertex order[] )
	{
		List<Integer[]> groupContainer = new Vector<Integer[]>();
		List<Integer>   edgeGroup      = null;
		
		for( int i = 0; i < order.length - 1; i++ )
		{
			edgeGroup = new Vector<Integer>();
			Vertex     source  = order[i];
			Vertex     dest    = order[ i + 1 ];
			List<Edge> outList = source.getOutEdgeList();
			
			for( int j = 0; j < outList.size(); j++ )
			{
				Edge out = outList.get( j );
				if( out.destination() == dest )
					edgeGroup.add( j );
			}
			Integer array[] = new Integer[edgeGroup.size()];	
			groupContainer.add( edgeGroup.toArray( array ) );
		}
		return groupContainer;
	}
			
	private void pathReturnBack( List<Vertex> order, List<Integer> orderBack ) 
	{
        // return���`�I,�ñNpath����Treturn��back���`�I
		if( orderBack.size() > 0 )
		{
		    int orderback = orderBack.get( orderBack.size() - 1 );		
		    while( order.size() > orderback )
			    order.remove( order.size() - 1 );	
		}
	}
		
	// debug
//	private void printFind( List<Vertex> order )
//	{
//		try 
//	    {
//			BufferedWriter outfile = new BufferedWriter( new FileWriter( "..\\output.txt", true ) );       
//			String outTest2 = "[Find]";
//			for( int i = 0; i < order.size(); i++ )
//			  	outTest2 = outTest2 + "->" + ( (GraphBase)order.get( i ).getUserObject() ).getName();
//			System.out.println( outTest2 );
//			outfile.write( outTest2 + "\r\n" );
//			outfile.write( "\r\n" );
//	        outfile.close();
//	    }
//	    catch( IOException e )    
//	    {    
//	    }
//	}
}
