/**
 *  wrote by David Wu 
 *  2007/01/20
 *  note:
 */
package gttlipse.testgen.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AdjacencyListGraph implements IGraph {
	List<Vertex> m_Vertices = null;
	List<Edge> m_Edges = null;
	Vertex m_EntryVertex = null;
	Vertex m_ExitVertex = null;

	public AdjacencyListGraph() {
		m_Vertices = new LinkedList<Vertex>();
		m_Edges = new LinkedList<Edge>();
	}

	public void setStart(Vertex start) {
		m_EntryVertex = start;
	}

	public Vertex getStart() {
		return m_EntryVertex;
	}

	public Vertex addVertex(Object obj) {
		Vertex vertex = new Vertex(obj);
		m_Vertices.add(vertex);
		return vertex;
	}

	public Edge addEdge(Vertex v1, Vertex v2, Object obj) {
		Edge edge = new Edge(v1, v2, obj);
		m_Edges.add(edge);
		return edge;
	}

	public List<Vertex> vertices() {
		return m_Vertices;
	}

	public List<Edge> edges() {
		return m_Edges;
	}

	public Vertex getVertex(Object obj) {
		Iterator<Vertex> ite = m_Vertices.iterator();
		while (ite.hasNext()) {
			Vertex vertex = ite.next();
			if (obj == vertex.getUserObject())
				return vertex;
		}

		// non-exists
		return null;
	}

	public Iterator<Vertex> inAdjacentVertices(Vertex v) {
		List<Vertex> vertices = new LinkedList<Vertex>();
		List<Edge> edgeList = v.getInEdgeList();

		for (int i = 0; i < edgeList.size(); i++) {
			Edge edge = edgeList.get(i);
			vertices.add(edge.origin());
		}

		return vertices.iterator();
	}

	public Iterator<Vertex> outAdjacentVertices(Vertex v) {
		List<Vertex> vertices = new LinkedList<Vertex>();
		List<Edge> edgeList = v.getOutEdgeList();

		for (int i = 0; i < edgeList.size(); i++) {
			Edge edge = edgeList.get(i);
			vertices.add(edge.destination());
		}

		return vertices.iterator();
	}

	public Iterator<Edge> inIncidentEdges(Vertex v) {
		return v.getInEdgeList().iterator();
	}

	public Iterator<Edge> outIncidentEdges(Vertex v) {
		return v.getOutEdgeList().iterator();
	}

	public Vertex getEnd() {
		return m_ExitVertex;
	}

	public void setEnd(Vertex end) {
		m_ExitVertex = end;
		
	}

	@Override
	public void removeEdge(Edge edge) {
		m_Edges.remove(edge);
	}
}
