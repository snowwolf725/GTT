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
		// �Y�ثe���X��`�I�֦��l�`�I,�h�N��l�`�I�̱����Jqueue����
		if (curVertex.getOutEdgeList().isEmpty()) {
			// �Y�ثe���X�쪺�`�I�S������l���I,��ܨ�����I,�h���@�ӵ��G
			recordFoundPath(path);
			return;
		}

		// has outgoing edges
		List<Edge> outEdgeList = curVertex.getOutEdgeList();

		// �Noutput����Tpush��stack����
		List<Vertex> temp = new Vector<Vertex>();
		int count = 0;
		for (int i = 0; i < outEdgeList.size(); i++) {
			Vertex dest = outEdgeList.get(i).destination();
			// dest ���ثe�B�z��vertex�A�h���Ϋ��X
			if (curVertex == dest)
				continue;
			// dest �w�g�s�bpath���F�A�h���Ϋ��X
			if (path.contains(dest))
				continue;
			// dest �p�G�w�g�O����n���X���I�A�N���ΦA�O�U�ӤF
			if (temp.contains(dest))
				continue;
			// dest �p�G�w�g���X�L�F�A�N���Ϋ��X
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
			// �S��������s���i���X���`�I - ���ͤ@��path
			recordFoundPath(path);
			// path.remove(path.size() - 1); // �h���ª����|
		}

	}

	public void startTrace() {
		// order�O�����@����쪺path��,vertex�Qtrace������
		List<Vertex> path = new Vector<Vertex>();

		// Stack<Vertex> stack = new Stack<Vertex>();
		// // returnStack�O����trace�쪺�`�I�O�_���l�`�I�i�����̫�@��,�@��orderBack�O�_����pop��flag
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

	// �ন�@��Test Case
	private List<Object> formTestCase(Vertex[] sequence) {
		// �C��edge�i�D�諸event list
		List<Integer[]> edges_list = buildEdgeGroup(sequence);

		List<Object> testcase = new Vector<Object>();
		for (int j = 0; j < edges_list.size(); j++) {
			// if (v.getOutEdgeList().size() == 0)
			// continue;
			if (edges_list.get(j).length == 0)
				return null; // no possible events

			Vertex v = sequence[j];
			Integer events[] = edges_list.get(j);
			// �@��edge �W�i�঳�h��event �i�H���A���O�H����@�ӧY�i
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
				// �D��X�i�H����edges
				Edge e = all_edges.get(idx);
				if (e.destination() == dest)
					feasable_edges.add(idx);
			}

			result.add((Integer[]) feasable_edges
					.toArray(new Integer[feasable_edges.size()]));
		}
		return result;
	}

	// �N��쪺path�O�U��
	private void recordFoundPath(List<Vertex> path) {
//		System.out.println("found path: " + path.toString());
		m_VertexSequences.addSequence(path);
		recordVisitedVertex(path);
	}

	// �O�����X�L�� vertex
	private void recordVisitedVertex(List<Vertex> path) {
		Iterator<Vertex> ite = path.iterator();
		while (ite.hasNext()) {
			Vertex v = ite.next();
			if (m_visitedState.contains(v))
				continue; // �w�g���X�L���`�I�A�N���ݭn�O��
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
