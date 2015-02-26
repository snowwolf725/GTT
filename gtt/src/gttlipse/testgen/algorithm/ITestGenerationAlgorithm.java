package gttlipse.testgen.algorithm;

import gttlipse.testgen.testsuite.TestSuite;

public interface ITestGenerationAlgorithm 
{
	public void startTrace();
	public TestSuite getTestSuite();
}
