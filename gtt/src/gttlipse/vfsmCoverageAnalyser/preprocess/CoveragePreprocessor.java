package gttlipse.vfsmCoverageAnalyser.preprocess;

import gtt.testscript.EventNode;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.model.CoverageErrorReport;
import gttlipse.vfsmCoverageAnalyser.model.EventNodeComparator;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;

import java.util.LinkedList;
import java.util.List;


public class CoveragePreprocessor {
	private TestCaseInformation _testCaseInfo;
	
	private FSMInformation _fsmInfo;
	
	private CoverageErrorReport _errorReport;
	
	public CoveragePreprocessor(TestCaseInformation testCaseInfo, FSMInformation fsmInfo) {
		_testCaseInfo = testCaseInfo;
		_fsmInfo = fsmInfo;
		_errorReport = new CoverageErrorReport();
	}
	
	public CoverageErrorReport getErrorReport() {
		return _errorReport;
	}
	
	public void preprocess() {
		LinkedList<LinkedList<Object>> eventsList = _testCaseInfo.getEventsList();
		
		for(int index = 0; index < eventsList.size(); index ++) {
			LinkedList<Vertex> paths = new LinkedList<Vertex>();
			if(!trace(eventsList.get(index), paths))
				index--;
		}
		
		System.out.println("Useful EventList Count:" + _testCaseInfo.getEventsListSize());
		_testCaseInfo.printData();
	}
	
	private void markInvalidPart(LinkedList<Object> events, EventNode errorEventNode,int errorIndex, LinkedList<Vertex> paths) {
		_testCaseInfo.setEventsUsefulSituation(events, false);
		_errorReport.addErrorPairItem(_testCaseInfo.getParentMethodNode(events), errorEventNode, errorIndex, paths);
	}
	
	public boolean trace(LinkedList<Object> eventList, LinkedList<Vertex> paths) {
		Vertex startVertex = _fsmInfo.getStartVertex();
		Vertex sourceVertex = null, targetVertex = null;
		boolean traceableFlag = false;
		EventNode errorEventNode;
		
		sourceVertex = targetVertex = startVertex;
		
		for(int i = 0; i < eventList.size(); i++) {
			Object testCaseObj = eventList.get(i);
			
			paths.add(sourceVertex);
			
			List<Edge> edges = sourceVertex.getOutEdgeList();
			for(int j = 0; j < edges.size() && traceableFlag == false; j++) {
				Object fsmObj = edges.get(j).getUserObject();
				if(EventNodeComparator.compare(testCaseObj, fsmObj)) {
					targetVertex = edges.get(j).destination();
					traceableFlag = true;
				}
			}
			
			if(traceableFlag == false) {
				errorEventNode = (EventNode)testCaseObj;
				if(errorEventNode == null)
					System.out.println("@Error@");
				markInvalidPart(eventList, errorEventNode, i, paths);
				return false;
			}
			else {
				traceableFlag = false;
				sourceVertex = targetVertex;
				
			}
		}
		paths.add(sourceVertex);
		
		return true;
	}
	
}
