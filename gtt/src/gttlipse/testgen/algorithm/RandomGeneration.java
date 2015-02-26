package gttlipse.testgen.algorithm;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;

import java.util.List;
import java.util.Stack;
import java.util.Vector;


class RandomGeneration implements ITestGenerationAlgorithm {
	public final static int DEFAULT_STEPS = 20;

	private IGraph m_graph = null;
	private TestSuite m_suite = new TestSuite();

	private int m_step;

	public RandomGeneration(IGraph graph) {
		this(graph, DEFAULT_STEPS);
	}

	public RandomGeneration(IGraph graph, int step) {
		m_graph = graph;
		m_step = step;
	}

	public TestSuite getTestSuite() {
		return m_suite;
	}

	public void startTrace() {
		Stack<Vertex> stack = new Stack<Vertex>();
		List<Object> testcase = new Vector<Object>();

		stack.add(m_graph.getStart());

		int cur_step = 0;
		while (cur_step < m_step) {
			Vertex current_vtx = stack.pop();
			Edge rndSelectedEdge = rndSelectOutEdge(current_vtx);

			if (rndSelectedEdge == null)
				continue; // 沒有適合的edge，這個vertex不能使用

			// 得到一個可用的edge，放入test case中
			testcase.add(rndSelectedEdge.getUserObject());

			// 將下一個 vertex放入 stack
			stack.add(rndSelectedEdge.destination());

			cur_step++;
		}

		m_suite.addTestCase(testcase);
	}

	private Edge rndSelectOutEdge(Vertex vtx) {
		Edge outEdge = pickEdge(vtx);
		int find = 0;
		while (find < 10) {
			outEdge = pickEdge(vtx);
			if (outEdge.destination().getOutEdgeList().size() != 0)
				return outEdge; // find it
			// 這條邊沒有下一條路，得要重新再選擇一個邊
			find++;
		}
		return null; // not find any edge
	}

	private Edge pickEdge(Vertex traceVertex) {
		// random index of outgoing edge
		List<Edge> out_edges = traceVertex.getOutEdgeList();
		int rnd_idx = (int) (Math.random() * (out_edges.size() - 1));
		return out_edges.get(rnd_idx);
	}

}
