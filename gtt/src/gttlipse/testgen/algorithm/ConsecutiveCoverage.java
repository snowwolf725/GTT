package gttlipse.testgen.algorithm;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class ConsecutiveCoverage implements ITestGenerationAlgorithm {
	private IGraph m_graph = null;
	private TestSuite m_TestSuite = new TestSuite();
	private List<EdgePair> m_visited = new LinkedList<EdgePair>();

	class EdgePair {
		Edge e1, e2;

		public EdgePair(Edge edge1, Edge edge2) {
			e1 = edge1;
			e2 = edge2;
		}

		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (!(obj instanceof EdgePair))
				return false;
			EdgePair p = (EdgePair) obj;
			if (e1 != p.e1)
				return false;
			if (e2!= p.e2)
				return false;
			return true;
		}
	}

	public ConsecutiveCoverage(IGraph graph) {
		m_graph = graph;
	}

	private boolean checkPath(List<Edge> path, EdgePair p) {
		Iterator<Edge> ite = path.iterator();
		Edge e1 = ite.next();
		while (ite.hasNext()) {
			Edge e2 = ite.next();
			EdgePair p2 = new EdgePair(e1, e2);
			e1 = e2;
			if (p.equals(p2))
				return true;
		}
		return false;
	}

	private void trace(Edge edge, List<Edge> path) {
		Vertex dest = edge.destination();

		Iterator<Edge> ite = dest.getOutEdgeList().iterator();
		boolean isEnd = true;
		while (ite.hasNext()) {
			Edge next_edge = ite.next();
			EdgePair p = new EdgePair(edge, next_edge);
			// 新edge已經存在path中，就不用拜訪
			if (checkPath(path, p))
				continue;
			// 新edge已經拜訪過，就不用拜訪
			if (m_visited.contains(p))
				continue;

			isEnd = false;

			List<Edge> path2 = new LinkedList<Edge>();
			path2.addAll(path);

			path.add(next_edge);
			trace(next_edge, path);

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
		// System.out.println("found " + path.toString());
		addTestCase(path);
		recordVisitedEdgePair(path);
	}

	private void addTestCase(List<Edge> path) {
		// System.out.println("found " + path.toString());
		List<Object> testcase = new LinkedList<Object>();
		for (int i = 0; i < path.size(); i++) {
			testcase.add(path.get(i).getUserObject());
		}
		if (testcase.size() < 2)
			return; // 至少要有2個事件
		m_TestSuite.addTestCase(testcase);
	}

	// 記錄已經走過的edge ，以後就不用再走回來
	private void recordVisitedEdgePair(List<Edge> path) {
		Iterator<Edge> ite = path.iterator();
		Edge e1 = ite.next();
		while (ite.hasNext()) {
			Edge e2 = ite.next();
			EdgePair p = new EdgePair(e1, e2);
			e1 = e2;
			if (m_visited.contains(p))
				continue;
			m_visited.add(p);
		}
	}

}
