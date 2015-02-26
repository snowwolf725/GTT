package gttlipse.vfsmCoverageAnalyser.analysis;

import java.util.LinkedList;


public abstract class CoverageAnalyzedResult {
	protected float _coverage;
	protected int _allComponentCount;
	protected int _coveredComponentCount;
	
	public CoverageAnalyzedResult() {
		_coverage = 0;
		_allComponentCount = 0;
		_coveredComponentCount = 0;
	}
		
	public abstract boolean setTracedComponent(Object component, Object value);
	
	protected abstract void calculateCoveredComponentCount();
	
	public abstract LinkedList<Object> getUncoveredPart();
	
	public abstract LinkedList<Object> getCoveredPart();
	
	public void calculateCoverage() {
		calculateCoveredComponentCount();
		_coverage = (float)_coveredComponentCount / _allComponentCount;
	}
	
	public float getCoverage() {
		return _coverage * 100;
	}
	
	public int getAllComponentCount() {
		return _allComponentCount;
	}
	
	public int getCoveredComponentCount() {
		return _coveredComponentCount;
	}
}
