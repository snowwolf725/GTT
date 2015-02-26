package gttlipse.vfsmCoverageAnalyser.suggest;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.analysis.EdgePairCoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.analysis.EventCoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.analysis.StateCoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.model.EventNodeComparator;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TransitionPair;
import gttlipse.vfsmEditor.model.State;

import java.util.LinkedList;
import java.util.List;


public class SuggestionEstimator {
	private FSMInformation _fsmInfo = null;

	public SuggestionEstimator(FSMInformation fsmInfo) {
		_fsmInfo = fsmInfo;
	}

	public void estimateEventCoverage(EventCoverageAnalyzedResult result,
			LinkedList<Object> events) {
		setUsedEvent(result, traceEdges(events));
		result.calculateCoverage();
	}

	public void estimateStateCoverage(StateCoverageAnalyzedResult result,
			LinkedList<Object> events) {
		setUsedState(result, traceStates(events));
		result.calculateCoverage();
	}

	public void estimateEdgePairCoverage(EdgePairCoverageAnalyzedResult result,
			LinkedList<Object> events) {
		setUsedEdgePair(result, traceTransitonPairs(events));
		result.calculateCoveredComponentCount();
	}

	private void setUsedEvent(EventCoverageAnalyzedResult result,
			LinkedList<Edge> events) {
		for (int i = 0; i < events.size(); i++) {
			result.setTracedComponent(events.get(i), true);
		}
	}

	private void setUsedState(StateCoverageAnalyzedResult result,
			LinkedList<State> states) {
		for (int i = 0; i < states.size(); i++) {
			result.setTracedComponent(states.get(i), true);
		}
	}

	private void setUsedEdgePair(EdgePairCoverageAnalyzedResult result,
			LinkedList<TransitionPair> transitionPairs) {
		for (int index = 0; index < transitionPairs.size(); index++) {
			result.setTracedComponent(transitionPairs.get(index), true);
		}
	}

	private LinkedList<State> traceStates(LinkedList<Object> list) {
		LinkedList<State> results = new LinkedList<State>();

		Vertex sourceVertex = _fsmInfo.getStartVertex();
		Vertex targetVertex = _fsmInfo.getStartVertex();

		results.add((State) sourceVertex.getUserObject());

		for (int i = 0; i < list.size(); i++) {
			boolean flag = false;
			List<Edge> outTransitions = sourceVertex.getOutEdgeList();

			for (int j = 0; j < outTransitions.size() && flag == false; j++) {
				Object obj = outTransitions.get(j).getUserObject();

				if (EventNodeComparator.compare(obj, list.get(i))) {
					targetVertex = outTransitions.get(j).destination();
					flag = true;
				}
			}

			if (flag == false) {
				System.out.println("traceStates error");
				return null;
			} 
			
			
			sourceVertex = targetVertex;
			results.add((State) sourceVertex.getUserObject());
		}

		return results;
	}

	private LinkedList<Edge> traceEdges(LinkedList<Object> eventList) {
		LinkedList<Edge> results = new LinkedList<Edge>();
		List<Edge> stateOutTransitionList;
		Vertex sourceVertex, targetVertex;

		sourceVertex = targetVertex = _fsmInfo.getStartVertex();

		for (int i = 0; i < eventList.size(); i++) {
			boolean flag = false;
			stateOutTransitionList = sourceVertex.getOutEdgeList();

			for (int j = 0; j < stateOutTransitionList.size() && flag == false; j++) {
				Edge edge = stateOutTransitionList.get(j);
				Object obj = edge.getUserObject();

				if (EventNodeComparator.compare(obj, eventList.get(i))) {
					targetVertex = stateOutTransitionList.get(j).destination();
					results.add(edge);
					flag = true;
				}
			}

			if (flag == false) {
				System.out.println("traceEdges error");
				return null;
			} else {
				sourceVertex = targetVertex;
			}
		}

		return results;
	}

	private LinkedList<TransitionPair> traceTransitonPairs(
			LinkedList<Object> eventList) {
		LinkedList<TransitionPair> results = new LinkedList<TransitionPair>();
		List<Edge> stateOutTransitionList;
		Edge firstTransition = null, secondTransition = null;

		Vertex sourceVertex = _fsmInfo.getStartVertex();
		Vertex targetVertex = _fsmInfo.getStartVertex();

		boolean traceableFlag = false;

		for (int i = 0; i < eventList.size(); i++) {
			stateOutTransitionList = sourceVertex.getOutEdgeList();

			for (int j = 0; j < stateOutTransitionList.size()
					&& traceableFlag == false; j++) {
				Edge edge = stateOutTransitionList.get(j);
				Object obj = edge.getUserObject();

				if (EventNodeComparator.compare(obj, eventList.get(i))) {
					traceableFlag = true;
					targetVertex = edge.destination();
					secondTransition = edge;
				}
			}

			if (traceableFlag == false) {
				System.out.println("traceTransitonPairs error");
				return null;
			} else {
				traceableFlag = false;
				sourceVertex = targetVertex;
				if (firstTransition != null) {
					TransitionPair transitionPair = new TransitionPair(
							firstTransition, secondTransition);
					if (!results.contains(transitionPair))
						results.push(transitionPair);
				}

				firstTransition = secondTransition;
			}
		}

		return results;
	}

}
