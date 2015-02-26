package gttlipse.testgen.algorithm;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;
import gttlipse.testgen.testsuite.VertexSequenceContainer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


/**
 * 2009/11/12 
 * 
 * @author zwshen
 * 
 */
class StateCoverageDFS implements ITestGenerationAlgorithm {
	private IGraph m_graph = null;
	private VertexSequenceContainer m_VertexSequences = new VertexSequenceContainer();
	private List<Vertex> m_visitedState = new Vector<Vertex>();

	public StateCoverageDFS(IGraph graph) {
		m_graph = graph;
	}

	private void trace(Vertex curVertex, List<Vertex> path) {
		// 若目前拜訪到節點擁有子節點,則將其子節點依條件放入queue之中
		if (curVertex.getOutEdgeList().isEmpty()) {
			// 若目前拜訪到的節點沒有任何子結點,表示走到終點,則找到一個結果
			recordFoundPath(path);
			return;
		}

		// has outgoing edges
		List<Edge> outEdgeList = curVertex.getOutEdgeList();

		// 將output的資訊push至stack之中
		List<Vertex> temp = new Vector<Vertex>();
		int count = 0;
		for (int i = 0; i < outEdgeList.size(); i++) {
			Vertex dest = outEdgeList.get(i).destination();
			// dest 為目前處理的vertex，則不用拜訪
			if (curVertex == dest)
				continue;
			// dest 已經存在path中了，則不用拜訪
			if (path.contains(dest))
				continue;
			// dest 如果已經是打算要拜訪的點，就不用再記下來了
			if (temp.contains(dest))
				continue;
			// dest 如果已經拜訪過了，就不用拜訪
			if (m_visitedState.contains(dest))
				continue;

			temp.add(dest);

			List<Vertex> now_path = new LinkedList<Vertex>();
			now_path.addAll(path);
			path.add(dest);
			trace(dest, path);
			path = now_path;

			count++;
		}

		if (count == 0) {
			// 沒有找到任何新的可拜訪的節點 - 產生一條path
			recordFoundPath(path);
			// path.remove(path.size() - 1); // 去掉舊的路徑
		}

	}

	public void startTrace() {
		// order是紀錄一條找到的path中,vertex被trace的順序
		List<Vertex> path = new Vector<Vertex>();

		// Stack<Vertex> stack = new Stack<Vertex>();
		// // returnStack是紀錄trace到的節點是否為子節點可走的最後一個,作為orderBack是否必須pop的flag
		// Queue<Boolean> returnStack = new LinkedList<Boolean>();

		if (m_graph.getStart() == null)
			return;

		path.add(m_graph.getStart());
		trace(m_graph.getStart(), path);
	}

	public TestSuite getTestSuite() {
		TestSuite suite = new TestSuite();
		for (int i = 0; i < m_VertexSequences.size(); i++) {
			Vertex sequence[] = m_VertexSequences.getSequence(i);
			// System.out.println( sequence.toString());
			// System.out.print("Test#" + i + "(" + sequence.length + ") : ");
			// for (int idx = 0; idx < sequence.length; idx++)
			// System.out.print(sequence[idx].toString() + ", ");
			// System.out.println("\n");

			List<Object> testcase = formTestCase(sequence);

			if (testcase != null)
				suite.addTestCase(testcase);
		}

		return suite;
	}

	// 轉成一個Test Case
	private List<Object> formTestCase(Vertex[] sequence) {
		// 每條edge可挑選的event list
		List<Integer[]> edges_list = buildEdgeGroup(sequence);

		List<Object> testcase = new Vector<Object>();
		for (int j = 0; j < edges_list.size(); j++) {
			// if (v.getOutEdgeList().size() == 0)
			// continue;
			if (edges_list.get(j).length == 0)
				return null; // no possible events

			Vertex v = sequence[j];
			Integer events[] = edges_list.get(j);
			// 一條edge 上可能有多個event 可以走，但是隨機選一個即可
			int eindex = (int) (Math.random() * events.length);
			Edge e = v.getOutEdgeList().get(events[eindex]);
			testcase.add(e.getUserObject());
		}

		return testcase;
	}

	private List<Integer[]> buildEdgeGroup(Vertex sequence[]) {
		List<Integer[]> result = new Vector<Integer[]>();

		for (int i = 0; i < sequence.length - 1; i++) {
			Vertex source = sequence[i];
			Vertex dest = sequence[i + 1];

			List<Integer> feasable_edges = new Vector<Integer>();

			List<Edge> all_edges = source.getOutEdgeList();
			for (int idx = 0; idx < all_edges.size(); idx++) {
				// 挑選出可以走的edges
				Edge e = all_edges.get(idx);
				if (e.destination() == dest)
					feasable_edges.add(idx);
			}

			result.add((Integer[]) feasable_edges
					.toArray(new Integer[feasable_edges.size()]));
		}
		return result;
	}

	// 將找到的path記下來
	private void recordFoundPath(List<Vertex> path) {
//		System.out.println("found path: " + path.toString());
		m_VertexSequences.addSequence(path);
		recordVisitedVertex(path);
	}

	// 記錄拜訪過的 vertex
	private void recordVisitedVertex(List<Vertex> path) {
		Iterator<Vertex> ite = path.iterator();
		while (ite.hasNext()) {
			Vertex v = ite.next();
			if (m_visitedState.contains(v))
				continue; // 已經拜訪過的節點，就不需要記錄
			m_visitedState.add(v);
		}
	}

	// debug
	// private void printFind( List<Vertex> order )
	// {
	// try
	// {
	// BufferedWriter outfile = new BufferedWriter( new FileWriter(
	// "..\\output.txt", true ) );
	// String outTest = "[Find]";
	// for( int i = 0; i < order.size(); i++ )
	// {
	// Object node = order.get( i ).getUserObject();
	// if( node instanceof GraphBase )
	// outTest = outTest + "->" + ( (GraphBase)node ).getName();
	// else
	// outTest = outTest + "->" + ( (macro.MFSM.State)node ).getName();
	// }
	// System.out.println( outTest );
	// outfile.write( outTest + "\r\n" );
	// outfile.write( "\r\n" );
	// outfile.close();
	// }
	// catch( IOException e )
	// {
	// }
	// }
}
