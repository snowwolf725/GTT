/**
 *  wrote by David Wu 
 *  2007/01/20
 *  note:
 */
package gttlipse.testgen.graph;

import java.util.Iterator;
import java.util.List;

public interface IGraph {
	public Vertex addVertex(Object o);

	public Edge addEdge(Vertex v1, Vertex v2, Object obj);
	
	public void removeEdge(Edge edge);

	public List<Vertex> vertices();

	public List<Edge> edges();

	public Iterator<Vertex> inAdjacentVertices(Vertex v);

	public Iterator<Vertex> outAdjacentVertices(Vertex v);

	public Iterator<Edge> inIncidentEdges(Vertex v);

	public Iterator<Edge> outIncidentEdges(Vertex v);

	public Vertex getVertex(Object o);
	
	public void setStart( Vertex start );
	
	public Vertex getStart() ;
	
	public void setEnd( Vertex end );
	
	public Vertex getEnd() ;

}
