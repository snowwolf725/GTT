package gttlipse.vfsmCoverageAnalyser.analysis;

import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;


public abstract class CoverageAnalyst {
	protected TestCaseInformation _testCaseInfo;
	protected FSMInformation _fsmInfo;
	
	public CoverageAnalyst() {
		
	}
	
	public void setTestCaseInformation(TestCaseInformation testCaseInfo) {
		_testCaseInfo = testCaseInfo;
	}
	
	public void setFSMInformation(FSMInformation fsmInfo) {
		_fsmInfo = fsmInfo;
	}
	
	abstract public void analyze();
}
