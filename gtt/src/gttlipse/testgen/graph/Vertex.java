/**
 *  wrote by David Wu 
 *  2007/01/20
 *  note:
 */
package gttlipse.testgen.graph;

import java.util.LinkedList;
import java.util.List;

public class Vertex {
	private Object m_obj = null;

	private List<Edge> m_inEdge = new LinkedList<Edge>();

	private List<Edge> m_outEdge = new LinkedList<Edge>();

	public Vertex(Object obj) {
		m_obj = obj;
	}

	public void addInEdge(Edge edge) {
		if (m_inEdge.contains(edge))
			return;

		m_inEdge.add(edge);
	}

	public void addOutEdge(Edge edge) {
		if (m_outEdge.contains(edge))
			return;

		m_outEdge.add(edge);
	}

	public List<Edge> getInEdgeList() {
		return m_inEdge;
	}

	public List<Edge> getOutEdgeList() {
		return m_outEdge;
	}

	public Object getUserObject() {
		return m_obj;
	}

	public void setUserObject(Object obj) {
		m_obj = obj;
	}
	
	public String toString() {
		return m_obj.toString();
	}
}
