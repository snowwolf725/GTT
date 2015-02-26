package gttlipse.vfsmCoverageAnalyser.model;

import gtt.macro.MacroDocument;
import gtt.macro.macroStructure.AbstractMacroNode;
import gtt.macro.macroStructure.ComponentEventNode;
import gtt.macro.macroStructure.MacroEventCallerNode;
import gtt.macro.macroStructure.MacroEventNode;
import gtt.testscript.EventNode;
import gtt.testscript.ReferenceMacroEventNode;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmEditor.builder.GraphBuilder;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.State;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



public class FSMInformation {
	private AbstractSuperState _source;
	
	private GraphBuilder _graphBuilder;
	
	private IGraph _graph;
	
	private MacroDocument _macroDoc;
	
	private HashMap<Edge, Connection> _edgeMap;
	
	public FSMInformation(AbstractSuperState source, MacroDocument macroDoc) {
		_graphBuilder = new GraphBuilder();
		_edgeMap = new HashMap<Edge, Connection>();
		setSuperState(source);
		setMacroDocument(macroDoc);
		parseSource();
	}
	
	//測試用的Constructor，直接設定Graph。
	public FSMInformation(IGraph graph) {
		_graph = graph;
	}
	
	public void setSuperState(AbstractSuperState source) {
		_source = source;
	}
	
	public void setMacroDocument(MacroDocument macroDoc) {
		_macroDoc = macroDoc;
	}
	
	public void parseSource() {
		_graph = _graphBuilder.build(_source);
		
		List<Edge> edges = _graph.edges();
		
		for(int index = 0; index < edges.size(); index++) {
			_edgeMap.put(edges.get(index), null);
			
			if(edges.get(index).getUserObject() instanceof ReferenceMacroEventNode) {
				AbstractMacroNode abstractMacroNode = _macroDoc.findByPath(((ReferenceMacroEventNode)edges.get(index).getUserObject()).getRefPath());
				if(abstractMacroNode != null)
					handleMacroEventNode((MacroEventNode)abstractMacroNode, edges.get(index));
			}
		}
		
		for(int index = 0; index < edges.size(); index++) {
			System.out.println(edges.get(index).getUserObject().getClass());
			if(((Connection)edges.get(index).getParentConnection()) != null)
				((Connection)edges.get(index).getParentConnection()).resetCoveredSituation();
		}
		
		for(int index = 0; index < edges.size(); index++) {
			System.out.println(edges.get(index).getUserObject().getClass());
			((Connection)edges.get(index).getParentConnection()).setUpCoveredItem((AbstractMacroNode)(edges.get(index).getUserObject()));
		}
		
	}
	
	private void handleMacroEventNode(AbstractMacroNode macroEventNode, Edge edge) {
		Vertex origin = edge.origin();
		Vertex destination = edge.destination();
		Vertex tempVertex1, tempVertex2;
		LinkedList<Object> events = new LinkedList<Object>();
		String name;
		
		
		
		parseMacroEventNode(macroEventNode, events);
		
		if(events.size() == 1) {
			edge.setUserObject(events.get(0));
		}
		else if(events.size() > 1) {
			int subStateNum = 1;
			
			origin.getOutEdgeList().remove(edge);
			destination.getInEdgeList().remove(edge);
			name = ((State)origin.getUserObject()).getName() + "-" + ((State)destination.getUserObject()).getName() + "_";
			
			
			
			tempVertex1 = origin;
			
			if(events.size() == 1) {
			
			}
			for(int index = 0; index < events.size(); index++) {
				if(index == events.size() - 1 ) {
					_graph.addEdge(tempVertex1, destination, events.get(index)).setParentConnection(edge.getParentConnection());
				}
				else {
					StringBuilder sb = new StringBuilder();
					sb.append(name);
					sb.append(subStateNum);
					State state = new State(sb.toString());
					tempVertex2 = _graph.addVertex(state);
					_graph.addEdge(tempVertex1, tempVertex2, events.get(index));
					tempVertex1 = tempVertex2;
					subStateNum++;
				}
			}
		}
		
	}
	
	private void parseMacroEventNode(AbstractMacroNode macroEventNode, LinkedList<Object> eventNodes) {
		
		Iterator<AbstractMacroNode> childrenIter;
		
		childrenIter = macroEventNode.iterator();
		
		while(childrenIter.hasNext()) {
			AbstractMacroNode child = childrenIter.next();
			
			if(child instanceof ComponentEventNode) {
				eventNodes.add((ComponentEventNode)child);
			}
			else if(child instanceof MacroEventCallerNode) {
				parseMacroEventNode(((MacroEventCallerNode)child).getReference(), eventNodes);
			}
		}
	}
	
	public List<Vertex> getVerticeList() {
		return _graph.vertices();
	}
	
	public List<Edge> getEdgesList() {
		return _graph.edges();
	}
	
	public Vertex getStartVertex() {
		return _graph.getStart();
	}
	
	public Vertex getEndVertex() {
		return _graph.getEnd();
	}
	
	public LinkedList<State> getStateList() {
		LinkedList<State> states = new LinkedList<State>();
		List<Vertex> vertices = _graph.vertices();
		
		for(int index = 0; index < vertices.size(); index++) {
			Object obj = vertices.get(index).getUserObject();
			if(obj instanceof State) {
				states.add((State)obj);
			}
			else {
				System.out.println("FSMInformation.getStateObjectList() Wrong.");
				return null;
			}
		}
		
		return states;
	}

	public LinkedList<Object> getEventList() {
		LinkedList<Object> eventNodes = new LinkedList<Object>();
		List<Edge> edges = _graph.edges();
		
		for(int index = 0; index < edges.size(); index++) {
			Object obj = edges.get(index).getUserObject();
			if(obj instanceof EventNode) {
				eventNodes.add((EventNode)obj);
			}
			else if(obj instanceof ComponentEventNode) {
				eventNodes.add((ComponentEventNode)obj);
			}
			else if(obj instanceof ReferenceMacroEventNode) {
				//return null;
			}
			else {
				System.out.println("FSMInformation.getEventList() Wrong.");
				return null;
			}
		}

		return eventNodes; 
	}
	
	public State getStartState() {
		Object obj;
		
		obj = _graph.getStart().getUserObject();
		
		if(obj instanceof State)
			return (State)obj;
		else
			return null;
	}
	
	public State getEndState() {
		Object obj;
		
		obj = _graph.getEnd().getUserObject();
		
		if(obj instanceof State)
			return (State)obj;
		else
			return null;
	}
	
	public LinkedList<TransitionPair> getEdgePairList() {
		List<Vertex> vertices = _graph.vertices();
		LinkedList<Vertex> usefulVertices = new LinkedList<Vertex>();		
		LinkedList<TransitionPair> edgePairs = null; 
		
		for(int index = 0; index < vertices.size(); index++) {
			if(vertices.get(index).getInEdgeList() != null &&
			   vertices.get(index).getOutEdgeList() != null)
				if(vertices.get(index).getInEdgeList().size() != 0 &&
				   vertices.get(index).getOutEdgeList().size() != 0)
					usefulVertices.add(vertices.get(index));
		}
		
		System.out.println("EdgePair userful vertex size:" + usefulVertices.size());
		
		if(usefulVertices.size() > 0) {
			edgePairs = new LinkedList<TransitionPair>();
		
			for(int i = 0; i < usefulVertices.size(); i++) {
				List<Edge> inEdges = usefulVertices.get(i).getInEdgeList();
				List<Edge> outEdges = usefulVertices.get(i).getOutEdgeList();
				
				System.out.println(((State)usefulVertices.get(i).getUserObject()).getName() + " in:" + inEdges.size() + "out:" + outEdges.size());
				for(int j = 0; j < inEdges.size(); j++) {
					for(int k = 0; k < outEdges.size(); k++)
						edgePairs.add(new TransitionPair(inEdges.get(j), outEdges.get(k)));
				}
			}
		}
		
		
		
		return edgePairs;
	}
	
	

	public Vertex findOutState(State stateName) {
		List<Vertex> vertices;
		Object obj;
		State state;
		
		vertices = _graph.vertices();
		
		for(int i = 0; i < vertices.size(); i++) {
			obj = vertices.get(i).getUserObject();
			if(obj instanceof State) {
				state = (State)obj;
				if(state.equals(stateName))
					return vertices.get(i);
			}
		}
		
		return null;
	}
	
	public Edge findOutTransition(Object event) {
		List<Edge> edges;
		Object obj;
//		EventNode eventNode;
//		ComponentEventNode componentEventNode;
		
		edges = _graph.edges();
		
		for(int i = 0; i< edges.size(); i++) {
			obj = edges.get(i).getUserObject();
			
			if(EventNodeComparator.compare(event, obj))
				return edges.get(i);
			
//			if(event instanceof EventNode) {
//				if(obj instanceof EventNode) {
//					eventNode = (EventNode)obj;
//					if(((EventNode)event).compare(eventNode))
//						return edges.get(i);
//				}
//				else if(obj instanceof ComponentEventNode) {
//					componentEventNode = (ComponentEventNode)obj;
//					if(((EventNode)event).compare(componentEventNode))
//						return edges.get(i);
//				}
//				else
//					return null;
//			}
//			else if(event instanceof ComponentEventNode) {
//				if(obj instanceof EventNode) {
//					eventNode = (EventNode)obj;
//					if(((ComponentEventNode)event).compare(eventNode))
//						return edges.get(i);
//				}
//				else if(obj instanceof ComponentEventNode) {
//					componentEventNode = (ComponentEventNode)obj;
//					if(((ComponentEventNode)event).compare(componentEventNode))
//						return edges.get(i);
//				}
//				else
//					return null;
//			}
//			else
//				return null;
		}
		
		return null;
	}
	
	public State findTargetState(Object event){
		Edge edge;
		State targetState = null;
		Object obj;
		
		edge = findOutTransition(event);
		if(edge != null) {
			obj = edge.destination();
			if(obj instanceof Vertex)
				targetState = (State)edge.destination().getUserObject();
			else
				System.out.println("FSMInformation.findTargetState(): wrong");
		}
		
		if(targetState != null)
			return targetState;
		else
			return null;
	}
	
	public List<Edge> getStateInEdgeList(State stateName) {
		List<Edge> vertexInEdgeList;
		Vertex vertex;
		
		vertex = findOutState(stateName);
		vertexInEdgeList = vertex.getInEdgeList();
			
		return vertexInEdgeList;
	}
	
	public List<Edge> getStateOutEdgeList(State stateName) {
		List<Edge> vertexOutEdgeList;
		Vertex vertex;
	
		vertex = findOutState(stateName);
		vertexOutEdgeList = vertex.getOutEdgeList();
			
		return vertexOutEdgeList;
	}
	
	public void printData() {
		List<Vertex> vertices = _graph.vertices();
		List<Edge> edges = _graph.edges();
		
		System.out.println("Vertex Part:");
		for(Vertex vertex: vertices) {
			System.out.println("S[" + ((State)vertex.getUserObject()).fullName() + "]");
		}
		
		System.out.println("Edge Part:");
		int index = 1;
		for(Edge edge: edges) {
			if(edge.getUserObject() instanceof EventNode)
				System.out.println(index + " E[ " + ((State)edge.origin().getUserObject()).fullName()+ ", " + ((EventNode)edge.getUserObject()).toDetailString() +
						           ", " + ((State)edge.destination().getUserObject()).fullName() + "]");
			else if(edge.getUserObject() instanceof ComponentEventNode)
				System.out.println(index + " E[ " + ((State)edge.origin().getUserObject()).fullName()+ ", " + ((ComponentEventNode)edge.getUserObject()).getName() +
						           ", " + ((State)edge.destination().getUserObject()).fullName() + "]");
			else if(edge.getUserObject() == null)
				System.out.println(index + " E[ " + ((State)edge.origin().getUserObject()).fullName()+ ", " + ((State)edge.destination().getUserObject()).fullName() + "]");
			index++;
		}
	}
}
