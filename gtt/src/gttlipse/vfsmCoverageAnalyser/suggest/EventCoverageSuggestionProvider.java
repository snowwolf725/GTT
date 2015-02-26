package gttlipse.vfsmCoverageAnalyser.suggest;

import gtt.macro.macroStructure.ComponentEventNode;
import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.analysis.CoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.analysis.EventCoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;
import gttlipse.vfsmEditor.model.State;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class EventCoverageSuggestionProvider extends AbstractSuggestionProvider {
	private HashMap<Vertex, LinkedList<Vertex>> _inShortestPath;

	private HashMap<Vertex, LinkedList<Vertex>> _outShortestPath;

	public EventCoverageSuggestionProvider(TestCaseInformation testcase,
			FSMInformation fsm) {
		setData(testcase, fsm);
	}

	@Override
	public LinkedList<LinkedList<Object>> provideSuggestion(
			CoverageAnalyzedResult report) {
		if (!(report instanceof EventCoverageAnalyzedResult))
			return null;

		copyCoveredSituation(report);

		EventCoverageAnalyzedResult privateReport;

		if (_estimationReport instanceof EventCoverageAnalyzedResult)
			privateReport = (EventCoverageAnalyzedResult) _estimationReport;
		else
			return null;

		SuggestionEstimator estimator = new SuggestionEstimator(_fsmInfo);

		LinkedList<Object> uncoveredList = privateReport.getUncoveredPart();

		LinkedList<Object> singleSuggestion;
		LinkedList<LinkedList<Object>> allSuggestion = new LinkedList<LinkedList<Object>>();

		prepareInitData();

		while (uncoveredList.size() > 0) {
			System.out.println(uncoveredList.size());
			singleSuggestion = findPathContainEvent((Edge) uncoveredList.get(0));
			estimator.estimateEventCoverage(privateReport, singleSuggestion);
			allSuggestion.add(singleSuggestion);
			uncoveredList = privateReport.getUncoveredPart();
		}

		return allSuggestion;
	}

	@Override
	public void copyCoveredSituation(CoverageAnalyzedResult originalReport) {
		EventCoverageAnalyzedResult eventOriginalReport = (EventCoverageAnalyzedResult) originalReport;

		HashMap<Edge, Boolean> coveredSituation = eventOriginalReport
				.getCoveredSituation();
		HashMap<Edge, Boolean> copy = new HashMap<Edge, Boolean>();
		Iterator<Edge> iter = coveredSituation.keySet().iterator();

		while (iter.hasNext()) {
			Edge edge = iter.next();
			if (coveredSituation.get(edge).equals(true))
				copy.put(edge, true);
			else
				copy.put(edge, false);
		}

		_estimationReport = new EventCoverageAnalyzedResult(copy);
	}

	private void prepareInitData() {
		Vertex startVertex = _fsmInfo.getStartVertex();
		Vertex endVertex = _fsmInfo.getEndVertex();

		_inShortestPath = new HashMap<Vertex, LinkedList<Vertex>>();
		_outShortestPath = new HashMap<Vertex, LinkedList<Vertex>>();

		for (int index = 0; index < _fsmInfo.getVerticeList().size(); index++) {
			_inShortestPath.put(_fsmInfo.getVerticeList().get(index), null);
			_outShortestPath.put(_fsmInfo.getVerticeList().get(index), null);
		}

		LinkedList<Vertex> temp1 = new LinkedList<Vertex>();
		temp1.add(startVertex);
		_inShortestPath.put(startVertex, temp1);

		LinkedList<Vertex> temp2 = new LinkedList<Vertex>();
		temp2.add(endVertex);
		_outShortestPath.put(endVertex, temp2);

		// Find InShortestPath Start
		LinkedList<Vertex> queueForIn = new LinkedList<Vertex>();

		queueForIn.addLast(startVertex);

		while (!queueForIn.isEmpty()) {
			Vertex source = queueForIn.removeFirst();
			List<Edge> edges = source.getOutEdgeList();

			for (int index = 0; index < edges.size(); index++) {
				if (_inShortestPath.get(edges.get(index).destination()) == null) {
					queueForIn.addLast(edges.get(index).destination());

					LinkedList<Vertex> paths = _inShortestPath.get(source);
					LinkedList<Vertex> currentVertexPaths = new LinkedList<Vertex>();

					for (int j = 0; j < paths.size(); j++) {
						currentVertexPaths.add(paths.get(j));
					}

					currentVertexPaths.add(edges.get(index).destination());
					_inShortestPath.put(edges.get(index).destination(),
							currentVertexPaths);
				}
			}
		}

		// Find OutShortesPath Start
		LinkedList<Vertex> queueForOut = new LinkedList<Vertex>();

		queueForOut.addLast(endVertex);

		while (!queueForOut.isEmpty()) {
			Vertex target = queueForOut.removeFirst();
			List<Edge> edges = target.getInEdgeList();

			for (int index = 0; index < edges.size(); index++) {
				if (_outShortestPath.get(edges.get(index).origin()) == null) {
					queueForOut.addLast(edges.get(index).origin());

					LinkedList<Vertex> paths = _outShortestPath.get(target);
					LinkedList<Vertex> currentVertexPaths = new LinkedList<Vertex>();

					for (int j = 0; j < paths.size(); j++) {
						currentVertexPaths.addFirst(paths.get(j));
					}

					currentVertexPaths.addFirst(edges.get(index).origin());
					_outShortestPath.put(edges.get(index).origin(),
							currentVertexPaths);
				}
			}
		}

	}

	private LinkedList<Object> findPathContainEvent(Edge edge) {
		Vertex source, target;

		LinkedList<Vertex> inPath, outPath;
		LinkedList<Object> eventList = new LinkedList<Object>();

		System.out.println(((State) edge.origin().getUserObject()).getParent()
				.getName()
				+ ((State) edge.origin().getUserObject()).getName()
				+ " "
				+ ((ComponentEventNode) edge.getUserObject()).getName()
				+ " "
				+ ((State) edge.destination().getUserObject()).getName());

		source = edge.origin();
		target = edge.destination();

		inPath = _inShortestPath.get(source);

		for (int index = 0; index < inPath.size() - 1; index++) {
			Vertex v1 = inPath.get(index);
			Vertex v2 = inPath.get(index + 1);
			List<Edge> edges = v1.getOutEdgeList();
			boolean flag = false;
			for (int j = 0; j < edges.size() && flag == false; j++) {
				if (edges.get(j).destination() == v2) {
					eventList.add(edges.get(j).getUserObject());
					flag = true;
				}
			}
		}

		eventList.add(edge.getUserObject());

		outPath = _outShortestPath.get(target);

		for (int index = 0; index < outPath.size() - 1; index++) {
			Vertex v1 = outPath.get(index);
			Vertex v2 = outPath.get(index + 1);
			List<Edge> edges = v1.getOutEdgeList();
			boolean flag = false;
			for (int j = 0; j < edges.size() && flag == false; j++) {
				if (edges.get(j).destination() == v2) {
					eventList.add(edges.get(j).getUserObject());
					flag = true;
				}
			}
		}

		return eventList;

	}
}
