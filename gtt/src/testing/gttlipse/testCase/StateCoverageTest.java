package testing.gttlipse.testCase;

import gttlipse.testgen.algorithm.TestCaseGenerator;
import gttlipse.testgen.graph.AdjacencyListGraph;
import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.graph.Vertex;
import gttlipse.testgen.testsuite.TestSuite;
import junit.framework.TestCase;

public class StateCoverageTest extends TestCase {

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
		TestSuite ts = gen.forStateCoverage();
		assertTrue(ts.size() == 0);
	}

	public void testOneTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		g.addEdge(v1, v2, new String("edge1"));
		g.setStart(v1);

		TestSuite ts = gen.forStateCoverage();

		assertEquals(1, ts.size());
		assertEquals(1, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "edge1");
	}

	public void testTwoTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		g.addEdge(v1, v2, new String("edge1"));
		g.addEdge(v1, v3, new String("edge2"));
		g.setStart(v1);

		TestSuite ts = gen.forStateCoverage();

		assertEquals(2, ts.size());
		assertEquals(1, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "edge1");

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals(ts.getTestCase(1)[0].toString(), "edge2");
	}

	public void testThreeTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		Vertex v4 = g.addVertex(new String("v4"));
		g.addEdge(v1, v2, new String("edge1"));
		g.addEdge(v1, v3, new String("edge2"));
		g.addEdge(v1, v4, new String("edge3"));
		g.setStart(v1);

		TestSuite ts = gen.forStateCoverage();

		assertEquals(3, ts.size());
		assertEquals(1, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "edge1");

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals(ts.getTestCase(1)[0].toString(), "edge2");

		assertEquals(1, ts.getTestCase(2).length);
		assertEquals(ts.getTestCase(2)[0].toString(), "edge3");
	}

	public void testTwoDeeperTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		Vertex v4 = g.addVertex(new String("v4"));
		Vertex v5 = g.addVertex(new String("v5"));
		g.addEdge(v1, v2, new String("edge1"));
		g.addEdge(v1, v3, new String("edge2"));
		g.addEdge(v2, v4, new String("edge3"));
		g.addEdge(v4, v5, new String("edge4"));
		g.setStart(v1);

		TestSuite ts = gen.forStateCoverage();

		assertEquals(2, ts.size());
		assertEquals(3, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "edge1");
		assertEquals(ts.getTestCase(0)[1].toString(), "edge3");
		assertEquals(ts.getTestCase(0)[2].toString(), "edge4");

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals(ts.getTestCase(1)[0].toString(), "edge2");
	}

	public void testThreeDeeperTestCase() {
		Vertex v1 = g.addVertex(new String("v1"));
		Vertex v2 = g.addVertex(new String("v2"));
		Vertex v3 = g.addVertex(new String("v3"));
		Vertex v4 = g.addVertex(new String("v4"));
		Vertex v5 = g.addVertex(new String("v5"));
		g.addEdge(v1, v2, new String("edge1"));
		g.addEdge(v1, v3, new String("edge2"));
		g.addEdge(v1, v4, new String("edge3"));
		g.addEdge(v2, v5, new String("edge4"));
		g.addEdge(v3, v5, new String("edge5"));
		g.addEdge(v4, v5, new String("edge6"));
		g.setStart(v1);

		TestSuite ts = gen.forStateCoverage();

		assertEquals(3, ts.size());
		assertEquals(2, ts.getTestCase(0).length);
		assertEquals(ts.getTestCase(0)[0].toString(), "edge1");
		assertEquals(ts.getTestCase(0)[1].toString(), "edge4");

		assertEquals(1, ts.getTestCase(1).length);
		assertEquals(ts.getTestCase(1)[0].toString(), "edge2");

		assertEquals(1, ts.getTestCase(2).length);
		assertEquals(ts.getTestCase(2)[0].toString(), "edge3");
	}

}
