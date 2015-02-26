package gttlipse.testgen.algorithm;

import gttlipse.testgen.graph.Edge;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;
import gttlipse.vfsmEditor.model.State;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.Vector;


class PathCoverage implements ITestGenerationAlgorithm {
	private IGraph m_graph = null;
	private TestSuite m_caseSet = new TestSuite();

	public PathCoverage(IGraph graph) {
		m_graph = graph;
	}

	public void startTrace() {
		// debug
		printCurrentTime();

		List<Vertex> order = new Vector<Vertex>();
		Stack<Integer> orderBack = new Stack<Integer>();
		List<Integer> path2 = new Vector<Integer>();
		Stack<Integer> pathStack2 = new Stack<Integer>();

		List<Edge> path = new Vector<Edge>();
		Stack<Integer> pathBack = new Stack<Integer>();
		Stack<Vertex> nodeStack = new Stack<Vertex>();
		Stack<Edge> pathStack = new Stack<Edge>();
		Stack<Integer> returnStack = new Stack<Integer>();
		Vertex start = getStartVertex(m_graph);
		nodeStack.push(start);
		order.add(start);

		while (nodeStack.isEmpty() != true) {
			int returnFlag = -1;
			int traceIndex = -1;
			Edge traceEdge = null;
			Vertex currentNode = nodeStack.pop();
			List<Edge> outList = currentNode.getOutEdgeList();

			if (pathStack.size() > 0) {
				traceEdge = pathStack.pop();
				traceIndex = pathStack2.pop();
			}

			if (returnStack.size() > 0) {
				returnFlag = returnStack.pop();
				// ���X��l�`�I���̫�@��,�]��back���`�I�٭n�b��W�@�h,stack��back��Tpop
				if (returnFlag == 0) {
					orderBack.pop();
					pathBack.pop();
				}
			}

			// �ˬd�O�_���L����,�Y�S���L�h�[�Jpath,�Y���L�h�N��node return
			if (traceEdge != null && path.contains(traceEdge) == false) {
				path.add(traceEdge);
				path2.add(traceIndex);
				order.add(currentNode);
			} else if (path.contains(traceEdge) == true) {
				// return back
				backOrderFixProcess(order, orderBack);
				backFixProcess(path, path2, pathBack);
				continue;
			}

			// �Y�ثe���X��`�I�֦��l�`�I,�h�N��l�`�I�̱����J���|����
			if (outList.isEmpty() == false) {
				int count = 0;

				for (int i = 0; i < outList.size(); i++) {
					Edge e = outList.get(i);
					Vertex dest = e.destination();

					if (currentNode != dest && path.contains(e) == false) {
						pathStack.push(e);
						pathStack2.push(i);
						nodeStack.push(dest);

						if (count == 0)
							returnStack.push(0);
						else
							returnStack.push(1);
						count++;
					}
				}

				if (count == 0) {
					// return back
					backOrderFixProcess(order, orderBack);
					backFixProcess(path, path2, pathBack);
					continue;
				} else {
					// �]���u���@�Ӥl�`�I�ɤ��|����back������(�]���S���N�q),�ҥHorderBack���ݳQpop
					if (count == 1)
						returnStack.set(returnStack.size() - 1, 1);

					// ����back������, �H�Kreturn back
					if (count > 1) {
						pathBack.push(path.size());
						orderBack.push(order.size());
					}
				}
			}
			// �Y�ثe���X�쪺�`�I�S������l���I,��ܨ�����I,�h���@�ӵ��G
			else {
				// printout and record
				if (path.size() > 30) {
					pushToTestCaseSet(path);
//					printFind(path, path2, order);
					break;
				}

				// return back
				backOrderFixProcess(order, orderBack);
				backFixProcess(path, path2, pathBack);
				continue;
			}
		}

		// debug
		printCurrentTime();
	}

	public TestSuite getTestSuite() {
		return m_caseSet;
	}

	private void pushToTestCaseSet(List<Edge> path) {
		List<Object> eventSeq = new Vector<Object>();

		for (int i = 0; i < path.size(); i++) {
			Edge e = path.get(i);
			eventSeq.add(e.getUserObject());
		}
		m_caseSet.addTestCase(eventSeq);
	}

	// find start
	private Vertex getStartVertex(IGraph graph) {
		List<Vertex> vertexList = graph.vertices();
		Vertex start = null;
		for (int i = 0; i < vertexList.size(); i++) {
			Vertex v = vertexList.get(i);
			// it might exist some bug in using VFSM model
			// - zwshen 2009/01/14
			State obj = (State) v.getUserObject();
			if (obj.getName().matches("Main"))
				start = v;
		}
		return start;
	}

	private void backFixProcess(List<Edge> path, List<Integer> path2,
			List<Integer> pathBack) {
		// path back
		if (pathBack.size() > 0) {
			int pathback = pathBack.get(pathBack.size() - 1);
			while (path.size() > pathback)
				path.remove(path.size() - 1);

			int pathback2 = pathBack.get(pathBack.size() - 1);
			while (path2.size() > pathback2)
				path2.remove(path2.size() - 1);
		}
	}

	// debug
	private void backOrderFixProcess(List<Vertex> order, List<Integer> orderBack) {
		// order back
		if (orderBack.size() > 0) {
			int orderback = orderBack.get(orderBack.size() - 1);
			while (order.size() > orderback)
				order.remove(order.size() - 1);
		}
	}

	// debug
//	private void printFind(List<Edge> path, List<Integer> path2,
//			List<Vertex> order) {
//		try {
//			BufferedWriter outfile = new BufferedWriter(new FileWriter(
//					"..\\output.txt", true));
//
//			String outTest = "[Find]";
//			for (int i = 0; i < path.size(); i++) {
//				EventNode node = (EventNode) path.get(i).getUserObject();
//				outTest = outTest + "->" + node.getComponent().getName() + "."
//						+ node.getEvent().getName();
//			}
//			// System.out.println( outTest );
//			outfile.write(outTest + "\r\n");
//
//			String outTest2 = "[Find]";
//			for (int i = 0; i < path2.size(); i++) {
//				outTest2 = outTest2 + "->" + path2.get(i);
//			}
//			// System.out.println( outTest2 );
//			outfile.write(outTest2 + "\r\n");
//
//			String outTest3 = "[Find]";
//			for (int i = 0; i < order.size(); i++)
//				outTest3 = outTest3 + "->"
//						+ ((IHFsmState) order.get(i).getUserObject()).getName();
//			// System.out.println( outTest3 );
//			outfile.write(outTest3 + "\r\n");
//
//			outfile.write("\r\n");
//			outfile.close();
//		} catch (IOException e) {
//		}
//	}

	private void printCurrentTime() {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd ( HH:mm::ss )");
		Date today = new Date();
		System.out.println(time.format(today));
	}
}
