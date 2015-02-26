package gttlipse.vfsmCoverageAnalyser.model;

import gtt.testscript.EventNode;
import gttlipse.scriptEditor.testScript.TestMethodNode;
import gttlipse.testgen.graph.Vertex;

import java.util.LinkedList;


public class CoverageErrorReport {
	private LinkedList<CoverageErrorPairItem> _errorList = null;
	
	public CoverageErrorReport() {
		_errorList = new LinkedList<CoverageErrorPairItem>();
	}
	
	public int getErrorItemSize() {
		return _errorList.size();
	}
	
	public void addErrorPairItem(TestMethodNode methodNode, EventNode errorEventNode,int errorIndex, LinkedList<Vertex> vertices) {
		_errorList.add(new CoverageErrorPairItem(methodNode, errorEventNode, errorIndex, vertices));
	}
	
	public LinkedList<CoverageErrorPairItem> getErrorItems() {
		return _errorList;
	}
	
	public CoverageErrorPairItem getErrorItem(int index) {
		return _errorList.get(index);
	}
} 
