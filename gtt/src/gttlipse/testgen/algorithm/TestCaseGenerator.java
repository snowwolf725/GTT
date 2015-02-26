package gttlipse.testgen.algorithm;

import gttlipse.testgen.graph.IGraph;
import gttlipse.testgen.testsuite.TestSuite;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TestCaseGenerator {
	private IGraph m_graph = null;

	public TestCaseGenerator(IGraph graph) {
		m_graph = graph;
	}

	// path coverage
	public TestSuite DFSForFindAllPath() {
		ITestGenerationAlgorithm aglorithm = new PathCoverage(m_graph);
		aglorithm.startTrace();
		return aglorithm.getTestSuite();
	}

	// state coverage
	public TestSuite forStateCoverage() {
		ITestGenerationAlgorithm algorithm = new StateCoverageDFS(m_graph);
		algorithm.startTrace();
		return algorithm.getTestSuite();
	}
	
//	// state coverage
//	public TestSuite BFSForStateCoverage() {
//		ITestGenerationAlgorithm algorithm = new StateCoverageDFS(m_graph);
//		algorithm.startTrace();
//		return algorithm.getTestSuite();
//	}

	// complete state coverage
	public TestSuite DFSForCompleteStateCoverage() {
		ITestGenerationAlgorithm algorithm = new CompleteStateCoverage(m_graph);
		algorithm.startTrace();
		return algorithm.getTestSuite();
	}

	// transition coverage
	public TestSuite forTransitionCoverage() {
		ITestGenerationAlgorithm algorithm = new TransitionCoverage(m_graph);
		algorithm.startTrace();
		return algorithm.getTestSuite();
	}

	// Concesutive coverage
	public TestSuite forConcesutiveCoverage() {
		System.out.print("forConcesutiveCoverage begin:");
		printTime();
		
		ITestGenerationAlgorithm algorithm = new ConsecutiveCoverage(m_graph);
		algorithm.startTrace();
		
		System.out.print("forConcesutiveCoverage end:");
		printTime();
		return algorithm.getTestSuite();
	}

	// monkey test
	public TestSuite monkeyTest() {
		return monkeyTest(RandomGeneration.DEFAULT_STEPS);	// ¹w³]¬O20 steps
	}

	public TestSuite monkeyTest(int step) {
		ITestGenerationAlgorithm algorithm = new RandomGeneration(m_graph, step);
		algorithm.startTrace();
		return algorithm.getTestSuite();
	}
	
    // debug
	private void printTime()
	{
		SimpleDateFormat time = new SimpleDateFormat( "yyyy-MM-dd ( HH:mm::ss )" ); 
		System.out.println( time.format( new Date() ) ); 
	}
}
