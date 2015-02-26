package gttlipse.vfsmEditor.parts.command;

import gttlipse.vfsmEditor.model.ConnectionBendpoint;


public class CreateBendpointCommand extends BendpointCommand {

	public void execute() {
		ConnectionBendpoint cbp = new ConnectionBendpoint();
		cbp.setDimension(d1,d2);
		m_connection.addBendpoint(index, cbp);
	}

	public void undo() {
		m_connection.removeBendpoint(index);
	}
}
