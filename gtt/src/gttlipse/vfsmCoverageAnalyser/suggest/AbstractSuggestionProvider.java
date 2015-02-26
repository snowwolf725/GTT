package gttlipse.vfsmCoverageAnalyser.suggest;

import gttlipse.vfsmCoverageAnalyser.analysis.CoverageAnalyzedResult;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;

import java.util.LinkedList;


public abstract class AbstractSuggestionProvider {
	protected TestCaseInformation _testCaseInfo;

	protected FSMInformation _fsmInfo;

	protected CoverageAnalyzedResult _estimationReport;

	public abstract LinkedList<LinkedList<Object>> provideSuggestion(
			CoverageAnalyzedResult report);

	public abstract void copyCoveredSituation(
			CoverageAnalyzedResult originalReport);

	protected void setData(TestCaseInformation testCaseInfo,
			FSMInformation fsmInfo) {
		_testCaseInfo = testCaseInfo;
		_fsmInfo = fsmInfo;
	}
}
