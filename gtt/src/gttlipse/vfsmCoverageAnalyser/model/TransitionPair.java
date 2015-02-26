package gttlipse.vfsmCoverageAnalyser.model;

import gttlipse.testgen.graph.Edge;

public class TransitionPair {
	private Edge _firstTransition;
	private Edge _secondTransition;
	
	public TransitionPair(Edge firstTransition, Edge secondTransition) {
			_firstTransition = (Edge)firstTransition;
			_secondTransition = (Edge)secondTransition;
	}
	
	public Object getFirstEvent() {
		return _firstTransition.getUserObject();
	}
	
	public Object getSecondEvent() {
		return _secondTransition.getUserObject();
	}
	
	public Edge getFirstTransition() {
		return _firstTransition;
	}
	
	public Edge getSecondTransition() {
		return _secondTransition;
	}
	
	public boolean equals(Object transitionPair) {
		if(transitionPair instanceof TransitionPair) {
			if(this == (TransitionPair)transitionPair)
				return true;
			else {
				if(this.getFirstTransition().equals(((TransitionPair)transitionPair).getFirstTransition()) &&
					this.getSecondTransition().equals(((TransitionPair)transitionPair).getSecondTransition()))
					return true;
				else
					return false;
			}
		}
		else
			return false;
	}
}
