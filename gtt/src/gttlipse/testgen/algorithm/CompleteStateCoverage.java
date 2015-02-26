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
        // order是紀錄一條找到的path中,vertex被trace的順序
        // orderBack是紀錄分歧點時order的長度,也就是若trace被return時,path要return的長度
        // nodeStack是紀錄搜尋節點遞迴的堆疊
		// returnStack是紀錄trace到的節點是否為子節點可走的最後一個,作為orderBack是否必須pop的flag
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
                // 拜訪到子節點的最後一個,因此back的節點還要在更上一層,stack的back資訊pop	
				if( returnFlag == 0 /*&& orderBack.size() > 0*/ )
			    	orderBack.pop();
			}
			
			// 在order存放的順序佇列裡,查詢目前拜訪到的節點是否已經拜訪過,若沒有拜訪過則放入order的佇列
			if( order.contains( currentNode ) == false )
				order.add( currentNode );
			 
            // 節點若是拜訪過
			else
			{   
                // return back
				pathReturnBack( order, orderBack );
				continue;
			}	
			
			// 若目前拜訪到節點擁有子節點,則將其子節點依條件放入堆疊之中
			if( outList.isEmpty() == false )
			{			   				 
			    // 將output的資訊push至stack之中
				List<Vertex> temp  = new Vector<Vertex>();
				int          count = 0;
				int          startIndex = outList.size() - 1;

				for( int i = startIndex; i >= 0; i-- )
				{			    
					Edge   e    = outList.get( i );
				    Vertex dest = e.destination();
				 	    
				    // 若是子節點和目前trace到的節點為相同的,則不放入堆疊
				    // 子節點中已經放入過堆疊的,則不放入第二次
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
				
				// 紀錄back的長度,方便得知return時back的位置
				if( count > 1 )
			        orderBack.push( order.size() );	
			}
			// 若目前拜訪到的節點沒有任何子結點,表示走到終點,則找到一個結果
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
        // return此節點,並將path的資訊return至back的節點
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
