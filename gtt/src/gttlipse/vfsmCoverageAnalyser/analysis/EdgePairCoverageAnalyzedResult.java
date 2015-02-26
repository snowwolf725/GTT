package gttlipse.vfsmCoverageAnalyser.analysis;

import gttlipse.vfsmCoverageAnalyser.model.TransitionPair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


public class EdgePairCoverageAnalyzedResult extends CoverageAnalyzedResult{
	private HashMap<TransitionPair, Boolean> _edgePairCoveredSituation;
	
	public EdgePairCoverageAnalyzedResult() {
		_edgePairCoveredSituation = new HashMap<TransitionPair, Boolean>();
	}
	
	public EdgePairCoverageAnalyzedResult(HashMap<TransitionPair, Boolean> edgePairCoveredSituation) {
		_edgePairCoveredSituation = edgePairCoveredSituation;
	}
	
	public void initialize(LinkedList<TransitionPair> edgePairs) {
		for(int index = 0; index < edgePairs.size(); index++) {
			_edgePairCoveredSituation.put(edgePairs.get(index), false);
		}
		
		_allComponentCount = edgePairs.size();
	}
	
	public HashMap<TransitionPair, Boolean> getCoveredSituation() {
		return _edgePairCoveredSituation;
	}
	
	@Override
	public void calculateCoveredComponentCount() {
		Iterator<TransitionPair> iter = _edgePairCoveredSituation.keySet().iterator();
		int coveredComponentCount = 0;
		Object obj;
		
		while(iter.hasNext()) {
			obj = iter.next();
			if(_edgePairCoveredSituation.get(obj).equals(true))
				coveredComponentCount++;
		}
		
		_coveredComponentCount = coveredComponentCount;
		
	}

	@Override
	public boolean setTracedComponent(Object component, Object value) {
		Iterator<TransitionPair> iter = _edgePairCoveredSituation.keySet().iterator();
		TransitionPair edgePair;
		
		if(component instanceof TransitionPair) {
			TransitionPair targetTransitionPair = (TransitionPair)component;
			while(iter.hasNext()) {
				edgePair = iter.next();
//				Object firstObj = edgePair.getFirstEvent();
//				Object secondObj = edgePair.getSecondEvent();
//				if(compareEvent(firstObj, targetEdgePair.getFirstEvent())
//					&& compareEvent(secondObj, targetEdgePair.getSecondEvent())) {
//					_edgePairCoveredSituation.put(edgePair, (Boolean)value);
//					return true;
//				}			
				if(edgePair.equals(targetTransitionPair)) {
					_edgePairCoveredSituation.put(edgePair, (Boolean)value);
					return true;
				}
			}
			
			return false;
		}
		else
			return false;
	}
	
//	private boolean compareEvent(Object firstObj, Object secondObj) {
//		return EventNodeComparator.compare(firstObj, secondObj);
//	}

	@Override
	public LinkedList<Object> getUncoveredPart() {
		LinkedList<Object> uncoveredList = new LinkedList<Object>();
		Iterator<TransitionPair> iter = _edgePairCoveredSituation.keySet().iterator();
		TransitionPair obj;
		
		while(iter.hasNext()) {
			obj = iter.next();
			
			if(_edgePairCoveredSituation.get(obj).equals(false))
				uncoveredList.add(obj);
		}	
		return uncoveredList;
	}

	@Override
	public LinkedList<Object> getCoveredPart() {
		// TODO Auto-generated method stub
		return null;
	}
}
