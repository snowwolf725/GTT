package gttlipse.testgen.testsuite;

import gtt.macro.macroStructure.MacroEventNode;
import gtt.testscript.EventNode;
import gtt.testscript.ReferenceMacroEventNode;
import gttlipse.testgen.graph.Edge;

import java.util.List;
import java.util.Vector;


public class ConsecutiveSet {

	private List<Edge[]> m_edgeSet = new Vector<Edge[]>();
	private List<Boolean> m_visited = new Vector<Boolean>();
	private int m_setSize = 0;

	public ConsecutiveSet() {
		m_setSize = 2;
	}

	public ConsecutiveSet(int size) {
		m_setSize = size;
	}

	public int size() {
		return m_edgeSet.size();
	}

	public Edge[] get(int index) {
		return m_edgeSet.get(index);
	}

	public void add(Edge[] consecutiveEdge) {
		m_edgeSet.add(consecutiveEdge);
		m_visited.add(false);
	}

	public boolean isVisited(int index) {
		if (index >= 0 && index < m_visited.size())
			return m_visited.get(index);

		return false;
	}

	public boolean isVisited(Edge[] consecutiveEdge) {
		int index = findEdgeSetIndex(consecutiveEdge);
		if (index >= 0 && index < m_visited.size())
			return m_visited.get(index);
		return false;
	}

	public boolean setVisited(int index, boolean set) {
		if (index < m_visited.size()) {
			m_visited.set(index, set);
			return true;
		}

		return false;
	}

	public boolean setVisited(Edge[] consecutiveEdge, boolean set) {
		int index = -1;

		if (consecutiveEdge.length != m_setSize)
			return false;

		if ((index = findEdgeSetIndex(consecutiveEdge)) >= 0) {
			m_visited.set(index, true);
			return true;
		}

		return false;
	}

	public int findEdgeSetIndex(Edge[] consecutiveEdge) {
		int findIndex = -1;

		for (int i = 0; i < m_edgeSet.size(); i++) {
			Edge edgeSet[] = m_edgeSet.get(i);
			boolean found = false;

			for (int j = 0; j < edgeSet.length; j++) {
				if (consecutiveEdge[j] == null)
					break;

				Object edgeObj = edgeSet[j].getUserObject();
				Object conObj = consecutiveEdge[j].getUserObject();

				String compName1 = "";
				String eventName1 = "";
				String compName2 = "";
				String eventName2 = "";

				if (edgeObj instanceof EventNode && conObj instanceof EventNode) {
					compName1 = ((EventNode) edgeObj).getComponent().getName();
					eventName1 = ((EventNode) edgeObj).getEvent().getName();
					compName2 = ((EventNode) conObj).getComponent().getName();
					eventName2 = ((EventNode) conObj).getEvent().getName();
				} else if ((edgeObj instanceof EventNode) == false
						&& (conObj instanceof EventNode) == false
						&& (edgeObj instanceof ReferenceMacroEventNode) == false
						&& (conObj instanceof ReferenceMacroEventNode) == false) {
					compName1 = ((MacroEventNode) edgeObj).getParent()
							.getName();
					eventName1 = ((MacroEventNode) edgeObj).getName();
					compName2 = ((MacroEventNode) conObj).getParent().getName();
					eventName2 = ((MacroEventNode) conObj).getName();
				} else
					break;

				if (compName1.equals(compName2) == false
						|| eventName1.equals(eventName2) == false)
					break;

				if (j == edgeSet.length - 1
						&& compName1.equals(compName2) == true
						&& eventName1.equals(eventName2) == true)
					found = true;
			}

			if (found == true) {
				findIndex = i;
				break;
			}
		}
		return findIndex;
	}

	// public int findEdgeSetIndex( Edge[] consecutiveEdge )
	// {
	// int findIndex = -1;
	//    	
	// for( int i = 0; i < m_edgeSet.size(); i++ )
	// {
	// Edge edgeSet[] = m_edgeSet.get( i );
	// boolean found = false;
	// for( int j = 0; j < edgeSet.length; j++ )
	// {
	// if( edgeSet[j] != consecutiveEdge[j] )
	// break;
	// else if( j == edgeSet.length - 1 && edgeSet[j] == consecutiveEdge[j] )
	// found = true;
	// }
	//    		
	// if( found == true )
	// {
	// findIndex = i;
	// break;
	// }
	// }
	// return findIndex;
	// }
}
