/**
 *  
 *  note: graph convert - this class implement a converter that convert the Visual Finite State Machine figure
 *        to the actual graph (IGarph). 
 */
package gttlipse.vfsmEditor.builder;

import gttlipse.testgen.graph.AdjacencyListGraph;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmEditor.VFSMDef;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.AndSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.Final;
import gttlipse.vfsmEditor.model.Initial;
import gttlipse.vfsmEditor.model.Node;
import gttlipse.vfsmEditor.model.State;
import gttlipse.vfsmEditor.model.SuperState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;


public class GraphBuilder {

	private IGraph m_graph;

	// Constructor
	public GraphBuilder() {
		m_graph = new AdjacencyListGraph();
	}

	/**
	 * build IGraph model from VFSM model
	 */
	public IGraph build(AbstractSuperState root) {

		// step 1
		traceVertices(root);
		// step 2
		traceEdges(root);
		// step 3
		findEntry(root);
		// step 4
		printresult();

		return this.getM_graph();
	}

	/**
	 * Step 1 of building IGaph search every element of VFSM
	 */
	public void traceVertices(AbstractSuperState sstate) {
		List<Node> queue = new ArrayList<Node>();
		for (int i = 0; i < sstate.getAll().size(); i++)
			queue.add(sstate.getAll().get(i));

		while (queue.size() != 0) {
			Node ele = queue.get(0);
			queue.remove(0);

			if (ele instanceof Initial) {
				continue;
			}

			if (ele instanceof Final) {
				Final f = (Final) ele;
				m_graph.setEnd(m_graph.addVertex(f));
				continue;
			}

			if (ele instanceof AbstractSuperState) {
				AbstractSuperState ss = (AbstractSuperState) ele;
				if (ss.getAll().get(0) instanceof AndSuperState) {
					List<State> nodes = processAND(ss);
					for (int i = 0; i < nodes.size(); i++)
						nodes.get(i).setParent(ss);
					for (int i = 0; i < nodes.size(); i++)
						queue.add(nodes.get(i));
				} else {
					for (int i = 0; i < ss.size(); i++) {
						queue.add(ss.getAll().get(i));
					}
				}
				continue;
			}

			if (ele instanceof State) {
				State state = (State) ele;
				m_graph.addVertex(state);
				continue;
			}

		} /* end while */
	}

	/**
	 * Step 2 of building IGaph
	 */
	public void traceEdges(AbstractSuperState sstate) {
		List<Vertex> vertexList = m_graph.vertices();
		for (int i = 0; i < vertexList.size(); i++) {
			Vertex v1 = vertexList.get(i);
			State node1 = (State) v1.getUserObject();
			List<ConnectionBase> node1_out = node1.getOutgoingConnections();
			for (int j = 0; j < node1_out.size(); j++) {
				State target = node1_out.get(j).getTarget();
				if (target instanceof AbstractSuperState) {
					AbstractSuperState targetAss = (AbstractSuperState) target;
					Iterator<ConnectionBase> subIter;
					State subInital;
					/* if subnode has initial node */
					if (targetAss.getChildrenByType(VFSMDef.TYPE_INITIAL) != null) {
						subInital = targetAss.getChildrenByType(VFSMDef.TYPE_INITIAL);
						subIter = subInital.getOutgoingConnections().iterator();
					} /* else assign first node */
					else {
						subIter = targetAss.getAll().get(0).getOutgoingConnections().iterator();
					}
					State subNode;
					if (subIter.hasNext()) {
						subNode = subIter.next().getTarget();
						Vertex v2 = getVertex(vertexList, subNode);
						if (v2 != null) {
							for (int k = 0; k < node1_out.get(j).getEventList()
									.size(); k++) {
								m_graph.addEdge(v1, v2, node1_out.get(j)
										.getEventList().get(k)).setParentConnection(node1_out.get(j));
							}
						}
					}
				} /* if target is a usual node */
				if (target instanceof State || target instanceof Final) {
					Vertex v2 = getVertex(vertexList, target);
					if (v2 != null) {
						for (int k = 0; k < node1_out.get(j).getEventList()
								.size(); k++) {
							m_graph.addEdge(v1, v2, node1_out.get(j)
									.getEventList().get(k)).setParentConnection(node1_out.get(j));
						}
					}
				}
			}
		}
	}

	/**
	 * Step 3
	 */
	private void findEntry(AbstractSuperState sstate) {
		List<State> queue = new ArrayList<State>();
		for (int i = 0; i < sstate.getAll().size(); i++)
			queue.add(sstate.getAll().get(i));
		while (queue.size() != 0) {
			State ele = queue.get(0);
			queue.remove(0);
			if ( ele instanceof Initial ) {
				if (ele.getOutgoingConnections().get(0) != null)
					m_graph.setStart(m_graph.getVertex(ele
							.getOutgoingConnections().get(0).getTarget()));
				break;
			}
			else if (ele instanceof AbstractSuperState) {
				AbstractSuperState ss = (AbstractSuperState) ele;
				for (int i = 0; i < ss.size(); i++)
					queue.add(ss.getAll().get(i));
				continue;
			}
		}
		if ( m_graph.getStart() == null ) {
			System.out.println(" IGraph getStart null exception ");
			m_graph.setStart( m_graph.vertices().get(0) );
		}
	}

	/**
	 * Step 4 is print out the IGraph building result
	 */
	public void printresult() {
		System.out.println("total vertex number is "
				+ m_graph.vertices().size());
		System.out.println("total edge number is " + m_graph.edges().size());
	}

	private Vertex getVertex(List<Vertex> vertexs, State node) {
		String name = node.getName();
		String parentName = node.getParent().getName();
		for (int i = 0; i < vertexs.size(); i++) {
			State node2 = (State) vertexs.get(i).getUserObject();
			if (name.equals(node2.getName())
					&& parentName.equals(node2.getParent().getName()))
				return vertexs.get(i);
		}
		return null;
	}

	/* process AND SuperState */
	private List<State> processAND(AbstractSuperState ss) {
		RecursiveCopySuperState copy = new RecursiveCopySuperState();
		int andSize = ss.size();
		SuperState temp = new SuperState();
		SuperState base = (SuperState) copy.parse(ss.getAll().get(0));
		if (base.getChildrenByType(VFSMDef.TYPE_INITIAL) != null) {
			base.removeState(base.getChildrenByType(VFSMDef.TYPE_INITIAL));
		}
		for (int i = 1; i < andSize; i++) {
			List<ConnectionBase> conns = new ArrayList<ConnectionBase>();
			for ( int baseSize = 0; baseSize < base.size(); baseSize++ ) {
				for ( int c = 0; c < base.getAll().get(baseSize).getOutgoingConnections().size(); c++ ) {
					conns.add(base.getAll().get(baseSize).getOutgoingConnections().get(c));
				}
			}
			
			SuperState other = (SuperState) copy.parse(ss.getAll().get(i));
			if (other.getChildrenByType(VFSMDef.TYPE_INITIAL) != null) {
				other.removeState(other.getChildrenByType(VFSMDef.TYPE_INITIAL));
			}
			temp.clear();
			for (int j = 0; j < base.size(); j++) {
				for (int k = 0; k < other.size(); k++) {
					State node;
					if (other.getAll().get(k) instanceof AbstractSuperState ||
							other.getAll().get(k) instanceof Initial ||
							other.getAll().get(k) instanceof Final ) {
						node = copy.parse(other.getAll().get(k));
					}
					else {
						node = new State();
						node.setName( new String( other.getAll().get(k).getName() ) );
						node.setLocation( new Point( other.getAll().get(k).getLocation().x, other.getAll().get(k).getLocation().y ) );
						node.setDimension( new Dimension( other.getAll().get(k).getDimension().width, other.getAll().get(k).getDimension().height ) );
						for ( int c = 0; c < other.getAll().get(k).getOutgoingConnections().size(); c++ ) {
							if( !conns.contains( other.getAll().get(k).getOutgoingConnections().get(c) ) )
								conns.add( other.getAll().get(k).getOutgoingConnections().get(c) );
						}
					}
					String name = base.getAll().get(j).getName() + ","
							+ other.getAll().get(k).getName();
					node.setName(name);
					temp.addState(node);
				}
			}
			for ( int connSize = 0; connSize < conns.size(); connSize++ ) {
				ConnectionBase cb = conns.get(connSize);
				final int TWO = 2;
				for ( int time = 0; time < TWO; time++ ) {
					String stname;
					if (time == 0)
						stname = cb.getSource().getName();
					else 
						stname = cb.getTarget().getName();
					List<State> statelist = new ArrayList<State>();
					for ( int tSize = 0; tSize < temp.size(); tSize++ ) {
						if ( temp.getAll().get(tSize).getName().contains(stname) ) {
							statelist.add( temp.getAll().get(tSize) );
						}
					}
					for ( int connSize2 = 0; connSize2 < conns.size(); connSize2++ ) {
						if ( !conns.get(connSize2).getSource().getName().contains(stname) &&
								!conns.get(connSize2).getTarget().getName().contains(stname) ) {
							State source = null;
							State target = null;
							for ( int sls =0; sls < statelist.size(); sls++ ) {
								if ( statelist.get(sls).getName().contains( conns.get(connSize2).getSource().getName() ) ) {
									source = statelist.get(sls);
								}
							}
							for ( int tls = 0; tls < statelist.size(); tls++ ) {
								if ( statelist.get(tls).getName().contains( conns.get(connSize2).getTarget().getName() ) ) {
									target = statelist.get(tls);
								}
							}
							if ( source != null && target != null ) {
								createConnection(temp.getChildrenByName(source.getName()), 
										temp.getChildrenByName(target.getName()), conns.get(connSize2));
							}
						}
					}
				} /* end of two times for first is source, second is target */
			}
			base.setAllState(((SuperState) copy.parse(temp)).getAll());
		}
		return base.getAll();
	}

	private static void createConnection(State src, State tar,
			ConnectionBase cur_connection) {
		Connection conn = Connection.create(src, tar);
		conn.setEventList(cur_connection.getEventList());
		conn.setBendpoints(cur_connection.getBendpoints());
		conn.setCondition(cur_connection.getCondition());
		conn.setAction(cur_connection.getAction());
	}
	
	public IGraph getM_graph() {
		return m_graph;
	}
}