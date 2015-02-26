/*
 * Created on 2005-1-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.Connection;
import gttlipse.vfsmEditor.model.ConnectionBase;
import gttlipse.vfsmEditor.model.ConnectionBendpoint;
import gttlipse.vfsmEditor.model.State;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;


/**
 * @author zhanghao
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateConnectionCommand extends Command {

	protected Connection m_connection;
	protected State m_Source;
	protected State m_Target;

	public void setSource(State source) {
		if (!source.getParent().getName().equals("FSM"))
			m_Source = source;
	}

	public void setTarget(State target) {
		if (!target.getParent().getName().equals("FSM"))
			m_Target = target;
	}

	// ------------------------------------------------------------------------
	// Overridden from Command

	public void execute() {
		if (m_Source == null)
			return;
		if (m_Target == null)
			return;
		m_connection = Connection.create(m_Source, m_Target);
		if (m_Source.equals(m_Target)) {
			// The start and end points of our connection are both at the center
			// of the rectangle,
			// so the two relative dimensions are equal.
			ConnectionBendpoint cbp = new ConnectionBendpoint();
			cbp.setDimension(new Dimension(0, -60), new Dimension(0, -60));
			m_connection.addBendpoint(0, cbp);

			ConnectionBendpoint cbp2 = new ConnectionBendpoint();
			cbp2.setDimension(new Dimension(100, -60), new Dimension(100, -60));
			m_connection.addBendpoint(1, cbp2);

			ConnectionBendpoint cbp3 = new ConnectionBendpoint();
			cbp3.setDimension(new Dimension(100, 0), new Dimension(100, 0));
			m_connection.addBendpoint(2, cbp3);
		}
	}

	public String getLabel() {
		return "Create Connection";
	}

	public void redo() {
		m_Source.addOutput(this.m_connection);
		m_Target.addInput(this.m_connection);
	}

	public void undo() {
		m_Source.removeOutput(this.m_connection);
		m_Target.removeInput(this.m_connection);
	}

	public boolean canExecute() {
		// Check for existence of connection already
		List<ConnectionBase> connections = m_Source.getOutgoingConnections();
		for (int i = 0; i < connections.size(); i++) {
			if (((ConnectionBase) connections.get(i)).getTarget().equals(
					m_Target))
				return false;
		}
		return true;
	}
}