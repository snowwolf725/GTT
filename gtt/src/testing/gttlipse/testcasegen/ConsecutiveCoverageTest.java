package testing.gttlipse.testcasegen;

import gttlipse.testgen.algorithm.TestCaseGenerator;
import gttlipse.testgen.graph.AdjacencyListGraph;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;
import junit.framework.TestCase;

public class ConsecutiveCoverageTest extends TestCase {

	IGraph g = null;
	TestCaseGenerator gen = null;

	protected void setUp() throws Exception {
		super.setUp();
		g = new AdjacencyListGraph();
		gen = new TestCaseGenerator(g);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		g = null;
		gen = null;
	}

	public void testEmptyGraph() {
		TestSuite ts = gen.forConcesutiveCoverage();
		assertTrue(ts.size() == 0);
	}
	
	public void testEmptyGraph2() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		// 只有一個edge 沒法子產生test case
		g.addEdge(v1, v2, new String("1to2"));
		g.setStart(v1);
		TestSuite ts = gen.forConcesutiveCoverage();
		assertEquals(0, ts.size());
	}


	public void testOneTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("1to2"));
		g.addEdge(v2, v3, new String("2to3"));
		g.setStart(v1);

		TestSuite ts = gen.forConcesutiveCoverage();

		assertEquals(1, ts.size());
		assertEquals(2, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "1to2");
		assertEquals(ts.getTestCase(0)[1].toString(), "2to3");
	}
	
	
	public void testTwoTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("1to2_a"));
		g.addEdge(v1, v2, new String("1to2_b"));
		g.addEdge(v2, v3, new String("2to3"));
		g.setStart(v1);

		TestSuite ts = gen.forConcesutiveCoverage();
		
		assertEquals(2, ts.size());
		assertEquals(2, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "1to2_a");
		assertEquals(ts.getTestCase(0)[1].toString(), "2to3");

		assertEquals(2, ts.getTestCase(1).length);
		assertEquals(ts.getTestCase(1)[0].toString(), "1to2_b");
		assertEquals(ts.getTestCase(1)[1].toString(), "2to3");
		
		// for transition coverage
		TestSuite ts2 = gen.forTransitionCoverage();
		
		assertEquals(2, ts2.size());
		assertEquals(2, ts2.getTestCase(0).length);
		assertEquals(ts2.getTestCase(0)[0].toString(), "1to2_a");
		assertEquals(ts2.getTestCase(0)[1].toString(), "2to3");

		assertEquals(1, ts2.getTestCase(1).length);
		assertEquals(ts2.getTestCase(1)[0].toString(), "1to2_b");
		// transition 沒有cover 這一條
//		assertEquals(ts.getTestCase(1)[0].toString(), "2to3");
	}
	public void testFourTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("1to2_a"));
		g.addEdge(v1, v2, new String("1to2_b"));
		g.addEdge(v2, v3, new String("2to3_a"));
		g.addEdge(v2, v3, new String("2to3_b"));
		g.setStart(v1);

		TestSuite ts = gen.forConcesutiveCoverage();
		
		// 如果使用 Transition Coverage 會產生三組而已
		// 但是這裡會產生四組

		assertEquals(4, ts.size());
		assertEquals(2, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "1to2_a");
		assertEquals(ts.getTestCase(0)[1].toString(), "2to3_a");

		assertEquals(2, ts.getTestCase(1).length);
		assertEquals(ts.getTestCase(1)[0].toString(), "1to2_a");
		assertEquals(ts.getTestCase(1)[1].toString(), "2to3_b");

		assertEquals(2, ts.getTestCase(2).length);
		assertEquals(ts.getTestCase(2)[0].toString(), "1to2_b");
		assertEquals(ts.getTestCase(2)[1].toString(), "2to3_a");

		assertEquals(2, ts.getTestCase(3).length);
		assertEquals(ts.getTestCase(3)[0].toString(), "1to2_b");
		assertEquals(ts.getTestCase(3)[1].toString(), "2to3_b");
	}
	
	
	public void testComplexTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		Vertex v4 = g.addVertex(new String("v4"));
		Vertex v5 = g.addVertex(new String("v5"));
		Vertex v6 = g.addVertex(new String("v6"));
		g.addEdge(v1, v2, new String("1to2"));
		g.addEdge(v1, v3, new String("1to3"));
		g.addEdge(v1, v4, new String("1to4"));
		g.addEdge(v1, v5, new String("1to5"));
		g.addEdge(v2, v6, new String("2to6"));
		g.addEdge(v6, v3, new String("6to3"));
		g.addEdge(v3, v1, new String("3to1"));
		g.addEdge(v4, v6, new String("4to6"));
		g.addEdge(v6, v5, new String("6to5"));
		g.addEdge(v5, v1, new String("5to1"));
		g.setStart(v1);

		TestSuite ts = gen.forConcesutiveCoverage();
		
		assertEquals(8, ts.size());
	}
}
