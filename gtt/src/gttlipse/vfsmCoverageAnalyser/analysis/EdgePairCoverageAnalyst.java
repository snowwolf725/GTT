package gttlipse.vfsmCoverageAnalyser.analysis;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.model.EventNodeComparator;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;
import gttlipse.vfsmCoverageAnalyser.model.TransitionPair;

import java.util.LinkedList;
import java.util.List;


public class EdgePairCoverageAnalyst extends CoverageAnalyst {
	private EdgePairCoverageAnalyzedResult _edgePairCoverageReport;
	
	public EdgePairCoverageAnalyst(TestCaseInformation testCaseInfo, FSMInformation fsmInfo) {
		LinkedList<TransitionPair> edgePairs;
		
		setTestCaseInformation(testCaseInfo);
		setFSMInformation(fsmInfo);
		
		_edgePairCoverageReport = new EdgePairCoverageAnalyzedResult();
	
		edgePairs = _fsmInfo.getEdgePairList();
		_edgePairCoverageReport.initialize(edgePairs);
	}
	
	public CoverageAnalyzedResult getResult() {
		return _edgePairCoverageReport;
	}
	
	@Override
	public void analyze() {
		// TODO Auto-generated method stub
		LinkedList<LinkedList<Object>> eventsList = _testCaseInfo.getEventsList();
		
		for(int index = 0; index < eventsList.size(); index++) {
			LinkedList<Object> eventList = eventsList.get(index);
			LinkedList<TransitionPair> transitionPairs = trace(eventList);
			
			setUsedTransitionPairs(transitionPairs);
		}
		
		_edgePairCoverageReport.calculateCoveredComponentCount();
		_edgePairCoverageReport.calculateCoverage();
	}
	
	private LinkedList<TransitionPair> trace(LinkedList<Object> eventList) {
		LinkedList<TransitionPair> transitionPairs = new LinkedList<TransitionPair>();
		List<Edge> stateOutTransitionList;
		Edge firstTransition = null, secondTransition = null;
		
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
					targetVertex = edge.destination();
					secondTransition = edge;
				}
			}
			
			if(traceableFlag == false) {
				System.out.println("Wrong");
				return null;
			}
			else {
				traceableFlag = false;
				sourceVertex = targetVertex;
				if(firstTransition != null) {
					TransitionPair transitionPair = new TransitionPair(firstTransition, secondTransition);
					if(!transitionPairs.contains(transitionPair))
						transitionPairs.push(transitionPair);
				}
				
				firstTransition = secondTransition;
			}
		}
	
		return transitionPairs;
	}

	private void setUsedTransitionPairs(LinkedList<TransitionPair> transitionPairs) {
		for(int index = 0; index < transitionPairs.size(); index++) {
			_edgePairCoverageReport.setTracedComponent(transitionPairs.get(index), true);
		}
	}
	
}
