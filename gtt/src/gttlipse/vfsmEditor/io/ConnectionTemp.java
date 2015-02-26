package gttlipse.vfsmEditor.io;

import gtt.testscript.AbstractNode;
import gtt.testscript.NodeFactory;
import gttlipse.vfsmEditor.model.AbstractSuperState;
import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.ConnectionBendpoint;
import gttlipse.vfsmEditor.model.State;

import java.util.ArrayList;
import java.util.List;


// 在讀檔時，暫時性地記錄 Connection 的資訊
// package private
class ConnectionTemp {

	private State m_source;
	private String m_targetPath;
	private String m_Condition;
	private String m_Action;

	private List<AbstractNode> m_EventList = new ArrayList<AbstractNode>();
	private List<ConnectionBendpoint> m_BendpointList = new ArrayList<ConnectionBendpoint>();

	public ConnectionTemp(State source) {
		m_source = source;
	}

	public void setTarget(String target) {
		m_targetPath = target;
	}

	public void setCondition(String cond) {
		m_Condition = cond;
	}

	public void setAction(String act) {
		m_Action = act;
	}

	public void addEvent(String event) {
		if (event == null)
			return;
		m_EventList.add(new NodeFactory().createReferenceMacroEventNode(event));
	}

	public void addBendpoint(int w1, int h1, int w2, int h2) {
		ConnectionBendpoint cb = new ConnectionBendpoint();
		cb.setD1(w1, h1);
		cb.setD2(w2, h2);
		m_BendpointList.add(cb);	
	}
	
	// 要有 main 及 declaration 兩個root 才能 rebuild
	public Connection build(AbstractSuperState main, AbstractSuperState decl) {
		return buildConnection(StateReferenceUtil.findTarget(main, decl,
				m_targetPath));
	}

	public Connection buildConnection(State target) {
		if (target == null)
			return null;
		if (m_source == null)
			return null;
		Connection conn = Connection.create(m_source, target);
		
		conn.setCondition(m_Condition);
		conn.setAction(m_Action);
		// event list
		conn.setEventList(m_EventList);
		conn.setBendpoints(m_BendpointList);
		return conn;
	}

	public String toString() {
		return m_source.getName() + " to " + m_targetPath;
	}
}
