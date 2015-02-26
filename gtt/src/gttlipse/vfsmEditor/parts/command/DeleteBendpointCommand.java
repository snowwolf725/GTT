package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.ConnectionBendpoint;

public class DeleteBendpointCommand extends BendpointCommand{
	private ConnectionBendpoint deletedBendpoint;
	public void execute() {
		deletedBendpoint = (ConnectionBendpoint)m_connection.getBendpoints().get(index);
		m_connection.removeBendpoint(index);
	}

	public void undo() {
		m_connection.addBendpoint(index, deletedBendpoint);
	}

}
