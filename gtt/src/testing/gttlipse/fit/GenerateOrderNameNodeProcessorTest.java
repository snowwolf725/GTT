package testing.gttlipse.fit;

import gtt.eventmodel.Argument;
import junit.framework.TestCase;
import gttlipse.fit.node.GenerateOrderNameNode;
import gttlipse.fit.node.processor.GenerateOrderNameNodeProcessor;

public class GenerateOrderNameNodeProcessorTest extends TestCase {
	GenerateOrderNameNodeProcessor m_processor;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGeneration() {
		String[] result;
		GenerateOrderNameNode node1 = new GenerateOrderNameNode();
		node1.setPrefix("Square(0,");
		node1.setSuffix(")");
		node1.setStart("1");
		node1.setEnd("10");
		result = new GenerateOrderNameNodeProcessor(node1).generate();
		assertTrue(result.length == 10);
		assertTrue(result[0].compareTo("Square(0,1)") == 0);
		assertTrue(result[1].compareTo("Square(0,2)") == 0);
		assertTrue(result[2].compareTo("Square(0,3)") == 0);
		assertTrue(result[3].compareTo("Square(0,4)") == 0);
		assertTrue(result[4].compareTo("Square(0,5)") == 0);
		assertTrue(result[5].compareTo("Square(0,6)") == 0);
		assertTrue(result[6].compareTo("Square(0,7)") == 0);
		assertTrue(result[7].compareTo("Square(0,8)") == 0);
		assertTrue(result[8].compareTo("Square(0,9)") == 0);
		assertTrue(result[9].compareTo("Square(0,10)") == 0);
		
		GenerateOrderNameNode node2 = new GenerateOrderNameNode();
		node2.getArguments().add(Argument.create("String", "X1", "5"));
		node2.setPrefix("Square(#X1#,");
		node2.setSuffix(")");
		node2.setStart("1");
		node2.setEnd("2");
		result = new GenerateOrderNameNodeProcessor(node2).generate();
		assertTrue(result.length == 2);
		assertTrue(result[0].compareTo("Square(5,1)") == 0);
		assertTrue(result[1].compareTo("Square(5,2)") == 0);
		
		GenerateOrderNameNode node3 = new GenerateOrderNameNode();
		node3.setPrefix("Square(0,");
		node3.setSuffix(")");
		node3.setStart("1");
		node3.setEnd("1");
		result = new GenerateOrderNameNodeProcessor(node3).generate();
		assertTrue(result.length == 1);
		assertTrue(result[0].compareTo("Square(0,1)") == 0);

		GenerateOrderNameNode node4 = new GenerateOrderNameNode();
		node4.getArguments().add(Argument.create("String", "X1", "5"));
		node4.getArguments().add(Argument.create("String", "a", "a"));
		node4.setPrefix("Squ#a#re(#X1#,");
		node4.setSuffix(")");
		node4.setStart("1");
		node4.setEnd("1");
		result = new GenerateOrderNameNodeProcessor(node4).generate();
		assertTrue(result.length == 1);
		assertTrue(result[0].compareTo("Square(5,1)") == 0);

	}
}
