/*
 * Created on 2005-1-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.model;

import gttlipse.vfsmEditor.VFSMDef;

/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Diagram extends Node {

	public static String PROP_LAYOUT = "LAYOUT";

	private AbstractSuperState m_RootState;

	public Diagram() {
		m_RootState = new SuperState();
		m_RootState.setName(VFSMDef.FSM_MAIN);
	}

	public void removeNode(State state) {
		m_RootState.removeState(state);
		fireStructureChange(state.getStateType(), m_RootState.getAll());
	}

	public void refreshDiagram() {
		State fakeNode = State.createDummyState();
		m_RootState.addState(fakeNode);
		
		fireStructureChange(m_RootState.getStateType(), m_RootState.getAll());
	}

	public AbstractSuperState getMainState() {
		return m_RootState;
	}
}