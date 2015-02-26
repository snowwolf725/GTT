package testing.gttlipse.testcasegen;

import gttlipse.testgen.algorithm.TestCaseGenerator;
import gttlipse.testgen.graph.AdjacencyListGraph;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;
import junit.framework.TestCase;

public class TransitionCoverageTest extends TestCase {

	IGraph g;
	TestCaseGenerator gen;

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
		TestSuite ts = gen.forTransitionCoverage();
		assertTrue(ts.size() == 0);
	}

	public void testOneTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		g.addEdge(v1, v2, new String("edge1"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		assertEquals(1, ts.size());
		assertEquals(1, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "edge1");
	}

	public void testOneDeeperTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("edge1"));
		g.addEdge(v2, v3, new String("edge2"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		assertEquals(1, ts.size());

		assertEquals(2, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "edge1");
		assertEquals(ts.getTestCase(0)[1].toString(), "edge2");

	}

	public void testTwoTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("1to2"));
		g.addEdge(v1, v3, new String("1to3"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		assertEquals(2, ts.size());

		assertEquals(1, ts.getTestCase(0).length);
		assertEquals("1to2", ts.getTestCase(0)[0].toString());

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals("1to3", ts.getTestCase(1)[0].toString());
	}

	public void testTwoDeeperTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		Vertex v4 = g.addVertex(new String("v4"));
		Vertex v5 = g.addVertex(new String("v5"));
		g.addEdge(v1, v2, new String("1to2"));
		g.addEdge(v1, v3, new String("1to3"));
		g.addEdge(v2, v4, new String("2to4"));
		g.addEdge(v4, v5, new String("4to5"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		assertEquals(2, ts.size());

		assertEquals(3, ts.getTestCase(0).length);
		assertEquals("1to2", ts.getTestCase(0)[0].toString());
		assertEquals("2to4", ts.getTestCase(0)[1].toString());
		assertEquals("4to5", ts.getTestCase(0)[2].toString());

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals("1to3", ts.getTestCase(1)[0].toString());
	}

	public void testMoreTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		Vertex v4 = g.addVertex(new String("v4"));
		Vertex v5 = g.addVertex(new String("v5"));
		g.addEdge(v1, v2, new String("1to2"));
		g.addEdge(v1, v3, new String("1to3"));
		g.addEdge(v1, v4, new String("1to4"));
		g.addEdge(v1, v5, new String("1to5"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		assertEquals(4, ts.size());

		assertEquals(1, ts.getTestCase(0).length);
		assertEquals("1to2", ts.getTestCase(0)[0].toString());

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals("1to3", ts.getTestCase(1)[0].toString());

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals("1to4", ts.getTestCase(2)[0].toString());

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals("1to5", ts.getTestCase(3)[0].toString());
	}
	
	
	public void testMoreTestCase2() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		Vertex v4 = g.addVertex(new String("v4"));
		Vertex v5 = g.addVertex(new String("v5"));
		g.addEdge(v1, v2, new String("1to2"));
		g.addEdge(v1, v3, new String("1to3"));
		g.addEdge(v1, v4, new String("1to4"));
		g.addEdge(v1, v5, new String("1to5"));
		g.addEdge(v2, v5, new String("2to5"));
		g.addEdge(v3, v5, new String("3to5"));
		g.addEdge(v4, v5, new String("4to5"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		assertEquals(4, ts.size());

		assertEquals(2, ts.getTestCase(0).length);
		assertEquals("1to2", ts.getTestCase(0)[0].toString());
		assertEquals("2to5", ts.getTestCase(0)[1].toString());

		assertEquals(2, ts.getTestCase(1).length);
		assertEquals("1to3", ts.getTestCase(1)[0].toString());
		assertEquals("3to5", ts.getTestCase(1)[1].toString());

		assertEquals(2, ts.getTestCase(1).length);
		assertEquals("1to4", ts.getTestCase(2)[0].toString());
		assertEquals("4to5", ts.getTestCase(2)[1].toString());
	}
	
	public void testMultipleEdges() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("1to2_e1"));
		g.addEdge(v1, v2, new String("1to2_e2"));
		g.addEdge(v1, v2, new String("1to2_e3"));
		g.addEdge(v1, v2, new String("1to2_e4"));
		g.addEdge(v2, v3, new String("2to3"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		assertEquals(4, ts.size());

		assertEquals(2, ts.getTestCase(0).length);
		assertEquals("1to2_e1", ts.getTestCase(0)[0].toString());
		assertEquals("2to3", ts.getTestCase(0)[1].toString());

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals("1to2_e2", ts.getTestCase(1)[0].toString());

		assertEquals(1, ts.getTestCase(2).length);
		assertEquals("1to2_e3", ts.getTestCase(2)[0].toString());

		assertEquals(1, ts.getTestCase(3).length);
		assertEquals("1to2_e4", ts.getTestCase(3)[0].toString());
	}
	
	public void testThreeTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("1to2_a"));
		g.addEdge(v1, v2, new String("1to2_b"));
		g.addEdge(v2, v3, new String("2to3_a"));
		g.addEdge(v2, v3, new String("2to3_b"));
		g.setStart(v1);

		TestSuite ts = gen.forTransitionCoverage();
		
		// 這個在 ConsectuiveCoverage 會產生4組test case
		assertEquals(3, ts.size());
		assertEquals(2, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "1to2_a");
		assertEquals(ts.getTestCase(0)[1].toString(), "2to3_a");

		assertEquals(2, ts.getTestCase(1).length);
		assertEquals(ts.getTestCase(1)[0].toString(), "1to2_a");
		assertEquals(ts.getTestCase(1)[1].toString(), "2to3_b");

		assertEquals(1, ts.getTestCase(2).length);
		assertEquals(ts.getTestCase(2)[0].toString(), "1to2_b");
		// transition coverage 不用cover 這一個edge
//		assertEquals(ts.getTestCase(2)[1].toString(), "2to3_a");

	}
}
