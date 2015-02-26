package gttlipse.vfsmCoverageAnalyser.suggest;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.Vertex;
import gttlipse.vfsmCoverageAnalyser.analysis.CoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.analysis.StateCoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;
import gttlipse.vfsmEditor.model.State;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class StateCoverageSuggestionProvider extends AbstractSuggestionProvider {
	private HashMap<Vertex, LinkedList<Vertex>> _inShortestPath;

	private HashMap<Vertex, LinkedList<Vertex>> _outShortestPath;

	public StateCoverageSuggestionProvider(TestCaseInformation testCaseInfo,
			FSMInformation fsmInfo) {
		setData(testCaseInfo, fsmInfo);
	}

	@Override
	public LinkedList<LinkedList<Object>> provideSuggestion(
			CoverageAnalyzedResult report) {
		if (!(report instanceof StateCoverageAnalyzedResult))
			return null;

		copyCoveredSituation(report);

		StateCoverageAnalyzedResult privateReport;

		if (_estimationReport instanceof StateCoverageAnalyzedResult)
			privateReport = (StateCoverageAnalyzedResult) _estimationReport;
		else
			return null;

		SuggestionEstimator estimator = new SuggestionEstimator(_fsmInfo);

		LinkedList<Object> uncoveredList = privateReport.getUncoveredPart();

		LinkedList<Object> singleSuggestion;
		LinkedList<LinkedList<Object>> allSuggestion = new LinkedList<LinkedList<Object>>();

		prepareInitData();

		while (uncoveredList.size() > 0) {
			singleSuggestion = findPathContainState((State) uncoveredList
					.get(0));
			estimator.estimateStateCoverage(privateReport, singleSuggestion);
			allSuggestion.add(singleSuggestion);
			uncoveredList = privateReport.getUncoveredPart();
		}

		return allSuggestion;
	}

	@Override
	public void copyCoveredSituation(CoverageAnalyzedResult originalReport) {
		StateCoverageAnalyzedResult stateOriginalReport = (StateCoverageAnalyzedResult) originalReport;

		HashMap<State, Boolean> coveredSituation = stateOriginalReport
				.getCoveredSituation();
		HashMap<State, Boolean> copy = new HashMap<State, Boolean>();
		Iterator<State> iter = coveredSituation.keySet().iterator();

		while (iter.hasNext()) {
			State obj = iter.next();
			if (coveredSituation.get(obj).equals(true))
				copy.put(obj, true);
			else
				copy.put(obj, false);
		}

		_estimationReport = new StateCoverageAnalyzedResult(copy);
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

		// for(int index = 0; index < _fsmInfo.getVerticeList().size(); index++)
		// {
		// Vertex v = _fsmInfo.getVerticeList().get(index);
		//			
		// System.out.print(((State)v.getUserObject()).fullName() + " ---- ");
		//			
		// for(int i = 0; i < _inShortestPath.get(v).size(); i++)
		// System.out.print(((State)_inShortestPath.get(v).get(i).getUserObject()).fullName()
		// + " ");
		//			
		// for(int i = 1; i < _outShortestPath.get(v).size(); i++)
		// System.out.print(((State)_outShortestPath.get(v).get(i).getUserObject()).fullName()
		// + " ");
		//			
		// System.out.println();
		// }

	}

	private LinkedList<Object> findPathContainState(State state) {
		Vertex midPoint;
		LinkedList<Vertex> inPath, outPath, fullPath;
		LinkedList<Object> eventList = new LinkedList<Object>();

		midPoint = _fsmInfo.findOutState(state);

		inPath = _inShortestPath.get(midPoint);
		outPath = _outShortestPath.get(midPoint);
		fullPath = new LinkedList<Vertex>();

		for (int index = 0; index < inPath.size(); index++) {
			fullPath.add(inPath.get(index));
		}

		for (int index = 1; index < outPath.size(); index++) {
			fullPath.add(outPath.get(index));
		}

		for (int index = 0; index < fullPath.size() - 1; index++) {
			Vertex v1 = fullPath.get(index);
			Vertex v2 = fullPath.get(index + 1);
			List<Edge> edges = v1.getOutEdgeList();
			boolean flag = false;
			for (int j = 0; j < edges.size() && flag == false; j++) {
				if (edges.get(j).destination() == v2) {
					eventList.add(edges.get(j).getUserObject());
					flag = true;
					// System.out.println("got it" +
					// edges.get(j).getUserObject().getClass().toString());
				}
			}
		}

		// for(int index = 0; index < eventList.size(); index++) {
		// System.out.print(((ComponentEventNode)eventList.get(index)).getComponentName()
		// + ", ");
		// }

		return eventList;
	}

}
