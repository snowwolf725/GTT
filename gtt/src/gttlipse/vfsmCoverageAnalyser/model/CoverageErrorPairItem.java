package gttlipse.vfsmCoverageAnalyser.model;

import gtt.testscript.EventNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.testgen.graph.Vertex;

import java.util.LinkedList;


public class CoverageErrorPairItem {
	private TestMethodNode _methodNodes = null;
	
	private EventNode _errorEventNode = null;
	
	private Vertex _endVertex = null;
	
	private int _errorIndex = -1;
	
	private LinkedList<Vertex> _vertices = null;
	
	public CoverageErrorPairItem(TestMethodNode methodNode, EventNode errorEventNode,int errorIndex, LinkedList<Vertex> vertices) {
		_methodNodes = methodNode;
		_errorEventNode = errorEventNode;
		_errorIndex = errorIndex;
		_vertices = vertices;
		_endVertex = _vertices.getLast();
	}
	
	public TestMethodNode getMethodNode() {
		return _methodNodes;
	}
	
	public EventNode getEndEventNode() {
		return _errorEventNode;
	}
	
	public int getErrorIndex() {
		return _errorIndex;
	}
	
	public Vertex getEndVertex() {
		return _endVertex;
	}
	
	public LinkedList<Vertex> getPath() {
		return _vertices;
	}
}
