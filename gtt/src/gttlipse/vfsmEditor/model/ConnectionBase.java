package gttlipse.vfsmEditor.model;

import gtt.testscript.AbstractNode;
import gttlipse.vfsmEditor.VFSMDef;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


public abstract class ConnectionBase extends Element {
	final public static String PROP_BENDPOINT = "BENDPOINT";
	final public static String PROP_EVENT = "EVENTNAME";
	final public static String PROP_CONDITION = "EVENTCOND";
	final public static String PROP_ACTION = "EVENTACTION";
	final public static String PROP_SOURCE = "SOURCE";
	final public static String PROP_TARGET = "TARGET";
	final public static String PROP_ROUTER = "ROUTER";

	private int routerId = VFSMDef.BENDPOINT_CONNECTION_ROUTER;
	private List<ConnectionBendpoint> m_bendpoints = new ArrayList<ConnectionBendpoint>();

	private double m_sourceAngle;
	private double m_targetAngle;

	public int getRouterId() {
		return routerId;
	}

	public void setRouterId(int routerId) {
		this.routerId = routerId;
	}

	public List<ConnectionBendpoint> getBendpoints() {
		return m_bendpoints;
	}

	public void setBendpoints(List<ConnectionBendpoint> bendpoints) {
		m_bendpoints = bendpoints;
	}

	public void addBendpoint(int index, ConnectionBendpoint point) {
		m_bendpoints.add(index, point);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

	public void setBendpointRelativeDimensions(int index, Dimension d1,
			Dimension d2) {
		m_bendpoints.get(index).setDimension(d1, d2);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

	public void removeBendpoint(int index) {
		m_bendpoints.remove(index);
		firePropertyChange(PROP_BENDPOINT, null, null);
	}

	public double getSourceAngle() {
		return m_sourceAngle;
	}

	public void setSourceAngle(double sourceAngle) {
		m_sourceAngle = sourceAngle;
	}

	public double getTargetAngle() {
		return m_targetAngle;
	}

	public void setTargetAngle(double targetAngle) {
		m_targetAngle = targetAngle;
	}

	// ------------------------------------------------------------------------
	// Abstract methods from IPropertySource

	public Object getEditableValue() {
		return this;
	}

	protected IPropertyDescriptor[] descriptors = new IPropertyDescriptor[] {
			new TextPropertyDescriptor(PROP_EVENT, "Event"),
			new TextPropertyDescriptor(PROP_CONDITION, "Condition"),
			new TextPropertyDescriptor(PROP_ACTION, "Action"),
			new TextPropertyDescriptor(PROP_SOURCE, "Source"),
			new TextPropertyDescriptor(PROP_TARGET, "Target"),
			new TextPropertyDescriptor(PROP_ROUTER, "Router") };

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	public Object getPropertyValue(Object id) {
		if (PROP_CONDITION.equals(id))
			return getCondition();
		if (PROP_ACTION.equals(id))
			return getAction();
		if (PROP_SOURCE.equals(id))
			return getSource();
		if (PROP_TARGET.equals(id))
			return getTarget();
		if (PROP_ROUTER.equals(id))
			return getRouterId();
		return null;
	}

	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	public void setPropertyValue(Object id, Object value) {
		if (PROP_CONDITION == id)
			setCondition((String) value);
		if (PROP_ACTION == id)
			setAction((String) value);
		if (PROP_SOURCE == id)
			setSource((State) value);
		if (PROP_TARGET == id)
			setSource((State) value);
		if (PROP_ROUTER == id)
			setRouterId((Integer) value);
	}

	// ///////////////////////////////////////////////////////

	public abstract void setCondition(String condition);

	public abstract void setAction(String action);

	public abstract void setSource(State st);

	public abstract void setTarget(State st);

	public abstract String getCondition();

	public abstract String getAction();

	public abstract State getSource();

	public abstract State getTarget();

	public abstract String getEvent();

	public abstract List<AbstractNode> getEventList();

	public abstract void removeConnection(State source, State target);

	// ///////////////////////////////////////////////////////////////////
	// static methods
	public static Connection create(State source, State target) {
		if (source.getStateType() == VFSMDef.TYPE_FINAL)
			return null; // final 不能做source
		if (target.getStateType() == VFSMDef.TYPE_INITIAL)
			return null; // inital 不能做target

		// inital state 只能有一個 output
		if (source.getStateType() == VFSMDef.TYPE_INITIAL
				&& source.getOutgoingConnections().size() > 1)
			return null;

		return new Connection(source, target);
	}

	// 檢查 connection 是否是建立同一個state內部
	public static boolean checkSameParent(ConnectionBase conn) {
		State srcParent = conn.getSource().getParent();
		State tarParent = conn.getTarget().getParent();
		return srcParent.equals(tarParent);
	}

	// 檢查 Connection 是已經存在
	public static boolean isExist(List<ConnectionBase> list,
			ConnectionBase connection) {
		String source = connection.getSource().fullName();
		String target = connection.getTarget().fullName();
		
		source = source.replace("RealState::", "");
		target = target.replace("RealState::", "");
		source = source.replace("ExtraState::", "");
		target = target.replace("ExtraState::", "");
		
		Iterator<ConnectionBase> ite = list.iterator();
		while (ite.hasNext()) {
			ConnectionBase cur = ite.next();
			if (!cur.getSource().fullName().equals(source))
				continue;
			if (!cur.getTarget().fullName().equals(target))
				continue;

			return true; // Yes! it exists
		}
		
//		System.out.println(connection.getSource().fullName()+ "---" + connection.getTarget().fullName());
		// 不存在
		return false;
	}

	public static ConnectionBase createByConnection(State src, State tar,
			ConnectionBase cur_connection) {
		Connection conn = Connection.create(src, tar);
		if (conn == null)
			return null;
		
//		List<AbstractNode> eventList = cur_connection.getEventList();
//		List<AbstractNode> newEventList = new LinkedList<AbstractNode>();
//		
//		for(int index = 0; index < eventList.size(); index++) {
//			newEventList.add(eventList.get(index));
//		}
		
		conn.setEventList(cur_connection.getEventList());
//		conn.setEventList(newEventList);
		conn.setBendpoints(cur_connection.getBendpoints());
		conn.setCondition(cur_connection.getCondition());
		conn.setAction(cur_connection.getAction());
		return conn;
	}

	/* connect all nodes */
	protected static void cloneConnections(AbstractSuperState target,
			AbstractSuperState source) {
		Iterator<State> state_ite = target.getAll().iterator();
		
		while (state_ite.hasNext()) {
			State child = state_ite.next();
			Iterator<ConnectionBase> cite = child.getOutgoingConnections()
					.iterator();
			while (cite.hasNext()) {
				ConnectionBase conn = cite.next();
				String srcName = conn.getSource().getName();
				if (source.getChildrenByName(srcName) == null)
					continue;
				String tarName = conn.getTarget().getName();
				if (source.getChildrenByName(tarName) == null)
					continue;
				
				ConnectionBase.createByConnection(source
						.getChildrenByName(srcName), source
						.getChildrenByName(tarName), conn);
			}

		}
	}
}
