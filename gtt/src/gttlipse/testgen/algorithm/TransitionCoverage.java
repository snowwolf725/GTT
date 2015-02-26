package gttlipse.testgen.algorithm;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


public class TransitionCoverage implements ITestGenerationAlgorithm {
	private IGraph m_graph = null;
	private TestSuite m_TestSuite = new TestSuite();
	private List<Edge> m_visitedEdge = new Vector<Edge>();

	public TransitionCoverage(IGraph graph) {
		m_graph = graph;
	}

	private void trace(Edge edge, List<Edge> path) {
		Vertex dest = edge.destination();

		Iterator<Edge> ite = dest.getOutEdgeList().iterator();
		boolean isEnd = true;
		while (ite.hasNext()) {
			Edge new_edge = ite.next();
			// 新edge已經存在path中，就不用拜訪
			if (path.contains(new_edge))
				continue;
			// 新edge已經拜訪過，就不用拜訪
			if (m_visitedEdge.contains(new_edge))
				continue;

			isEnd = false;

			List<Edge> path2 = new LinkedList<Edge>();
			path2.addAll(path);

			path.add(new_edge);
			trace(new_edge, path);

			path = path2;
		}
		if (isEnd) {
			processFoundPath(path);
			return;
		}

	}

	public void startTrace() {
		Vertex start = m_graph.getStart();

		if (start == null)
			return;

		Iterator<Edge> ite = start.getOutEdgeList().iterator();
		while (ite.hasNext()) {
			Edge e = ite.next();
			List<Edge> path = new LinkedList<Edge>();
			path.add(e);
			trace(e, path);
		}
	}

	public TestSuite getTestSuite() {
		return m_TestSuite;
	}

	private void processFoundPath(List<Edge> path) {
		addTestCase(path);
		recordVisitedEdge(path);
	}

	private void addTestCase(List<Edge> path) {
		List<Object> testcase = new Vector<Object>();

		for (int i = 0; i < path.size(); i++) {
			Edge e = path.get(i);
			testcase.add(e.getUserObject());
		}
		m_TestSuite.addTestCase(testcase);
	}

	// 記錄已經走過的edge ，以後就不用再走回來
	private void recordVisitedEdge(List<Edge> path) {

		Iterator<Edge> ite = path.iterator();
		while (ite.hasNext()) {
			Edge e = ite.next();
			if (m_visitedEdge.contains(e))
				continue;
			m_visitedEdge.add(e);

		}

	}

	// private void pathReturnBack(List<Edge> path, List<Integer> path2,
	// List<Integer> pathBack) {
	// // path back
	// if (pathBack.size() > 0) {
	// int pathback = pathBack.get(pathBack.size() - 1);
	// while (path.size() > pathback)
	// path.remove(path.size() - 1);
	//
	// int pathback2 = pathBack.get(pathBack.size() - 1);
	// while (path2.size() > pathback2)
	// path2.remove(path2.size() - 1);
	// }
	// }
	//
	// private void orderReturnBack(List<Vertex> order, List<Integer> orderBack)
	// {
	// // order back
	// if (orderBack.size() > 0) {
	// int orderback = orderBack.get(orderBack.size() - 1);
	// while (order.size() > orderback)
	// order.remove(order.size() - 1);
	// }
	// }
}
