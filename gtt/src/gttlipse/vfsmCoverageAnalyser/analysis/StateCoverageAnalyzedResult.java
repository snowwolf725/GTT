package gttlipse.vfsmCoverageAnalyser.analysis;

import gttlipse.vfsmEditor.model.State;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class StateCoverageAnalyzedResult extends CoverageAnalyzedResult{
	private HashMap<State, Boolean> _stateCoveredSituation;
	
	public StateCoverageAnalyzedResult() {
		_stateCoveredSituation = new HashMap<State, Boolean>();
	}
	
	public StateCoverageAnalyzedResult(HashMap<State, Boolean> stateCoveredSituation) {
		_stateCoveredSituation = stateCoveredSituation;
		_allComponentCount = _stateCoveredSituation.size();
	}
	
	public void initialize(LinkedList<State> states) {
		for(int index = 0; index < states.size(); index++) {
			_stateCoveredSituation.put(states.get(index), false);
		}
		
		_allComponentCount = states.size();
	}
	
	public HashMap<State, Boolean> getCoveredSituation() {
		return _stateCoveredSituation;
	}
	
	@Override
	protected void calculateCoveredComponentCount() {
		Iterator<State> iter = _stateCoveredSituation.keySet().iterator();
		int coveredComponentCount = 0;
		State state;
		
		while(iter.hasNext()) {
			state = iter.next();
			if(_stateCoveredSituation.get(state).equals(true))
				coveredComponentCount++;
		}
		
		_coveredComponentCount = coveredComponentCount;
		
	}

	@Override
	public boolean setTracedComponent(Object component, Object value) {
		if(component instanceof State && value instanceof Boolean) {
			if(_stateCoveredSituation.containsKey((State)component) != false) {
				_stateCoveredSituation.put((State)component, (Boolean)value);
				return true;
			}
			else 
				return false;
		}
		else
			return false;
	}

	@Override
	public LinkedList<Object> getUncoveredPart() {
		LinkedList<Object> uncoveredList = new LinkedList<Object>();
		Iterator<State> iter = _stateCoveredSituation.keySet().iterator();
		Object obj;
		
		while(iter.hasNext()) {
			obj = iter.next();
			
			if(_stateCoveredSituation.get(obj).equals(false))
				uncoveredList.add(obj);
		}	
		return uncoveredList;
	}

	@Override
	public LinkedList<Object> getCoveredPart() {
		LinkedList<Object> coveredList = new LinkedList<Object>();
		Iterator<State> iter = _stateCoveredSituation.keySet().iterator();
		Object obj;
		
		while(iter.hasNext()) {
			obj = iter.next();
			
			if(_stateCoveredSituation.get(obj).equals(true))
				coveredList.add(obj);
		}	
		return coveredList;
	}

}
