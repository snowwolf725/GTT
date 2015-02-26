package gttlipse.vfsmCoverageAnalyser.analysis;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.model.EventNodeComparator;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;

import java.util.LinkedList;
import java.util.List;


public class EventCoverageAnalyst extends CoverageAnalyst{
	private EventCoverageAnalyzedResult _eventCoverageReport;
	
	public EventCoverageAnalyst(TestCaseInformation testCaseInfo, FSMInformation fsmInfo) {
		LinkedList<Edge> edges = new LinkedList<Edge>();
		
		setTestCaseInformation(testCaseInfo);
		setFSMInformation(fsmInfo);
		
		List<Edge> tempEdges = _fsmInfo.getEdgesList();
		
		
		System.out.println("Temp Edge number:" + tempEdges.size());
		
		_eventCoverageReport =  new EventCoverageAnalyzedResult();
		
		for(int index = 0; index < tempEdges.size(); index++)
			edges.add(tempEdges.get(index));
		
		System.out.println("Edge number:" + edges.size());
		
		
		_eventCoverageReport.initialize(edges);
	}
	
	public CoverageAnalyzedResult getResult() {
		return _eventCoverageReport;
	}

	@Override
	public void analyze() {
		LinkedList<LinkedList<Object>> eventsList = _testCaseInfo.getEventsList();
		
		for(int index = 0; index < eventsList.size(); index++) {
			LinkedList<Object> eventList = eventsList.get(index);
			LinkedList<Edge> edges = trace(eventList);
			
			setUsedEdge(edges);
		}
		
		_eventCoverageReport.calculateCoveredComponentCount();
		_eventCoverageReport.calculateCoverage();
		
	}
	
	private LinkedList<Edge> trace(LinkedList<Object> eventList) {
		LinkedList<Edge> edges = new LinkedList<Edge>();
		List<Edge> stateOutTransitionList;
		
		Vertex sourceVertex, targetVertex;
		boolean traceableFlag = false;
		
		sourceVertex = targetVertex = _fsmInfo.getStartVertex();
		
		for(int i =0; i < eventList.size(); i++) {
			stateOutTransitionList = sourceVertex.getOutEdgeList();
			
			for(int j = 0; j < stateOutTransitionList.size() && traceableFlag == false; j++) {
				Edge edge = stateOutTransitionList.get(j);
				Object obj = edge.getUserObject();
				
				if(EventNodeComparator.compare(obj, eventList.get(i))) {
					traceableFlag = true;
					//targetState = _fsmInfo.findTargetState(eventList.get(i));
					edges.add(edge);
					targetVertex = edge.destination();
				}
			}
			
			if(traceableFlag == false) {
				System.out.println("Wrong");
				return null;
			}
			else {
				traceableFlag = false;
				sourceVertex = targetVertex;
			}
		}
	
		return edges;
	}
	
	private void setUsedEdge(LinkedList<Edge> events) {
		for(int i = 0; i < events.size(); i++) {
			_eventCoverageReport.setTracedComponent(events.get(i), true);
		}
	}
}
