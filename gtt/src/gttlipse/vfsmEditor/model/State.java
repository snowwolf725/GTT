package gttlipse.vfsmEditor.model;

import gttlipse.vfsmEditor.VFSMDef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;


public class State extends Node implements Cloneable {

	protected String m_Name;
	
	protected Color m_color;
	
	public static Color originalColor = new Color(null, 240, 128, 128); /* Light Coral */
	
	public static Color markedColor = new Color(null, 254, 0, 0);
	
	public static Color coveredColor = new Color(null, 0, 254, 0);

	public State() {
		m_Name = "State";
		setDimension(new Dimension(50, 20));
		m_color = originalColor;
	}

	public State(String name) {
		m_Name = name;
		setDimension(new Dimension(50, 20));
	}

	public String getStateType() {
		return VFSMDef.PROP_STATE;
	}
	
	public void setBackgroundMarkColor() {
		m_color = markedColor;
	}
	
	public void setBackgroundOriginalColor() {
		m_color = originalColor;
	}
	
	public void setBackgroundCoveredColor() {
		m_color = coveredColor;
	}
	
	public void setBackgroundColor(Color color) {
		m_color = color;
	}
	
	public Color getBackgroundColor() {
		return m_color;
	}

	final public String getName() {
		return m_Name;
	}

	final public void setName(String name) {
		if (this.m_Name.equals(name)) {
			return;
		}
		m_Name = name;
		firePropertyChange(PROP_NAME, null, m_Name);
	}

	public void accept(IVFSMVisitor v) {
		v.visit(this);
	}

	State m_parent;

	final public void setParent(State parent) {
		m_parent = parent;
	}

	final public State getParent() {
		return m_parent;
	}

	/**
	 * 取得node在VFSM的完整存取路徑
	 * 
	 * @return
	 */
	public final String fullName() {
		String result = m_Name;
		State parent = getParent();
		while (parent != null) {
			result = parent.getName() + "::" + result;
			parent = parent.getParent();
		}
		return result;
	}

	// connections
	private List<ConnectionBase> m_Outputs = new ArrayList<ConnectionBase>();
	private List<ConnectionBase> m_Inputs = new ArrayList<ConnectionBase>();

	final public void addInput(ConnectionBase connection) {
		if (ConnectionBase.isExist(m_Inputs, connection))
			return;

		m_Inputs.add(connection);
		fireStructureChange(PROP_INPUTS, connection);
	}

	final public void addOutput(ConnectionBase connection) {
		if (ConnectionBase.isExist(m_Outputs, connection))
			return;

		m_Outputs.add(connection);
		fireStructureChange(PROP_OUTPUTS, connection);
	}

	final public List<ConnectionBase> getIncomingConnections() {
		return m_Inputs;
	}

	final public List<ConnectionBase> getOutgoingConnections() {
		return m_Outputs;
	}

	final public void removeInput(ConnectionBase connection) {
		this.m_Inputs.remove(connection);
		fireStructureChange(PROP_INPUTS, connection);
	}

	final public void removeOutput(ConnectionBase connection) {
		this.m_Outputs.remove(connection);
		fireStructureChange(PROP_OUTPUTS, connection);
	}

	public static State createDummyState() {
		// 用 state 代替 Node
		return new State();
	}

	private boolean declarationFlag = false;

	final public void setDeclaration(boolean flag) {
		declarationFlag = flag;
	}

	final public boolean isDeclaration() {
		return declarationFlag;
	}

	public void addState(State s) {
		/* nothing */
	}

	public State getChildrenByName(String name) {
		return null;
	}

	public State getChildrenByType(String type) {
		return null;
	}

	/* get compositeNode */
	public List<State> getAll() {
		return new ArrayList<State>(); // empty list
	}

	/* return SuperState children's number */
	public int size() {
		return 0;
	}

	public boolean isMoveable() {
		return true;
	}

	public boolean isResizeable() {
		return true;
	}

	public State clone() {
		State s = new State();
		defaultCopy(s);
		return s;
	}

	final protected void defaultCopy(State s) {
		s.setName(m_Name);
		s.setLocation(new Point(m_location));
		s.setDimension(new Dimension(m_Size));

		// 底下的連線會形成額外的 connections
//		Iterator<ConnectionBase> cite;
//		cite = getIncomingConnections().iterator();
//		while (cite.hasNext()) {
//			s.addInput(cite.next());
//		}
//		cite = getOutgoingConnections().iterator();
//		while (cite.hasNext()) {
//			s.addOutput(cite.next());
//		}
	}
}
