package gttlipse.vfsmCoverageAnalyser.analysis;

import gttlipse.testgen.graph.Edge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;


public class EventCoverageAnalyzedResult extends CoverageAnalyzedResult{
	private HashMap<Edge, Boolean> _eventCoveredSituation;
	
	public EventCoverageAnalyzedResult() {
		_eventCoveredSituation = new HashMap<Edge, Boolean>();
	}
	
	public EventCoverageAnalyzedResult(HashMap<Edge, Boolean> eventCoveredSituation) {
		_eventCoveredSituation = eventCoveredSituation;
		_allComponentCount = _eventCoveredSituation.size();
		calculateCoverage();
	}
	
	public void initialize(LinkedList<Edge> edges) {
		for(int index = 0; index < edges.size(); index++) {
			_eventCoveredSituation.put(edges.get(index), false);
		}
	
		_allComponentCount = edges.size();
	}
	
	public HashMap<Edge, Boolean> getCoveredSituation() {
		return _eventCoveredSituation;
	}
	
	public Boolean getEventCoveredSituation(Object obj) {
		return _eventCoveredSituation.get(obj);
	}

	@Override
	protected void calculateCoveredComponentCount() {
		Iterator<Edge> iter = _eventCoveredSituation.keySet().iterator();
		int coveredComponentCount = 0;
		Object obj;
		
		while(iter.hasNext()) {
			obj = iter.next();
			if(_eventCoveredSituation.get(obj).equals(true))
				coveredComponentCount++;
		}
		
		_coveredComponentCount = coveredComponentCount;
		
	}
	
	@Override
	public boolean setTracedComponent(Object component, Object value) {
		Set<Edge> keySet = _eventCoveredSituation.keySet();
		Iterator<Edge> iter = keySet.iterator();
		Edge edge;
		
		while(iter.hasNext()) {
			edge = iter.next();
			if(edge == (Edge)component) {
				_eventCoveredSituation.put(edge, (Boolean)value);
				return true;
			}
		}
		
		System.out.println("EventCoverageAnalyzedResult:settracedComponent() Error: Do not match data.");
		
		return false;
	}

	@Override
	public LinkedList<Object> getUncoveredPart() {
		LinkedList<Object> uncoveredList = new LinkedList<Object>();
		Iterator<Edge> iter = _eventCoveredSituation.keySet().iterator();
		Edge edge;
		
		while(iter.hasNext()) {
			edge = iter.next();
			
			if(_eventCoveredSituation.get(edge).equals(false))
				uncoveredList.add(edge);
		}	
		return uncoveredList;
	}

	@Override
	public LinkedList<Object> getCoveredPart() {
		LinkedList<Object> coveredList = new LinkedList<Object>();
		Iterator<Edge> iter = _eventCoveredSituation.keySet().iterator();
		Edge edge;
		
		while(iter.hasNext()) {
			edge = iter.next();
			
			if(_eventCoveredSituation.get(edge).equals(true))
				coveredList.add(edge);
		}	
		return coveredList;
	}
}
