package gttlipse.vfsmCoverageAnalyser.analysis;

import gttlipse.testgen.graph.Edge;
import gttlipse.vfsmCoverageAnalyser.model.EventNodeComparator;
import gttlipse.vfsmCoverageAnalyser.model.FSMInformation;
import gttlipse.vfsmCoverageAnalyser.model.TestCaseInformation;
import gttlipse.vfsmEditor.model.State;

import java.util.LinkedList;
import java.util.List;


public class StateCoverageAnalyst extends CoverageAnalyst{
	private StateCoverageAnalyzedResult _stateCoverageReport;
		
	public StateCoverageAnalyst(TestCaseInformation testCaseInfo, FSMInformation fsmInfo) {
		LinkedList<State> states;
		
		setTestCaseInformation(testCaseInfo);
		setFSMInformation(fsmInfo);
		
		_stateCoverageReport = new StateCoverageAnalyzedResult();
	
		states = _fsmInfo.getStateList();
		_stateCoverageReport.initialize(states);
		
	}
	
	public CoverageAnalyzedResult getResult() {
		return _stateCoverageReport;
	}

	@Override
	public void analyze() {
		LinkedList<LinkedList<Object>> eventsList = _testCaseInfo.getEventsList();
		
		for(int index = 0; index < eventsList.size(); index++) {
			LinkedList<Object> eventList = eventsList.get(index);
			LinkedList<State> coveredStates = trace(eventList);
			System.out.println(coveredStates.size());
			setUsedState(coveredStates);
		}
		
		_stateCoverageReport.calculateCoveredComponentCount();
		_stateCoverageReport.calculateCoverage();
	}
	
	
	private LinkedList<State> trace(LinkedList<Object> eventList) {
		LinkedList<State> states = new LinkedList<State>();
		List<Edge> stateOutTransitionList;
		State sourceState, targetState;
		boolean traceableFlag = false;
		
		sourceState = targetState = _fsmInfo.getStartState();
		states.add(sourceState);
		
		for(int i =0; i < eventList.size(); i++) {
			stateOutTransitionList = _fsmInfo.getStateOutEdgeList(sourceState);
			for(int j = 0; j < stateOutTransitionList.size(); j++) {
				Object obj = stateOutTransitionList.get(j).getUserObject();
				
				if(EventNodeComparator.compare(obj, eventList.get(i))) {
					traceableFlag = true;
					//targetState = _fsmInfo.findTargetState(eventList.get(i));
					targetState = (State)stateOutTransitionList.get(j).destination().getUserObject();
				}
			}
			
			if(traceableFlag == false) {
				return null;
			}
			else {
				traceableFlag = false;
				states.add(sourceState);
				sourceState = targetState;
			}
		}
		
		states.add(targetState);
				
		return states;
	}
	
	private void setUsedState(LinkedList<State> states) {
		for(int i = 0; i < states.size(); i++) {
			_stateCoverageReport.setTracedComponent(states.get(i), true);
		}
	}
}
