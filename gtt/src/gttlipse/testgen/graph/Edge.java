/**
 *  wrote by David Wu 
 *  2007/01/20
 *  note:
 */
package gttlipse.testgen.graph;

import gttlipse.vfsmEditor.model.ConnectionBase;

public class Edge {
	Object m_obj = null;

	Vertex m_origin = null;

	Vertex m_destination = null;
	
	ConnectionBase m_parent = null;

	public Edge(Vertex v1, Vertex v2, Object obj) {
		m_origin = v1;
		m_destination = v2;
		m_obj = obj;
		v1.addOutEdge(this);
		v2.addInEdge(this);
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

	public Vertex origin() {
		return m_origin;
	}

	public Vertex destination() {
		return m_destination;
	}
	
	public void setParentConnection(ConnectionBase parent) {
		m_parent = parent;
	}
	
	public ConnectionBase getParentConnection() {
		return m_parent;
	}
}
